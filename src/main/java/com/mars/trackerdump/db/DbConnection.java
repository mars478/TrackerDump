package com.mars.trackerdump.db;


import com.mars.trackerdump.db.dialect.DiaAbstract;
import com.mars.trackerdump.db.dialect.DiaManager;
import com.mars.trackerdump.db.log.DbLoggable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.StringUtils;

public class DbConnection extends DbLoggable implements AutoCloseable {

    protected static long counter = 1l;

    final long id;

    @Nonnull
    final String username;

    @Nonnull
    final String password;

    @Nonnull
    final String connStr;

    @Nonnull
    private Connection con = null;

    @Nonnull
    final DiaAbstract dialect;

    boolean autocommit = false;
    boolean blocked = false;

    @ParametersAreNonnullByDefault
    public DbConnection(String username, String password, String connStr) {
        this(username, password, connStr, DiaManager.impementation(connStr));
    }

    @ParametersAreNonnullByDefault
    public DbConnection(String username, String password, String connStr, DiaAbstract dialect) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("DbConenction: blank username passed");
        } else if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("DbConenction: blank password passed");
        } else if (StringUtils.isBlank(connStr)) {
            throw new IllegalArgumentException("DbConenction: blank connection string passed");
        } else if (dialect == null) {
            throw new IllegalArgumentException("DbConenction: null dialect passed");
        }

        this.connStr = connStr;
        this.username = username;
        this.password = password;
        this.dialect = dialect;

        synchronized (DbConnection.class) {
            id = counter++;
        }
    }

    public static DbConnection copy(DbConnection dbc) {
        return dbc == null
                ? null
                : new DbConnection(dbc.username, dbc.password, dbc.connStr, dbc.dialect);
    }

    @Override
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
        }
        con = null;
        logQuery("close " + info());
    }

    /**
     *
     * @return true if connection was null or closed
     */
    private boolean killConnectionIfClosed() {
        boolean nullReturn = false;
        try {
            if (con == null) {
                nullReturn = true;
            } else if (con.isClosed()) {
                close();
                return true;
            }
        } catch (Exception e) {
        }
        if (nullReturn) {
            con = null;
        }
        return nullReturn;
    }

    public Connection con() throws SQLException {
        if (blocked) {
            throw new SQLException("Unable to connect: " + info() + " blocked");
        }
        if (con == null || killConnectionIfClosed()) {
            logQuery("open " + info() + " '" + connStr + "'...");
            con = DriverManager.getConnection(connStr, username, password);
            logQuery(info() + " created");
        }
        return con;
    }

    public boolean isAutocommit() {
        return autocommit;
    }

    public DbConnection setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
        return this;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    protected String getDbUsername() {
        return username;
    }

    private String info() {
        return "connection['" + getDbUsername() + "'] #" + id;
    }

}
