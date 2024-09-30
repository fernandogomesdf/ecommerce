package br.eti.fernandogomes.ecommerce.service;


import br.eti.fernandogomes.ecommerce.dto.ProductDTO;
import br.eti.fernandogomes.ecommerce.entity.Category;
import br.eti.fernandogomes.ecommerce.entity.Product;
import br.eti.fernandogomes.ecommerce.mapper.ProductMapper;
import br.eti.fernandogomes.ecommerce.repository.CategoryRepository;
import br.eti.fernandogomes.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    // Create
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }
        if (productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Product must have a valid category");
        }
        categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        var product = productMapper.toEntity(productDTO);
        return productMapper.toDTO(productRepository.save(product));
    }

    // Read
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toDTO);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        return productRepository.findByCategory(category).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Update
    public ProductDTO updateProduct(Long id, ProductDTO productDetails) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (productDetails != null) {
            if (productDetails.getName() != null) {
                product.setName(productDetails.getName());
            }

            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }

            if (productDetails.getPrice() != null) {
                product.setPrice(productDetails.getPrice());
            }

            if (productDetails.getAvailable() != null) {
                product.setAvailable(productDetails.getAvailable());
            }

            if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
                categoryRepository.findById(productDetails.getCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                product.setCategory(categoryRepository.findById(productDetails.getCategory().getId()).orElse(null));
            }
        }

        return productMapper.toDTO(productRepository.save(product));
    }

    // Delete
    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productRepository.delete(product);
    }

    public Page<ProductDTO> getAllProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(productMapper::toDTO);
    }
}
