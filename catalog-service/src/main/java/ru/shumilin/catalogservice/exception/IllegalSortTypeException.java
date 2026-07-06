package ru.shumilin.catalogservice.exception;

public class IllegalSortTypeException extends RuntimeException {
    public IllegalSortTypeException(String sortType) {
        super("SortType: %s is illegal".formatted(sortType));
    }
}
