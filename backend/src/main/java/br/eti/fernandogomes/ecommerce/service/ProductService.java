package br.eti.fernandogomes.ecommerce.service;

import br.eti.fernandogomes.ecommerce.dto.CategoryDTO;
import br.eti.fernandogomes.ecommerce.dto.ProductDTO;
import br.eti.fernandogomes.ecommerce.entity.Category;
import br.eti.fernandogomes.ecommerce.entity.Product;
import br.eti.fernandogomes.ecommerce.repository.CategoryRepository;
import br.eti.fernandogomes.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Create
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }
        if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Product must have a valid category");
        }
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        var product = dtoToEntity(productDTO);
        product.setCategory(category);
        product = productRepository.save(product);
        return entityToDTO(product);
    }

    // Read
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(this::entityToDTO);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        return productRepository.findByCategory(category).stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::entityToDTO)
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
                Category category = categoryRepository.findById(productDetails.getCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                product.setCategory(category);
            }
        }

        product = productRepository.save(product);
        return entityToDTO(product);
    }

    // Delete
    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productRepository.delete(product);
    }

    // Paginated Read
    public Page<ProductDTO> getAllProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
    }

    // Manual mapping from DTO to Entity
    private Product dtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setAvailable(productDTO.getAvailable());
        // Category will be set in create/update methods after fetching from repository
        return product;
    }

    // Manual mapping from Entity to DTO
    private ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setAvailable(product.getAvailable());

        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            productDTO.setCategory(categoryDTO);
        }

        return productDTO;
    }
}
