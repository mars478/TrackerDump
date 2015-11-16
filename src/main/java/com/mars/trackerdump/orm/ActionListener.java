package com.mars.trackerdump.orm;

import java.io.Serializable;

public interface ActionListener<RET, ARG> extends Serializable {

    public RET onAction(ARG arg) throws Exception;

}
