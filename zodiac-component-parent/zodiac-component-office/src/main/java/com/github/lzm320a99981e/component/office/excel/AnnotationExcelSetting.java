package com.github.lzm320a99981e.component.office.excel;

/**
 * 基于注解配置的Excel操作(read & write)的配置
 */
public interface AnnotationExcelSetting {
    /**
     * 设置表格范围
     *
     * @param type
     * @param start
     * @return
     * @see com.github.lzm320a99981e.component.office.excel.metadata.ExcelTable
     */
    AnnotationExcelSetting setTableLimit(Class<?> type, int start);

    /**
     * 设置表格范围
     *
     * @param type
     * @param start
     * @param size
     * @return
     * @see com.github.lzm320a99981e.component.office.excel.metadata.ExcelTable
     */
    AnnotationExcelSetting setTableLimit(Class<?> type, int start, int size);
}
