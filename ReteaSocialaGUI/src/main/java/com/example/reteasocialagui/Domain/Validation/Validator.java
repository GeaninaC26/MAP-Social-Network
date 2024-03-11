package com.example.reteasocialagui.Domain.Validation;
import com.example.reteasocialagui.Domain.Validation.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
