package com.mars.trackerdump.orm;

import com.mars.trackerdump.entity.Pair;
import com.mars.trackerdump.entity.Quad;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public class Cast {

    // key - db class, val - java class, thriple - DB-Java converter, quad - Java-DB converter
    static List<Quad<Integer, Class, ActionListener, ActionListener>> map = new LinkedList();

    @Nonnull
    public static Object castDbJava(@Nonnull Integer typeDb, @Nonnull Object dbObject) throws Exception {
        ActionListener a = null;
        for (Quad<Integer, Class, ActionListener, ActionListener> db : map) {
            if (Objects.equals(typeDb, db.getKey())) {
                a = db.getThird();
                return a == null ? dbObject : db.getThird().onAction(dbObject);
            }
        }
        throw new ClassCastException("Unable to perform DB->Java cast on class " + dbObject.getClass().getName());
    }

    @Nonnull
    public static Pair<Integer, Object> castJavaDb(@Nonnull Object javaObject) throws Exception {
        Class cl = javaObject.getClass();
        ActionListener a = null;
        for (Quad<Integer, Class, ActionListener, ActionListener> db : map) {
            if (cl == db.getVal()) {
                a = db.getQuad();
                return new Pair<>(db.getKey(), a == null ? javaObject : a.onAction(javaObject));
            }
        }
        throw new ClassCastException("Unable to perform Java->DB cast on class " + javaObject.getClass().getName());
    }

}
