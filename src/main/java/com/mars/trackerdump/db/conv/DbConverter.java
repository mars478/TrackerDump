package com.mars.trackerdump.db.conv;

import com.mars.trackerdump.db.DbRow;

public abstract class DbConverter<DEST> {

    public abstract DbRow toDbRow(DEST obj);

    public abstract DEST fromDbRow(DbRow row, DEST dummy);

}
