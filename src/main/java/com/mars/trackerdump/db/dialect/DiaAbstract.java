package com.mars.trackerdump.db.dialect;


import com.mars.trackerdump.db.DbMeta;
import com.mars.trackerdump.db.DbVal;
import com.mars.trackerdump.db.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class DiaAbstract implements Serializable {

    public DiaAbstract() {
        super();
        try {
            Class.forName(jdbcDriver());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found:" + jdbcDriver());
        }
        DiaManager.register(this, aliases());
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public abstract DiaAbstract put(PreparedStatement pst, Query.ParserData<DbVal> pd) throws SQLException;

    @Nonnull
    @ParametersAreNonnullByDefault
    public abstract List<DbVal> read(DbMeta meta, ResultSet rs) throws SQLException;

    @Nullable
    public abstract BigDecimal convertToNumber(@Nullable Object o);

    @Nullable
    public abstract String convertToString(@Nullable Object o);

    @Nullable
    public abstract Date convertToDate(@Nullable Object o) throws ParseException;

    @Nullable
    public abstract byte[] convertToBytes(@Nullable Object o);

    @Nonnull
    public abstract String[] aliases();

    @Nonnull
    public abstract String jdbcDriver();

    public abstract boolean detect(String connStr);
}
