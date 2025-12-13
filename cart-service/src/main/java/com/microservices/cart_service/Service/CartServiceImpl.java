package com.microservices.cart_service.Service;

import com.microservices.cart_service.Entity.Cart;
import com.microservices.cart_service.Entity.CartItem;
import com.microservices.cart_service.Mappers.CartMapper;
import com.microservices.cart_service.Repository.CartRepository;
import com.microservices.cart_service.Request.AddToCartRequest;
import com.microservices.cart_service.Response.CartResponse;
import com.microservices.cart_service.Response.ProductServiceResponse;
import com.microservices.cart_service.DTO.ProductDto;
import com.microservices.cart_service.Producer.CartEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository repository;
    private final CartMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ExternalApiService externalApiService;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final CartEventProducer cartEventProducer;

    @Override
    public CartResponse getCartById(Long cartId) {
        CartResponse response = new CartResponse();
        // Assuming cartId in the database is Integer. If it's Long, change findById
        // parameter.
        Cart cart = repository.findById(cartId.intValue()).orElse(null);
        response.setCartDto(mapper.toDto(cart));
        return response;
    }

    @Override
    public CartResponse getCartByUserId(String userId) {
        CartResponse response = new CartResponse();
        Cart cart = repository.findByUserId(userId).orElse(null);
        response.setCartDto(mapper.toDto(cart));
        return response;
    }

    @Override
    @Transactional
    public CartResponse createCart(String userId) {
        CartResponse response = new CartResponse();

        // Eğer cart zaten varsa, mevcut olanı döndür
        Cart existingCart = repository.findByUserId(userId).orElse(null);
        if (existingCart != null) {
            response.setCartDto(mapper.toDto(existingCart));
            return response;
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalAmount(BigDecimal.ZERO);
        cart = repository.save(cart);

        response.setCartDto(mapper.toDto(cart));
        log.info("New cart created for user: {}", userId);
        return response;
    }

    @Override
    @Transactional
    public CartResponse addItemsToCart(String userId, List<AddToCartRequest.CartItemRequest> cartItemRequests) {
        // Step 2: User Service -> Validate User
        try {
            UserServiceClient.UserValidationResponse userResponse = userServiceClient.validateUser(userId);
            log.info("User validation response received: {}", userResponse);

            if (userResponse == null) {
                log.warn("User validation response is null for userId: {}. Aborting add to cart.", userId);
                return new CartResponse(); // Return empty response
            }

            log.info("User validation response - valid: {}, userId: {}, email: {}",
                    userResponse.valid, userResponse.userId, userResponse.email);

            if (!userResponse.valid) {
                log.warn("User validation failed for userId: {}. Response: {}", userId, userResponse);
                return new CartResponse(); // Return empty response
            }
            log.info("User validation successful for userId: {}", userId);
        } catch (Exception e) {
            log.error("An error occurred during user validation for userId: {}", userId, e);
            return new CartResponse();
        }

        CartResponse response = new CartResponse();

        // Stock kontrolü ve product bilgilerini alma
        for (AddToCartRequest.CartItemRequest cartItemRequest : cartItemRequests) {
            // 1. Stock kontrolü
            boolean hasStock = externalApiService.checkProductStock(cartItemRequest.getProductId(),
                    cartItemRequest.getQuantity());
            if (!hasStock) {
                log.warn("Insufficient stock for product: {}", cartItemRequest.getProductId());
                return response; // Empty response döner
            }

            // 2. Product bilgilerini al ve CartItem'a set et
            try {
                log.info("Fetching product details for productId: {}", cartItemRequest.getProductId());
                ProductServiceResponse productResponse = productServiceClient
                        .getProduct(cartItemRequest.getProductId());

                if (productResponse != null && productResponse.getProductDTO() != null) {
                    ProductDto product = productResponse.getProductDTO();

                    // CartItem oluştur
                    CartItem cartItem = new CartItem();
                    cartItem.setProductId(cartItemRequest.getProductId());
                    cartItem.setQuantity(cartItemRequest.getQuantity());
                    cartItem.setProductName(product.getName());
                    cartItem.setPrice(product.getPrice());
                    cartItem.setSubtotal(cartItem.getSubtotal());

                    log.info("Product details set: name={}, price={}, subtotal={}",
                            product.getName(), product.getPrice(), cartItem.getSubtotal());
                } else {
                    log.warn("Could not fetch product details for productId: {}", cartItemRequest.getProductId());
                    // Default değerler set edebiliriz
                    CartItem cartItem = new CartItem();
                    cartItem.setProductId(cartItemRequest.getProductId());
                    cartItem.setQuantity(cartItemRequest.getQuantity());
                    cartItem.setProductName("Unknown Product");
                    cartItem.setPrice(BigDecimal.ZERO);
                    cartItem.setSubtotal(BigDecimal.ZERO);
                }
            } catch (Exception e) {
                log.error("Error fetching product details for productId: {}, error: {}",
                        cartItemRequest.getProductId(), e.getMessage(), e);
                // Default değerler
                CartItem cartItem = new CartItem();
                cartItem.setProductId(cartItemRequest.getProductId());
                cartItem.setQuantity(cartItemRequest.getQuantity());
                cartItem.setProductName("Unknown Product");
                cartItem.setPrice(BigDecimal.ZERO);
                cartItem.setSubtotal(BigDecimal.ZERO);
            }
        }

        Cart cart = repository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setTotalAmount(BigDecimal.ZERO);
            return repository.save(newCart);
        });

        // Mevcut item'ları güncelle veya yeni ekle
        for (AddToCartRequest.CartItemRequest cartItemRequest : cartItemRequests) {
            // Product bilgilerini al
            ProductServiceResponse productResponse = productServiceClient.getProduct(cartItemRequest.getProductId());
            ProductDto product = productResponse.getProductDTO();

            // CartItem oluştur
            CartItem newItem = new CartItem();
            newItem.setProductId(cartItemRequest.getProductId());
            newItem.setQuantity(cartItemRequest.getQuantity());
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setSubtotal(newItem.getSubtotal());

            boolean itemExists = false;
            for (CartItem existingItem : cart.getCartItems()) {
                if (existingItem.getProductId().equals(newItem.getProductId())) {
                    existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                    // Subtotal otomatik hesaplanacak
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                cart.getCartItems().add(newItem);
            }

            // Kafka event gönder
            cartEventProducer.publishItemAddedToCart(userId, newItem.getProductId(), newItem.getQuantity());
        }

        // Total amount'ı yeniden hesapla
        BigDecimal totalAmount = cart.getCartItems().stream()
                .map(CartItem::getSubtotal) // getSubtotal() otomatik hesaplıyor
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);

        cart = repository.save(cart);

        response.setCartDto(mapper.toDto(cart));
        log.info("Items added to cart for user: {}, total amount: {}", userId, totalAmount);
        return response;
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String userId, CartItem cartItem) {
        CartResponse response = new CartResponse();
        Cart cart = repository.findByUserId(userId).orElse(null);

        if (cart != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getProductId().equals(cartItem.getProductId())) {
                    item.setQuantity(cartItem.getQuantity());
                    // Subtotal otomatik hesaplanacak
                    break;
                }
            }

            // Total amount'ı yeniden hesapla
            BigDecimal totalAmount = cart.getCartItems().stream()
                    .map(CartItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalAmount(totalAmount);

            cart = repository.save(cart);
            response.setCartDto(mapper.toDto(cart));
            log.info("Cart item updated for user: {}", userId);
        }

        return response;
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(String userId, CartItem cartItem) {
        CartResponse response = new CartResponse();
        Cart cart = repository.findByUserId(userId).orElse(null);

        if (cart != null) {
            List<CartItem> cartItems = cart.getCartItems();
            cart.setCartItems(cartItems.stream()
                    .filter(item -> !item.getProductId().equals(cartItem.getProductId()))
                    .collect(Collectors.toList()));

            // Total amount'ı yeniden hesapla
            BigDecimal totalAmount = cart.getCartItems().stream()
                    .map(CartItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalAmount(totalAmount);

            cart = repository.save(cart);
            response.setCartDto(mapper.toDto(cart));
            log.info("Cart item removed for user: {}", userId);
        }
        return response;
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        Cart cart = repository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getCartItems().clear();
            cart.setTotalAmount(BigDecimal.ZERO);
            repository.save(cart);
            log.info("Cart cleared for user: {}", userId);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Her saat
    public void publishAbandonedCarts() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<Cart> inactiveCarts = repository.findInactiveCarts(threshold);

        for (Cart cart : inactiveCarts) {
            cartEventProducer.publishCartAbandoned(cart.getUserId());
            log.info("Abandoned cart event published for user: {}", cart.getUserId());
        }
    }
}