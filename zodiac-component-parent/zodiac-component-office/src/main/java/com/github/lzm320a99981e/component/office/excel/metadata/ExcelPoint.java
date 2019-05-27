package com.github.lzm320a99981e.component.office.excel.metadata;

import java.lang.annotation.*;

/**
 * excel表格注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelPoint {
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
     * 行号
     *
     * @return
     */
    int rowNumber();

    /**
     * 列号
     *
     * @return
     */
    int columnNumber();
}
