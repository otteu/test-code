package dev.example.db.repository.product;

import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductSellingStatus;
import dev.example.db.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static dev.example.db.domain.product.ProductSellingStatus.*;
import static dev.example.db.domain.product.ProductType.*;

import static org.assertj.core.api.Assertions.*;

//@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAll_Where_SellingStatus_In() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, 4000, "아메리카노");

        Product product2 = createProduct("002", HANDMADE, HOLD, 4500, "카페라떼");

        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, 7000, "팥빙수");
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
//        List<Product> products = productRepository.findAll_Where_SellingStatus_In(List.of(SELLING, HOLD));
        List<Product> products = productRepository.findAll_Where_SellingStatus_In(forDisplay());
        // then
        // 사이즈
        assertThat(products).hasSize(2)
                // 검증 하고자 하는 필드만 검출
                .extracting("productNumber", "name", "sellingStatus")
                // 필드 순서 상관 없이
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)
                );

    }

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAll_Where_ProductNumber_In() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, 4000, "아메리카노");

        Product product2 = createProduct("002", HANDMADE, HOLD, 4500, "카페라떼");

        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, 7000, "팥빙수");
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
//        List<Product> products = productRepository.findAll_Where_SellingStatus_In(List.of(SELLING, HOLD));
        List<Product> products = productRepository.findAll_Where_ProductNumber_In(List.of("001", "002"));
        // then
        // 사이즈
        assertThat(products).hasSize(2)
                // 검증 하고자 하는 필드만 검출
                .extracting("productNumber", "name", "sellingStatus")
                // 필드 순서 상관 없이
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)
                );

    }


    @DisplayName("가장 마지막으로 저장한 상품 번호를 가져온다.")
    @Test
    void findLatestProduct() {
        // given
        String latestNumber = "003";

        Product product1 = createProduct("001", HANDMADE, SELLING,  4000, "아메리카노");
        Product product2 = createProduct("002", HANDMADE, SELLING, 4500, "카페라떼");
        Product product3 = createProduct(latestNumber, HANDMADE, STOP_SELLING, 7000, "팥빙수");
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
//        List<Product> products = productRepository.findAll_Where_SellingStatus_In(List.of(SELLING, HOLD));
        String latestProductNumber = productRepository.findOne_Latest_ProductNumber();
        // then
        // 사이즈
        assertThat(latestProductNumber).isEqualTo(latestNumber);

    }

    @DisplayName("가장 마지막으로 저장한 상품 번호를 읽어올 떄, 상품이 하나도 없는 경우 에는 NULL을 반환한다.")
    @Test
    void findLatestProduct_empty_null() {
        // given
        // when
//        List<Product> products = productRepository.findAll_Where_SellingStatus_In(List.of(SELLING, HOLD));
        String latestProductNumber = productRepository.findOne_Latest_ProductNumber();
        // then
        // 사이즈
        assertThat(latestProductNumber).isNull();

    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus productSellingStatus,  int price, String name) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}