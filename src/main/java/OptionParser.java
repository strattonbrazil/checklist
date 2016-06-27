package com.github.strattonbrazil.checklist;

import java.util.*;

public class OptionParser {
    public static boolean getBoolean(LinkedHashMap options, String key, boolean defaultVal) {
        if (options.containsKey(key)) {
            Object val = options.get(key);
            if (val instanceof Boolean)
                return (Boolean)val;
            else
                throw new RuntimeException("expected boolean option for '" + key + "': " + val);
        }
        return defaultVal;
    }
}
