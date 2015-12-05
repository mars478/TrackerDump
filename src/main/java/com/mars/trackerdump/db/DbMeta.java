package com.mars.trackerdump.db;

import com.mars.trackerdump.db.dialect.DiaAbstract;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("EqualsAndHashcode")
public class DbMeta implements Serializable {

    @Nonnull
    DiaAbstract dialect;

    @Nonnull
    public final String[] fNames;

    @Nonnull
    public final Integer[] types;

    public final int columns;

    @ParametersAreNonnullByDefault
    public DbMeta(ResultSet rs, DiaAbstract dialect) throws SQLException {
        if (dialect == null) {
            throw new IllegalArgumentException("DbMeta: null dialect passed");
        }
        List<String> namesL = new LinkedList<>();
        List<Integer> typesL = new LinkedList<>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int col = 1; col < cols + 1; col++) {
            namesL.add(rsmd.getColumnName(col).toUpperCase());
            typesL.add(rsmd.getColumnType(col));
        }

        if (namesL.size() != cols || typesL.size() != cols) {
            throw new IllegalArgumentException("DbMeta: names and types counts mismatch");
        }

        this.columns = cols;
        this.fNames = namesL.toArray(new String[cols]);
        this.types = typesL.toArray(new Integer[cols]);
        this.dialect = dialect;
    }

    public DbMeta(String[] fNames, Integer[] types) {
        if (ArrayUtils.isEmpty(fNames) || ArrayUtils.isEmpty(types)) {
            throw new IllegalArgumentException("DbMeta: empty data passed");
        } else if (fNames.length != types.length) {
            throw new IllegalArgumentException("DbMeta: names and types counts mismatch");
        }
        this.fNames = fNames;
        this.types = types;
        this.columns = types.length;
        this.dialect = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DbMeta)) {
            return false;
        }
        DbMeta m = (DbMeta) obj;
        return StringUtils.equalsIgnoreCase(dialect.jdbcDriver(), m.dialect.jdbcDriver())
                && Arrays.deepEquals(fNames, m.fNames)
                && Arrays.deepEquals(types, m.types);
    }

}
