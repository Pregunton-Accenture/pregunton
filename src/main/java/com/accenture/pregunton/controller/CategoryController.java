package com.accenture.pregunton.controller;

import com.accenture.pregunton.model.Category;
import com.accenture.pregunton.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

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
        return categories.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categories);
    }
}
