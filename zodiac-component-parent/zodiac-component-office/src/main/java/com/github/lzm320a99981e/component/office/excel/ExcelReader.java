package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.interceptor.DefaultExcelReadInterceptor;
import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelReadInterceptor;
import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.google.common.base.Preconditions;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Excel 读取
 */
public class ExcelReader {
    private List<Table> tables = new ArrayList<>();
    private List<Point> points = new ArrayList<>();
    private ExcelReadInterceptor interceptor = new DefaultExcelReadInterceptor();

    public static ExcelReader create() {
        return new ExcelReader();
    }

    public ExcelReader setInterceptor(ExcelReadInterceptor interceptor) {
        this.interceptor = Preconditions.checkNotNull(interceptor);
        return this;
    }

    public ExcelReader addTable(Table table, String dataKey) {
        table.setDataKey(Preconditions.checkNotNull(dataKey));
        this.tables.add(table);
        return this;
    }

    public ExcelReader addPoint(Point point, String dataKey) {
        point.setDataKey(Preconditions.checkNotNull(dataKey));
        this.points.add(point);
        return this;
    }

    public ExcelReader addPoints(Map<String, Point> points) {
        points.keySet().forEach(dataKey -> addPoint(points.get(dataKey), dataKey));
        return this;
    }

    public Map<String, Object> read(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Map<String, Object> data = read(inputStream);
            inputStream.close();
            return data;
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
        return null;
    }

    /**
     * TODO 还未解决取读取多个表格和和在表格下面的单元格，需要根据上一个表格的行数进行判断
     * TODO 怎么判断已读取到表格的最后一行（暂时的做法是，该行所有列的数据都为空，则认为已读取到表格的最后一行）
     *
     * @param inputStream
     * @return
     */
    public Map<String, Object> read(InputStream inputStream) {
        if (this.tables.isEmpty() && this.points.isEmpty()) {
            throw new RuntimeException("未添加任何元数据，请添加元数据后操作");
        }
        try {
            // 创建工作本
            final Workbook workbook = WorkbookFactory.create(inputStream);

            // 读取的数据
            final Map<String, Object> data = new LinkedHashMap<>();

            // 读取表格
            this.tables.forEach(item -> {
                final Sheet sheet = ExcelHelper.findSheet(workbook, item);
                // ------------------------ 拦截器 --------------------------
                if (!this.interceptor.beforeReadTable(sheet, item)) {
                    return;
                }
                final List<Map<String, Object>> tableData = readTable(workbook, item);
                data.put(item.getDataKey(), this.interceptor.afterReadTable(sheet, item, tableData));
            });

            // 读取单元格
            this.points.forEach(item -> data.put(item.getDataKey(), readPoint(workbook, item)));

            // 关闭工作本
            workbook.close();

            // 返回数据
            return data;
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
        return null;
    }

    private List<Map<String, Object>> readTable(Workbook workbook, Table table) {
        List<Map<String, Object>> data = new ArrayList<>();
        Sheet sheet = ExcelHelper.findSheet(workbook, table);
        Integer startRowNumber = table.getStartRowNumber();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = startRowNumber; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            // ------------------------ 拦截器 --------------------------
            if (!this.interceptor.beforeReadRow(row, table)) {
                continue;
            }
            Map<String, Object> rowData = this.interceptor.afterReadRow(row, table, readRow(row, table));
            /*
             * !!!注意：这里如果读取到的行数据为空，则认为已经读取到数据表格的末行了(TODO 这个方案还有待改善)
             */
            if (Objects.isNull(rowData) || rowData.isEmpty()) {
                break;
            }
            data.add(rowData);
        }
        return data.isEmpty() ? null : data;
    }

    private Map<String, Object> readRow(Row row, Table table) {
        if (Objects.isNull(row)) {
            return null;
        }
        Map<String, Object> rowData = new LinkedHashMap<>();
        Map<Integer, String> columnNumberWithDataKeyMap = table.getColumnNumberWithDataKeyMap();
        for (Map.Entry<Integer, String> entry : columnNumberWithDataKeyMap.entrySet()) {
            Cell cell = row.getCell(entry.getKey());
            // ------------------------ 拦截器 --------------------------
            if (!this.interceptor.beforeReadCell(cell, table)) {
                continue;
            }
            Object cellValue = this.interceptor.afterReadCell(cell, table, ExcelHelper.getCellValue(cell));
            if (Objects.isNull(cellValue)) {
                continue;
            }
            rowData.put(entry.getValue(), cellValue);
        }
        return rowData.isEmpty() ? null : rowData;
    }

    private Object readPoint(Workbook workbook, Point point) {
        Sheet sheet = ExcelHelper.findSheet(workbook, point);
        Row row = sheet.getRow(point.getRowNumber());
        if (Objects.isNull(row)) {
            return null;
        }
        Cell cell = row.getCell(point.getColumnNumber());
        if (Objects.isNull(cell)) {
            return null;
        }
        // ------------------------ 拦截器 --------------------------
        if (!this.interceptor.beforeReadPoint(cell, point)) {
            return null;
        }
        return this.interceptor.afterReadPoint(cell, point, ExcelHelper.getCellValue(cell));
    }


}
