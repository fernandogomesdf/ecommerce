package br.eti.fernandogomes.ecommerce.mapper;

import br.eti.fernandogomes.ecommerce.dto.CategoryDTO;
import br.eti.fernandogomes.ecommerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category category);
}
