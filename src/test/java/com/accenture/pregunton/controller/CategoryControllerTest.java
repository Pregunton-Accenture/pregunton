package com.accenture.pregunton.controller;

import com.accenture.pregunton.pojo.Category;
import com.accenture.pregunton.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    public static final String PERSONAJES = "PERSONAJES";
    public static final String PELICULAS = "PELICULAS";
    public static final String SERIES = "SERIES";
    public static final String LUGARES = "LUGARES";
    public static final String ANIMALES = "ANIMALES";
    public static List<Category> CATEGORY_LIST;

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
        CATEGORY_LIST.add(Category.builder().id(1L).name(PERSONAJES).build());
        CATEGORY_LIST.add(Category.builder().id(2L).name(PELICULAS).build());
        CATEGORY_LIST.add(Category.builder().id(3L).name(SERIES).build());
        CATEGORY_LIST.add(Category.builder().id(4L).name(LUGARES).build());
        CATEGORY_LIST.add(Category.builder().id(5L).name(ANIMALES).build());

        doReturn(CATEGORY_LIST).when(categoryService).getAll();

        this.mockMvc.perform(get("/categories/v1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(CATEGORY_LIST)));
    }

    @Test
    public void getAll_WhenNoCategoriesSetOnDB_ShouldReturnsAnEmptyListOfCategories() throws Exception {
        doReturn(CATEGORY_LIST).when(categoryService).getAll();

        this.mockMvc.perform(get("/categories/v1.0"))
                .andExpect(status().isNoContent());
    }
}
