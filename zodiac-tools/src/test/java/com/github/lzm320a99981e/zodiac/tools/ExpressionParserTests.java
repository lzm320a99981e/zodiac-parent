package com.github.lzm320a99981e.zodiac.tools;

import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParserTests {
    @Test
    public void test() {
        final Author author = new Author();
        author.setName("战三");
        final Personal personal = new Personal();
        personal.setName("李四");
        author.setPersonals(Arrays.asList(personal));
        final Object name = ExpressionParser.create().parse(author, "personals[0].strings[1][0]");
        System.out.println(name);
    }

    @Test
    public void test2() {
        final String exp = "personals[0].name";
        final String[] items = exp.split("\\.");
        System.out.println(Arrays.toString(items));
        System.out.println("personals[0".matches(".+\\[\\d+]$"));
    }

    @Test
    public void test3() {
        final String exp = "personal[0][2]";
        final Matcher matcher = Pattern.compile("\\[\\d+]").matcher(exp);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Data
    static class Author {
        private String name;
        private List<Personal> personals;
    }

    @Data
    static class Personal {
        private String name;
        private String[][] strings = {{"1", "2"}, {"a", "b"}};
    }
}
