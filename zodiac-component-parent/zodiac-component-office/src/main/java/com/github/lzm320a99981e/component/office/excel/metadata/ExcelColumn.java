package com.github.lzm320a99981e.component.office.excel.metadata;

import java.lang.annotation.*;

/**
 * excel表格列注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * 位置
     *
     * @return
     */
    int index() default Integer.MIN_VALUE;
}
