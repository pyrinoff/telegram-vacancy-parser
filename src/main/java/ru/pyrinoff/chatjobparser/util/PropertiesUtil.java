package ru.pyrinoff.chatjobparser.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface PropertiesUtil {

    @SneakyThrows
    static @Nullable String getPropertyFromFile(@Nullable final String filepath, @Nullable final String propertyName) {
        if(filepath == null) throw new FileNotFoundException("Property file is null");
        if(propertyName == null) throw new IllegalArgumentException("Property name is null!");
        @NotNull final Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(filepath));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        return properties.getProperty(propertyName);
    }

}
