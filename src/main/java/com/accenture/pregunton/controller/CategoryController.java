package com.accenture.pregunton.controller;

import com.accenture.model.Category;
import com.accenture.pojo.CategoryDto;
import com.accenture.pregunton.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "categories")
@Api(tags = "Category API", description = "This API has operations related to Category Controller")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @PostMapping(value = "v1.0", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("Adds a new category.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  public ResponseEntity<Void> insert(@RequestBody CategoryDto categoryDto) {
    categoryService.insert(categoryDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "v1.0", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("Retrieves a list of categories.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 204, message = "No Content"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 500, message = "Internal Server Error"),
  })
  @ResponseBody
  public ResponseEntity<List<Category>> getAll() {
    List<Category> categories = categoryService.getAll();
    return categories.isEmpty() ? ResponseEntity.noContent()
        .build() : ResponseEntity.ok(categories);
  }
}
