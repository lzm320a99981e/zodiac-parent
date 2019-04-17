package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式解析器
 */
public class ExpressionParser {
    private static final String DEFAULT_SEPARATOR = ".";
    private String separator = DEFAULT_SEPARATOR;

    /**
     * 创建解析器
     *
     * @return
     */
    public static ExpressionParser create() {
        return new ExpressionParser();
    }

    /**
     * 表达式分隔符
     *
     * @param separator
     * @return
     */
    public ExpressionParser separator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * 解析表达式
     *
     * @param root
     * @param expression
     * @return
     */
    public Object parse(Object root, String expression) {
        final List<String> expressions = split(expression);
        Object node = JSON.parse(JSON.toJSONString(root));
        boolean found = true;
        for (String exp : expressions) {
            String json = JSON.toJSONString(node);
            // 对象
            if (json.startsWith("{")) {
                JSONObject jsonObject = JSON.parseObject(json);
                if (jsonObject.containsKey(exp)) {
                    node = jsonObject.get(exp);
                    continue;
                }
            }
            // 集合
            if (json.startsWith("[") && exp.matches("^0|(\\+)?[1-9]+[0-9]*$")) {
                JSONArray jsonArray = JSON.parseArray(json);
                int index = Integer.parseInt(exp);
                if (jsonArray.size() > index) {
                    node = jsonArray.get(index);
                    continue;
                }
            }
            // 基础类型数据忽略
            found = false;
            break;
        }
        return found ? node : null;
    }

    /**
     * 拆分表达式
     *
     * @param expression
     * @return
     */
    private List<String> split(String expression) {
        // 将表达式分切
        List<String> items = Splitter.on(separator).splitToList(expression);
        // 创建一个新的分隔符，用于连接切分的表达式
        String newSeparator = "__" + UUID.randomUUID().toString().replace("-", "") + "__";
        // 重新组装和拆分表达式
        String newExpressions = Joiner.on(newSeparator).join(items).replace("\\" + newSeparator, separator);

        final List<String> expressions = new ArrayList<>();
        final List<String> newItems = Splitter.on(newSeparator).splitToList(newExpressions);
        final Pattern arrayPattern = Pattern.compile("\\[\\d+]");

        for (String newItem : newItems) {
            // 数组匹配
            if (!newItem.matches(".+\\[\\d+]$")) {
                expressions.add(newItem);
                continue;
            }
            // 数组表达式拆分
            expressions.add(newItem.substring(0, newItem.indexOf("[")));
            final Matcher matcher = arrayPattern.matcher(newItem);
            while (matcher.find()) {
                expressions.add(matcher.group().replace("[", "").replace("]", ""));
            }
        }

        return expressions;
    }
}
