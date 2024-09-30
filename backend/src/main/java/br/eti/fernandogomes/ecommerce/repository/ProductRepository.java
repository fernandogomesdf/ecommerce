package br.eti.fernandogomes.ecommerce.repository;


import br.eti.fernandogomes.ecommerce.entity.Category;
import br.eti.fernandogomes.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String search);

    List<Product> findByCategory(Category category);

    Page<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);

    boolean existsByName(String name);
}
