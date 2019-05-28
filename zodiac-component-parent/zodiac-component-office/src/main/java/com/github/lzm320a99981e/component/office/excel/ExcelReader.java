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
    /**
     * 表格配置
     */
    private List<Table> tables = new ArrayList<>();

    /**
     * 单元格配置
     */
    private List<Point> points = new ArrayList<>();

    /**
     * 拦截器
     */
    private ExcelReadInterceptor interceptor = new DefaultExcelReadInterceptor();

    /**
     * 创建ExcelReader实例
     *
     * @return
     */
    public static ExcelReader create() {
        return new ExcelReader();
    }

    /**
     * 设置拦截器
     *
     * @param interceptor
     * @return
     */
    public ExcelReader setInterceptor(ExcelReadInterceptor interceptor) {
        this.interceptor = Preconditions.checkNotNull(interceptor);
        return this;
    }

    /**
     * 添加表格配置信息
     *
     * @param table
     * @return
     */
    public ExcelReader addTable(Table table) {
        table.setDataKey(Preconditions.checkNotNull(table.getDataKey()));
        this.tables.add(table);
        return this;
    }

    /**
     * 添加单元格配置信息
     *
     * @param point
     * @return
     */
    public ExcelReader addPoint(Point point) {
        point.setDataKey(Preconditions.checkNotNull(point.getDataKey()));
        this.points.add(point);
        return this;
    }

    /**
     * 添加多个单元格配置信息
     *
     * @param points
     * @return
     */
    public ExcelReader addPoints(List<Point> points) {
        points.forEach(this::addPoint);
        return this;
    }

    /**
     * 读取Excel文件
     *
     * @param excel
     * @return
     */
    public Map<String, Object> read(File excel) {
        try {
            FileInputStream inputStream = new FileInputStream(excel);
            Map<String, Object> result = read(inputStream);
            inputStream.close();
            return result;
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
        return null;
    }

    /**
     * 读取Excel
     *
     * @param excel
     * @return
     */
    public Map<String, Object> read(InputStream excel) {
        try {
            final Workbook workbook = WorkbookFactory.create(Preconditions.checkNotNull(excel));
            final Map<String, Object> result = read(workbook);
            workbook.close();
            return result;
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
        return null;
    }

    /**
     * 读取Excel
     *
     * @param workbook
     * @return
     */
    public Map<String, Object> read(final Workbook workbook) {
        if (this.tables.isEmpty() && this.points.isEmpty()) {
            throw new RuntimeException("未添加任何元数据，请添加元数据后操作");
        }
        try {
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

            // 返回数据
            return data;
        } catch (Exception e) {
            this.interceptor.onException(e);
        }
        return null;
    }

    /**
     * 读取Sheet中配置的表格
     *
     * @param workbook
     * @param table
     * @return
     */
    private List<Map<String, Object>> readTable(Workbook workbook, Table table) {
        List<Map<String, Object>> data = new ArrayList<>();
        Sheet sheet = ExcelHelper.findSheet(workbook, table);
        Integer startRowNumber = table.getStartRowNumber();
        int endLastRowNumber = sheet.getLastRowNum();
        Integer tableSize = table.getSize();
        if (Objects.nonNull(tableSize) && tableSize > 0 && (startRowNumber + tableSize) < endLastRowNumber) {
            endLastRowNumber = startRowNumber + tableSize;
        }

        for (int i = startRowNumber; i < endLastRowNumber; i++) {
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

    /**
     * 读取行
     *
     * @param row
     * @param table
     * @return
     */
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

    /**
     * 读取单元格
     *
     * @param workbook
     * @param point
     * @return
     */
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
