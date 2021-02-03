package com.accenture.pregunton.service;

import com.accenture.model.Category;
import com.accenture.pregunton.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

  public static final String PERSONAJES = "PERSONAJES";
  public static final String PELICULAS = "PELICULAS";
  public static final String SERIES = "SERIES";
  public static final String LUGARES = "LUGARES";
  public static final String ANIMALES = "ANIMALES";
  public static List<Category> CATEGORY_LIST;

  @InjectMocks
  private CategoryService categoryService;
  @Mock
  private CategoryRepository categoryRepository;

  @Before
  public void setUp() {
    CATEGORY_LIST = new ArrayList<>();
  }

  @Test
  public void getAll_When5CategoriesSetOnDB_ShouldReturnsAListWithAllCategories() {
    CATEGORY_LIST.add(Category.builder()
        .id(1L)
        .name(PERSONAJES)
        .build());
    CATEGORY_LIST.add(Category.builder()
        .id(2L)
        .name(PELICULAS)
        .build());
    CATEGORY_LIST.add(Category.builder()
        .id(3L)
        .name(SERIES)
        .build());
    CATEGORY_LIST.add(Category.builder()
        .id(4L)
        .name(LUGARES)
        .build());
    CATEGORY_LIST.add(Category.builder()
        .id(5L)
        .name(ANIMALES)
        .build());

    doReturn(CATEGORY_LIST).when(categoryRepository)
        .findAll(any(Sort.class));

    List<Category> result = categoryService.getAll();

    verify(categoryRepository, times(1)).findAll(any(Sort.class)); assertEquals(CATEGORY_LIST, result);
  }

  @Test
  public void getAll_WhenNoCategoriesSetOnDB_ShouldReturnsAnEmptyListOfCategories() {
    doReturn(CATEGORY_LIST).when(categoryRepository)
        .findAll(any(Sort.class));

    List<Category> result = categoryService.getAll();

    verify(categoryRepository, times(1)).findAll(any(Sort.class)); assertEquals(CATEGORY_LIST, result);
  }
}
