package com.accenture.pregunton.controller;

import com.accenture.model.Category;
import com.accenture.pregunton.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

  private static final String BASE_URL = "/categories/v1.0";
  private static final String PERSONAJES = "PERSONAJES";
  private static final String PELICULAS = "PELICULAS";
  private static final String SERIES = "SERIES";
  private static final String LUGARES = "LUGARES";
  private static final String ANIMALES = "ANIMALES";
  private static List<Category> CATEGORY_LIST;

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @InjectMocks
  private CategoryController categoryController;
  @MockBean
  private CategoryService categoryService;

  @Before
  public void setUp() {
    CATEGORY_LIST = new ArrayList<>();
  }

  @Test
  public void getAll_When5CategoriesSetOnDB_ShouldReturnsAListWithAllCategoriesAnd200() throws Exception {
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

    when(categoryService.getAll()).thenReturn(CATEGORY_LIST);

    this.mockMvc.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(CATEGORY_LIST)));
  }

  @Test
  public void getAll_WhenNoCategoriesSetOnDB_ShouldReturnsAnEmptyListOfCategories() throws Exception {
    when(categoryService.getAll()).thenReturn(CATEGORY_LIST);

    this.mockMvc.perform(get(BASE_URL))
        .andExpect(status().isNoContent());
  }
}
