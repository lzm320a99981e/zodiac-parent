package com.github.lzm320a99981e.component.office.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表格单元格合并区域
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableCellMergeRange {
    /**
     * 开始行数据索引
     */
    private Integer startRowDataIndex;
    /**
     * 结束行数据索引
     */
    private Integer endRowDataIndex;
    /**
     * 开始行数据Key
     */
    private String startColumnDataKey;
    /**
     * 结束行数据Key
     */
    private String endColumnDataKey;
}
