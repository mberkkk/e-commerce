package com.example.order.order_service.infrastructure.persistence.client;

import com.example.order.order_service.application.dto.ProductDetailsDto;
import com.example.order.order_service.application.port.out.ProductServiceClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component; // Bu kalacak
import org.springframework.web.reactive.function.client.WebClient; // Bu kalacak

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component // Bu kalacak
@Profile("mock")
public class ProductServiceRestAdapter implements ProductServiceClientPort {

    private final WebClient webClient; // Bu kalacak
    private final String reserveStockPath; // Bu kalacak
    private final String getDetailsPath; // Bu kalacak

    private final Map<String, Integer> mockStocks = new ConcurrentHashMap<>();
    private final Map<String, ProductDetailsDto> mockProducts = new ConcurrentHashMap<>();

    public ProductServiceRestAdapter(WebClient.Builder webClientBuilder,
            @Value("${services.product.base-url}") String productServiceBaseUrl,
            @Value("${services.product.endpoints.reserve-stock}") String reserveStockPath,
            @Value("${services.product.endpoints.get-details}") String getDetailsPath) {
        this.webClient = webClientBuilder.baseUrl(productServiceBaseUrl).build();
        this.reserveStockPath = reserveStockPath;
        this.getDetailsPath = getDetailsPath;

        // Mock data initialization - Tüm parametreleri ekle
        mockStocks.put("1", 10); // Cart Service ile uyumlu ID'ler
        mockStocks.put("2", 50);
        mockStocks.put("3", 5);
        mockStocks.put("999", 0);

        // ProductDetailsDto constructor: (productId, name, description, price,
        // stockQuantity, isActive)
        mockProducts.put("1", new ProductDetailsDto(
                "1",
                "Laptop XPS 15",
                "High-performance laptop", // description parametresi
                BigDecimal.valueOf(1200.00),
                10,
                true // isActive parametresi
        ));

        mockProducts.put("2", new ProductDetailsDto(
                "2",
                "Gaming Mouse",
                "RGB gaming mouse", // description parametresi
                BigDecimal.valueOf(25.00),
                50,
                true // isActive parametresi
        ));

        mockProducts.put("3", new ProductDetailsDto(
                "3",
                "Mechanical Keyboard",
                "Cherry MX switches", // description parametresi
                BigDecimal.valueOf(75.00),
                5,
                true // isActive parametresi
        ));

        mockProducts.put("999", new ProductDetailsDto(
                "999",
                "Out of Stock Item",
                "Currently unavailable", // description parametresi
                BigDecimal.valueOf(100.00),
                0,
                false // isActive parametresi
        ));
    }

    @Override
    public boolean reserveStock(Map<String, Integer> productQuantities) {
        System.out.println("Mock ProductService: Attempting to reserve stock: " + productQuantities);
        // Simulate stock check and reservation
        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productId = entry.getKey();
            Integer requestedQuantity = entry.getValue();

            if (!mockStocks.containsKey(productId) || mockStocks.get(productId) < requestedQuantity) {
                System.out.println("Mock ProductService: Insufficient stock for " + productId);
                releaseStock(productQuantities.entrySet().stream()
                        .filter(e -> mockStocks.containsKey(e.getKey()) && mockStocks.get(e.getKey()) >= e.getValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                return false;
            }
        }
        productQuantities
                .forEach((productId, quantity) -> mockStocks.computeIfPresent(productId, (k, v) -> v - quantity));
        System.out.println("Mock ProductService: Stock reserved. Current stocks: " + mockStocks);
        return true;
        // --- GERÇEK ENTEGRASYON KODU (ŞİMDİLİK YORUM SATIRI YAPIN) ---
        /*
         * Boolean result = this.webClient.post()
         * .uri(reserveStockPath)
         * .body(Mono.just(productQuantities), Map.class)
         * .retrieve()
         * .onStatus(httpStatus -> httpStatus.isError(),
         * response -> Mono.error(new
         * RuntimeException("Failed to reserve stock on Product Service")))
         * .bodyToMono(Boolean.class)
         * .block();
         * return Boolean.TRUE.equals(result);
         */
    }

    @Override
    public void releaseStock(Map<String, Integer> productQuantities) {
        System.out.println("Mock ProductService: Attempting to release stock: " + productQuantities);
        productQuantities
                .forEach((productId, quantity) -> mockStocks.computeIfPresent(productId, (k, v) -> v + quantity));
        System.out.println("Mock ProductService: Stock released. Current stocks: " + mockStocks);
        // --- GERÇEK ENTEGRASYON KODU (ŞİMDİLİK YORUM SATIRI YAPIN) ---
        // this.webClient.post().uri("/api/inventory/release")...
    }

    @Override
    public Map<String, ProductDetailsDto> getProductDetails(List<String> productIds) {
        return productIds.stream()
                .filter(mockProducts::containsKey)
                .collect(Collectors.toMap(id -> id, mockProducts::get));
        // --- GERÇEK ENTEGRASYON KODU (ŞİMDİLİK YORUM SATIRI YAPIN) ---
        /*
         * List<ProductDetailsDto> detailsList = this.webClient.get()
         * .uri(uriBuilder -> uriBuilder.path(getDetailsPath)
         * .queryParam("ids", String.join(",", productIds))
         * .build())
         * .retrieve()
         * .bodyToFlux(ProductDetailsDto.class)
         * .collectList()
         * .block();
         * return detailsList != null ?
         * detailsList.stream().collect(Collectors.toMap(ProductDetailsDto::
         * getProductId, dto -> dto)) : Map.of();
         */
    }
}