package com.mars.trackerdump.db;

import java.sql.SQLException;
import javax.annotation.Nonnull;

public class DbUtil {

    public static int UNKNOWN_NUMBER = Integer.MIN_VALUE + 1;
    public static int UNKNOWN_STRING = Integer.MIN_VALUE + 2;
    public static int UNKNOWN_DATE = Integer.MIN_VALUE + 3;
    public static int UNKNOWN_BLOB = Integer.MIN_VALUE + 4;

    public static boolean isNumber(@Nonnull DbMeta metaData, int n) throws SQLException {
        switch (metaData.types[n - 1]) {
            case java.sql.Types.INTEGER:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.DOUBLE:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.FLOAT:
            case -5:
            case 2:
                return true;
            default:
                return false;
        }
    }

    public static boolean isString(@Nonnull DbMeta metaData, int n) throws SQLException {
        switch (metaData.types[n - 1]) {
            case 1:
            case 12:
                return true;
        }
        return !isNumber(metaData, n) && !isDate(metaData, n) && !isBlob(metaData, n);
    }

    public static boolean isDate(@Nonnull DbMeta metaData, int n) throws SQLException {
        switch (metaData.types[n - 1]) {
            case 91:
            case 93:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBlob(@Nonnull DbMeta metaData, int n) throws SQLException {
        switch (metaData.types[n - 1]) {
            case java.sql.Types.BLOB:
            case -2:
                return true;
            default:
                return false;
        }
    }
}
