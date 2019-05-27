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
    private Integer startRowNumber;
    /**
     * 表格列
     */
    private List<Point> columns;
    /**
     * 数据名称
     */
    private String dataKey;

    public Table(String sheetName, Integer startRowNumber) {
        this.sheetName = Preconditions.checkNotNull(sheetName);
        this.startRowNumber = Preconditions.checkNotNull(startRowNumber);
    }

    public Table(Integer sheetIndex, Integer startRowNumber) {
        this.sheetIndex = Preconditions.checkNotNull(sheetIndex);
        this.startRowNumber = Preconditions.checkNotNull(startRowNumber);
    }

    public Table(String sheetName, Integer startRowNumber, String dataKey) {
        this(sheetName, startRowNumber);
        this.dataKey = dataKey;
    }

    public Table(Integer sheetIndex, Integer startRowNumber, String dataKey) {
        this(sheetIndex, startRowNumber);
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
    private Map<Integer, String> columnNumberWithDataKeyMap;

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

    public Map<Integer, String> getColumnNumberWithDataKeyMap() {
        if (Objects.nonNull(this.columnNumberWithDataKeyMap)) {
            return this.columnNumberWithDataKeyMap;
        }
        this.columnNumberWithDataKeyMap = new LinkedHashMap<>();
        Map<String, Integer> dataKeyWithColumnNumberMap = getDataKeyWithColumnNumberMap();
        for (Map.Entry<String, Integer> entry : dataKeyWithColumnNumberMap.entrySet()) {
            this.columnNumberWithDataKeyMap.put(entry.getValue(), entry.getKey());
        }
        return this.columnNumberWithDataKeyMap;
    }
}


