package com.microservices.cart_service.Mappers;

import com.microservices.cart_service.DTO.CartDto;
import com.microservices.cart_service.Entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CartMapper {
    @Mapping(target = "totalAmount", ignore = true)
    Cart toEntity(CartDto cartDto);
    CartDto toDto(Cart cart);
}
