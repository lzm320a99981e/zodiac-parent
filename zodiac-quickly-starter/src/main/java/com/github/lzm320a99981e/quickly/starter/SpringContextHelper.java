package com.github.lzm320a99981e.quickly.starter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component(Constants.BEAN_NAME_PREFIX + "SpringContextHelper")
public class SpringContextHelper implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> type) {
        return getContext().getBean(type);
    }

    public static Object getBean(String name) {
        return getContext().getBean(name);
    }
}
