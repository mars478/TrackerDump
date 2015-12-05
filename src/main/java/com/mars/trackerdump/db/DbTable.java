package com.mars.trackerdump.db;

import com.mars.trackerdump.db.dialect.DiaAbstract;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class DbTable extends ArrayList<DbRow> implements Serializable {

    @Nullable
    private DbPush push = null;

    @Nonnull
    public final DbMeta meta;

    @Nullable
    public DbRow currentRow = null;

    @Override
    public final boolean add(DbRow e) {
        if (e != null && meta.equals(e.meta)) {
            if (push == null) {
                push = e.push;
            }
            if (push == null || push.equals(e.push)) {
                return super.add(e);
            }
        }
        return false;
    }

    @Override
    public final void add(int index, DbRow e) {
        if (e != null && meta.equals(e.meta)) {
            if (push == null) {
                push = e.push;
            }
            if (push == null || push.equals(e.push)) {
                super.add(index, e);
            }
        }
    }

    public DbTable(DbMeta meta) {
        super();
        if (meta == null) {
            throw new IllegalArgumentException("DbTable: null meta");
        }
        this.meta = meta;
    }

    public DbTable(@Nonnull Query q) throws SQLException {
        this(q.rs, q.dbc.dialect);
    }

    @ParametersAreNonnullByDefault
    public DbTable(ResultSet rs, DiaAbstract dialect) throws SQLException {
        super();
        if (rs == null) {
            throw new IllegalArgumentException("DbTable: null ResultSet");
        } else if (rs.isClosed()) {
            throw new IllegalArgumentException("DbTable: closed ResultSet");
        } else if (dialect == null) {
            throw new IllegalArgumentException("DbTable: null dialect");
        }

        meta = new DbMeta(rs, dialect);
        while (rs.next()) {
            add(new DbRow(meta, rs));
        }
    }

    @ParametersAreNonnullByDefault
    public DbTable setPush(DbPush push) {
        this.push = push;
        return this;
    }

    @ParametersAreNonnullByDefault
    public DbTable setPush(String pk, String table, DbConnection dbc) {
        this.push = new DbPush(pk, table, dbc);
        return this;
    }

    @Nonnull
    public DbRow insert() throws SQLException {
        add(currentRow = DbRow.insert(meta));
        return currentRow;
    }

    public boolean delete() throws SQLException {
        if (currentRow != null) {
            if (currentRow.push == null) {
                currentRow.push = push;
            }
            if (currentRow.delete()) {
                currentRow = null;
                return true;
            }
        }
        return false;
    }

    public DbRow refresh() throws SQLException {
        if (currentRow != null) {
            if (currentRow.push == null) {
                currentRow.push = push;
            }

            DbRow ref = DbRow.refresh(currentRow);
            int index = indexOf(currentRow);
            remove(index);
            if (ref != null) {
                add(index, ref);
                currentRow = ref;
            } else {
                currentRow = null;
            }
        }
        return currentRow;
    }

    public DbTable sync() throws SQLException {
        for (int i = 0; i < size(); i++) {
            currentRow = get(i);
            if (currentRow.push == null) {
                currentRow.push = push;
            }
            if (currentRow.hasChanges()) {
                try {
                    currentRow.sync();
                } catch (SQLException e) {
                    currentRow.resetChanges();
                    throw e;
                }
            } else if (refresh() == null) {
                i--;
            }
        }

        currentRow = null;
        return this;
    }

    public DbPush getPush() {
        return push;
    }

    public DbRow getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(DbRow currentRow) {
        this.currentRow = currentRow;
    }

    public void setCurrentRow(int index) {
        if (isEmpty()) {
            currentRow = null;
            return;
        }

        if (index < 0) {
            index = 0;
        } else if (index > (size() - 1)) {
            index = size() - 1;
        }
        currentRow = get(index);
    }
}
