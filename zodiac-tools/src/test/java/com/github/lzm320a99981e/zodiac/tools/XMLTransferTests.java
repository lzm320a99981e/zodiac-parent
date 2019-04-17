package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class XMLTransferTests {
    @Test
    public void test() {
        final XMLTransfer xmlTransfer = XMLTransfer.create();
        final Bean bean = createBean();
        final String xml = xmlTransfer.toXML(bean);
        log.info("bean to xml -> \n{}", xml);
        final Bean parse = xmlTransfer.fromXML(xml, Bean.class);
        log.info("xml to bean -> \n{}", JSON.toJSONString(parse, true));
        assert Objects.equals(bean, parse);
    }

    @Test
    public void test1() {
        final Bean bean = createBean();
        final XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", Bean.class);
        xStream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", null, null));
        System.out.println(xStream.toXML(bean));
    }

    @Test
    public void test2() {
        final Bean bean = createBean();
        final XStream xStream = new XStream();
        xStream.aliasType("xml", Parent.class);
        xStream.useAttributeFor(Bean.class, "name");
        xStream.useAttributeFor("email", BigDecimal.class);
        xStream.aliasAttribute("emailxxxxxxxx", "email");
        System.out.println(xStream.toXML(bean));
    }

    @XStreamAlias("root")
    interface Parent {
        String getName();
    }

    @Data
    @XStreamAlias("root")
    static class Bean {
        private String name;
        private String age;
        private Date date;
        private BigDecimal bigDecimal;
        private List<Personal> personals;
        private Personal personal;
        private Phone phone;
    }

    @Data
    @XStreamAlias("personal")
    static class Personal {
        private String phone;
        private String email;
    }

    @Data
    static class Phone {
        private String phone;
    }

    private Bean createBean() {
        final Bean bean = new Bean();
        bean.setName("name");
        bean.setAge("18");
        bean.setDate(new Date());
        bean.setBigDecimal(BigDecimal.valueOf(100.22));

        final ArrayList<Personal> personals = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Personal personal = new Personal();
            personal.setEmail("1234234@qq.com");
            personal.setPhone("13380384844");
            personals.add(personal);
        }
        bean.setPersonals(personals);

        final Personal personal = new Personal();
        personal.setEmail("234234@3r.com");
        bean.setPersonal(personal);

        final Phone phone = new Phone();
        phone.setPhone("13380833838");
        bean.setPhone(phone);

        return bean;
    }
}
