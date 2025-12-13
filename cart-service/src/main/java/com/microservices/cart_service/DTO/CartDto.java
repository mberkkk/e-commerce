package com.microservices.cart_service.DTO;

import com.microservices.cart_service.Entity.CartItem;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto extends BaseDTO {
    private long id;
    private String userId;
    private List<CartItem> cartItems;
}
