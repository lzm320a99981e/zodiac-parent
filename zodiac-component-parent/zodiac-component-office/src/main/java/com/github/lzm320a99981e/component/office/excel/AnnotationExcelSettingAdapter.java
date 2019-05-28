package com.github.lzm320a99981e.component.office.excel;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

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
}
