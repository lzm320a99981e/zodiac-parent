package com.github.lzm320a99981e.component.office.excel.metadata;

import java.lang.annotation.*;

/**
 * excel表格注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelTable {
    /**
     * sheet名称(与 sheetIndex 二选一，优先使用sheetName)
     *
     * @return
     */
    String sheetName() default "";

    /**
     * sheet索引(与 sheetName 二选一，优先使用sheetName)
     *
     * @return
     */
    int sheetIndex() default 0;

    /**
     * 读取excel或写入excel的限制，与mysql分页用法类似，limit 0,5 标识，从第0行开始，读取或写入后面的5行
     *
     * @return
     */
    int[] limit();
}
