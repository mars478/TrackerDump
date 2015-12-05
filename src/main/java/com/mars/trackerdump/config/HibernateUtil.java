package com.mars.trackerdump.config;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(String... dbName) {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        String db = null;
        if (ArrayUtils.isEmpty(dbName) || StringUtils.isBlank(db = dbName[0])) {
            throw new IllegalArgumentException("Wrong dbName");
        }

        Configuration configuration = new Configuration();

        configuration.setProperty("connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://192.168.1.6:5433/postgres");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("show_sql", "true");
        //configuration.setProperty("hibernate.connection.pool_size", "10");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return sessionFactory = configuration.buildSessionFactory(builder.build());
    }
}
