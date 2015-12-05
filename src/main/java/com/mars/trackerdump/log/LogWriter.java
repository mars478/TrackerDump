package com.mars.trackerdump.log;

public interface LogWriter {

    public void log(Exception e);

    public void log(String line);

    public String ident();

    public Class[] filterPass();
}
