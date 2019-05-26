package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Metadata;
import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
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
    public static ExcelWriter create() {
        return new ExcelWriter();
    }

    /**
     * 表格数据
     */
    private Map<String, TableDataEntry> tableDataMap = new HashMap<>();
    /**
     * 单元格数据
     */
    private Map<String, PointDataEntry> pointDataMap = new HashMap<>();
    /**
     * 表格单元格合并区域
     */
    private Map<Table, List<TableCellMergeRange>> tableCellMergeRangesMap = new HashMap<>();

    /**
     * 添加表格
     *
     * @param table
     * @param data
     * @return
     */
    public ExcelWriter addTable(Table table, List<Map<String, Object>> data) {
        Preconditions.checkState(!tableDataMap.containsKey(table.getDataKey()), "已存在的 dataKey -> %s", table.getDataKey());
        this.tableDataMap.put(table.getDataKey(), new TableDataEntry(table, data));
        return this;
    }

    /**
     * 添加表格
     *
     * @param table
     * @param data
     * @param strategy
     * @return
     */
    public ExcelWriter addTable(Table table, List<Map<String, Object>> data, TableCellMergeStrategy strategy) {
        Preconditions.checkNotNull(strategy);
        addTable(table, data);
        this.tableCellMergeRangesMap.put(table, strategy.findCellMergeRanges(data));
        return this;
    }

    /**
     * 添加表格
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
     * 添加单元格
     *
     * @param point
     * @param data
     * @return
     */
    public ExcelWriter addPoint(Point point, Object data) {
        Preconditions.checkState(!tableDataMap.containsKey(point.getDataKey()), "已存在的 dataKey -> %s", point.getDataKey());
        this.pointDataMap.put(point.getDataKey(), new PointDataEntry(point, data));
        return this;
    }

    /**
     * 添加单元格
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
    public byte[] write(InputStream template) {
        if (tableDataMap.isEmpty() && pointDataMap.isEmpty()) {
            throw new RuntimeException("未添加任何数据，请添加数据后操作");
        }
        try {
            // 创建工作本
            Workbook workbook = WorkbookFactory.create(template);

            // 写入表格数据
            this.tableDataMap.values().forEach(item -> writeTable(workbook, item));

            // 写入单元格数据
            this.pointDataMap.values().forEach(item -> writePoint(workbook, item));

            // 获取写入后的数据
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

    /**
     * 根据模板写入
     *
     * @param template
     * @return
     */
    public byte[] write(File template) {
        try (FileInputStream inputStream = new FileInputStream(template)) {
            byte[] data = write(inputStream);
            inputStream.close();
            return data;
        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

    /**
     * 写入到单元格
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
        Sheet sheet = findSheet(workbook, point);
        Integer rowNumber = point.getRowNumber();
        Row row = Preconditions.checkNotNull(sheet.getRow(rowNumber), "在sheet[%s]中，未找到行号[%s]对应的行", sheet.getSheetName(), rowNumber);

        Integer columnNumber = point.getColumnNumber();
        Cell cell = Preconditions.checkNotNull(row.getCell(columnNumber), "在sheet[%s]的第[%s]行中，未找到列号[%s]对应的列", sheet.getSheetName(), rowNumber, columnNumber);
        ExcelHelper.setCellValue(cell, data);
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

        Sheet sheet = findSheet(workbook, table);
        int dataSize = data.size();
        Integer startRowNumber = table.getStartRow();
        AtomicBoolean moved = new AtomicBoolean(false);
        AtomicInteger moveRows = new AtomicInteger(0);

        IntStream.range(startRowNumber, dataSize).forEach(i -> {
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
                sheet.shiftRows(i, sheet.getLastRowNum(), dataSize - i);
                moveRows.addAndGet(dataSize - i);
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
                int firstRowNumber = item.getStartRowDataIndex() + startRowNumber + moveRows.get();
                int lastRowNumber = firstRowNumber + item.getEndRowDataIndex();
                Integer firstColumnNumber = dataKeyWithColumnNumberMap.get(item.getStartColumnDataKey());
                Integer lastColumnNumber = dataKeyWithColumnNumberMap.get(item.getEndColumnDataKey());
                sheet.addMergedRegion(new CellRangeAddress(firstRowNumber, lastRowNumber, firstColumnNumber, lastColumnNumber));
            });
        }
    }

    private boolean needShiftRow(Row row, Table table) {
        return table.getDataKeyWithColumnNumberMap().values().stream().anyMatch(item -> {
            Cell cell = row.getCell(item);
            return Objects.nonNull(cell) && Objects.nonNull(ExcelHelper.getCellValue(cell));
        });
    }

    private void writeRow(Row row, Table table, Map<String, Object> data) {
        final Map<String, Integer> dataKeyWithColumnNumberMap = table.getDataKeyWithColumnNumberMap();
        table.getDataKeys().forEach(dataKey -> {
            Integer columnNumber = dataKeyWithColumnNumberMap.get(dataKey);
            writeCell(ExcelHelper.createCellIfNonExistent(row, columnNumber), data.get(dataKey));
        });
    }

    private void writeCell(Cell cell, Object data) {
        ExcelHelper.setCellValue(cell, data);
    }

    private Sheet findSheet(Workbook workbook, Metadata metadata) {
        Integer sheetIndex = metadata.getSheetIndex();
        if (Objects.nonNull(sheetIndex)) {
            return Preconditions.checkNotNull(workbook.getSheetAt(sheetIndex), "根据 sheetIndex -> %s 未找到对应的sheet", sheetIndex);
        }
        String sheetName = metadata.getSheetName();
        return Preconditions.checkNotNull(workbook.getSheet(sheetName), "根据 sheetName -> %s 未找到对应的sheet", sheetName);
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