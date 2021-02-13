package com.accenture.pregunton.service;

import com.accenture.model.Category;
import com.accenture.pojo.CategoryDto;
import com.accenture.pregunton.exception.CategoryExistsException;
import com.accenture.pregunton.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Inserts a new category.
   *
   * @return The inserted category
   */
  public Category insert(CategoryDto categoryDto) {
    Objects.requireNonNull(categoryDto);
    if (categoryRepository.existsByName(categoryDto.getName())) {
      throw new CategoryExistsException(categoryDto.getName());
    }
    Category category = objectMapper.convertValue(categoryDto, Category.class);
    return this.categoryRepository.save(category);
  }

  /**
   * Returns a list of categories.
   *
   * @return If no category exists on categories DB table returns an empty list; otherwise returns all categories.
   */
  public List<Category> getAll() {
    return this.categoryRepository.findAll(Sort.by("id"));
  }
}
