package com.mars.trackerdump.db;

import com.mars.trackerdump.db.common.MapProxy;
import com.mars.trackerdump.db.common.Pair;
import com.mars.trackerdump.db.dialect.DiaAbstract;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DbRow extends MapProxy implements Serializable {

    @Nullable
    public DbPush push = null;

    @Nonnull
    public final DbMeta meta;

    @Nonnull
    private final DbVal[] data;

    @Nullable
    private Map<String, DbVal> changeMap = null;

    @ParametersAreNonnullByDefault
    public DbRow(DbMeta meta, ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new IllegalArgumentException("DbRow: null ResultSet");
        } else if (rs.isClosed()) {
            throw new IllegalArgumentException("DbRow: closed ResultSet");
        } else if (meta == null) {
            throw new IllegalArgumentException("DbRow: null meta");
        }

        List<DbVal> temp = meta.dialect.read(meta, rs);
        this.data = temp.toArray(new DbVal[temp.size()]);
        this.meta = meta;
    }

    @ParametersAreNonnullByDefault
    public DbRow(DbMeta meta, DbVal[] data) {
        if (meta == null) {
            throw new IllegalArgumentException("DbRow: null meta");
        } else if (ArrayUtils.isEmpty(data)) {
            throw new IllegalArgumentException("DbRow: empty data");
        }

        this.data = data;
        this.meta = meta;
    }

    @Nonnull
    public static DbRow insert(@Nonnull DbMeta meta) throws SQLException {
        if (meta == null) {
            throw new IllegalArgumentException("DbRow: null meta");
        }

        Class cl;
        List<DbVal> data = new ArrayList<>();
        for (int i = 0; i < meta.columns; i++) {
            cl = null;
            if (DbUtil.isBlob(meta, i)) {
                cl = Byte[].class;
            } else if (DbUtil.isDate(meta, i)) {
                cl = Date.class;
            } else if (DbUtil.isNumber(meta, i)) {
                cl = BigDecimal.class;
            } else if (DbUtil.isString(meta, i)) {
                cl = String.class;
            } else {
                throw new IllegalArgumentException("DbRow.insert: invalid meta type");
            }
            data.add(new DbVal(null, cl));
        }

        return new DbRow(meta, data.toArray(new DbVal[data.size()]));
    }

    @Nullable
    public static DbRow refresh(@Nonnull DbRow source) throws SQLException {
        if (source == null) {
            throw new IllegalArgumentException("DbRow: null source");
        } else if (source.meta == null) {
            throw new IllegalArgumentException("DbRow: null meta");
        } else if (source.push == null) {
            throw new IllegalArgumentException("DbRow: null source.push info");
        }

        DbMeta m = source.meta;
        DbPush p = source.push;
        String sql = "";

        for (String f : m.fNames) {
            if (StringUtils.isNotBlank(sql)) {
                sql = sql + ",";
            }
            sql = sql + f;
        }

        Object pkVal = source.get(p.pk);
        if (!(pkVal instanceof DbVal) || ((DbVal) pkVal).isNull()) {
            throw new IllegalArgumentException("DbRow.refresh: no source.pk val found");
        }
        List params = Arrays.asList(new Pair("PK", pkVal));
        sql = "SELECT " + sql + " FROM " + p.table + " WHERE " + p.pk + "=#PK#";
        ResultSet rs = new Query(p.dbc).execPrepared(sql, params).rs();
        if (rs.next()) {
            DbRow ret = new DbRow(m, rs);
            ret.setPush(p.pk, p.table, p.dbc);
            return ret;
        }
        return null;
    }

    @Override
    public DbVal get(@Nullable Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        String k = ((String) key).toUpperCase().trim();
        if (changeMap != null && changeMap.containsKey(k)) {
            return changeMap.get(k);
        }
        int index = 0;
        for (String s : meta.fNames) {
            if (s.equalsIgnoreCase(k)) {
                return data[index];
            }
            index++;
        }
        return null;
    }

    @Override
    @Nullable
    @ParametersAreNullableByDefault
    public Object put(Object key, Object value) {
        if (!(key instanceof String)) {
            return null;
        }
        String k = ((String) key).toUpperCase().trim();
        int index = 0;
        for (String s : meta.fNames) {
            if (s.equalsIgnoreCase(k)) {
                try {
                    return putFound(k, value, index);
                } catch (ParseException | SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            index++;
        }
        return null;
    }

    public boolean delete() throws SQLException {
        if (push == null) {
            throw new IllegalArgumentException("DbRow.delete: no push info found");
        }

        Object pkVal = get(push.pk);
        if (!(pkVal instanceof DbVal) || ((DbVal) pkVal).isNull()) {
            throw new IllegalArgumentException("DbRow.delete: no pk val found");
        }

        List params = Arrays.asList(new Pair("PK", pkVal));
        String sql = "DELETE FROM " + push.table + " WHERE " + push.pk + "=#PK#";
        return new Query(push.dbc).execPrepared(sql, params).result() > 0;
    }

    public int sync() throws SQLException {
        if (push == null) {
            throw new IllegalArgumentException("DbRow.pushChanges: no push info found");
        }

        if (changeMap == null || CollectionUtils.isEmpty(changeMap.keySet())) {
            return -1;
        }

        List<Pair<String, DbVal>> params = new ArrayList<>();
        String sql = "";
        String insCols = null;
        Object pkVal = get(push.pk);
        if (pkVal instanceof DbVal && !((DbVal) pkVal).isNull()) {
            insCols = "";
        }

        String k = null;
        boolean fUpdate = insCols == null;

        if (fUpdate) {
            for (String key : changeMap.keySet()) {
                if (StringUtils.isNotBlank(sql)) {
                    sql = sql + ",";
                }
                k = key.toUpperCase();
                sql = sql + "key=#" + k + "# ";
                params.add(new Pair(k, changeMap.get(key)));
            }
        } else { // insert
            for (String key : meta.fNames) {
                if (key.equalsIgnoreCase(push.pk)) {
                    continue;
                }

                k = key.toUpperCase();
                if (StringUtils.isNotBlank(insCols)) {
                    insCols = insCols + ",";
                }
                insCols = insCols + key;

                if (StringUtils.isNotBlank(sql)) {
                    sql = sql + ",";
                }
                sql = sql + "#" + k + "# ";
                params.add(new Pair(k, get(key)));
            }
        }

        if (insCols == null) { // update
            sql = "UPDATE " + push.table + " SET " + sql + " WHERE " + push.pk + "=#PK#";
            params.add(new Pair("PK", pkVal));
        } else { // insert
            sql = "INSERT INTO  " + push.table + " (" + insCols + ") VALUES ( " + sql + " )";
        }

        return new Query(push.dbc).execPrepared(sql, params).result();
    }

    @ParametersAreNonnullByDefault
    public DbRow setPush(DbPush push) {
        this.push = push;
        return this;
    }

    @ParametersAreNonnullByDefault
    public DbRow setPush(String pk, String table, DbConnection dbc) {
        this.push = new DbPush(pk, table, dbc);
        return this;
    }

    public void resetChanges() {
        changeMap = null;
    }

    public boolean hasChanges() {
        return changeMap != null;
    }

    @Nullable
    private Object putFound(@Nonnull String field, @Nullable Object value, int index) throws SQLException, ParseException {
        DiaAbstract da = meta.dialect;
        Class cl = null;
        index++;
        if (DbUtil.isNumber(meta, index)) {
            if ((value = da.convertToNumber(value)) == null) {
                return null;
            }
            cl = BigDecimal.class;
        } else if (DbUtil.isString(meta, index)) {
            if ((value = da.convertToString(value)) == null) {
                return null;
            }
            cl = String.class;
        } else if (DbUtil.isDate(meta, index)) {
            if ((value = da.convertToDate(value)) == null) {
                return null;
            }
            cl = Date.class;
        } else if (DbUtil.isBlob(meta, index)) {
            if ((value = da.convertToBytes(value)) == null) {
                return null;
            }
            cl = Byte[].class;
        } else {
            value = null;
            cl = Void.class;
        }

        changeMap = changeMap != null ? changeMap : new HashMap();
        changeMap.put(field, new DbVal(value, cl));
        return value;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int size() {
        return Integer.MIN_VALUE;
    }

    @Nullable
    public String getString(@Nullable String field) {
        DbVal v = get(field);
        return v == null ? null : v.getString();
    }

    @Nullable
    public Date getDate(@Nullable String field) {
        DbVal v = get(field);
        return v == null ? null : v.getDate();
    }

    @Nullable
    public BigDecimal getNumber(@Nullable String field) {
        DbVal v = get(field);
        return v == null ? null : v.getNumber();
    }

    @Nullable
    public byte[] getBlob(@Nullable String field) {
        DbVal v = get(field);
        return v == null ? null : v.getBlob();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String f : meta.fNames) {
            sb = sb.append(f).append(" : ").append(get(f)).append(" |\t");
        }
        return sb.toString();
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(Arrays.asList(meta.fNames));
    }

}
