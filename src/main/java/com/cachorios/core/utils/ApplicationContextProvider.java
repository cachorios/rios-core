package com.cachorios.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;
    private static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static AnnotationConfigApplicationContext getContext() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }
}