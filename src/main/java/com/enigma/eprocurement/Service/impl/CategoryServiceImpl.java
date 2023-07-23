package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.CategoryService;
import com.enigma.eprocurement.entity.Category;
import com.enigma.eprocurement.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getOrSave(String name) {
        return categoryRepository.findByName(name).orElseGet(() -> categoryRepository.saveAndFlush(Category.builder().name(name).build()));
    }
}
