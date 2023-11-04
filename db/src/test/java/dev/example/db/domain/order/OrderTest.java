package dev.example.db.domain.order;

import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductType;
import dev.example.db.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static dev.example.db.domain.order.OrderStatus.*;
import static dev.example.db.domain.product.ProductSellingStatus.SELLING;
import static dev.example.db.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculate_TotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", HANDMADE, 1000),
                createProduct("002", HANDMADE, 3000),
                createProduct("003", HANDMADE, 5000)
        );
        int totalPrice = products.stream().mapToInt(Product::getPrice).sum();
        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);

    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void orderStatus_INIT() {
        // given
        List<Product> products = List.of(
                createProduct("001", HANDMADE, 1000),
                createProduct("002", HANDMADE, 3000),
                createProduct("003", HANDMADE, 5000)
        );
        int totalPrice = products.stream().mapToInt(Product::getPrice).sum();
        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(INIT);

    }

    @DisplayName("주문 생성 시 주문 시간을 넣어준다.")
    @Test
    void order_time_input() {
        // given
        LocalDateTime registeredDateTime =  LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", HANDMADE, 1000),
                createProduct("002", HANDMADE, 3000),
                createProduct("003", HANDMADE, 5000)
        );

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);

    }

    private Product createProduct(String productNumber, ProductType type, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}