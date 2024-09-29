package br.eti.fernandogomes.ecommerce.service;


import br.eti.fernandogomes.ecommerce.dto.ProductDTO;
import br.eti.fernandogomes.ecommerce.entity.Product;
import br.eti.fernandogomes.ecommerce.mapper.ProductMapper;
import br.eti.fernandogomes.ecommerce.repository.CategoryRepository;
import br.eti.fernandogomes.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (productDTO.getCategoryId() == null) {
            throw new IllegalArgumentException("Product must have a valid category");
        }
        categoryRepository.findById(productDTO.getCategoryId())
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
        return productRepository.findByCategory_Id(categoryId).stream()
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

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setAvailable(productDetails.getAvailable());

        if (productDetails.getCategoryId() != null) {
            categoryRepository.findById(productDetails.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            product.setCategory(categoryRepository.findById(productDetails.getCategoryId()).orElse(null));
        }

        return productMapper.toDTO(productRepository.save(product));
    }

    // Delete
    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productRepository.delete(product);
    }
}
