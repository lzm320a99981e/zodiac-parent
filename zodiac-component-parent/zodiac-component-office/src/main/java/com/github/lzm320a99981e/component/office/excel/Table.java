package com.github.lzm320a99981e.component.office.excel;

import com.google.common.base.Preconditions;
import lombok.Data;

import java.util.List;

/**
 * 表格定义
 */
@Data
public class Table implements Metadata {
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * sheet索引
     */
    private Integer sheetIndex;
    /**
     * 开始行
     */
    private Integer startRow;
    /**
     * 表格大小
     */
    private Integer size;
    /**
     * 表格列
     */
    private List<Point> columns;
    /**
     * 数据名称
     */
    private String dataKey;

    public Table(String sheetName, Integer startRow, String dataKey) {
        this(startRow, dataKey);
        this.sheetName = Preconditions.checkNotNull(sheetName);
    }

    public Table(Integer sheetIndex, Integer startRow, String dataKey) {
        this(startRow, dataKey);
        this.sheetIndex = Preconditions.checkNotNull(sheetIndex);
    }

    public Table(Integer startRow, String dataKey) {
        this.startRow = Preconditions.checkNotNull(startRow);
        this.dataKey = Preconditions.checkNotNull(dataKey);
    }

    public static Table create(String sheetName, Integer startRow, String dataKey) {
        return new Table(sheetName, startRow, dataKey);
    }

    public static Table create(Integer sheetIndex, Integer startRow, String dataKey) {
        return new Table(sheetIndex, startRow, dataKey);
    }
}


