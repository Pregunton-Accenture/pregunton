package com.accenture.pregunton.service;

import com.accenture.pregunton.model.Category;
import com.accenture.pregunton.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Returns a list of categories.
     *
     * @return If no category exists on categories DB table returns
     * an empty list; otherwise returns all categories.
     */
    public List<Category> getAll() {
        return this.categoryRepository.findAll(Sort.by("id"));
    }
}
