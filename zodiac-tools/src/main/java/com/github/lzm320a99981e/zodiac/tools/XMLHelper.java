package com.github.lzm320a99981e.zodiac.tools;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

import java.io.Writer;

/**
 * XML与对象之间的转换
 */
public abstract class XMLHelper {
    private static XStream xStream;
    private static final String CDATA_PREFIX = "<![CDATA[";
    private static final String CDATA_SUFFIX = "]]>";

    static {
        xStream = new XStream(
                new Xpp3DomDriver() {
                    @Override
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out, getNameCoder()) {
                            @Override
                            protected void writeText(QuickWriter writer, String text) {
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
                }
        );
        // 自动注解识别
        xStream.autodetectAnnotations(true);
        // 忽略位置的元素
        xStream.ignoreUnknownElements();
    }

    /**
     * 初始化别名注解
     *
     * @param root 跟节点对象
     * @see XStreamAlias
     */
    private static void initializationAliasAnnotation(Object root) {
        Class<?> cls = root.getClass();
        XStreamAlias alias = cls.getAnnotation(XStreamAlias.class);
        if (null != alias) {
            xStream.alias(alias.value(), cls);
        } else {
            Class<?> superclass = cls.getSuperclass();
            while (Object.class != superclass) {
                alias = superclass.getAnnotation(XStreamAlias.class);
                if (null != alias) {
                    xStream.alias(alias.value(), cls);
                    break;
                }
                superclass = superclass.getSuperclass();
            }
        }
    }

    /**
     * XML 解析为 对象
     *
     * @param xml  XML
     * @param type 解析对象类型
     * @param <T>  返回解析后的类型
     * @return 返回解析后的对象
     */
    public static <T> T parse(String xml, Class<T> type) {
        final T t = newInstance(type);
        parse(xml, t);
        return t;
    }

    /**
     * XML 解析为 对象
     *
     * @param xml  XML
     * @param root 根节点对象
     */
    public static void parse(String xml, Object root) {
        initializationAliasAnnotation(root);
        xStream.fromXML(xml, root);
    }

    /**
     * 对象 解析为 XML
     *
     * @param root 根节点对象
     * @return 转换后的XML
     */
    public static String format(Object root) {
        initializationAliasAnnotation(root);
        return xStream.toXML(root);
    }

    private static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
