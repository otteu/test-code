package dev.example.db.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지 확인한다.")
    @Test
    void productType_check_true() {
        // given
        ProductType bakery = ProductType.BAKERY;

        // when
        Boolean re = ProductType.containsStockType(bakery);

        // then
        assertThat(re).isTrue();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지 확인한다.")
    @Test
    void productType_check_false() {
        // given
        ProductType handmade = ProductType.HANDMADE;

        // when
        Boolean re = ProductType.containsStockType(handmade);

        // then
        assertThat(re).isFalse();
    }

}