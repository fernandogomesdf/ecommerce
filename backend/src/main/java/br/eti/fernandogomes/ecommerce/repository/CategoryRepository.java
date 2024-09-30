package br.eti.fernandogomes.ecommerce.repository;

import br.eti.fernandogomes.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Custom method to find a category by name
    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);

    // Custom method to check if a category exists by name
    boolean existsByName(String name);

}
