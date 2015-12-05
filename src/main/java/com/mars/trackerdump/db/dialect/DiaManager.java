package com.mars.trackerdump.db.dialect;

import com.mars.trackerdump.db.dialect.DiaAbstract;
import java.util.HashMap;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DiaManager {

    private final static HashMap<String, DiaAbstract> DIALECTS = new HashMap<>();

    @ParametersAreNonnullByDefault
    public static synchronized boolean register(DiaAbstract dia, String... aliases) {
        if (dia == null) {
            throw new IllegalArgumentException("DiaManager.register: null dialect passed");
        } else if (ArrayUtils.isEmpty(aliases)) {
            throw new IllegalArgumentException("DiaManager.register: emptyl aliases passed");
        }

        boolean ret = false;
        String alias = null;
        for (String s : aliases) {
            alias = s.toUpperCase().trim();
            if (!DIALECTS.containsKey(alias)) {
                DIALECTS.put(alias, dia);
                ret = true;
            }
        }
        return ret;
    }

    @Nullable
    public static synchronized DiaAbstract impementation(@Nullable String aliasOrConnectionString) {
        String s = aliasOrConnectionString;
        if (StringUtils.isNotBlank(aliasOrConnectionString)) {
            DiaAbstract ret = DIALECTS.get(s.toUpperCase().trim());
            if (ret != null) {
                return ret;
            }
            for (DiaAbstract da : DIALECTS.values()) {
                if (da.detect(s)) {
                    return da;
                }
            }
        }
        return null;
    }

}
