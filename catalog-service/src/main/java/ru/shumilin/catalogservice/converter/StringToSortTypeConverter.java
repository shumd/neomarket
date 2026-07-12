package ru.shumilin.catalogservice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.shumilin.catalogservice.exception.IllegalSortTypeException;
import ru.shumilin.catalogservice.model.SortType;

@Component
public class StringToSortTypeConverter implements Converter<String, SortType> {
    @Override
    public SortType convert(String source) {
        try {
            if (source == null || source.isBlank()) {
                throw new IllegalSortTypeException(source);
            }

            return SortType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalSortTypeException(source);
        }
    }
}
