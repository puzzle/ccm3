package ch.puzzle.ccm3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Provider
public class JacksonConvertersProvider implements ParamConverterProvider {
    private final Logger logger = LoggerFactory.getLogger(JacksonConvertersProvider.class);

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.getName().equals(LocalDateTime.class.getName())) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String value) {
                    if (value == null || value.isEmpty()) {
                        return null;
                    }
                    try {
                        return (T) LocalDateTime.parse(value);
                    } catch (DateTimeParseException e) {
                        logger.debug("Could not convert provided String {} as LocalDateTime: {}", value, e.getMessage());
                        return null;
                    }
                }

                @Override
                public String toString(T value) {
                    return value != null ? value.toString() : null;
                }
            };
        }
        return null;
    }
}