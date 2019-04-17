package com.github.lzm320a99981e.zodiac.tools;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

import java.io.Writer;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * XML与对象之间的转换
 */
public class XMLTransfer {
    private static final String CDATA_PREFIX = "<![CDATA[";
    private static final String CDATA_SUFFIX = "]]>";
    private static HierarchicalStreamDriver driver;

    static {
        driver = new Xpp3DomDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out, getNameCoder()) {
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        System.out.println(text);
                        // 数字不需要使用CDATA
                        if (text.matches("[0-9]+")) {
                            writer.write(text);
                            return;
                        }
                        // 已经使用了CDATA
                        if (text.startsWith(CDATA_PREFIX) && text.endsWith(CDATA_SUFFIX)) {
                            writer.write(text);
                            return;
                        }
                        // 其他的
                        writer.write(CDATA_PREFIX + text + CDATA_SUFFIX);
                    }
                };
            }

            @Override
            protected NameCoder getNameCoder() {
                return new NoNameCoder();
            }
        };
    }

    private XStream xStream;

    /**
     * 创建实例
     *
     * @return
     */
    public static XMLTransfer create() {
        final XMLTransfer instance = new XMLTransfer();
        instance.xStream = new XStream(driver);
        // 自动注解识别
        instance.xStream.autodetectAnnotations(true);
        // 忽略未知的元素
        instance.xStream.ignoreUnknownElements();
        return instance;
    }

    /**
     * 类别名
     *
     * @param alias
     * @param cls
     * @return
     */
    public XMLTransfer aliasClass(String alias, Class cls) {
        this.xStream.alias(alias, cls);
        return this;
    }

    /**
     * 类型别名
     *
     * @param alias
     * @param type
     * @return
     */
    public XMLTransfer aliasType(String alias, Class type) {
        this.xStream.aliasType(alias, type);
        return this;
    }

    /**
     * 字段别名
     *
     * @param field
     * @param alias
     * @param definedIn
     * @return
     */
    public XMLTransfer aliasField(String field, String alias, Class definedIn) {
        this.xStream.aliasField(alias, definedIn, field);
        return this;
    }

    /**
     * 属性字段
     *
     * @param field
     * @param definedIn
     * @return
     */
    public XMLTransfer attribute(String field, Class definedIn) {
        this.xStream.useAttributeFor(definedIn, field);
        return this;
    }

    /**
     * 属性别名
     *
     * @param attribute
     * @param alias
     * @return
     */
    public XMLTransfer aliasAttribute(String attribute, String alias) {
        this.xStream.aliasAttribute(attribute, alias);
        return this;
    }

    /**
     * 属性别名
     *
     * @param attribute
     * @param alias
     * @param definedIn
     * @return
     */
    public XMLTransfer aliasAttribute(String attribute, String alias, Class definedIn) {
        this.xStream.aliasAttribute(definedIn, attribute, alias);
        return this;
    }

    /**
     * 忽略字段
     *
     * @param field
     * @param definedIn
     * @return
     */
    public XMLTransfer ignoreField(String field, Class definedIn) {
        this.xStream.omitField(definedIn, field);
        return this;
    }

    /**
     * 忽略所有未知的元素
     *
     * @return
     */
    public XMLTransfer ignoreUnknownElements() {
        this.xStream.ignoreUnknownElements();
        return this;
    }

    /**
     * 忽略匹配到的未知元素
     *
     * @param pattern
     * @return
     */
    public XMLTransfer ignoreUnknownElements(String pattern) {
        this.xStream.ignoreUnknownElements(pattern);
        return this;
    }

    /**
     * 忽略匹配到的未知元素
     *
     * @param pattern
     * @return
     */
    public XMLTransfer ignoreUnknownElements(Pattern pattern) {
        this.xStream.ignoreUnknownElements(pattern);
        return this;
    }

    /**
     * 是否识别注解
     *
     * @param mode true:自动识别注解，false:不自动识别注解
     * @return
     */
    public XMLTransfer autodetectAnnotations(boolean mode) {
        this.xStream.autodetectAnnotations(mode);
        return this;
    }

    /**
     * 日期类型转换
     *
     * @param pattern 日期格式
     * @return
     */
    public XMLTransfer dateFormat(String pattern) {
        this.xStream.registerConverter(new DateConverter(pattern, null, null));
        return this;
    }

    /**
     * 注册转换器
     *
     * @param converter 类型转换器
     * @return
     */
    public XMLTransfer registerConverter(Converter converter) {
        this.xStream.registerConverter(converter);
        return this;
    }

    /**
     * 对象转XML
     *
     * @param root 根节点对象
     * @return
     */
    public String toXML(Object root) {
        setAliasFromExtends(root.getClass());
        return this.xStream.toXML(root);
    }

    /**
     * XML转换对象
     *
     * @param xml
     * @param type
     * @param <T>
     * @return
     */
    public <T> T fromXML(String xml, Class<T> type) {
        final T root = create(type);
        setAliasFromExtends(type);
        this.xStream.fromXML(xml, root);
        return root;
    }

    /**
     * XML转对象
     *
     * @param xml
     * @param root
     */
    public void fromXML(String xml, Object root) {
        setAliasFromExtends(root.getClass());
        this.xStream.fromXML(xml, root);
    }

    /**
     * 设置别名可从父类上获取
     *
     * @param cls
     */
    private void setAliasFromExtends(Class<?> cls) {
        if (Objects.nonNull(cls.getAnnotation(XStreamAlias.class))) {
            this.xStream.alias(cls.getAnnotation(XStreamAlias.class).value(), cls);
            return;
        }
        Class<?> superclass = cls.getSuperclass();
        while (Object.class != superclass) {
            if (Objects.nonNull(cls.getAnnotation(XStreamAlias.class))) {
                this.xStream.alias(cls.getAnnotation(XStreamAlias.class).value(), cls);
                return;
            }
            superclass = superclass.getSuperclass();
        }
    }

    /**
     * 创建实例
     *
     * @param type
     * @param <T>
     * @return
     */
    private static <T> T create(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
