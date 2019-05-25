package com.github.lzm320a99981e.component.office.excel;

import com.google.common.base.Preconditions;
import lombok.Data;

/**
 * 单元格定义
 */
@Data
public class Point implements Metadata {
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * sheet索引
     */
    private Integer sheetIndex;
    /**
     * 行号
     */
    private Integer rowNumber;
    /**
     * 列号
     */
    private Integer columnNumber;
    /**
     * 数据名称
     */
    private String dataKey;

    private Point(String sheetName, Integer rowNumber, Integer columnNumber, String dataKey) {
        this(rowNumber, columnNumber, dataKey);
        this.sheetName = Preconditions.checkNotNull(sheetName);
    }

    private Point(Integer sheetIndex, Integer rowNumber, Integer columnNumber, String dataKey) {
        this(rowNumber, columnNumber, dataKey);
        this.sheetIndex = Preconditions.checkNotNull(sheetIndex);
    }

    private Point(Integer rowNumber, Integer columnNumber, String dataKey) {
        this.rowNumber = Preconditions.checkNotNull(rowNumber);
        this.columnNumber = Preconditions.checkNotNull(columnNumber);
        this.dataKey = Preconditions.checkNotNull(dataKey);
    }

    public static Point create(String sheetName, Integer rowNumber, Integer columnNumber, String dataKey) {
        return new Point(sheetName, rowNumber, columnNumber, dataKey);
    }

    public static Point create(Integer sheetIndex, Integer rowNumber, Integer columnNumber, String dataKey) {
        return new Point(sheetIndex, rowNumber, columnNumber, dataKey);
    }
}
