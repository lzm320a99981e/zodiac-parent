package com.github.lzm320a99981e.component.office.word;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Word模板处理类
 */
@Slf4j
public class WordTemplateProcessor {
    @Setter
    private WordProcessInterceptor interceptor;
    private WordProcessContext context;

    private String templateVariablePrefix = "${";
    private String templateVariableSuffix = "}";
    private List<Character> templateMarkCharacters = Arrays.asList('$', '{', '}');
    private Pattern templateVariablePattern = Pattern.compile("\\$\\{[^${}]}");


    public void process(XWPFDocument template, Map<String, Object> data) {
        this.context = new WordProcessContext();
        this.context.setTemplate(template);
        this.context.setTemplateData(data);
        try {
            // 文档处理开始
            if (Objects.equals(false, doInterceptor(context, InterceptorPhase.START_PROCESS_DOCUMENT))) {
                return;
            }

            // 段落处理
            template.getParagraphs().forEach(paragraph -> processParagraph(paragraph, data));
            // 表格处理
            template.getTables().forEach(table -> processTable(table, data));

            // 文档处理结束
            doInterceptor(context, InterceptorPhase.END_PROCESS_DOCUMENT);
        } catch (Exception e) {
            // 文档处理发生异常
            context.setException(e);
            doInterceptor(context, InterceptorPhase.ON_FAILURE);
        }
    }


    /**
     * 段落处理
     *
     * @param paragraph
     * @param data
     * @return
     */
    private void processParagraph(XWPFParagraph paragraph, Map<String, Object> data) {
        processParagraph(null, paragraph, data, 0);
    }

    /**
     * 表格处理
     *
     * @param table
     * @param data
     */
    private void processTable(XWPFTable table, Map<String, Object> data) {
        final int numberOfRows = table.getNumberOfRows();
        for (int i = 0; i < numberOfRows; i++) {
            final XWPFTableRow row = table.getRow(i);
            // 表格列表
            if (isIterableRow(row, data)) {
                processTableRow(row, data);
                continue;
            }
            // 表格表单
            processFormRow(row, data);
        }
    }

    /**
     * 重复的表头行记录（以便进行表头行之间进行比较）
     */
    private Map<String, AtomicInteger> repeatHeadRowMap = new HashMap<>();

    /**
     * 表格行处理（表格行是一个列表行）
     *
     * @param row
     * @param data
     */
    private void processTableRow(XWPFTableRow row, Map<String, Object> data) {
        final String variable = getFirstIterableVariable(row, data);
        final Collection value = (Collection) getVariableValue(variable, data);
        if (value.isEmpty()) {
            // 清空模板行
            row.getTableCells().forEach(cell -> cell.getParagraphs().forEach(p -> p.getRuns().forEach(r -> r.setText("", 0))));
            return;
        }
        // 只有一行数据，覆盖模板行
        int dataSize = value.size();
        if (dataSize == 1) {
            row.getTableCells().forEach(cell -> cell.getParagraphs().forEach(paragraph -> processParagraph(cell, paragraph, data, 0)));
            return;
        }

        final XWPFTable table = row.getTable();
        List<XWPFTableRow> rows = table.getRows();
        int startRowNumber = 0;
        for (int i = 0; i < rows.size(); i++) {
            if (Objects.equals(rows.get(i), row)) {
                startRowNumber = i;
                break;
            }
        }

        // 从第三行开始，第一行是表头，第二行是模板，所以从第三行开始
        int rowCount = rows.size();
        String headRowText = joinRowText(table.getRow(startRowNumber - 1));
        // 记录重复的表头行，以便从对应的数据集合取出对应索引的数据
        if (!repeatHeadRowMap.containsKey(headRowText)) {
            repeatHeadRowMap.put(headRowText, new AtomicInteger(0));
        }

        final int firstRowDataIndex = repeatHeadRowMap.get(headRowText).get();
        int dataIndex = firstRowDataIndex + 1;
        for (int i = startRowNumber + 1; (i < rowCount && dataIndex < dataSize); i++) {
            final XWPFTableRow insertRow = table.getRow(i);
            // 表头一致，跳过此行，从下一行开始插入
            String insertRowText = joinRowText(insertRow);
            if (Objects.equals(headRowText, insertRowText)) {
                continue;
            }

            // 遇到非空行，说明已经进入到下一个表格的表头行了
            if (insertRow.getTableCells().stream().anyMatch(item -> !item.getText().trim().isEmpty())) {
                break;
            }

            // 将表格模板行复制给插入的行，有了模板变量之后便可以插入数据
            WordHelper.copy(row, insertRow);
            // 数据是从第二条开始，第一条数据需要给模板行留着
            int finalDataIndex = dataIndex;
            insertRow.getTableCells().forEach(cell -> cell.getParagraphs().forEach(paragraph -> processParagraph(cell, paragraph, data, finalDataIndex)));
            dataIndex++;
        }
        repeatHeadRowMap.get(headRowText).set(dataIndex);

        row.getTableCells().forEach(cell -> cell.getParagraphs().forEach(paragraph -> processParagraph(cell, paragraph, data, firstRowDataIndex)));
    }

