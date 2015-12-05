package com.mars.trackerdump.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DbVal implements Serializable {

    public static final Class DB_VOID = Void.class;
    public static final Class DB_NUMBER = BigDecimal.class;
    public static final Class DB_STRING = String.class;
    public static final Class DB_DATE = Date.class;
    public static final Class DB_BLOB = Byte[].class;

    public static final List<Class> CLASSES = Arrays.asList(DB_VOID, DB_NUMBER, DB_STRING, DB_DATE, DB_BLOB);

    @Nullable
    protected Object val = null;

    @Nonnull
    protected Class klass = DB_VOID;

    public DbVal(@Nullable Object o) {
        set(o);
    }

    public DbVal(@Nullable Object o, Class c) {
        set(o, c);
    }

    public final DbVal set(@Nullable Object o) {
        return set(o, null);
    }

    public final DbVal set(@Nullable Object o, Class c) {
        if (c == null) {
            if (o instanceof BigDecimal) {
                klass = DB_NUMBER;
            } else if (o instanceof String) {
                klass = DB_STRING;
            } else if (o instanceof Date) {
                klass = DB_DATE;
            } else if (o instanceof Byte[]) {
                klass = DB_BLOB;
            } else if (o == null) {
                klass = DB_VOID;
            } else {
                throw new IllegalArgumentException("Invalid DbVal init type:" + o);
            }
        } else if (!CLASSES.contains(c)) {
            throw new IllegalArgumentException("Invalid DbVal init class:" + c);
        } else {
            klass = c;
        }
        val = o;
        return this;
    }

    @Nullable
    public String getString() {
        return (String) (isString() ? val : null);
    }

    @Nullable
    public Date getDate() {
        return (Date) (isDate() ? val : null);
    }

    @Nullable
    public BigDecimal getNumber() {
        return (BigDecimal) (isNumber() ? val : null);
    }

    @Nullable
    public byte[] getBlob() {
        return (byte[]) (isBlob() ? val : null);
    }

    public boolean isString() {
        return klass == DB_STRING;
    }

    public boolean isDate() {
        return klass == DB_DATE;
    }

    public boolean isNumber() {
        return klass == DB_NUMBER;
    }

    public boolean isBlob() {
        return klass == DB_BLOB;
    }

    public boolean isVoid() {
        return klass == DB_VOID;
    }

    public boolean isTypedNull() {
        return isNull() && !isVoid();
    }

    public boolean isPureNull() {
        return isNull() && isVoid();
    }

    public boolean isNull() {
        return val == null;
    }

    public DbVal clear() {
        val = null;
        klass = DB_VOID;
        return this;
    }

    @Override
    public String toString() {
        if (val == null || klass == DB_VOID) {
            return "";
        } else if (!isDate()) {
            return val.toString();
        }
        Date d = (Date) val;
        boolean hasTime = (long) (d.getTime() % (long) 86400000) != 0l; // 86400000ms per day
        return new SimpleDateFormat(hasTime ? "dd.MM.yyyy HH:mm:ss" : "dd.MM.yyyy").format(d);
    }

}
