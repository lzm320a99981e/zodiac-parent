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
 * excel读取拦截器
 */
public interface ExcelReadInterceptor {
    /**
     * 读取table前拦截
     *
     * @param sheet
     * @param table
     * @return
     */
    default boolean beforeReadTable(Sheet sheet, Table table) {
        return true;
    }

    /**
     * 读取table后拦截
     *
     * @param sheet
     * @param table
     * @param data
     */
    default List<Map<String, Object>> afterReadTable(Sheet sheet, Table table, List<Map<String, Object>> data) {
        return data;
    }

    /**
     * 读取row前拦截
     *
     * @param row
     * @param table
     * @return
     */
    default boolean beforeReadRow(Row row, Table table) {
        return true;
    }

    /**
     * 读取row后拦截
     *
     * @param row
     * @param table
     * @param data
     */
    default Map<String, Object> afterReadRow(Row row, Table table, Map<String, Object> data) {
        return data;
    }

    /**
     * 读取cell前拦截
     *
     * @param cell
     * @param table
     * @return
     */
    default boolean beforeReadCell(Cell cell, Table table) {
        return true;
    }

    /**
     * 读取cell后拦截
     *
     * @param cell
     * @param table
     * @param data
     */
    default Object afterReadCell(Cell cell, Table table, Object data) {
        return data;
    }

    /**
     * 读取point前拦截
     *
     * @param cell
     * @param point
     * @return
     */
    default boolean beforeReadPoint(Cell cell, Point point) {
        return true;
    }

    /**
     * 读取point拦截
     *
     * @param cell
     * @param point
     * @param data
     */
    default Object afterReadPoint(Cell cell, Point point, Object data) {
        return data;
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
