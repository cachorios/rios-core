///package com.cachorios.core.utils;

//
//import org.springframework.web.context.AbstractContextLoaderInitializer;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//
//public class AnnotationsBasedApplicationInitializer
//  extends AbstractContextLoaderInitializer {
//
//    @Override
//    protected WebApplicationContext createRootApplicationContext() {
//        AnnotationConfigWebApplicationContext rootContext
//          = new AnnotationConfigWebApplicationContext();
//        //rootContext.register(RootApplicationConfig.class);
//        rootContext.register(Application.class);
//        return rootContext;
//    }
//}