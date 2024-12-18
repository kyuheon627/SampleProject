package com.example.sampleproject.product.controller;

import com.example.sampleproject.member.spring.AuthInfo;
import com.example.sampleproject.product.entity.Product;
import com.example.sampleproject.product.entity.Review;
import com.example.sampleproject.product.repository.ReviewRepository;
import com.example.sampleproject.product.service.ProductService;
import com.example.sampleproject.product.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    // 생성자 주입
    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String category,
                                 @RequestParam(required = false) String query,
                                 @RequestParam(required = false) String event,
                                 @PageableDefault(page = 0, size = 20) Pageable pageable,
                                 Model model) {
        // 모든 카테고리와 이벤트 필터 가져오기
        List<String> categories = List.of("전체", "식품", "음료", "과자", "아이스크림", "생활용품");
        List<String> eventTypes = List.of("전체", "1+1", "2+1");

        // "전체"를 선택한 경우 또는 빈 문자열이 전달된 경우 category를 null로 설정
        if (category == null || category.isEmpty() || "전체".equals(category)) {
            category = null;
        }
        if (event == null || event.isEmpty() || "전체".equals(event)) {
            event = null;
        }

        // 카테고리, 검색어, 이벤트 필터를 동시에 처리
        List<Product> products = productService.searchProducts(category, query, event);

        // 총 상품 갯수
        int totalProducts = products.size();

        // 페이징 처리
        int totalElements = products.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Product> paginatedProducts = products.subList(start, end);

        Page<Product> paginatedPage = new PageImpl<>(paginatedProducts, pageable, totalElements);

        // 모델에 데이터 추가
        model.addAttribute("products", paginatedPage);
        model.addAttribute("categories", categories); // 전체 카테고리 전달
        model.addAttribute("eventTypes", eventTypes); // 이벤트 필터 전달
        model.addAttribute("category", category); // 현재 선택된 카테고리 전달
        model.addAttribute("event", event); // 현재 선택된 이벤트 전달
        model.addAttribute("query", query);
        model.addAttribute("totalProducts", totalProducts); // 총 상품 갯수 전달

        return "/product/search";
    }

    @GetMapping("/product")
    public String getProductDetail(@RequestParam String id, Model model) {
        log.info("Fetching product with ID: {}", id);
        Product product = productService.getProductById(id);

        if (product == null) {
            log.warn("Product with ID {} not found", id);
            return "error/404";
        }

        // 서비스 계층을 통해 리뷰 조회
        List<Review> reviews = reviewService.getReviewsByProductId(id);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "product/detail";
    }

    @PostMapping("/product/review")
    public String addReview(@RequestParam String productId,
                            @RequestParam String content,
                            HttpSession session) {
        Object authInfo = session.getAttribute("authInfo");
        if (authInfo == null || !(authInfo instanceof AuthInfo)) {
            return "redirect:/login";
        }

        String loggedInUsername = ((AuthInfo) authInfo).getName();
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for ID: " + productId);
        }

        Review review = new Review();
        review.setProductId(productId);
        review.setReviewer(loggedInUsername);
        review.setContent(content);
        review.setCreatedAt(LocalDateTime.now());

        // 서비스 계층을 통해 리뷰 저장
        reviewService.saveReview(review);

        return "redirect:/product?id=" + productId;
    }


}