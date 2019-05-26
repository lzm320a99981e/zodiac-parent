package com.github.lzm320a99981e.component.office.excel;

import com.github.lzm320a99981e.component.office.excel.metadata.Metadata;
import com.google.common.base.Preconditions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Excel操作辅助类
 */
public class ExcelHelper {


    /**
     * 断言Excel是xlsx类型（2007）
     *
     * @param input
     * @return
     */
    public static boolean isXssf(InputStream input) {
        try {
            new XSSFWorkbook(input);
            return false;
        } catch (IOException e) {
            // ignore
            return true;
        }
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        if (null == cell) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        if (CellType.STRING == cellType) {
            return cell.getRichStringCellValue().getString();
        }
        if (CellType.NUMERIC == cellType) {
            return DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
        }
        if (CellType.BOOLEAN == cellType) {
            return cell.getBooleanCellValue();
        }
        if (CellType.FORMULA == cellType) {
            return cell.getNumericCellValue();
        }

        return null;
    }

    /**
     * 设置单元格的值
     *
     * @param cell
     * @param value
     * @return
     */
    public static Cell setCellValue(Cell cell, Object value) {
        if (null == cell || null == value) {
            return cell;
        }
        Class<?> valueClass = value.getClass();
        if (String.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((String) value);
            return cell;
        }
        if (Boolean.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((Boolean) value);
            return cell;
        }
        if (Date.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((Date) value);
            return cell;
        }
        if (Calendar.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((Calendar) value);
            return cell;
        }
        if (Double.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((Double) value);
            return cell;
        }
        if (RichTextString.class.isAssignableFrom(valueClass)) {
            cell.setCellValue((RichTextString) value);
            return cell;
        }
        cell.setCellValue(value.toString());
        return cell;
    }

    /**
     * 创建单元格（存在：直接返回，不存在：创建）
     *
     * @param row
     * @param cellIndex
     * @return
     */
    public static Cell createCellIfNonExistent(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (null != cell) {
            return cell;
        }
        return row.createCell(cellIndex);
    }

    /**
     * 创建行（存在：直接返回，不存在：创建）
     *
     * @param sheet
     * @param rowIndex
     * @return
     */
    public static Row createRowIfNonExistent(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (null != row) {
            return row;
        }
        return sheet.createRow(rowIndex);
    }

    /**
     * 复制行
     *
     * @param source
     * @param target
     */
    public static void copyRow(Row source, Row target) {
        copyRow(source, target, false);
    }

    /**
     * 复制行
     *
     * @param source
     * @param target
     * @param copyValue
     */
    public static void copyRow(Row source, Row target, boolean copyValue) {
        // 复制高度
        target.setHeightInPoints(source.getHeightInPoints());

        // 复制单元格
        Iterator<Cell> iterator = source.cellIterator();
        short firstCellNum = source.getFirstCellNum();
        while (iterator.hasNext()) {
            copyCell(iterator.next(), target.createCell(firstCellNum++), copyValue);
        }
        // 复制合并区域
        Sheet sheet = source.getSheet();
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        int sourceRowNum = source.getRowNum();
        int targetRowNum = target.getRowNum();
        for (CellRangeAddress mergedRegion : mergedRegions) {
            if (sourceRowNum == mergedRegion.getFirstRow() && sourceRowNum == mergedRegion.getLastRow()) {
                target.getSheet().addMergedRegion(new CellRangeAddress(targetRowNum, targetRowNum, mergedRegion.getFirstColumn(), mergedRegion.getLastColumn()));
            }
        }
    }

    /**
     * 复制单元格
     *
     * @param source
     * @param target
     */
    public static void copyCell(Cell source, Cell target) {
        copyCell(source, target, false);
    }

    /**
     * 复制单元格
     *
     * @param source
     * @param target
     * @param copyValue
     */
    public static void copyCell(Cell source, Cell target, boolean copyValue) {
        target.setCellStyle(source.getCellStyle());
        if (copyValue) {
            setCellValue(target, getCellValue(source));
        }
    }

    /**
     * 获取sheet
     *
     * @param workbook
     * @param metadata
     * @return
     */
    public static Sheet findSheet(Workbook workbook, Metadata metadata) {
        Integer sheetIndex = metadata.getSheetIndex();
        if (Objects.nonNull(sheetIndex)) {
            return Preconditions.checkNotNull(workbook.getSheetAt(sheetIndex), "根据 sheetIndex -> %s 未找到对应的sheet", sheetIndex);
        }
        String sheetName = metadata.getSheetName();
        return Preconditions.checkNotNull(workbook.getSheet(sheetName), "根据 sheetName -> %s 未找到对应的sheet", sheetName);
    }

}
