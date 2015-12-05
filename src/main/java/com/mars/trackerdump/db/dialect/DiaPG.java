package com.mars.trackerdump.db.dialect;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class DiaPG extends DiaDefault implements Serializable {

    public DiaPG() {
        super();
    }

    @Override
    public String[] aliases() {
        return new String[]{"PG", "Postgres", "PostgreSQL"};
    }

    @Override
    public String jdbcDriver() {
        return "org.postgresql.Driver";
    }

    @Override
    public boolean detect(String connStr) {
        return StringUtils.containsIgnoreCase(connStr, "jdbc:postgresql:");
    }

    @Override
    public byte[] convertToBytes(Object o) {
        if (o instanceof Byte[]) {
            return (byte[]) o;
        }
        return null;
    }

}
