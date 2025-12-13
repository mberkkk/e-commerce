package com.microservices.product_service.Service;

import com.microservices.product_service.CsvModels.CategoryCSVModel;
import com.microservices.product_service.CsvModels.ProductCSVModel;
import com.microservices.product_service.CsvModels.StockReservationCSVModel;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class CSVService {

    public List<ProductCSVModel> convertProductCSV(File file) throws FileNotFoundException {
        List<ProductCSVModel> productCSVModels = new CsvToBeanBuilder<ProductCSVModel>(new FileReader(file))
                .withType(ProductCSVModel.class)
                .build().parse();
        return productCSVModels;
    }

    public List<CategoryCSVModel> convertCategoryCSV(File file) throws FileNotFoundException {
        List<CategoryCSVModel> categoryCSVModels = new CsvToBeanBuilder<CategoryCSVModel>(new FileReader(file))
                .withType(CategoryCSVModel.class)
                .build().parse();
        return categoryCSVModels;
    }

    public List<StockReservationCSVModel> convertStockReservationCSV(File file) throws FileNotFoundException {
        List<StockReservationCSVModel> stockReservationCSVModels = new CsvToBeanBuilder<StockReservationCSVModel>(new FileReader(file))
                .withType(StockReservationCSVModel.class)
                .build().parse();
        return stockReservationCSVModels;
    }
}
