package com.github.lzm320a99981e.component.office.excel.interceptor;

import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

/**
 * excel写入拦截器
 */
public interface ExcelWriteInterceptor {
    /**
     * 写入table前拦截
     *
     * @param sheet
     * @param table
     * @param data
     * @return
     */
    default List<Map<String, Object>> beforeWriteTable(Sheet sheet, Table table, List<Map<String, Object>> data) {
        return data;
    }

    /**
     * 写入table后拦截
     *
     * @param sheet
     * @param table
     * @param data
     */
    default void afterWriteTable(Sheet sheet, Table table, List<Map<String, Object>> data) {
    }

    /**
     * 写入row前拦截
     *
     * @param row
     * @param table
     * @param data
     * @return
     */
    default Map<String, Object> beforeWriteRow(Row row, Table table, Map<String, Object> data) {
        return data;
    }

    /**
     * 写入row后拦截
     *
     * @param row
     * @param table
     * @param data
     */
    default void afterWriteRow(Row row, Table table, Map<String, Object> data) {
    }

    /**
     * 写入cell前拦截
     *
     * @param cell
     * @param table
     * @param data
     * @return
     */
    default Object beforeWriteCell(Cell cell, Table table, Object data) {
        return data;
    }

    /**
     * 写入cell后拦截
     *
     * @param cell
     * @param table
     * @param data
     */
    default void afterWriteCell(Cell cell, Table table, Object data) {
    }

    /**
     * 写入point前拦截
     *
     * @param cell
     * @param point
     * @param data
     * @return
     */
    default Object beforeWritePoint(Cell cell, Point point, Object data) {
        return data;
    }

    /**
     * 写入point后拦截
     *
     * @param cell
     * @param point
     * @param data
     */
    default void afterWritePoint(Cell cell, Point point, Object data) {
    }

    /**
     * 发生异常后拦截
     *
     * @param e
     */
    default void onException(Exception e) {
        throw ExceptionHelper.wrappedRuntimeException(e);
    }
}
