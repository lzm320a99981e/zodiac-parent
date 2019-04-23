package com.github.lzm320a99981e.component.office.word;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Word 操作辅助类
 */
public class WordHelper {
    /**
     * 复制表格行
     *
     * @param source
     * @param target
     */
    public static void copy(final XWPFTableRow source, final XWPFTableRow target) {
        // 复制底层属性
        target.getCtRow().setTrPr(source.getCtRow().getTrPr());
        // 复制单元格
        final AtomicInteger index = new AtomicInteger(0);
        source.getTableCells().forEach(cell -> {
            final XWPFTableCell targetCell = target.getCell(index.getAndIncrement());
            copy(cell, Objects.nonNull(targetCell) ? targetCell : target.createCell());
        });
    }

    /**
     * 复制表格单元格
     *
     * @param source
     * @param target
     */
    public static void copy(final XWPFTableCell source, final XWPFTableCell target) {
        // 复制底层属性
        target.getCTTc().setTcPr(source.getCTTc().getTcPr());
        // 移除目标单元格中的段落
        for (int i = 0; i < target.getParagraphs().size(); i++) {
            target.removeParagraph(i);
        }
        // 复制单元格段落
        source.getParagraphs().forEach(paragraph -> copy(paragraph, target.addParagraph()));
    }

    /**
     * 复制段落
     *
     * @param source
     * @param target
     */
    public static void copy(final XWPFParagraph source, final XWPFParagraph target) {
        // 复制底层属性
        target.getCTP().setPPr(source.getCTP().getPPr());
        // 复制段落中的文本
        source.getRuns().forEach(run -> copy(run, target.createRun()));
    }

    /**
     * 复制段落中的文本
     *
     * @param source
     * @param target
     */
    public static void copy(final XWPFRun source, final XWPFRun target) {
        // 复制底层属性
        target.getCTR().setRPr(source.getCTR().getRPr());
        // 复制文本
        target.setText(source.text());
    }

}
