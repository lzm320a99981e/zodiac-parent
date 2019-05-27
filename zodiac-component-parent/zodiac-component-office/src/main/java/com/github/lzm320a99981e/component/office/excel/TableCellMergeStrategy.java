package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 表格单元格合并策略
 */
public enum TableCellMergeStrategy {
    /**
     * 重复合并(
     * 跨行：[[张三,18],[张三,19],[张三,20]] -> [张三 -> 夸三行, 18],[19],[20]
     * 跨列：[张三, 李四, 王五, 合计，合计，合计] -> [张三, 李四, 王五, 合计 -> 跨三列]
     */
    REPEAT;

    /**
     * 查找合并单元格区域
     *
     * @param data
     * @return
     */
    public List<TableCellMergeRange> findCellMergeRanges(Table table, List<Map<String, Object>> data) {
        List<TableCellMergeRange> tableCellMergeRanges = new ArrayList<>();
        Map<String, RowspanCounter> rowspanCounterMap = new HashMap<>();

        int dataSize = data.size();
        Integer tableSize = table.getSize();
        if (Objects.nonNull(tableSize) && tableSize > 0 && tableSize < dataSize) {
            dataSize = tableSize;
        }

        for (int i = 0; i < dataSize; i++) {
            Map<String, Object> rowData = data.get(i);

            // 获取跨列
            List<String> keys = new ArrayList<>(table.getDataKeyWithColumnNumberMap().keySet());
            ColspanCounter colspanCounter = new ColspanCounter(keys.get(0));
            for (int j = 1; j < keys.size(); j++) {
                String startColumnDataKey = colspanCounter.getStartColumnDataKey();
                String columnDataKey = keys.get(j);

                if (!Objects.equals(rowData.get(startColumnDataKey), rowData.get(columnDataKey))) {
                    if (colspanCounter.getCount().get() > 0) {
                        tableCellMergeRanges.add(new TableCellMergeRange(i, i, startColumnDataKey, columnDataKey));
                    }
                    colspanCounter = new ColspanCounter(columnDataKey);
                    continue;
                }
                colspanCounter.getCount().incrementAndGet();
            }
            if (colspanCounter.getCount().get() > 0) {
                tableCellMergeRanges.add(new TableCellMergeRange(i, i, colspanCounter.getStartColumnDataKey(), keys.get(keys.size() - 1)));
            }

            // 获取跨行
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (!rowspanCounterMap.containsKey(key)) {
                    if (Objects.nonNull(value)) {
                        rowspanCounterMap.put(key, new RowspanCounter(key, i));
                    }
                    continue;
                }

                // 判断是否满足合并条件
                Object previous = data.get(i - 1).get(key);
                RowspanCounter rowspanCounter = rowspanCounterMap.get(key);
                // 不满足 -> 没有连续重复
                if (!Objects.equals(previous, value)) {
                    if (rowspanCounter.getCount().get() > 0) {
                        tableCellMergeRanges.add(new TableCellMergeRange(rowspanCounter.getStartRowIndex(), rowspanCounter.getRowEndIndex(), key, key));
                    }
                    rowspanCounterMap.put(key, new RowspanCounter(key, i));
                    continue;
                }
                // 满足 -> 连续重复
                rowspanCounter.getCount().incrementAndGet();
            }
        }
        for (Map.Entry<String, RowspanCounter> entry : rowspanCounterMap.entrySet()) {
            String key = entry.getKey();
            RowspanCounter rowspanCounter = entry.getValue();
            if (rowspanCounter.getCount().get() > 0) {
                tableCellMergeRanges.add(new TableCellMergeRange(rowspanCounter.getStartRowIndex(), rowspanCounter.getRowEndIndex(), key, key));
            }
        }

        return tableCellMergeRanges;
    }

    @Data
    @NoArgsConstructor
    static class RowspanCounter {
        private String columnDataKey;
        private Integer startRowIndex;
        private AtomicInteger count = new AtomicInteger(0);

        Integer getRowEndIndex() {
            return this.startRowIndex + this.count.get();
        }

        RowspanCounter(String columnDataKey, Integer startRowIndex) {
            this.columnDataKey = columnDataKey;
            this.startRowIndex = startRowIndex;
        }
    }

    @Data
    @NoArgsConstructor
    static class ColspanCounter {
        private String startColumnDataKey;
        private AtomicInteger count = new AtomicInteger(0);

        ColspanCounter(String startColumnDataKey) {
            this.startColumnDataKey = startColumnDataKey;
        }
    }
}
