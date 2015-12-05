package com.mars.trackerdump.log;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Loggable {

    protected boolean logFilter = false;

    @Nullable
    public static final List<LogWriter> LOGGERS = new LinkedList();

    public static synchronized void addLogger(LogWriter logWriter) {
        if (logWriter == null) {
            throw new IllegalArgumentException("Loggable.setLogger: unable to replace logWriter logger");
        } else if (Loggable.LOGGERS.contains(logWriter)) {
        } else {
            Loggable.LOGGERS.add(logWriter);
        }
    }

    protected void log(Exception e) {
        synchronized (LOGGERS) {
            Logger l = slf4j();
            if (l != null) {
                l.error(ExceptionUtils.getStackTrace(e));
            }
            for (LogWriter lw : LOGGERS) {
                if (filterMatch(this, lw)) {
                    lw.log(e);
                }
            }
        }
    }

    protected synchronized void log(String line) {
        synchronized (LOGGERS) {
            Logger l = slf4j();
            if (l != null) {
                l.info(line);
            }
            for (LogWriter lw : LOGGERS) {
                if (filterMatch(this, lw)) {
                    lw.log(line);
                }
            }
        }
    }

    protected Logger slf4j() {
        return LoggerFactory.getLogger(getClass());
    }

    private static boolean filterMatch(Loggable obj, LogWriter lw) {
        return obj.logFilter
                ? ArrayUtils.contains(lw.filterPass(), obj.getClass())
                : true;
    }
}
