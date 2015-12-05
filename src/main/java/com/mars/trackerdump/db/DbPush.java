package com.mars.trackerdump.db;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("EqualsAndHashcode")
public class DbPush {

    @Nonnull
    public final String pk;

    @Nonnull
    public final String table;

    @Nonnull
    public final DbConnection dbc;

    @ParametersAreNonnullByDefault
    public DbPush(String pk, String table, DbConnection dbc) {
        if (StringUtils.isBlank(pk)) {
            throw new IllegalArgumentException("DbPush: empty primary key passed");
        } else if (StringUtils.isBlank(table)) {
            throw new IllegalArgumentException("DbPush: empty table passed");
        } else if (dbc == null) {
            throw new IllegalArgumentException("DbPush: null db connection passed");
        }

        this.pk = pk;
        this.table = table;
        this.dbc = dbc;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DbPush)) {
            return false;
        }
        DbPush p = (DbPush) obj;
        return StringUtils.equalsIgnoreCase(pk, p.pk) && StringUtils.equalsIgnoreCase(table, p.table);
    }
}
