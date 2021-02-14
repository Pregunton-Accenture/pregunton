package com.accenture.pregunton.exception;

public class CategoryExistsException extends RuntimeException {

  public CategoryExistsException(String category) {
    super("Category " + category + " already exists.");
  }
}
