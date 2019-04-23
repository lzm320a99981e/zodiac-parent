package com.github.lzm320a99981e.component.office;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 参考：
 * <p>
 * http://www.xdocin.com/xcache/72qraomol5ppuzqn7kmq5jbabm.html word报表
 * http://53873039oycg.iteye.com/blog/2185999
 * https://www.cnblogs.com/yfrs/p/wordpoi.html
 * https://blog.csdn.net/qq_23555141/article/details/76016880
 * https://www.jianshu.com/p/0a32d8bd6878
 * </p>
 */
public class WordTemplateTests {
    static final String basePath = System.getProperty("user.dir");
    static final File resources = Paths.get(basePath, "src/test/resources").toFile();



    @Test
    public void testDoc() throws Exception {
        File readFile = new File(resources, "EXAMPLE_EXPORT.doc");
        HWPFDocument document = new HWPFDocument(new FileInputStream(readFile));
        Range range = document.getRange();

        // 段落
        final Section section = range.getSection(0);
        for (int i = 0; i < section.numParagraphs(); i++) {
            final Paragraph paragraph = section.getParagraph(i);
            if (paragraph.isInTable()) {
                continue;
            }
            final StringBuilder text = new StringBuilder();
            for (int j = 0; j < paragraph.numCharacterRuns(); j++) {
//                paragraph.replaceText("${name}", "张光勇");
                final CharacterRun run = paragraph.getCharacterRun(j);
                text.append(run.text());
            }
            System.out.println(text.toString());
//            System.out.println(paragraph.text() + " -> " + paragraph.isInTable() + " -> " + paragraph.isInList());
        }


        // 表格
//        final TableIterator tableIterator = new TableIterator(range);
//        while (tableIterator.hasNext()) {
//            final Table table = tableIterator.next();
//            for (int i = 0; i < table.numRows(); i++) {
//
//                final TableRow row = table.getRow(i);
//                for (int j = 0; j < row.numCells(); j++) {
//                    final TableCell cell = row.getCell(j);
//                    cell.replaceText("${list.name}", "张三");
//                    cell.replaceText("${username}", "往无觅");
//                    cell.replaceText("${list.sex}", "人水电费妖");
//                    cell.replaceText("${email}", "234234234@qq.com");
//                    cell.replaceText("${list.age}", "年龄3333@qq.com");
//                    cell.replaceText("${password}", "4444444444444");
//
//                    System.out.println(cell.text());
//                }
//            }
//        }


        File writeFile = new File(resources, "EXAMPLE_EXPORT_OUTPUT.doc");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testList() throws Exception {
        File readFile = new File(resources, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

//        template.createParagraph().createRun().setText("少时诵诗书所所所所所多付绿扩军所绿多付");

        final List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            System.out.println(paragraph.getElementType() + " - " + paragraph.getText());
        }

        final XWPFParagraph source = paragraphs.get(paragraphs.size() - 2);
        final XWPFParagraph target = document.createParagraph();

        // 复制原始属性
        target.getCTP().setPPr(source.getCTP().getPPr());

        // 复制段落中的文本
        final List<XWPFRun> runs = source.getRuns();
        for (XWPFRun run : runs) {
            final XWPFRun newRun = target.createRun();

            // 复制原始属性
            newRun.getCTR().setRPr(run.getCTR().getRPr());
            // 复制文本
            newRun.setText(run.text());
        }

        File writeFile = new File(resources, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testImage() throws Exception {
        File readFile = new File(resources, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

        // 添加一张图片
        final XWPFRun run = document.createParagraph().createRun();
        final File imageFile = new File(resources, "test.jpg");
        final FileInputStream imageStream = new FileInputStream(imageFile);
        final int format = XWPFDocument.PICTURE_TYPE_JPEG;
        run.setText("了深刻的解放路口世纪东方");
        run.addBreak(BreakType.TEXT_WRAPPING);
        run.addPicture(imageStream, format, "", Units.toEMU(200), Units.toEMU(200));

        File writeFile = new File(resources, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testParagraph() throws Exception {
        File readFile = new File(resources, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

        // 段落
        final List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs.isEmpty()) {
                continue;
            }
            System.out.println(paragraph.getText());
            List<String> runTextList = runs.stream().map(XWPFRun::text).collect(Collectors.toList());

            // 模板字符
            final List<Character> templateSymbols = Arrays.asList('$', '{', '}');
            final ArrayList<List<Integer>> templateSymbolPositions = new ArrayList<>();
            List<Integer> templateSymbolPosition = new ArrayList<>();
            // 模板变量
            List<String> templateVariables = new ArrayList<>();
            String templateVariable = "";
            boolean matchedTemplateSymbol = false;

            for (int i = 0; i < runTextList.size(); i++) {
                final char[] chars = runTextList.get(i).toCharArray();

                for (int j = 0; j < chars.length; j++) {
                    if (matchedTemplateSymbol) {
                        templateVariable += chars[j];
                    }

                    final Character nextTemplateSymbol = templateSymbols.get(templateSymbolPosition.size() / 2);
                    if (nextTemplateSymbol == chars[j]) {
                        matchedTemplateSymbol = true;
                        templateSymbolPosition.add(i);
                        templateSymbolPosition.add(j);

                        if (templateSymbolPosition.size() == (templateSymbols.size() * 2)) {
                            templateSymbolPositions.add(templateSymbolPosition);
                            templateSymbolPosition = new ArrayList<>();

                            templateVariables.add(templateVariable);
                            templateVariable = "";
                            matchedTemplateSymbol = false;
                        }
                    }
                }
            }

            System.out.println(JSON.toJSONString(templateSymbolPositions, true));
            System.out.println(JSON.toJSONString(templateVariables, true));

            // 替换
            final List<Integer> toRemoveRunIndexs = new ArrayList<>();
            for (List<Integer> symbolPosition : templateSymbolPositions) {
                // 头部处理（替换）
                final Integer headRunIndex = symbolPosition.get(0);
                runs.get(headRunIndex).setText("张三1", symbolPosition.get(1));

                // 尾部处理（截断）
                final Integer tailRunIndex = symbolPosition.get(symbolPosition.size() - 2);
                if (tailRunIndex.equals(headRunIndex)) {
                    continue;
                }
                final XWPFRun tailRun = runs.get(tailRunIndex);
                final String tailRunText = tailRun.text();
                if (Objects.equals("}", tailRunText)) {
                    toRemoveRunIndexs.add(tailRunIndex);
                } else {
                    tailRun.setText(tailRunText, symbolPosition.get(symbolPosition.size() - 1));
                }

                // 中间处理（删除）
                for (int i = headRunIndex + 1; i < tailRunIndex; i++) {
                    toRemoveRunIndexs.add(i);
                }
            }

            for (Integer toRemoveRunIndex : toRemoveRunIndexs) {
                runs.get(toRemoveRunIndex).setText("", 0);
            }
        }

        File writeFile = new File(resources, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();

    }


    @Test
    public void testTemplateSymbolRegex() {
        String templateVariable = "${name}${sdsdf$}${age}";
        String regex = "\\$\\{[^${}]+}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(templateVariable);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
