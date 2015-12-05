package com.mars.trackerdump.prop;

import java.io.InputStream;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class Prop {

    final static String FILE_CONFIG = "config.prop";
    final static Properties PROP = new Properties();
    final static String[] BOOLEAN_ALIASES = new String[]{"+", "TRUE", "1"};

    private synchronized static boolean init() {
        try (InputStream input = Prop.class.getClassLoader().getResourceAsStream(FILE_CONFIG);) {
            PROP.load(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Nullable
    public static String getValue(@Nonnull String propName) {
        return (PROP.isEmpty() && !init())
                ? null
                : PROP.getProperty(propName);
    }

    public static boolean isTrue(@Nonnull String propName) {
        String val = getValue(propName);
        if (StringUtils.isNotBlank(val)) {
            for (String s : BOOLEAN_ALIASES) {
                if (s.equalsIgnoreCase(val)) {
                    return true;
                }
            }
        }
        return false;
    }

}
