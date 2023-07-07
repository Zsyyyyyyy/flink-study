package com.zsy.utils;


import cn.hutool.core.io.resource.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

public class PropertiesConfigUtils {
    public static Properties getConfig(String envName) throws IOException {
        if (envName == null) {
            envName = "local";
        }

        String file = "application-" + envName + ".properties";
        ClassPathResource resource = new ClassPathResource(file);
        Properties properties = new Properties();
        properties.load(resource.getStream());
        return properties;
    }
}
