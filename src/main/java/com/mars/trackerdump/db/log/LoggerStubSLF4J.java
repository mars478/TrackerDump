package com.mars.trackerdump.db.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

@SuppressWarnings("override")
public abstract class LoggerStubSLF4J implements Logger {

    public abstract String getName();

    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String string) {
    }

    public void trace(String string, Object o) {
    }

    public void trace(String string, Object o, Object o1) {
    }

    public void trace(String string, Object... os) {
    }

    public void trace(String string, Throwable thrwbl) {
    }

    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    public void trace(Marker marker, String string) {
    }

    public void trace(Marker marker, String string, Object o) {
    }

    public void trace(Marker marker, String string, Object o, Object o1) {
    }

    public void trace(Marker marker, String string, Object... os) {
    }

    public void trace(Marker marker, String string, Throwable thrwbl) {
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public void debug(String string) {
    }

    public void debug(String string, Object o) {
    }

    public void debug(String string, Object o, Object o1) {
    }

    public void debug(String string, Object... os) {
    }

    public void debug(String string, Throwable thrwbl) {
    }

    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    public void debug(Marker marker, String string) {
    }

    public void debug(Marker marker, String string, Object o) {
    }

    public void debug(Marker marker, String string, Object o, Object o1) {
    }

    public void debug(Marker marker, String string, Object... os) {
    }

    public void debug(Marker marker, String string, Throwable thrwbl) {
    }

    public boolean isInfoEnabled() {
        return true;
    }

    public abstract void info(String string);

    public void info(String string, Object o) {
    }

    public void info(String string, Object o, Object o1) {
    }

    public void info(String string, Object... os) {
    }

    public void info(String string, Throwable thrwbl) {
    }

    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    public void info(Marker marker, String string) {
    }

    public void info(Marker marker, String string, Object o) {
    }

    public void info(Marker marker, String string, Object o, Object o1) {
    }

    public void info(Marker marker, String string, Object... os) {
    }

    public void info(Marker marker, String string, Throwable thrwbl) {
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public void warn(String string) {
    }

    public void warn(String string, Object o) {
    }

    public void warn(String string, Object... os) {
    }

    public void warn(String string, Object o, Object o1) {
    }

    public void warn(String string, Throwable thrwbl) {
    }

    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    public void warn(Marker marker, String string) {
    }

    public void warn(Marker marker, String string, Object o) {
    }

    public void warn(Marker marker, String string, Object o, Object o1) {
    }

    public void warn(Marker marker, String string, Object... os) {
    }

    public void warn(Marker marker, String string, Throwable thrwbl) {
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public abstract void error(String string);

    public void error(String string, Object o) {
    }

    public void error(String string, Object o, Object o1) {
    }

    public void error(String string, Object... os) {
    }

    public void error(String string, Throwable thrwbl) {
    }

    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    public void error(Marker marker, String string) {
    }

    public void error(Marker marker, String string, Object o) {
    }

    public void error(Marker marker, String string, Object o, Object o1) {
    }

    public void error(Marker marker, String string, Object... os) {
    }

    public void error(Marker marker, String string, Throwable thrwbl) {
    }

}
