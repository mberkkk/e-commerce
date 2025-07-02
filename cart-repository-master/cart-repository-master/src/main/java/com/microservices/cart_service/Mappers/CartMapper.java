package com.microservices.cart_service.Mappers;

import com.microservices.cart_service.DTO.CartDto;
import com.microservices.cart_service.Entity.Cart;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {
    Cart toEntity(CartDto cartDto);
    CartDto toDto(Cart cart);
}
