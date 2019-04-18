package com.github.lzm320a99981e.compnent.weixinpay;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static freemarker.template.Configuration.VERSION_2_3_28;

public class GenerateWechatPayApiTests {

    public Api parseApi(String url) {
        final List<String> urlLabels = Arrays.asList("接口链接", "URL地址", "接口地址");

        try {
            final Api api = new Api();
            api.setDocumentUrl(url);

            final Element body = Jsoup.connect(url).get().body();
            api.setName(body.select("div.content-hd").text().trim());
            final Elements elements = body.select("div.content>div.data-box").isEmpty() ? body.select("div.content-bd div.data-box") : body.select("div.content>div.data-box");

            for (Element element : elements) {
                // url
                final String elementText = element.text().trim();
                for (String urlLabel : urlLabels) {
                    if (elementText.contains(urlLabel)) {
                        api.setUrl(elementText.substring(elementText.indexOf("https")));
                        break;
                    }
                }
                // parameters
                final Elements tables = element.select(">div.data-bd table");
                if (Objects.nonNull(tables) && !tables.isEmpty()) {
                    final String key = element.selectFirst("div.data-hd").text().trim();
                    final List<Map<String, Object>> mapList = Lists.newArrayList();
                    api.getTableMap().put(key, mapList);

                    for (Element table : tables) {

                        String rowFilter = "";
                        Elements heads = table.select("thead tr:eq(0)>*");
                        if (heads.isEmpty()) {
                            heads = table.select("tbody tr:eq(0)>*");
                            rowFilter = ":gt(0)";
                        }
                        final List<String> keys = heads.stream().map(item -> item.text().trim()).collect(Collectors.toList());
                        final Elements rows = table.select("tbody tr" + rowFilter);

                        for (Element row : rows) {
                            final Map<String, Object> rowMap = Maps.newLinkedHashMap();
                            final Elements columns = row.select("td");
                            if (columns.size() < keys.size()) {
                                // 跨行的情况，跳过
                                continue;
                            }
                            for (int i = 0; i < columns.size(); i++) {
                                try {

                                    rowMap.put(keys.get(i), columns.get(i).text().trim());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            mapList.add(rowMap);
                        }
                    }
                }
            }
            return api;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test3() {
        final Api api = parseApi("https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_16&index=11");
        System.out.println(JSON.toJSONString(api, true));
//        generate(api);
    }

    private List generate(Api api) {
        try {
            final String projectDir = System.getProperty("user.dir");
            Configuration configuration = new Configuration(VERSION_2_3_28);
            final File dir = Paths.get(projectDir, "src/test/resources").toFile();
            configuration.setDirectoryForTemplateLoading(dir);
            final Template template = configuration.getTemplate("dto.ftl");

            final ArrayList<String> keys = new ArrayList<>(api.getTableMap().keySet());
            final Map<String, String> typeMap = Maps.newLinkedHashMap();
            typeMap.put(keys.get(0), "Request");
            typeMap.put(keys.get(1), "Response");

            for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                api.setType(entry.getKey());
                final String className = StringUtils.capitalize(api.getAction()) + entry.getValue();
                api.setClassName(className);
                final StringWriter writer = new StringWriter();
                template.process(api, writer);
                final File file = Paths.get(projectDir, "src/main/java/com/github/lzm320a99981e/compnent/weixinpay/dto/" + className + ".java").toFile();
                Files.write(file.toPath(), writer.toString().getBytes(StandardCharsets.UTF_8));
                writer.close();
            }

            return Arrays.asList(
                    api.getTableMap().get(keys.get(0)).stream().map(item -> item.get("变量名")).collect(Collectors.toList()),
                    api.getTableMap().get(keys.get(1)).stream().map(item -> item.get("变量名")).collect(Collectors.toList())
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test2() throws IOException {
        final List<List> parametersList = Lists.newArrayList();
        String baseUrl = "https://pay.weixin.qq.com/wiki/doc/api/native.php";
        final Elements elements = Jsoup.connect(baseUrl + "?chapter=9_1").get().body().select("div.munu-bd dl");
        for (Element element : elements) {
            if (element.selectFirst("dt").text().contains("API列表")) {
                final Elements links = element.select("dd");
                for (Element link : links) {
                    final Element a = link.selectFirst("a");
                    final String href = baseUrl + a.attr("href");
                    final String label = a.text();
                    System.out.println(label + " -> " + href);
                    parametersList.add(generate(parseApi(href)));
                }
                break;
            }
        }

        // 获取参数的交集，以便把公共的参数抽取出来作为公共参数
        List first = parametersList.get(0);
        for (int i = 1; i < parametersList.size(); i++) {
            final List list = parametersList.get(i);
            for (int j = 0; j < list.size(); j++) {
                first.set(j, CollectionUtils.intersection((List) first.get(j), (List) list.get(j)));
            }
        }
        System.out.println(JSON.toJSONString(first, true));
    }

    @Data
    public static class Api {
        private String type;
        private String name;
        private String url;
        private String documentUrl;
        private String className;
        private Map<String, List<Map<String, Object>>> tableMap = new LinkedHashMap<>();

        public String getAction() {
            final Map<String, String> actionMap = Maps.newHashMap();
            actionMap.put("支付结果通知", "payNotify");
            actionMap.put("交易保障", "payitilReport");
            actionMap.put("退款结果通知", "refundNotify");
            if (actionMap.containsKey(name)) {
                return actionMap.get(name);
            }
            return url.substring(url.lastIndexOf("/") + 1);
        }
    }
}
