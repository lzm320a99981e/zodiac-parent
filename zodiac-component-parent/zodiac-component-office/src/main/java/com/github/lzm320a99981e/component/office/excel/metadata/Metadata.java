package com.github.lzm320a99981e.component.office.excel.metadata;

/**
 * 元数据
 */
public interface Metadata {
    /**
     * 获取sheet名称
     *
     * @return
     */
    String getSheetName();

    /**
     * 获取sheet索引
     *
     * @return
     */
    Integer getSheetIndex();

    /**
     * 获取数据名称
     *
     * @return
     */
    String getDataKey();
}
