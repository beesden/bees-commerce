package org.beesden.commerce.common.util;

import feign.codec.Encoder;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class RequestObjectEncoder {

    @Bean
    public Encoder encoder() {
        Encoder defaultEncoder = new Encoder.Default();

        return (object, bodyType, template) -> {
            Class clazz = (Class) bodyType;
            if (clazz.getAnnotation(RequestObject.class) != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        Object data = field.get(object);
                        if (data == null) continue;
                        if (field.getType().getName().equals("java.util.List")) {
                            List<String> s = new ArrayList<>();
                            for (Object o : (List<?>) data) {
                                s.add(o.toString());
                            }
                            template.query(field.getName(), s.toArray(new String[]{}));
                        } else {
                            template.query(field.getName(), data.toString());
                        }
                    } catch (IllegalAccessException e) {
                        continue;
                    }
                }
            } else {
                defaultEncoder.encode(object, bodyType, template);
            }
        };
    }

}

