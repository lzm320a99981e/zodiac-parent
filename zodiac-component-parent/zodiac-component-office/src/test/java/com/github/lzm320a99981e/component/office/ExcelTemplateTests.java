package com.github.lzm320a99981e.component.office;

import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelTemplateTests {
    static final String basePath = System.getProperty("user.dir");
    static final File resources = Paths.get(basePath, "src/test/resources").toFile();

    @Test
    public void test() throws IOException {
        final Workbook workbook = WorkbookFactory.create(new File(resources, "test.xlsx"));
        System.out.println(workbook);
        final Sheet sheet = workbook.getSheetAt(0);
        final int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i < lastRowNum; i++) {
            final Row row = sheet.getRow(i);
            // 判断是否是模板行

            final short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                row.getCell(j).removeCellComment();
            }
        }
    }

    @Test
    public void test2() throws IOException {
        final Workbook workbook = WorkbookFactory.create(new File(resources, "test.xlsx"));
        final RichTextString richTextString= workbook.getSheetAt(0).getRow(11).getCell(0).getRichStringCellValue();
        final int runs = richTextString.numFormattingRuns();
        System.out.println(runs);
    }

    private boolean isTableRow(Row row) {
        final Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            final Cell cell = cellIterator.next();
            final String value = cell.getStringCellValue();
            final RichTextString richStringCellValue = cell.getRichStringCellValue();


        }
        return false;
    }

    @Test
    public void testMatchTemplateVar() {
        String var = "姓名：{name}，年龄：{{age}}岁，省份证号：{goods}";
        final Pattern pattern = Pattern.compile("\\{[^{}]+}");
        final Matcher matcher = pattern.matcher(var);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
