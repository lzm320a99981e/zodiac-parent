package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于注解操作Excel的配置适配器
 */
public class AnnotationExcelSettingAdapter implements AnnotationExcelSetting {
    protected Map<Class, int[]> tableLimitMap = new HashMap<>();

    @Override
    public AnnotationExcelSetting setTableLimit(Class<?> type, int start) {
        Preconditions.checkNotNull(type);
        Preconditions.checkState(start >= 0);
        this.tableLimitMap.put(type, new int[]{start});
        return this;
    }

    @Override
    public AnnotationExcelSetting setTableLimit(Class<?> type, int start, int size) {
        Preconditions.checkNotNull(type);
        Preconditions.checkState(start >= 0 && size > 0);
        this.tableLimitMap.put(type, new int[]{start, size});
        return this;
    }

    protected Table parseTableType(Class<?> type) {
        final Table table = ExcelHelper.classToTable(type);
        if (!this.tableLimitMap.containsKey(type)) {
            return table;
        }
        final int[] limit = this.tableLimitMap.get(type);
        table.setStartRowNumber(limit[0]);
        if (limit.length == 2) {
            table.setSize(limit[1]);
        }
        return table;
    }
}
