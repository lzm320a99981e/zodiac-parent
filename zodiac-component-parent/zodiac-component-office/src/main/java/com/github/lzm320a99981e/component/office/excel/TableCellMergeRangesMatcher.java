package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Table;

import java.util.List;
import java.util.Map;

/**
 * 表格单元格合并范围匹配
 */
public interface TableCellMergeRangesMatcher {
    List<TableCellMergeRange> match(Table table, List<Map<String, Object>> data);
}
