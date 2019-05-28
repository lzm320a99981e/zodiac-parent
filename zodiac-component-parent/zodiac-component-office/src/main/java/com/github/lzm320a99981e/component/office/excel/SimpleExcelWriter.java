package com.github.lzm320a99981e.component.office.excel;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelWriteInterceptor;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 简单 Excel 写入
 */
public class SimpleExcelWriter {
    private AnnotationExcelWriter excelWriter;
    private Object template;

    private SimpleExcelWriter(AnnotationExcelWriter excelWriter, Object template) {
        this.excelWriter = excelWriter;
        this.template = Preconditions.checkNotNull(template);
    }

    public static SimpleExcelWriter create(File template) {
        return new SimpleExcelWriter(AnnotationExcelWriter.create(), template);
    }

    public static SimpleExcelWriter create(InputStream template) {
        return new SimpleExcelWriter(AnnotationExcelWriter.create(), template);
    }

    public SimpleExcelWriter setInterceptor(ExcelWriteInterceptor interceptor) {
        this.excelWriter.setInterceptor(interceptor);
        return this;
    }

    public void writeTable(List<?> data, OutputStream output) {
        this.excelWriter.addTable(data.get(0).getClass(), (List) JSON.parseArray(JSON.toJSONString(data)));
        this.write(output);
    }

    public void writeTable(List<?> data, TableCellMergeRangesMatcher matcher, OutputStream output) {
        this.excelWriter.addTable(data.get(0).getClass(), (List) JSON.parseArray(JSON.toJSONString(data)), matcher);
        this.write(output);
    }

    public void writeTable(List<?> data, List<TableCellMergeRange> tableCellMergeRanges, OutputStream output) {
        this.excelWriter.addTable(data.get(0).getClass(), (List) JSON.parseArray(JSON.toJSONString(data)), tableCellMergeRanges);
        this.write(output);
    }

    public void writePoints(Object data, OutputStream output) {
        this.excelWriter.addPoints(data.getClass(), JSON.parseObject(JSON.toJSONString(data)));
        this.write(output);
    }

    private void write(OutputStream output) {
        if (InputStream.class.isAssignableFrom(this.template.getClass())) {
            this.excelWriter.write((InputStream) this.template, output);
            return;
        }
        this.excelWriter.write((File) this.template, output);
    }
}
