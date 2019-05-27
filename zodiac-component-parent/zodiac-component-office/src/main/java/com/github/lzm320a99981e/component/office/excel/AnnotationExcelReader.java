package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.interceptor.DefaultExcelReadInterceptor;
import com.github.lzm320a99981e.component.office.excel.interceptor.ExcelReadInterceptor;
import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 读取
 */
public class AnnotationExcelReader {
    private List<Table> tables = new ArrayList<>();
    private List<Point> points = new ArrayList<>();
    private ExcelReadInterceptor interceptor = new DefaultExcelReadInterceptor();

    public static AnnotationExcelReader create() {
        return new AnnotationExcelReader();
    }

    public AnnotationExcelReader setInterceptor(ExcelReadInterceptor interceptor) {
        this.interceptor = Preconditions.checkNotNull(interceptor);
        return this;
    }

    public AnnotationExcelReader addTable(Class<?> type) {
        return addTable(type, type.getName());
    }

    public AnnotationExcelReader addTable(Class<?> type, String dataKey) {

        return this;
    }

    public AnnotationExcelReader addPoint(Point point, String dataKey) {
        point.setDataKey(Preconditions.checkNotNull(dataKey));
        this.points.add(point);
        return this;
    }

    public AnnotationExcelReader addPoints(Map<String, Point> points) {
        points.keySet().forEach(dataKey -> addPoint(points.get(dataKey), dataKey));
        return this;
    }

}
