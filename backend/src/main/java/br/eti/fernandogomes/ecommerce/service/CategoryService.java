package br.eti.fernandogomes.ecommerce.service;

import br.eti.fernandogomes.ecommerce.dto.CategoryDTO;
import br.eti.fernandogomes.ecommerce.entity.Category;
import br.eti.fernandogomes.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
    }

    // Read
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO dto =  new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            categoryDTOs.add(dto);
        }
        return categoryDTOs;
    }
}