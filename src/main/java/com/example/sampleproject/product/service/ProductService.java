package com.example.sampleproject.product.service;

import com.example.sampleproject.product.entity.Product;
import com.example.sampleproject.product.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final CsvUtil csvUtil;
    private final List<Product> productList; // CSV 또는 API 데이터

    @Autowired
    public ProductService(CsvUtil csvUtil) {
        this.csvUtil = csvUtil;
        this.productList = loadProducts(); // 데이터를 로드
    }

//  특정 검색어 + 카테고리 검색
public List<Product> searchProducts(String category, String query, String event) {
    return productList.stream()
            .filter(product -> (category == null || category.isEmpty() || category.equalsIgnoreCase(product.getCategory()))
                    && (query == null || query.isEmpty() || product.getName().toLowerCase().contains(query.toLowerCase()))
                    && (event == null || event.isEmpty() || event.equalsIgnoreCase(product.getEvent())))
            .collect(Collectors.toList());
}

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return defaultValue; // 빈 값 처리
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid number format: {}", value, e);
            return defaultValue; // 잘못된 값 처리
        }
    }

    private List<Product> loadProducts() {
        List<Map<String, String>> csvData = csvUtil.loadCsv("/Products.csv");

        return csvData.stream().map(data -> new Product(
                data.get("상품ID"),
                data.get("상품명"),
                data.get("카테고리"),
                data.get("가격"),
                data.get("행사"),
                Integer.parseInt(data.get("재고량")),
                data.get("이미지")
        )).collect(Collectors.toList());
    }

    public Product getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        if (productList.isEmpty()) {
            log.warn("Product list is empty!");
        }
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}