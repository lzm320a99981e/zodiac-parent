package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.interceptor.DefaultExcelWriteInterceptor;
import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelWriteInterceptor;
import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.github.lzm320a99981e.zodiac.tools.IdGenerator;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Excel 读取
 */
@Slf4j
public class ExcelWriter {
    /**
     * 表格数据
     */
    private Map<String, TableDataEntry> tableDataMap = new LinkedHashMap<>();
    /**
     * 单元格数据
     */
    private Map<String, PointDataEntry> pointDataMap = new LinkedHashMap<>();
    /**
     * 表格单元格合并区域
     */
    private Map<Table, List<TableCellMergeRange>> tableCellMergeRangesMap = new LinkedHashMap<>();
    /**
     * sheet移动行计数器
     */
    private Map<Sheet, AtomicInteger> sheetShiftRowCounterMap = new LinkedHashMap<>();
    /**
     * 拦截器
     */
    private ExcelWriteInterceptor interceptor = new DefaultExcelWriteInterceptor();

    /**
     * 创建ExcelWriter实例
     *
     * @return
     */
    public static ExcelWriter create() {
        return new ExcelWriter();
    }

    /**
     * 设置拦截器
     *
     * @param interceptor
     * @return
     */
    public ExcelWriter setInterceptor(ExcelWriteInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    /**
     * 添加表格配置
     *
     * @param table
     * @param data
     * @return
     */
    public ExcelWriter addTable(Table table, List<Map<String, Object>> data) {
        table.setDataKey(IdGenerator.uuid32());
        this.tableDataMap.put(table.getDataKey(), new TableDataEntry(table, data));
        return this;
    }

    /**
     * 添加表格配置以及表格内单元格的合并策略
     *
     * @param table
     * @param data
     * @param matcher
     * @return
     */
    public ExcelWriter addTable(Table table, List<Map<String, Object>> data, TableCellMergeRangesMatcher matcher) {
        Preconditions.checkNotNull(matcher);
        addTable(table, data);
        this.tableCellMergeRangesMap.put(table, matcher.match(table, data));
        return this;
    }

    /**
     * 添加表格配置以及表格内单元格的合并策略
     *
     * @param table
     * @param data
     * @param tableCellMergeRanges
     * @return
     */
    public ExcelWriter addTable(Table table, List<Map<String, Object>> data, List<TableCellMergeRange> tableCellMergeRanges) {
        Preconditions.checkNotNull(tableCellMergeRanges);
        addTable(table, data);
        this.tableCellMergeRangesMap.put(table, tableCellMergeRanges);
        return this;
    }

    /**
     * 添加单元格配置
     *
     * @param point
     * @param data
     * @return
     */
    public ExcelWriter addPoint(Point point, Object data) {
        if (Objects.isNull(point.getDataKey())) {
            point.setDataKey(IdGenerator.uuid32());
        }
        Preconditions.checkState(!tableDataMap.containsKey(point.getDataKey()), "已存在的 dataKey -> %s", point.getDataKey());
        this.pointDataMap.put(point.getDataKey(), new PointDataEntry(point, data));
        return this;
    }

    /**
     * 添加单元格配置
     *
     * @param points
     * @param data
     * @return
     */
    public ExcelWriter addPoints(List<Point> points, Map<String, Object> data) {
        points.stream().forEach(item -> this.addPoint(item, data.get(item.getDataKey())));
        return this;
    }

    /**
     * 根据模板写入
     *
     * @param template
     * @return
     */
    public void write(final Workbook template, final OutputStream output) {
        Preconditions.checkNotNull(template);
        Preconditions.checkNotNull(output);
        // 校验配置信息
        if (tableDataMap.isEmpty() && pointDataMap.isEmpty()) {
            throw new RuntimeException("未添加任何元数据，请添加元数据后操作");
        }

        try {
            // 写入单元格数据
            this.pointDataMap.values().forEach(item -> writePoint(template, item));

            // 写入表格数据
            this.tableDataMap.values().forEach(item -> {
                final Sheet sheet = ExcelHelper.findSheet(template, item.getTable());
                // ------------------------ 拦截器 --------------------------
                final List<Map<String, Object>> filteredData = this.interceptor.beforeWriteTable(sheet, item.getTable(), item.getData());
                if (Objects.isNull(filteredData)) {
                    return;
                }
                item.setData(filteredData);
                writeTable(template, item);
                this.interceptor.afterWriteTable(sheet, item.getTable(), item.getData());
            });

            // 写入到输出流
            template.write(output);
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
    }

    public void write(InputStream template, OutputStream output) {
        Preconditions.checkNotNull(template);
        Preconditions.checkNotNull(output);
        try {
            // 创建工作本
            Workbook workbook = WorkbookFactory.create(template);
            write(workbook, output);
            // 关闭工作本
            workbook.close();
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
    }

    /**
     * 根据模板写入
     *
     * @param template
     * @return
     */
    public void write(File template, OutputStream output) {
        try {
            write(new FileInputStream(template), output);
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
    }

    /**
     * 写入到表格
     *
     * @param workbook
     * @param dataEntry
     */
    private void writeTable(Workbook workbook, TableDataEntry dataEntry) {
        Table table = dataEntry.getTable();
        List<Map<String, Object>> data = dataEntry.getData();
        if (Objects.isNull(data) || data.isEmpty()) {
            log.warn("表格 -> {} 设置的数据为空", table.getDataKey());
            return;
        }

        Sheet sheet = ExcelHelper.findSheet(workbook, table);
        // 确定写入行数
        int dataSize = data.size();
        Integer tableSize = table.getSize();
        if (Objects.nonNull(tableSize) && tableSize > 0 && tableSize < dataSize) {
            dataSize = tableSize;
        }

        // 写入范围
        Integer startRowNumber = table.getStartRowNumber() + (sheetShiftRowCounterMap.containsKey(sheet) ? sheetShiftRowCounterMap.get(sheet).get() : 0);
        Integer endRowNumber = dataSize + startRowNumber;

        // 在向表格中写入数据的时候，是否移动了行
        AtomicBoolean moved = new AtomicBoolean(false);

        // 遍历写入范围，开始写入
        IntStream.range(startRowNumber, endRowNumber).forEach(i -> {
            Row row = sheet.getRow(i);
            Map<String, Object> rowData = data.get(i - startRowNumber);
            // 空行处理
            if (Objects.isNull(row)) {
                Row createdRow = sheet.createRow(i);
                // 复制上一行的属性
                if (i > startRowNumber) {
                    ExcelHelper.copyRow(sheet.getRow(i - 1), createdRow);
                }
                writeRow(createdRow, table, rowData);
                return;
            }

            // 非空行，需要判断是否需要进行移动（判断依据：行的单元格已有数据，并且包含在需要填充数据的单元格）
            if (!moved.get() && needShiftRow(row, table)) {
                if (!sheetShiftRowCounterMap.containsKey(sheet)) {
                    sheetShiftRowCounterMap.put(sheet, new AtomicInteger(0));
                }
                int shiftRows = endRowNumber - i;
                sheetShiftRowCounterMap.get(sheet).addAndGet(shiftRows);
                sheet.shiftRows(i, sheet.getLastRowNum(), shiftRows);
                moved.set(true);
            }

            // 已移动行，移动开始 到 移动结束之间的行都是空行，需要创建
            if (moved.get()) {
                Row createdRow = sheet.createRow(i);
                ExcelHelper.copyRow(sheet.getRow(i - 1), createdRow);
                writeRow(createdRow, table, rowData);
                return;
            }

            // 未移动行且行非空
            writeRow(row, table, rowData);
        });

        // 合并单元格处理
        List<TableCellMergeRange> tableCellMergeRanges = tableCellMergeRangesMap.get(table);
        if (Objects.nonNull(tableCellMergeRanges) && !tableCellMergeRanges.isEmpty()) {
            Map<String, Integer> dataKeyWithColumnNumberMap = table.getDataKeyWithColumnNumberMap();
            tableCellMergeRanges.forEach(item -> {
                int firstRowNumber = item.getStartRowDataIndex() + startRowNumber;
                int lastRowNumber = firstRowNumber + (item.getEndRowDataIndex() - item.getStartRowDataIndex());
                Integer firstColumnNumber = dataKeyWithColumnNumberMap.get(item.getStartColumnDataKey());
                Integer lastColumnNumber = dataKeyWithColumnNumberMap.get(item.getEndColumnDataKey());
                sheet.addMergedRegion(new CellRangeAddress(firstRowNumber, lastRowNumber, firstColumnNumber, lastColumnNumber));
            });
        }
    }

    /**
     * 写入到表格行
     *
     * @param row
     * @param table
     * @param data
     */
    private void writeRow(Row row, Table table, Map<String, Object> data) {
        // ------------------------ 拦截器 --------------------------
        final Map<String, Object> filteredData = this.interceptor.beforeWriteRow(row, table, data);
        if (Objects.isNull(filteredData)) {
            return;
        }

        final Map<String, Integer> dataKeyWithColumnNumberMap = table.getDataKeyWithColumnNumberMap();
        dataKeyWithColumnNumberMap.keySet().forEach(dataKey -> {
            Integer columnNumber = dataKeyWithColumnNumberMap.get(dataKey);
            writeCell(ExcelHelper.createCellIfNonExistent(row, columnNumber), table, filteredData.get(dataKey));
        });

        this.interceptor.afterWriteRow(row, table, filteredData);
    }

    /**
     * 写入到表格单元格
     *
     * @param cell
     * @param data
     */
    private void writeCell(Cell cell, Table table, Object data) {
        // ------------------------ 拦截器 --------------------------
        final Object filteredData = this.interceptor.beforeWriteCell(cell, table, data);
        if (Objects.isNull(filteredData)) {
            return;
        }
        ExcelHelper.setCellValue(cell, filteredData);
        this.interceptor.afterWriteCell(cell, table, filteredData);
    }

    /**
     * 写入到sheet单元格
     *
     * @param workbook
     * @param dataEntry
     */
    private void writePoint(Workbook workbook, PointDataEntry dataEntry) {
        Point point = dataEntry.getPoint();
        Object data = dataEntry.getData();
        if (Objects.isNull(data)) {
            log.warn("单元格 -> {} 设置的数据为空", point.getDataKey());
            return;
        }
        Sheet sheet = ExcelHelper.findSheet(workbook, point);
        Integer rowNumber = point.getRowNumber();
        Row row = Preconditions.checkNotNull(sheet.getRow(rowNumber), "在sheet[%s]中，未找到行号[%s]对应的行", sheet.getSheetName(), rowNumber);

        Integer columnNumber = point.getColumnNumber();
        Cell cell = Preconditions.checkNotNull(row.getCell(columnNumber), "在sheet[%s]的第[%s]行中，未找到列号[%s]对应的列", sheet.getSheetName(), rowNumber, columnNumber);

        // ------------------------ 拦截器 --------------------------
        final Object filteredData = this.interceptor.beforeWritePoint(cell, point, data);
        if (Objects.isNull(filteredData)) {
            return;
        }
        ExcelHelper.setCellValue(cell, filteredData);
        this.interceptor.afterWritePoint(cell, point, filteredData);
    }

    /**
     * 判断是否需要移动行
     *
     * @param row
     * @param table
     * @return
     */
    private boolean needShiftRow(Row row, Table table) {
        return table.getDataKeyWithColumnNumberMap().values().stream().anyMatch(item -> {
            Cell cell = row.getCell(item);
            return Objects.nonNull(cell) && Objects.nonNull(ExcelHelper.getCellValue(cell));
        });
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TableDataEntry {
        private Table table;
        private List<Map<String, Object>> data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PointDataEntry {
        private Point point;
        private Object data;
    }

}
