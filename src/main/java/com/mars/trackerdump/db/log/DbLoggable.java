package com.mars.trackerdump.db.log;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DbLoggable {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public synchronized void setLogger(Logger lgr) {
        logger = lgr;
    }

    protected synchronized void logException(Exception e) {
        if (logger != null) {
            logger.error(userInfo() + ExceptionUtils.getStackTrace(e));
        }
    }

    protected synchronized void logQuery(String line) {
        if (logger != null) {
            logger.info(userInfo() + line);
        }
    }

    protected String userInfo() {
        return "Username: " + getDbUsername() + "\n";
    }

    protected abstract String getDbUsername();

}
