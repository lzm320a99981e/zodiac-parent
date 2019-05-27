package com.github.lzm320a99981e.component.office.excel;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelReadInterceptor;

import java.io.File;
import java.util.List;

/**
 * 简单 Excel 读取
 */
public class SimpleExcelReader {
    private AnnotationExcelReader excelReader;

    private SimpleExcelReader(AnnotationExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public static SimpleExcelReader create() {
        return new SimpleExcelReader(AnnotationExcelReader.create());
    }

    public SimpleExcelReader setInterceptor(ExcelReadInterceptor interceptor) {
        this.excelReader.setInterceptor(interceptor);
        return this;
    }

    public <T> List<T> readTable(Class<T> type, File excel) {
        Object data = this.excelReader.addTable(type).read(excel).get(type.getName());
        return JSON.parseArray(JSON.toJSONString(data), type);
    }
}
