package com.microservices.product_service.Bootstrap;

import com.microservices.product_service.CsvModels.CategoryCSVModel;
import com.microservices.product_service.CsvModels.ProductCSVModel;
import com.microservices.product_service.CsvModels.StockReservationCSVModel;
import com.microservices.product_service.Entity.Category;
import com.microservices.product_service.Entity.CategoryType;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Entity.StockReservation;
import com.microservices.product_service.Repository.CategoryRepository;
import com.microservices.product_service.Repository.ProductRepository;
import com.microservices.product_service.Repository.StockReservationRepository;
import com.microservices.product_service.Service.CSVService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {
    private final CSVService csvService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockReservationRepository stockReservationRepository;

    @Override
    public void run(String... args) throws Exception {
//        deleteData();
//        setCategoryCSVData();
//        setProductCSVData();
//        setStockReservationCSV();
    }
    private void deleteData() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        stockReservationRepository.deleteAll();
    }

    private void setCategoryCSVData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/categories.csv");

        List<CategoryCSVModel> categoryCSVModels = csvService.convertCategoryCSV(file);

        for(CategoryCSVModel item : categoryCSVModels) {
            categoryRepository.save(Category.builder()
                    .categoryType(CategoryType.valueOf(item.getCategoryType().toUpperCase()))
                            .categoryCode(item.getCategoryCode())
                    .build());
        }
    }

    public void setProductCSVData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/products.csv");

        List<ProductCSVModel> productCSVModels = csvService.convertProductCSV(file);


        for(ProductCSVModel item : productCSVModels) {
            Category category = categoryRepository.findByCategoryCode(item.getCategoryCode())
                    .orElseThrow(() -> new RuntimeException("kategori bulunamadi"));

            productRepository.save(Product.builder()
                    .name(item.getName())
                    .description(item.getDescription())
                    .price(item.getPrice())
                    .stockQuantity(item.getStockQuantity())
                    .imageUrl(item.getImageUrl())
                    .popularityScore(item.getPopularityScore())
                    .category(category)
                    .isActive(item.getIsActive())
                    .build());
        }
    }

    public void setStockReservationCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/stock_reservations.csv");

        List<StockReservationCSVModel> stockReservationCSVModels = csvService.convertStockReservationCSV(file);


        for (StockReservationCSVModel item : stockReservationCSVModels) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("product bulunamadi"));

            stockReservationRepository.save(StockReservation.builder()
                    .product(product)
                    .reservedQuantity(item.getReservedQuantity())
                    .orderReference(item.getOrderReference())
                    .confirmed(item.getConfirmed())
                    .build());
        }
    }
}
