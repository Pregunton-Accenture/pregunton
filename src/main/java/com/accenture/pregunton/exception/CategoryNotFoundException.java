package com.accenture.pregunton.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id);
    }

}
