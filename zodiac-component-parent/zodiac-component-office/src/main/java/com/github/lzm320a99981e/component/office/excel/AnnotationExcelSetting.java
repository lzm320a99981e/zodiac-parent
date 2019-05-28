package com.github.lzm320a99981e.component.office.excel;

public interface AnnotationExcelSetting {
    AnnotationExcelSetting setTableLimit(Class<?> type, int start);

    AnnotationExcelSetting setTableLimit(Class<?> type, int start, int size);
}
