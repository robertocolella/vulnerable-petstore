package io.swagger.injectablepetstore.service;

import io.swagger.injectablepetstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void deleteAll() {
        categoryRepository.reset();
    }
}
