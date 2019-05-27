package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelReadInterceptor;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Excel 读取
 */
public class AnnotationExcelReader {
    private ExcelReader excelReader;

    private AnnotationExcelReader(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public static AnnotationExcelReader create() {
        return new AnnotationExcelReader(ExcelReader.create());
    }

    public AnnotationExcelReader setInterceptor(ExcelReadInterceptor interceptor) {
        this.excelReader.setInterceptor(interceptor);
        return this;
    }

    public AnnotationExcelReader addTable(Class<?> type) {
        return addTable(type, type.getName());
    }

    public AnnotationExcelReader addTable(Class<?> type, String dataKey) {
        Preconditions.checkNotNull(dataKey);
        Table table = ExcelHelper.classToTable(Preconditions.checkNotNull(type));
        table.setDataKey(dataKey);
        this.excelReader.addTable(table);
        return this;
    }

    public AnnotationExcelReader addPoints(Class<?> type) {
        this.excelReader.addPoints(ExcelHelper.classToPoints(Preconditions.checkNotNull(type)));
        return this;
    }

    public Map<String, Object> read(File excel) {
        return this.excelReader.read(excel);
    }

    public Map<String, Object> read(InputStream excel) {
        return this.excelReader.read(excel);
    }

}
