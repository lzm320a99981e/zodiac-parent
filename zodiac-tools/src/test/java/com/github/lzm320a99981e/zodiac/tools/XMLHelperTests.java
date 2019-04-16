package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class XMLHelperTests {
    @Test
    public void test() {
        final Bean bean = new Bean();
        bean.setName("name");
        bean.setAge("18");
        bean.setDate(new Date());
        bean.setBigDecimal(BigDecimal.valueOf(100.22));

        final String xml = XMLHelper.format(bean);
        log.info("bean to xml -> \n{}", xml);
        final Bean parse = XMLHelper.parse(xml, Bean.class);
        log.info("xml to bean -> \n{}", JSON.toJSONString(parse, true));
        assert Objects.equals(bean, parse);

    }

    @Data
    @XStreamAlias("xml")
    static class Bean {
        private String name;
        private String age;
        private Date date;
        private BigDecimal bigDecimal;
    }
}
