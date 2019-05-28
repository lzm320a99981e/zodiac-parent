package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelWriteInterceptor;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel读取，使用注解配置信息
 */
public class AnnotationExcelWriter extends AnnotationExcelSettingAdapter {
    private ExcelWriter excelWriter;

    private AnnotationExcelWriter(ExcelWriter excelWriter) {
        this.excelWriter = excelWriter;
    }

    public static AnnotationExcelWriter create() {
        return new AnnotationExcelWriter(ExcelWriter.create());
    }

    public AnnotationExcelWriter setInterceptor(ExcelWriteInterceptor interceptor) {
        this.excelWriter.setInterceptor(interceptor);
        return this;
    }

    public AnnotationExcelWriter addTable(Class<?> type, List<Map<String, Object>> data) {
        this.excelWriter.addTable(parseTableType(type), data);
        return this;
    }

    public AnnotationExcelWriter addTable(Class<?> type, List<Map<String, Object>> data, TableCellMergeRangesMatcher matcher) {
        this.excelWriter.addTable(parseTableType(type), data, matcher);
        return this;
    }

    public AnnotationExcelWriter addTable(Class<?> type, List<Map<String, Object>> data, List<TableCellMergeRange> tableCellMergeRanges) {
        this.excelWriter.addTable(parseTableType(type), data, tableCellMergeRanges);
        return this;
    }

    private Table parseTableType(Class<?> type) {
        final Table table = ExcelHelper.classToTable(type);
        if (!this.tableLimitMap.containsKey(type)) {
            return table;
        }
        final int[] limit = this.tableLimitMap.get(type);
        table.setStartRowNumber(limit[0]);
        if (limit.length == 2) {
            table.setSize(limit[1]);
        }
        return table;
    }

    public AnnotationExcelWriter addPoints(Class<?> type, Map<String, Object> data) {
        this.excelWriter.addPoints(ExcelHelper.classToPoints(type), data);
        return this;
    }

    public void write(InputStream template, OutputStream output) {
        this.excelWriter.write(template, output);
    }

    public void write(File template, OutputStream output) {
        this.excelWriter.write(template, output);
    }
}
