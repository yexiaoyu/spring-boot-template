package com.guanxun.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * Created by lvdin on 2016/10/22.
 */
public class SpringHelper implements ApplicationContextAware {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SpringHelper.class);
    private static ApplicationContext context;

    public SpringHelper() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void setContext(ApplicationContext applicationContext) {
        if(context == null) {
            context = applicationContext;
        }

    }

    public static Object getBean(String beanId) {
        try {
            return context.getBean(beanId);
        } catch (Exception var2) {
            LOGGER.debug("getBean:" + beanId + "," + var2.getMessage());
            return null;
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        try {
            return context.getBean(beanClass);
        } catch (Exception var2) {
            LOGGER.debug("getBean:" + beanClass + "," + var2.getMessage());
            return null;
        }
    }
    public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}
    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }

    public static void cleanup() {
        context = null;
    }
}
