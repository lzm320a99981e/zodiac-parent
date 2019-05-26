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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Excel 读取
 */
@Slf4j
public class ExcelWriter {
    public static ExcelWriter create() {
        return new ExcelWriter();
    }

    private Map<String, TableDataEntry> tableDataMap = new HashMap<>();
    private Map<String, PointDataEntry> pointDataMap = new HashMap<>();

    public ExcelWriter addTable(Table table, Collection<Map<String, Object>> data) {
        Preconditions.checkState(!tableDataMap.containsKey(table.getDataKey()), "已存在的 dataKey -> %s", table.getDataKey());
        this.tableDataMap.put(table.getDataKey(), new TableDataEntry(table, data));
        return this;
    }

    public ExcelWriter addPoint(Point point, Object data) {
        Preconditions.checkState(!tableDataMap.containsKey(point.getDataKey()), "已存在的 dataKey -> %s", point.getDataKey());
        this.pointDataMap.put(point.getDataKey(), new PointDataEntry(point, data));
        return this;
    }

    public ExcelWriter addPoints(Collection<Point> points, Map<String, Object> data) {
        points.stream().forEach(item -> this.addPoint(item, data.get(item.getDataKey())));
        return this;
    }

    public byte[] write(InputStream template) {
        if (tableDataMap.isEmpty() && pointDataMap.isEmpty()) {
            throw new RuntimeException("未添加任何数据，请添加数据后操作");
        }
        try {
            // 创建工作本
            Workbook workbook = WorkbookFactory.create(template);

            // 表格数据写入
            tableDataMap.values().forEach(item -> writeTable(workbook, item));

            // 单元格数据写入
            pointDataMap.values().forEach(item -> writePoint(workbook, item));

            // 获取写入后的数据
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

    public byte[] write(File template) {
        try (FileInputStream inputStream = new FileInputStream(template)) {
            byte[] data = write(inputStream);
            inputStream.close();
            return data;
        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

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

    private void writeTable(Workbook workbook, TableDataEntry dataEntry) {
        Table table = dataEntry.getTable();
        Collection<Map<String, Object>> data = dataEntry.getData();
        if (Objects.isNull(data) || data.isEmpty()) {
            log.warn("表格 -> {} 设置的数据为空", table.getDataKey());
            return;
        }

        Sheet sheet = findSheet(workbook, table);
        List<Map<String, Object>> dataList = List.class.isAssignableFrom(data.getClass()) ? (List<Map<String, Object>>) data : new ArrayList<>(data);
        int dataSize = dataList.size();
        Integer startRowNumber = table.getStartRow();
        AtomicBoolean shiftRows = new AtomicBoolean(false);

        IntStream.range(startRowNumber, dataSize).forEach(i -> {
            Row row = sheet.getRow(i);
            Map<String, Object> rowData = dataList.get(i - startRowNumber);
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
            if (!shiftRows.get() && needShiftRow(row, table)) {
                sheet.shiftRows(i, sheet.getLastRowNum(), dataSize - i);
                shiftRows.set(true);
            }

            // 已移动行，移动开始 到 移动结束之间的行都是空行，需要创建
            if (shiftRows.get()) {
                Row createdRow = sheet.createRow(i);
                ExcelHelper.copyRow(sheet.getRow(i - 1), createdRow);
                writeRow(createdRow, table, rowData);
                return;
            }

            // 未移动行且行非空
            writeRow(row, table, rowData);
        });
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
        private Collection<Map<String, Object>> data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PointDataEntry {
        private Point point;
        private Object data;
    }

}
