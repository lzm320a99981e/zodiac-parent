package com.github.lzm320a99981e.component.office.excel.metadata;

import com.google.common.base.Preconditions;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

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
     * 表格列
     */
    private List<Point> columns;
    /**
     * 数据名称
     */
    private String dataKey;

    public Table(String sheetName, Integer startRow) {
        this.sheetName = Preconditions.checkNotNull(sheetName);
        this.startRow = Preconditions.checkNotNull(startRow);
    }

    public Table(Integer sheetIndex, Integer startRow) {
        this.sheetIndex = Preconditions.checkNotNull(sheetIndex);
        this.startRow = Preconditions.checkNotNull(startRow);
    }

    public Table(String sheetName, Integer startRow, String dataKey) {
        this(sheetName, startRow);
        this.dataKey = dataKey;
    }

    public Table(Integer sheetIndex, Integer startRow, String dataKey) {
        this(sheetIndex, startRow);
        this.dataKey = dataKey;
    }

    public static Table create(String sheetName, Integer startRow) {
        return new Table(sheetName, startRow);
    }

    public static Table create(Integer sheetIndex, Integer startRow) {
        return new Table(sheetIndex, startRow);
    }

    public static Table create(String sheetName, Integer startRow, String dataKey) {
        return new Table(sheetName, startRow, dataKey);
    }

    public static Table create(Integer sheetIndex, Integer startRow, String dataKey) {
        return new Table(sheetIndex, startRow, dataKey);
    }


    private Map<String, Integer> dataKeyWithColumnNumberMap;

    public Map<String, Integer> getDataKeyWithColumnNumberMap() {
        if (Objects.nonNull(this.dataKeyWithColumnNumberMap)) {
            return this.dataKeyWithColumnNumberMap;
        }
        List<Point> sortedByColumnNumber = this.getColumns().stream().sorted(Comparator.comparingInt(Point::getColumnNumber)).collect(Collectors.toList());
        this.dataKeyWithColumnNumberMap = new LinkedHashMap<>();
        for (Point column : sortedByColumnNumber) {
            this.dataKeyWithColumnNumberMap.put(column.getDataKey(), column.getColumnNumber());
        }
        return this.dataKeyWithColumnNumberMap;
    }
}


