package com.mars.trackerdump.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang3.StringUtils;

public class SQLite {

    public static void init(String dbName) throws ClassNotFoundException, SQLException {
        if (StringUtils.isBlank(dbName)) {
            throw new IllegalArgumentException("Wrong DbName");
        }

        Class.forName("org.sqlite.JDBC");

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
                Statement stmt = c.createStatement()) {
            String sql = "create table if not exists topic "
                    + "(topic_id       INTEGER PRIMARY KEY  NOT NULL,"
                    + " forum_id       INTEGER    NOT NULL, "
                    + " forum_name     TEXT, "
                    + " topic_hash     TEXT, "
                    + " topic_name     TEXT,"
                    + " topic_size     INTEGER, "
                    + " topic_date     TEXT) ";
            stmt.executeUpdate(sql);
        }
    }

}
