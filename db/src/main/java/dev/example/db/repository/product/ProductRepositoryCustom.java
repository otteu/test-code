package dev.example.db.repository.product;

import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductSellingStatus;

import java.util.List;

public interface ProductRepositoryCustom {

    // 판매 가능한 상품 조회
    List<Product> findAll_Where_SellingStatus_In(List<ProductSellingStatus> sellingTypes);

    // 상품번호로 상품 조회
    List<Product> findAll_Where_ProductNumber_In(List<String> productNumbers);

    String findOne_Latest_ProductNumber();

}
