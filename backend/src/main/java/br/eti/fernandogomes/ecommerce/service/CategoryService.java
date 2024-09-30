package br.eti.fernandogomes.ecommerce.service;

import br.eti.fernandogomes.ecommerce.dto.CategoryDTO;
import br.eti.fernandogomes.ecommerce.entity.Category;
import br.eti.fernandogomes.ecommerce.mapper.CategoryMapper;
import br.eti.fernandogomes.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductService productService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productService = productService;
    }

    // Create
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    // Read
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDTO);
    }

    public Optional<CategoryDTO> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryMapper::toDTO);
    }

    // Update
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(categoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    // Delete
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Check if there are any products associated with this category
        if (!productService.getProductsByCategory(id).isEmpty()) {
            throw new IllegalArgumentException("Cannot delete category. There are products associated with it.");
        }

        // Delete all child categories recursively
        deleteChildCategories(category);

        categoryRepository.delete(category);
    }

    // Helper method to delete child categories recursively
    private void deleteChildCategories(Category category) {
        for (Category childCategory : category.getChildren()) {
            deleteChildCategories(childCategory); // Recursive call for nested children
            categoryRepository.delete(childCategory);
        }
    }
}