    /**
     * 将表格行每个单元的文本连接起来，以便进行比较，这里主要是表头与表头的比较，因为有的文档是表格是分页展示的，所以会出现一个表格的数据分成两个相同的表格来展示
     *
     * @param row
     * @return
     */
    private String joinRowText(XWPFTableRow row) {
        return row.getTableCells().stream().map(item -> item.getText().trim()).collect(Collectors.joining(",")).replaceAll("\\s*|\t|\r|\n", "");
    }

    /**
     * 表单行处理（表格行是一个表单）
     *
     * @param row
     * @param data
     */
    private void processFormRow(XWPFTableRow row, Map<String, Object> data) {
        row.getTableCells().forEach(cell -> cell.getParagraphs().forEach(paragraph -> processParagraph(paragraph, data)));
    }

    /**
     * 判断行是否为可迭代的行（根据行内单元格的变量对应的数据类型来判断 -> 集合或数组：true，否则：false）
     *
     * @param row
     * @param data
     * @return
     */
    private boolean isIterableRow(XWPFTableRow row, Map<String, Object> data) {
        return Objects.nonNull(getFirstIterableVariable(row, data));
    }

    /**
     * 获取单元格中第一个可迭代的变量（集合或者数组）
     *
     * @param row
     * @param data
     * @return
     */
    private String getFirstIterableVariable(XWPFTableRow row, Map<String, Object> data) {
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
            Matcher matcher = this.templateVariablePattern.matcher(cell.getText());
            while (matcher.find()) {
                // 模板变量
                String templateVariable = matcher.group().replace(this.templateVariablePrefix, "").replace(this.templateVariableSuffix, "");
                if (!templateVariable.contains(".")) {
                    continue;
                }
                // table.list.name -> table.list (截取到集合变量)
                templateVariable = templateVariable.substring(0, templateVariable.lastIndexOf("."));
                // 判断变量是否是集合类型
                Object variableValue = getVariableValue(templateVariable, data);
                // 集合
                if (Iterable.class.isAssignableFrom(variableValue.getClass())) {
                    return templateVariable;
                }
            }
        }
        return null;
    }


    /**
     * 段落处理
     *
     * @param paragraph
     * @param data
     * @param collectionDataIndex 如果是处理表格里面的段落，需要知道使用集合里面的哪条数据
     */
    private void processParagraph(XWPFTableCell cell, XWPFParagraph paragraph, Map<String, Object> data, Integer collectionDataIndex) {
        this.context.setParagraph(paragraph);
        if (Objects.nonNull(cell)) {
            this.context.setCell(cell);
        }

        final List<XWPFRun> runs = paragraph.getRuns();
        if (runs.isEmpty()) {
            return;
        }

        // 段落中的文本列表
        final List<String> runTextList = runs.stream().map(XWPFRun::text).collect(Collectors.toList());

        // 模板字符位置记录，例如：我的名字是：${name}，来自：${address}，请多多指教
        // 这里将会记录：${name} 与 ${address} 的 ${ 模板前缀 与 } 模板后缀 分别在哪个Run的第几个开始字符
        final List<List<Integer>> templateMarkPositions = new ArrayList<>();

        // 每个模板变量将需要记录6个( 也就是：${} x 2 = 6 ) $ -> 第 i1 Run 文本的 j1 位置， { -> 第 i2 Run 文本的 j2 位置，} -> 第 i3 Run 文本的 j3 位置
        List<Integer> singleTemplateMarkPosition = new ArrayList<>();

        // 模板变量（保存匹配到的模板变量：${name}, ${address}）
        List<String> templateVariables = new ArrayList<>();

        // 最终将会得到 {name} 或 {address}，去掉了模板的第一个字符
        StringBuilder templateVariable = new StringBuilder();

        // 是否匹配到模板字符
        boolean matchedTemplateMark = false;

        for (int i = 0; i < runTextList.size(); i++) {
            final char[] chars = runTextList.get(i).toCharArray();

            for (int j = 0; j < chars.length; j++) {
                if (matchedTemplateMark) {
                    templateVariable.append(chars[j]);
                }
                // 计算出下一个模板字符 $ -> next -> { -> next -> }
                int templateMarkIndex = singleTemplateMarkPosition.size() / 2;
                final Character nextTemplateMark = templateMarkCharacters.get(templateMarkIndex);
                if (nextTemplateMark == chars[j]) {
                    // 模板字符正确性校验（模板符号位置(前缀与后缀必须连接在一起) 与 模板变量名称(变量名称中不包含模板符号) 的校验）
                    matchedTemplateMark = true;
                    singleTemplateMarkPosition.add(i);
                    singleTemplateMarkPosition.add(j);

                    if (singleTemplateMarkPosition.size() == (templateMarkCharacters.size() * 2)) {
                        templateMarkPositions.add(singleTemplateMarkPosition);
                        singleTemplateMarkPosition = new ArrayList<>();
                        // {name} -> name 出去模板字符，得到真实变量
                        String variable = templateVariable.toString();
                        variable = variable.substring(templateVariablePrefix.length() - 1, variable.length() - templateVariableSuffix.length());
                        templateVariables.add(variable);
                        templateVariable = new StringBuilder();
                        matchedTemplateMark = false;
                    }
                }
            }
        }

        // 处理 Run 中的模板变量
        final List<Integer> toRemoveRunIndexList = new ArrayList<>();
        for (int i = 0; i < templateMarkPositions.size(); i++) {
            final List<Integer> templateMarkPosition = templateMarkPositions.get(i);
            // 头部处理（替换）
            final Integer headRunIndex = templateMarkPosition.get(0);
            // 替换 Run 中模板变量的值
            setRunValue(runs.get(headRunIndex), templateMarkPosition.get(1), templateVariables.get(i), data, collectionDataIndex);

            // 尾部处理（截断）
            final Integer tailRunIndex = templateMarkPosition.get(templateMarkPosition.size() - 2);
            if (tailRunIndex.equals(headRunIndex)) {
                continue;
            }
            final XWPFRun tailRun = runs.get(tailRunIndex);
            final String tailRunText = tailRun.text();
            if (tailRunText.endsWith(this.templateVariableSuffix)) {
                toRemoveRunIndexList.add(tailRunIndex);
            } else {
                tailRun.setText(tailRunText, templateMarkPosition.get(templateMarkPosition.size() - 1));
            }

            // 中间处理（删除）
            for (int j = headRunIndex + 1; j < tailRunIndex; j++) {
                toRemoveRunIndexList.add(j);
            }
        }

        for (Integer toRemoveRunIndex : toRemoveRunIndexList) {
            runs.get(toRemoveRunIndex).setText("", 0);
        }
    }

    private void setRunValue(XWPFRun run, int position, String variable, Map<String, Object> data, Integer collectionDataIndex) {
        Object variableValue = getVariableValue(variable, data, collectionDataIndex);

        // 处理变量前
        context.setTemplateVariable(variable);
        context.setTemplateVariableValue(variableValue);
        if (Objects.equals(false, doInterceptor(context, InterceptorPhase.BEFORE_PROCESS_VARIABLE))) {
            return;
        }

        String text = run.getText(position);
        log.info("Word Template 处理 :: 设置段落值 :: 原始值 -> {}, 变量名 -> {}, 设置值 -> {}", text, variable, variableValue);
        setRunValue(run, position, variableValue);

        // 处理变量后
        doInterceptor(context, InterceptorPhase.AFTER_PROCESS_VARIABLE);
    }

    private void setRunValue(XWPFRun run, int position, Object value) {
        // TODO 二进制数据待处理
        run.setText(value.toString(), position);
    }

    private Object getVariableValue(String variable, Map<String, Object> data) {
        return getVariableValue(variable, data, 0);
    }

    private Object getVariableValue(String variable, Map<String, Object> data, Integer collectionDataIndex) {
        String[] names = variable.split("\\.");
        Object nextLevelData = data.get(names[0]);
        if (Objects.isNull(nextLevelData)) {
            throw new RuntimeException(String.format("节点变量：[%s]不存在或值为NULL，总变量：[%s]，目标数据：[%s]", names[0], variable, JSON.toJSONString(data, true)));
        }

        if (names.length == 1) {
            return nextLevelData;
        }

        // 集合数据处理
        if (Collection.class.isAssignableFrom(nextLevelData.getClass())) {
            final Object[] array = ((Collection) nextLevelData).toArray();
            if (array.length > collectionDataIndex) {
                return ((Map) array[collectionDataIndex]).get(names[1]);
            }
            return null;
        }

        List<String> nextLevelNames = new ArrayList<>();
        for (int i = 1; i < names.length; i++) {
            nextLevelNames.add(names[i]);
        }

        return getVariableValue(String.join(".", nextLevelNames), (Map<String, Object>) nextLevelData, collectionDataIndex);
    }

    private enum InterceptorPhase {
        START_PROCESS_DOCUMENT,
        BEFORE_PROCESS_VARIABLE,
        AFTER_PROCESS_VARIABLE,
        END_PROCESS_DOCUMENT,
        ON_FAILURE
    }

    private Object doInterceptor(WordProcessContext context, InterceptorPhase phase) {
        if (Objects.isNull(this.interceptor)) {
            if (phase == InterceptorPhase.START_PROCESS_DOCUMENT || phase == InterceptorPhase.BEFORE_PROCESS_VARIABLE) {
                return true;
            }
            if (phase == InterceptorPhase.ON_FAILURE) {
                ExceptionHelper.rethrowRuntimeException(context.getException());
            }
            return null;
        }
        switch (phase) {
            case START_PROCESS_DOCUMENT:
                return interceptor.startProcessDocument(context);
            case BEFORE_PROCESS_VARIABLE:
                return interceptor.beforeProcessVariable(context);
            case AFTER_PROCESS_VARIABLE:
                interceptor.afterProcessVariable(context);
                break;
            case END_PROCESS_DOCUMENT:
                interceptor.endProcessDocument(context);
                break;
            case ON_FAILURE:
                interceptor.onFailure(context);
                break;
        }
        return null;
    }
}
