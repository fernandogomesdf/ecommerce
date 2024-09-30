package br.eti.fernandogomes.ecommerce.mapper;

import br.eti.fernandogomes.ecommerce.dto.ProductDTO;
import br.eti.fernandogomes.ecommerce.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductDTO toDTO(Product product);


    Product toEntity(ProductDTO productDTO);
}
