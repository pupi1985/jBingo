package model.helpers;

import java.util.HashMap;
import java.util.Properties;

public class ArrayPropertiesHandler {

    /**
     * Return array from properties file. Array must be defined as "key.0=value0",
     * "key.1=value1", etc.
     *
     * @param properties
     * @param key
     * @return
     */
    public HashMap<Integer, String> getArrayPorperties(Properties properties, String key) {
        HashMap<Integer, String> result = new HashMap<>();

        for (String aKey : properties.stringPropertyNames()) {
            if (aKey.startsWith(key)) {
                String value = properties.getProperty(aKey);

                if (value == null) {
                    continue;
                }

                String index = aKey.substring(key.length() + 1);
                result.put(Integer.valueOf(index), value);
            }
        }

        return result;
    }
}
