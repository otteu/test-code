package dev.example.db.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum ProductSellingStatus {

    SELLING("판매중"),
    HOLD("판매보류"),
    STOP_SELLING("판매중지");

    private final String text;

    // 진열 가능 한것만 구별 하기
    public static List<ProductSellingStatus> forDisplay() {
        return List.of(SELLING, HOLD);
    }

}
