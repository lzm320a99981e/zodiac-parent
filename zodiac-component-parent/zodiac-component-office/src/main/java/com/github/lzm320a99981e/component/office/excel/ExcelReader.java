package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel 读取
 */
public class ExcelReader {
    public static ExcelReader create() {
        return new ExcelReader();
    }

    private List<com.github.lzm320a99981e.component.office.excel.metadata.Table> tables = new ArrayList<>();
    private List<Point> points = new ArrayList<>();

    public ExcelReader addTable(com.github.lzm320a99981e.component.office.excel.metadata.Table table) {
        this.tables.add(table);
        return this;
    }

    public ExcelReader addTables(Collection<com.github.lzm320a99981e.component.office.excel.metadata.Table> tables) {
        this.tables.addAll(tables);
        return this;
    }

    public ExcelReader addPoint(Point point) {
        this.points.add(point);
        return this;
    }

    public ExcelReader addPoint(Collection<Point> points) {
        this.points.addAll(points);
        return this;
    }

    public Map<String, Object> read(File file) throws Exception {
        if (this.tables.isEmpty() && this.points.isEmpty()) {
            // todo 没有配置元数据
        }

        final Workbook workbook = WorkbookFactory.create(file);
        Map<String, Object> data = new HashMap<>();

        if (!tables.isEmpty()) {
            Map<String, Object> tablesData = new HashMap<>();
            data.put("tables", tablesData);

            for (Table table : tables) {
                List<Map<String, Object>> tableData = new ArrayList<>();
                tablesData.put(table.getDataKey(), tableData);

                Sheet sheet = workbook.getSheetAt(table.getSheetIndex());
                Map<Integer, String> columnNumberDataKeyWithMap = table.getColumns().stream().collect(Collectors.toMap(item -> item.getColumnNumber(), item -> item.getDataKey()));


                Integer startRow = table.getStartRow();
                for (int i = startRow; i < sheet.getLastRowNum(); i++) {
                    HashMap<String, Object> rowData = new HashMap<>();
                    tableData.add(rowData);

                    Row row = sheet.getRow(i);
                    for (Map.Entry<Integer, String> entry : columnNumberDataKeyWithMap.entrySet()) {
                        Cell cell = row.getCell(entry.getKey());
                        rowData.put(entry.getValue(), cell.getStringCellValue());
                    }
                }
            }
        }

        return data;
    }


}
