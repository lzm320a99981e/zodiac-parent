package com.github.lzm320a99981e.compnent.office;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gsps.commons.WordHelper;
import com.gsps.commons.WordTemplateProcessor;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.*;
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
    //    static final File dir = Paths.get(Paths.get(basePath).getParent().toFile().getAbsolutePath(), "src/test/resources").toFile();
    static final File dir = Paths.get(basePath, "src/test/resources").toFile();


    @Test
    public void testDoc() throws Exception {
        File readFile = new File(dir, "EXAMPLE_EXPORT.doc");
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


        File writeFile = new File(dir, "EXAMPLE_EXPORT_OUTPUT.doc");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testTemplate() throws Exception {
        File readFile = new File(dir, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));
        Map<String, Object> data = Maps.newHashMap();
        data.put("name", "张三");
        data.put("name1", "张三1");
        data.put("name2", "张三2");
        data.put("name3", "张三3");
        data.put("phone", "133238432");

        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", "张三" + i);
            map.put("sex", "男");
            map.put("age", i * 10);
            mapList.add(map);
        }
        data.put("list", mapList);

        data.put("username", "133238432");
        data.put("password", "133238432");
        data.put("email", "133238432");
        data.put("captcha", "133238432");

        WordTemplateProcessor.create("${", "}").process(document, data);

        File writeFile = new File(dir, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testDaBian() throws Exception {
        File readFile = new File(dir, "答辩题目表.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));
        Map<String, Object> data = Maps.newHashMap();

        data.put("group", "事业10组");
        data.put("orderNo", "201800001");
        data.put("name", "张三");
        data.put("title", "论文标题111");
        data.put("question1", "答辩题目111");
        data.put("question2", "答辩题目222");
        data.put("question3", "答辩题目333");

        WordTemplateProcessor.create().process(document, data);

        File writeFile = new File(dir, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testList() throws Exception {
        File readFile = new File(dir, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

//        document.createParagraph().createRun().setText("少时诵诗书所所所所所多付绿扩军所绿多付");

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

        File writeFile = new File(dir, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testImage() throws Exception {
        File dir = Paths.get(basePath, "gsps-api", "src/main/resources/templates/word").toFile();
        File readFile = new File(dir, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

        // 添加一张图片
        final XWPFRun run = document.createParagraph().createRun();
        final File imageFile = new File(dir, "test.jpg");
        final FileInputStream imageStream = new FileInputStream(imageFile);
        final int format = XWPFDocument.PICTURE_TYPE_JPEG;
        run.setText("了深刻的解放路口世纪东方");
        run.addBreak(BreakType.TEXT_WRAPPING);
        run.addPicture(imageStream, format, "", Units.toEMU(200), Units.toEMU(200));

        File writeFile = new File(dir, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testTable() throws Exception {
//        File dir = Paths.get(Paths.get(basePath).getParent().toFile().getAbsolutePath(), "gsps-api", "src/main/resources/templates/word").toFile();
        File dir = Paths.get(basePath, "gsps-api", "src/main/resources/templates/word").toFile();
        File readFile = new File(dir, "EXAMPLE_EXPORT.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));
        final List<XWPFTable> tables = document.getTables();

        XWPFTable table = tables.get(0);
        WordHelper.copy(table.getRow(1), table.insertNewTableRow(table.getRows().size()));
        WordHelper.copy(table.getRow(1), table.insertNewTableRow(table.getRows().size()));


//        table.insertNewTableRow(2);

//        XWPFTableRow row = table.getRow(1);
//
//        final XWPFTableRow newRow = table.createRow();
//
//        // 复制原始属性
//        newRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
//
//        // 复制单元格
//        final List<XWPFTableCell> cells = row.getTableCells();
//        for (int i = 0; i < cells.size(); i++) {
//            final XWPFTableCell cell = cells.get(i);
//            final XWPFTableCell newCell = newRow.getCell(i);
//
//            // 复制原始属性
//            newCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
//
//            // 删除目标单元格中所有的段落
//            for (int j = 0; j < newCell.getParagraphs().size(); j++) {
//                newCell.removeParagraph(j);
//            }
//
//            // 复制段落
//            final List<XWPFParagraph> paragraphs = cell.getParagraphs();
//            for (XWPFParagraph paragraph : paragraphs) {
//                final XWPFParagraph newParagraph = newCell.addParagraph();
//
//                // 复制原始属性
//                newParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
//
//                // 复制段落中的文本
//                final List<XWPFRun> runs = paragraph.getRuns();
//                for (XWPFRun run : runs) {
//                    final XWPFRun newRun = newParagraph.createRun();
//
//                    // 复制原始属性
//                    newRun.getCTR().setRPr(run.getCTR().getRPr());
//                    // 复制文本
//                    newRun.setText(run.text());
//                }
//
//            }
//        }


        File writeFile = new File(dir, "tableOutput.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void testParagraph() throws Exception {
        File dir = Paths.get(basePath, "gsps-api", "src/main/resources/templates/word").toFile();
        File readFile = new File(dir, "EXAMPLE_EXPORT.docx");
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

        File writeFile = new File(dir, "tableOutput.docx");
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

    @Test
    public void testChars() {
        String[] split = "sdfsdf".split("");
        List<String> strings = Arrays.asList(split);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void testSubstring() {
        final String s = "${{{name}}}";
        final String substring = s.substring(4 - 1, s.length() - 3);
        System.out.println(substring);

        String name = "A1.管理层次及工作复杂程度";
        System.out.println(name.substring(name.indexOf(".") + 1));
    }

    @Test
    public void testGsps() throws Exception {
        File readFile = new File(dir, "SENIOR_ACCOUNTING_PROFESSIONAL_TECHNICAL_QUALIFICATION_APPRAISAL_FORM.docx");
        XWPFDocument document = new XWPFDocument(new FileInputStream(readFile));

        JSONObject data = JSON.parseObject("{" +
                "\"rpoOverview\":\"asfdasfdasfdasfdasfdasdfasdfasdf\"," +
                "\"competencydetailInfos\":[" +
                "{" +
                "\"witness\":\"阿明\"," +
                "\"projectintro\":\"1234123412342134111111111111111111111111111111111111123412341234213411111111111111111111111111111111111112341234123421341111111111111111111111111111111111111234123412342134111111111111111111111111111111111111123412341234\"," +
                "\"endtime\":\"201906\"," +
                "\"project\":\"高快设计\"," +
                "\"begintime\":\"201104\"," +
                "\"projectrelation\":\"1234123412342134111111111111111111111111111111111111123412341234213411111111111111111111111111111111111112341234123421341111111111111111111111111111111111111234123412342134111111111111111111111111111111111111\"" +
                "}" +
                "]," +
                "\"achievementInfos\":[]," +
                "\"paperInfo\":{" +
                "\"ipublish\":\"已发表\"," +
                "\"absTract\":\"人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎人生路 快乐少年郎\"," +
                "\"thesis\":\"asdfasdfasdf\"," +
                "\"publishDate\":\"20181202\"," +
                "\"writeDate\":\"20181204\"," +
                "\"title\":\"fasfasfdsadfsafd\"" +
                "}," +
                "\"extra\":\"阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达阿法法师法师法发大水发达\"," +
                "\"coverInfo\":{" +
                "\"workUnit\":\"我的工作单位\"," +
                "\"orderNo\":\"1910001\"," +
                "\"month\":\"04\"," +
                "\"areaName\":\"A5251\"," +
                "\"year\":\"2019\"," +
                "\"name\":\"测试\"," +
                "\"postDate\":\"20190413\"," +
                "\"day\":\"13\"," +
                "\"declarationQualifications\":\"高级会计师\"" +
                "}," +
                "\"publicationInfos\":[" +
                "{" +
                "\"author\":\"22\"," +
                "\"publication\":\"大师傅大是大非\"," +
                "\"publish\":\"201502\"," +
                "\"thesis\":\"示范法\"," +
                "\"totalsection\":\" 2222\"" +
                "}" +
                "]," +
                "\"basicInfo\":{" +
                "\"referenceEducation\":{" +
                "\"college\":\"清华大学\"," +
                "\"eduyear\":\"4\"," +
                "\"speciality\":\"计算机科学与技术\"," +
                "\"edu\":\"4\"," +
                "\"certificate\":\"1231546\"," +
                "\"degree\":\"5\"," +
                "\"gradute\":\"201903\"" +
                "}," +
                "\"workUnit\":\"我的工作单位\"," +
                "\"pictureFileId\":\"38ddb72a332d466e85f8ed8436da6950\"," +
                "\"unittype\":\"行政单位\"," +
                "\"unitcount\":\"50\"," +
                "\"nation\":\"蒙古族\"," +
                "\"idCard\":\"110011199909091001\"," +
                "\"sex\":\"女\"," +
                "\"technicalQualification\":{" +
                "\"accno\":\"123465789\"," +
                "\"accexamtime\":\"20190423\"," +
                "\"accscore\":70" +
                "}," +
                "\"academeduty\":\"羽毛球协会\"," +
                "\"birth\":\"19990909\"," +
                "\"academe\":\"羽毛球协会\"," +
                "\"unitlevel\":\"二级\"," +
                "\"dept\":\"研发部\"," +
                "\"title\":\"会计行政职务\"," +
                "\"resumes\":[" +
                "{" +
                "\"workunit\":\"手动阀\"," +
                "\"duty\":\"撒大幅\"," +
                "\"endtime\":\"201902\"," +
                "\"begintime\":\"201901\"," +
                "\"dept\":\"撒大幅\"" +
                "}," +
                "{" +
                "\"workunit\":\"腾讯\"," +
                "\"duty\":\"事业部经理\"," +
                "\"endtime\":\"201908\"," +
                "\"begintime\":\"201902\"," +
                "\"dept\":\"事业部\"" +
                "}" +
                "]," +
                "\"unitscale\":\"500\"," +
                "\"workTime\":\"201801\"," +
                "\"accedus\":[]," +
                "\"evaluationEducation\":{" +
                "\"college\":\"北京大学\"," +
                "\"eduyear\":\"3\"," +
                "\"speciality\":\"\"," +
                "\"edu\":\"大学普通班\"," +
                "\"certificate\":\"12345678\"," +
                "\"degree\":\"双学士\"," +
                "\"gradute\":\"201902\"" +
                "}," +
                "\"service\":\"201802\"," +
                "\"titlegettime\":\"201903\"," +
                "\"name\":\"测试\"," +
                "\"duty\":\"\"" +
                "}" +
                "}");
        JSONArray accedus = data.getJSONObject("basicInfo").getJSONArray("accedus");
        for (int i = 0; i < 5; i++) {
            GpAccedu accedu = GpAccedu.builder().begintime("2018020" + i).endtime("2017010" + i).ogan("ogan" + i).content("content1").model("model1").hour("4").result("通过").build();
            accedus.add(JSON.toJSON(accedu));
        }

        WordTemplateProcessor.create().process(document, data);
        File writeFile = new File(dir, "高级会计专业技术资格评审申报表Output.docx");
        document.write(new FileOutputStream(writeFile));
        document.close();
    }

    @Test
    public void test3() {
        System.out.println("20181102".substring(0, 4));
        System.out.println("20181102".substring(4, 6));
        System.out.println("20181102".substring(6, 8));
    }

    @Builder
    @Data
    public static class GpAccedu {
        /* 主键 */
        private Long id;
        /* 报名ID */
        private String signupId;
        /* 起始时间(20110101)	 */
        private String begintime;
        /* 截止时间(20110101)_	 */
        private String endtime;
        /* 组织单位 */
        private String ogan;
        /* 学习内容 */
        private String content;
        /* 学习形式 */
        private String model;
        /* 学时 */
        private String hour;
        /* 考试考核结果 */
        private String result;
        /* 培训文件ID */
        private String trainFileId;
        /* 培训文件名称(03教育培训+起始时)	 */
        private String trainFileName;
    }
}
