package dev.example.db.domain.product;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리");

    private final String text;

    public static Boolean containsStockType(ProductType type) {
        return List.of(BOTTLE, BAKERY).contains(type);
    }

}