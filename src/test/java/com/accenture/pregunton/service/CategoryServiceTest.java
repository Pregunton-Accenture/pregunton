package com.accenture.pregunton.service;

import com.accenture.model.Category;
import com.accenture.pregunton.exception.CategoryExistsException;
import com.accenture.pregunton.repository.CategoryRepository;
import com.accenture.pregunton.util.ModelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
  @Mock
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    CATEGORY_LIST = new ArrayList<>();
    doReturn(ModelUtil.CATEGORY).when(objectMapper)
        .convertValue(ModelUtil.CATEGORY_DTO, Category.class);
  }

  @Test
  public void insert_WhenValidCategory_ShouldReturnCategory() {
    doReturn(false).when(categoryRepository)
        .existsByName(ModelUtil.CATEGORY_DTO.getName());
    doReturn(ModelUtil.CATEGORY).when(categoryRepository)
        .save(ModelUtil.CATEGORY);

    Category result = categoryService.insert(ModelUtil.CATEGORY_DTO);

    assertEquals(ModelUtil.CATEGORY, result);
  }

  @Test
  public void insert_WhenNullCategory_ShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> categoryService.insert(null));
  }

  @Test
  public void insert_WhenExistingCategory_ShouldThrowCategoryExistsException() {
    doReturn(true).when(categoryRepository)
        .existsByName(ModelUtil.CATEGORY_DTO.getName());

    assertThrows(CategoryExistsException.class, () -> categoryService.insert(ModelUtil.CATEGORY_DTO));
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

    verify(categoryRepository, times(1)).findAll(any(Sort.class));
    assertEquals(CATEGORY_LIST, result);
  }

  @Test
  public void getAll_WhenTokenIsExpired_ShouldReturnsUnauthorized() {
    doReturn(CATEGORY_LIST).when(categoryRepository)
        .findAll(any(Sort.class));

    List<Category> result = categoryService.getAll();

    verify(categoryRepository, times(1)).findAll(any(Sort.class));
    assertEquals(CATEGORY_LIST, result);
  }
}
