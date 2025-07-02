package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.CategoryQueryRequest;
import com.microservices.product_service.Response.GetCategoriesResponse;
import com.microservices.product_service.Response.ProductListResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategoryController {


    GetCategoriesResponse listCategories();
}
