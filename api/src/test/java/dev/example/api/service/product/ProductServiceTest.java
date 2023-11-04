package dev.example.api.service.product;

import dev.example.api.controller.product.dto.request.ProductCreateRequest;
import dev.example.api.service.product.response.ProductResponse;
import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductSellingStatus;
import dev.example.db.domain.product.ProductType;
import dev.example.db.repository.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static dev.example.db.domain.product.ProductSellingStatus.*;
import static dev.example.db.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호 1을 중가한다.")
    @Test
    void test() {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, 7000, "팥빙수");
        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();
        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("002", HANDMADE, SELLING, 5000, "카푸치노");

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "price","name")
                .containsExactlyInAnyOrder(
                    tuple("001", HANDMADE, SELLING, 7000, "팥빙수"),
                    tuple("002", HANDMADE, SELLING,  5000,"카푸치노")
                );

    }

    @DisplayName("신규 상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    @Test
    void createProduct_Empry_001() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();
        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("001", HANDMADE, SELLING, 5000, "카푸치노");

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains(
                        tuple("001", HANDMADE, SELLING, 5000, "카푸치노")
                );
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus productSellingStatus, int price, String name) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}