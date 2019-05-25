package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel 读取
 */
public class ExcelWriter {
    public static ExcelWriter create() {
        return new ExcelWriter();
    }

    private Map<String, TableDataEntry> tableDataMap = new HashMap<>();
    private Map<String, PointDataEntry> pointDataMap = new HashMap<>();

    public ExcelWriter addTable(Table table, Collection<Map<String, Object>> data) {
        this.tableDataMap.put(table.getDataKey(), new TableDataEntry(table, data));
        return this;
    }

    public ExcelWriter addPoint(Point point, Object data) {
        this.pointDataMap.put(point.getDataKey(), new PointDataEntry(point, data));
        return this;
    }

    public ExcelWriter addPoints(Collection<Point> points, Map<String, Object> data) {
        points.stream().forEach(item -> this.addPoint(item, data.get(item.getDataKey())));
        return this;
    }

    public void write(File file) throws Exception {
        final Workbook workbook = WorkbookFactory.create(file);
        tableDataMap.values().stream().forEach(item -> {
            List<Map<String, Object>> data = new ArrayList<>(item.getData());
            Table table = item.getTable();
            Sheet sheet = workbook.getSheetAt(table.getSheetIndex());
            Integer startRow = table.getStartRow();
            Map<String, Integer> dataKeyWithColumnNumberMap = table.getColumns().stream().collect(Collectors.toMap(Point::getDataKey, Point::getColumnNumber));
            Set<String> dataKeys = dataKeyWithColumnNumberMap.keySet();

            for (int i = startRow; i < data.size(); i++) {
                Row row = ExcelHelper.createRowIfNonExistent(sheet, i);
                Map<String, Object> rowData = data.get(i - startRow);

                // 设置值
                dataKeys.forEach(dataKey -> {
                    Integer columnNumber = dataKeyWithColumnNumberMap.get(dataKey);
                    Cell cell = ExcelHelper.createCellIfNonExistent(row, columnNumber);
                    ExcelHelper.setCellValue(cell, rowData.get(dataKey));
                });

            }
        });
        workbook.write(new FileOutputStream("test001.xlsx"));
        workbook.close();
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
