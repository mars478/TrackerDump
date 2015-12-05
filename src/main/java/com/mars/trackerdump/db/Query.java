package com.mars.trackerdump.db;

import com.mars.trackerdump.db.common.Pair;
import com.mars.trackerdump.db.log.DbLoggable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query extends DbLoggable implements AutoCloseable {

    protected static Logger LOGGER = LoggerFactory.getLogger(Query.class);

    @Nonnull
    protected final DbConnection dbc;

    protected String sql = null;
    protected Statement st = null;
    protected ResultSet rs = null;
    protected int result = -1;

    protected int timeout = 0;

    public Query(@Nonnull DbConnection dbc) {
        if (dbc == null) {
            throw new IllegalArgumentException("Query: null connection");
        }
        this.dbc = dbc;
    }

    @Nonnull
    public Query exec(@Nonnull String sql) throws SQLException {
        try {
            close();
            if (StringUtils.isBlank(sql)) {
                throw new SQLException("Query.exec: blank sql");
            }
            st = dbc.con().createStatement();
            st.setQueryTimeout(timeout);

            logQuery(info() + "\n" + sql);

            if (notSelectQuery(sql)) {  // update || insert || delete
                result = st.executeUpdate(sql);
                if (dbc.autocommit) {
                    commit();
                }
            } else { // select
                rs = st.executeQuery(sql);
            }
            return this;
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    /**
     *
     * @param sql wrap case insensitive param names with '#'
     * @param params array of Pair<String, DbVal>
     * @return java.sql.ResultSet
     * @throws SQLException
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public Query execPrepared(String sql, Pair<String, DbVal>... params) throws SQLException {
        return execPrepared(sql, Arrays.asList(params));
    }

    /**
     *
     * @param sql wrap case insensitive param names with '#'
     * @param params list of Pair<String, DbVal>
     * @return java.sql.ResultSet
     * @throws SQLException
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public Query execPrepared(String sql, List<Pair<String, DbVal>> params) throws SQLException {
        try {
            close();
            if (StringUtils.isBlank(sql)) {
                throw new SQLException("Query.exec: blank sql");
            }
            sql = sql.trim();

            ParserData<DbVal> pd = paramsSort(sql, params);
            PreparedStatement pst = ((PreparedStatement) (st = dbc.con().prepareStatement(pd.sql)));
            pst.setQueryTimeout(timeout);

            logQuery(info() + "\n" + sql + "\n" + stringifyParams(params));

            dbc.dialect.put(pst, pd);
            if (notSelectQuery(sql)) { // update || insert || delete
                result = pst.executeUpdate();
                if (dbc.autocommit) {
                    commit();
                }
            } else { // select
                rs = pst.executeQuery();
            }
            return this;
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public Query commit() throws SQLException {
        if (st != null && !st.isClosed()) {
            Connection c = st.getConnection();
            if (c != null && !c.isClosed()) {
                c.commit();
                logQuery("commit:" + info());
            }
        }
        return this;
    }

    public Query rollback() throws SQLException {
        if (st != null && !st.isClosed()) {
            Connection c = st.getConnection();
            if (c != null && !c.isClosed()) {
                c.rollback();
            }
        }
        return this;
    }

    // <editor-fold defaultstate="collapsed" desc="prepared statement parameters parsing">
    @Nonnull
    @ParametersAreNonnullByDefault
    private ParserData<DbVal> paramsSort(String sql, List<Pair<String, DbVal>> params) throws SQLException {
        ParserData pd = parseParams(sql);

        List<Pair> out = pd.params;
        List<DbVal> ret = new ArrayList(params.size());

        if (params.size() != out.size()) {
            throw new SQLException("Query.execPrepared: mismatch sql and passed parameters counts");
        }

        SORT:
        for (Pair<Integer, String> sorter : out) {
            for (Pair<String, DbVal> par : params) {
                if (StringUtils.equalsIgnoreCase(sorter.getVal().trim(), par.getKey().trim())) {
                    ret.add(sorter.getKey(), par.getVal());
                    continue SORT;
                }
            }
            throw new SQLException("Query.execPrepared: invalid parameter names, sort failed");
        }
        return new ParserData(ret, pd.sql);
    }

    @Nonnull
    private ParserData<Pair> parseParams(@Nonnull String sql) {
        Pattern p = Pattern.compile("#([\\w|\\d]+)#");
        List<Pair> out = new LinkedList<>();
        Matcher m = p.matcher(sql);
        String pName = null;
        int count = 0;
        while (m.find()) {
            pName = m.group().trim();
            out.add(new Pair(count++, StringUtils.substringBetween(pName, "#").toUpperCase()));
            sql = sql.replaceFirst(pName, "?");
        }
        return new ParserData(out, sql);
    }
// </editor-fold>

    @Override
    public void close() {
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            logException(e);
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logException(e);
        }
        st = null;
        rs = null;
    }

    public ResultSet rs() {
        return rs;
    }

    public int result() {
        return result;
    }

    public int getTimeout() {
        return timeout;
    }

    public Query setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    private String stringifyParams(@Nonnull List<Pair<String, DbVal>> params) {
        StringBuilder sb = new StringBuilder();
        for (Pair<String, DbVal> p : params) {
            sb.append(p.getKey()).append("=").append(p.getVal().toString()).append("\n");
        }
        return sb.toString();
    }

    private String info() {
        return "connection['" + getDbUsername() + "'] #" + dbc.id;
    }

    private boolean notSelectQuery(String sql) {
        sql = StringUtils.defaultString(sql).trim();
        return StringUtils.startsWithIgnoreCase(sql, "UPDATE") || StringUtils.startsWithIgnoreCase(sql, "INSERT") || StringUtils.startsWithIgnoreCase(sql, "DELETE");
    }

    @Override
    protected String getDbUsername() {
        return dbc.username;
    }

    static public class ParserData<T> {

        public final List<T> params;
        public final String sql;

        public ParserData(List<T> params, String sql) {
            this.params = params;
            this.sql = sql;
        }
    }
}
