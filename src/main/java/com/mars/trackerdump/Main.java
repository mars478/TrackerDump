package com.mars.trackerdump;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {

    private static final String CONFIG_PACKAGE = "com.mars";

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.scan(CONFIG_PACKAGE);
            ctx.refresh();

            Main bean = ctx.getBean(Main.class);
            bean.start();
        }
    }

    public void start() {
        System.out.println("start!");
    }
}
