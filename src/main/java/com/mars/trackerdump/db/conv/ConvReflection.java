package com.mars.trackerdump.db.conv;

import com.mars.trackerdump.db.DbMeta;
import com.mars.trackerdump.db.DbRow;
import com.mars.trackerdump.db.DbUtil;
import com.mars.trackerdump.db.DbVal;
import com.mars.trackerdump.db.common.Pair;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * case insensitive orm - fieldnames forced to uppercase
 *
 */
public class ConvReflection extends DbConverter<Object> {

    private final static String GETTER_PREFIX = "GET";
    private final static String SETTER_PREFIX = "SET";

    @Override
    public DbRow toDbRow(@Nonnull Object obj) {
        try {
            return createDbRow(readObject(obj));
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Object fromDbRow(DbRow row, Object dummy) {
        try {
            return writeObject(row, dummy);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected DbRow createDbRow(Map<String, Pair<Class, Object>> obj) throws Exception {
        if (obj == null || obj.keySet().isEmpty()) {
            return null;
        }

        boolean nl;
        Object push;
        List<String> fields = new LinkedList<>();
        List<Integer> type = new LinkedList<>();
        List<DbVal> val = new LinkedList<>();
        Pair<Class, Object> p;

        for (String key : obj.keySet()) {
            p = obj.get(key);
            push = p.getVal();
            nl = push == null;

            fields.add(key.toUpperCase().trim());
            if (Number.class.isAssignableFrom(p.getKey())) {
                type.add(DbUtil.UNKNOWN_NUMBER);
                val.add(new DbVal(nl ? null : new BigDecimal(push.toString()), DbVal.DB_NUMBER));
            } else if (CharSequence.class.isAssignableFrom(p.getKey())) {
                type.add(DbUtil.UNKNOWN_STRING);
                val.add(new DbVal(nl ? null : push.toString(), DbVal.DB_STRING));
            } else if (Byte[].class.isAssignableFrom(p.getKey())) {
                type.add(DbUtil.UNKNOWN_BLOB);
                val.add(new DbVal(nl ? null : push, DbVal.DB_BLOB));
            } else if (Byte[].class.isAssignableFrom(p.getKey())) {
                type.add(DbUtil.UNKNOWN_DATE);
                val.add(new DbVal(nl ? null : push, DbVal.DB_DATE));
            } else {
                throw new Exception("ConvReflection: unable to convert object");
            }
        }

        String[] fNames = fields.toArray(new String[fields.size()]);
        Integer[] types = type.toArray(new Integer[type.size()]);
        DbVal[] vals = val.toArray(new DbVal[val.size()]);

        DbMeta meta = new DbMeta(fNames, types);
        return new DbRow(meta, vals);
    }

    @Nullable
    protected Map<String, Pair<Class, Object>> readObject(@Nonnull Object obj) throws Exception {
        Method[] methods = obj.getClass().getDeclaredMethods();
        if (ArrayUtils.isEmpty(methods)) {
            return null;
        }

        String mName = null;
        Map<String, Pair<Class, Object>> ret = new HashMap<>();
        for (Method m : methods) {
            if (m.isAccessible() && (mName = m.getName()).toUpperCase().startsWith(GETTER_PREFIX)) {
                ret.put(mName.toUpperCase().replaceFirst(GETTER_PREFIX, ""), new Pair<Class, Object>(m.getReturnType(), m.invoke(obj)));
            }
        }
        return ret;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    protected <T> T writeObject(DbRow fields, T obj) throws Exception {
        if (fields == null || fields.keySet().isEmpty()) {
            return obj;
        }

        Object set;
        Class expect;
        DbVal val;
        String mName;
        Method[] methods = obj.getClass().getDeclaredMethods();
        ITER_METHOD:
        for (Method m : methods) {
            if (m.isAccessible() && (mName = m.getName()).toUpperCase().startsWith(SETTER_PREFIX)) {
                mName = mName.replaceFirst(SETTER_PREFIX, "");
                for (String key : fields.keySet()) {
                    if (StringUtils.equalsIgnoreCase(key, mName)) {
                        set = null;
                        val = fields.get(key);
                        expect = m.getParameterTypes()[0];
                        if (val.isBlob()) {
                            if (expect == Byte[].class) {
                                set = val.getBlob();
                            }
                        } else if (val.isDate()) {
                            if (expect == Date.class) {
                                set = val.getDate();
                            } else if (expect == Timestamp.class) {
                                set = val.getDate();
                            }
                        } else if (val.isNumber()) {
                            BigDecimal bd = val.getNumber();
                            if (bd == null && !(expect == BigDecimal.class)) {
                                continue;
                            }
                            if (expect == Integer.class) {
                                set = bd.intValue();
                            } else if (expect == Byte.class) {
                                set = bd.byteValue();
                            } else if (expect == Long.class) {
                                set = bd.longValue();
                            } else if (expect == Float.class) {
                                set = bd.floatValue();
                            } else if (expect == BigDecimal.class) {
                                set = bd;
                            }
                        } else if (val.isString()) {
                            set = val.getString();
                        } else {
                            continue;
                        }
                        m.invoke(obj, set);
                        continue ITER_METHOD;
                    }
                }
            }
        }

        return obj;
    }
}
