package com.mars.trackerdump.log;

import java.io.Serializable;
import javax.annotation.Nonnull;

@SuppressWarnings("override")
public class SoutLogger implements LogWriter, Serializable {

    public void log(@Nonnull Exception e) {
        e.printStackTrace(System.err);
    }

    public void log(@Nonnull String s) {
        System.err.println(s);
    }

    public String ident() {
        return "System.out/err";
    }

    public Class[] filterPass() {
        return null;
    }
}
