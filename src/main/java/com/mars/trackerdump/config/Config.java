package com.mars.trackerdump.config;

import java.sql.SQLException;

public class Config {

    public static void setDbName(String dbName) throws ClassNotFoundException, SQLException {
        SQLite.init(dbName);
    }
}
