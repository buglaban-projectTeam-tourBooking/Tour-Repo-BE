package org.buglaban.travelapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.model.Category;
import org.buglaban.travelapi.service.ICategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.buglaban.travelapi.dto.response.ResponseData;
import org.buglaban.travelapi.dto.response.ResponseFailure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseData<List<Category>> getAllCategories() {
        try {
            List<Category> result  = categoryService.findAllCate();
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting categories: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
