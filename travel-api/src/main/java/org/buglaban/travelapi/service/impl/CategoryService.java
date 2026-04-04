package org.buglaban.travelapi.service.impl;

import org.buglaban.travelapi.model.Category;
import org.buglaban.travelapi.repository.ICategoryRepository;
import org.buglaban.travelapi.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Override
    public List<Category> findAllCate() {
        List<Category> categories;
        categories = categoryRepository.findAll();
        return categories;
    }
}
