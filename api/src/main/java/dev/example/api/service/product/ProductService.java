package dev.example.api.service.product;

import dev.example.api.controller.product.dto.request.ProductCreateRequest;
import dev.example.api.service.product.response.ProductResponse;
import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductSellingStatus;
import dev.example.db.domain.product.ProductType;
import dev.example.db.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static dev.example.db.domain.product.ProductSellingStatus.*;
import static dev.example.db.domain.product.ProductType.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈
    // UUID
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();


        Product product = request.toEntity(nextProductNumber);
        Product saveProduct = productRepository.save(product);

        return ProductResponse.of(saveProduct);
    }


    // 1. 판매중 인상품 조회 && 조회 이후 바로 형변환

    public List<ProductResponse> getSellingProducts() {
        // 1. 저장소에서 조회
        List<Product> products = productRepository.findAll_Where_SellingStatus_In(forDisplay());

        // 2. 형변환
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findOne_Latest_ProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d",nextProductNumberInt);
    }


}
