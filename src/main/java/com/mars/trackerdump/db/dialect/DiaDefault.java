package com.mars.trackerdump.db.dialect;

import com.mars.trackerdump.db.DbMeta;
import com.mars.trackerdump.db.DbUtil;
import com.mars.trackerdump.db.DbVal;
import com.mars.trackerdump.db.Query;
import com.mars.trackerdump.db.common.DateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("override")
public abstract class DiaDefault extends DiaAbstract implements Serializable {

    public DiaDefault() {
        super();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public DiaDefault put(PreparedStatement pst, Query.ParserData<DbVal> pd) throws SQLException {
        int index = 1;
        for (DbVal v : pd.params) {
            if (v.isBlob()) {
                pst.setBytes(index, v.getBlob());
            } else if (v.isDate()) {
                Date d = v.getDate();
                pst.setTimestamp(index, d == null ? null : new Timestamp(d.getTime()));
            } else if (v.isNumber()) {
                pst.setBigDecimal(index, v.getNumber());
            } else if (v.isString()) {
                pst.setString(index, v.getString());
            } else if (v.isVoid()) {
                throw new SQLException("Query.exec: void parameter found (undefined class with null value)");
            }
            index++;
        }

        return this;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public List<DbVal> read(DbMeta meta, ResultSet rs) throws SQLException {
        List<DbVal> temp = new LinkedList<>();
        for (int col = 1; col < meta.columns + 1; col++) {
            if (DbUtil.isNumber(meta, col)) {
                temp.add(new DbVal(rs.getBigDecimal(col)));
            } else if (DbUtil.isString(meta, col)) {
                temp.add(new DbVal(rs.getString(col)));
            } else if (DbUtil.isDate(meta, col)) {
                temp.add(new DbVal(rs.getTimestamp(col)));
            } else if (DbUtil.isBlob(meta, col)) {
                temp.add(new DbVal(rs.getBytes(col)));
            } else {
                temp.add(new DbVal(null));
            }
        }
        return temp;
    }

    @Nullable
    public BigDecimal convertToNumber(@Nullable Object o) {
        return o == null ? null : new BigDecimal(o.toString());
    }

    @Nullable
    public String convertToString(@Nullable Object o) {
        return o == null ? null : o.toString();
    }

    @Nullable
    public Date convertToDate(@Nullable Object o) throws ParseException {
        if (o instanceof Date) {
            return (Date) o;
        } else if (o instanceof Timestamp) {
            return (Timestamp) o;
        } else if (o instanceof String) {
            String d = ((String) o).trim().replaceAll("\\.", "-");
            String f = DateUtil.determineDateFormat(d);
            if (f != null) {
                return new SimpleDateFormat(f).parse(d);
            }
        }

        return null;
    }

    @Override
    public byte[] convertToBytes(Object o) {
        throw new UnsupportedOperationException("DiaDefault.convertToBytes is not supported yet.");
    }
}
