package com.mars.trackerdump.orm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * case insensitive orm - fieldnames forced to uppercase
 *
 */
public class Mapper {

    private final static String GETTER_PREFIX = "GET";
    private final static String SETTER_PREFIX = "SET";

    @Nullable
    private Map readObject(@Nonnull Object obj) throws Exception {
        Method[] methods = obj.getClass().getDeclaredMethods();
        if (ArrayUtils.isEmpty(methods)) {
            return null;
        }

        String mName = null;
        Map<String, Object> ret = new HashMap<>();
        for (Method m : methods) {
            if (m.isAccessible() && (mName = m.getName()).toUpperCase().startsWith(GETTER_PREFIX)) {
                ret.put(mName.toUpperCase(), m.invoke(obj));
            }
        }
        return ret;
    }

    @Nullable
    private <T> T writeObject(@Nullable Map<String, Object> fields, @Nonnull Class<T> clz) throws Exception {
        T obj = clz.newInstance();
        if (fields == null || fields.keySet().isEmpty()) {
            return obj;
        }

        Object val = null;
        String mName = null;
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAccessible() && (mName = m.getName()).toUpperCase().startsWith(SETTER_PREFIX)) {
                mName = mName.replaceFirst(SETTER_PREFIX, "");
                for (String key : fields.keySet()) {
                    if (StringUtils.equalsIgnoreCase(key, mName)) {
                        val = fields.get(key);
                        m.invoke(obj, val);
                        break;
                    }
                }
            }
        }

        return obj;
    }
}
