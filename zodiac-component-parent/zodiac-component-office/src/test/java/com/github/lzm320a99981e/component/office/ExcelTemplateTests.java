package com.github.lzm320a99981e.component.office;

import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.component.office.excel.ExcelWriter;
import com.github.lzm320a99981e.component.office.excel.TableCellMergeStrategy;
import com.github.lzm320a99981e.component.office.excel.metadata.Point;
import com.github.lzm320a99981e.component.office.excel.metadata.Table;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExcelTemplateTests {
    static final String basePath = System.getProperty("user.dir");
    static final File resources = Paths.get(basePath, "src/test/resources").toFile();

    @Test
    public void test() throws Exception {
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
    public void test2() throws Exception {
        final Workbook workbook = WorkbookFactory.create(new File(resources, "test.xlsx"));
        final RichTextString richTextString = workbook.getSheetAt(0).getRow(11).getCell(0).getRichStringCellValue();
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
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    public void testFrequency() {
        List<Integer> numbers = Arrays.asList(new Integer[]{1, 2, 1, 3, 4, 4});
        numbers.stream().filter(i -> Collections.frequency(numbers, i) > 1)
                .collect(Collectors.toSet()).forEach(System.out::println);
    }

    @Test
    public void test3() throws Exception {
        Table table = Table.create(0, 1);
        Integer sheetIndex = table.getSheetIndex();
        Integer startRow = table.getStartRow();
        // 姓名	年龄	性别	出生
        table.setColumns(
                Arrays.asList(
                        Point.create(sheetIndex, startRow, 0, "name"),
                        Point.create(sheetIndex, startRow, 2, "age"),
                        Point.create(sheetIndex, startRow, 3, "birth")
                )
        );

        Table table1 = Table.create(0, 7);
        Integer sheetIndex1 = table.getSheetIndex();
        Integer startRow1 = table.getStartRow();
        // 姓名	年龄	性别	出生
        table1.setColumns(
                Arrays.asList(
                        Point.create(sheetIndex1, startRow1, 0, "name"),
                        Point.create(sheetIndex1, startRow1, 2, "age"),
                        Point.create(sheetIndex1, startRow1, 3, "birth")
                )
        );

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject rowData = new JSONObject();
            rowData.put("name", "zhangsan");
            rowData.put("age", "zhangsan" + i);
            rowData.put("birth", "zhangsan" + i);
            data.add(rowData);
        }
        File template = new File(resources, "test.xlsx");
        byte[] bytes = ExcelWriter.create()
                .addTable(table, data, TableCellMergeStrategy.REPEAT)
                .addTable(table1, data, TableCellMergeStrategy.REPEAT)
                .addPoint(Point.create(sheetIndex, 10, 1), "熊逸")
                .addPoint(Point.create(sheetIndex, 11, 1), "熊逸1234")
                .addPoint(Point.create(sheetIndex, 12, 1), "234234234324")
                .write(template);
        Files.write(Paths.get(template.getParentFile().getAbsolutePath(), "output.xlsx"), bytes);
    }

    @Test
    public void testShiftRows() throws Exception {
        File template = new File(resources, "test.xlsx");
        FileInputStream input = new FileInputStream(template);
        Workbook workbook = WorkbookFactory.create(input);
        Sheet sheet = workbook.getSheetAt(0);
        sheet.shiftRows(1, sheet.getLastRowNum(), 5, true, false);
        Path output = Paths.get(template.getParentFile().getAbsolutePath(), "output.xlsx");
        FileOutputStream outputStream = new FileOutputStream(output.toFile());

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        input.close();
    }

    @Test
    public void testMergedRegion() throws Exception {
        File template = new File(resources, "test.xlsx");
        FileInputStream input = new FileInputStream(template);
        Workbook workbook = WorkbookFactory.create(input);
        Sheet sheet = workbook.getSheetAt(0);
//        sheet.addMergedRegion()

        sheet.shiftRows(1, sheet.getLastRowNum(), 5, true, false);
        Path output = Paths.get(template.getParentFile().getAbsolutePath(), "output.xlsx");
        FileOutputStream outputStream = new FileOutputStream(output.toFile());

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        input.close();
    }
}
