package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class JSONTests {
    @Test
    public void test() {
        final Author author = new Author();
        author.setName("张三");
        author.setBirthday(new Date());
        author.setMoney(BigDecimal.valueOf(10.0));

        final Book book = new Book();
        book.setAmount(BigDecimal.valueOf(100.50));
        book.setName("《三国演义》");
        book.setPages(101);

        author.setBook(book);
//        author.setAttributes(new HashMap<>());
//        author.getAttributes().put("bytes", new byte[]{1, 2, 3});

        for (int i = 0; i < 5000; i++) {
            final HashMap<String, Object> a1 = new HashMap<>();
            final HashMap<String, Object> a2 = new HashMap<>();
            for (int j = 0; j < 1000; j++) {
                final String key = RandomStringUtils.random(10);
                final String value = RandomStringUtils.randomAlphanumeric(1);
                a1.put(key, value);
                a2.put(key, value);
            }
            author.setAttributes(a1);
            final String s = JSON.toJSONString(author);
            author.setAttributes(a2);
            final String s1 = JSON.toJSONString(author, SerializerFeature.SortField);
            assert s.equals(s1);
        }
    }

    @Test
    public void test2() {
        final HashSet<Object> objects = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            objects.add(RandomStringUtils. randomAlphabetic(10));
        }
        String s = "";
        for (int i = 0; i < 10; i++) {
            System.out.println(objects.toString());
        }
    }

    @Data
    static class Author {
        private String name;
        private BigDecimal money;
        private Date birthday;
        private Book book;
        private Map<String, Object> attributes;
    }

    @Data
    static class Book {
        private String name;
        private Integer pages;
        private BigDecimal amount;
    }
}
