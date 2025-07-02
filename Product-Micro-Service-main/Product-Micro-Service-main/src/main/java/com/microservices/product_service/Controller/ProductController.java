package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.ProductListResponse;
import com.microservices.product_service.Response.ProductResponse;
import org.springframework.web.bind.annotation.*;

public interface ProductController {

    ProductListResponse getProducts();

    ProductResponse getProduct(@PathVariable("id") Long id);

    ProductResponse addProduct(@RequestBody AddProductRequest request);

    ProductResponse updateStock(@PathVariable("id") Long id, @RequestBody AddProductRequest request);

    ProductListResponse searchProducts(@RequestParam(required = false) String categoryType, @RequestParam(required = false) Long categoryCode);
}
