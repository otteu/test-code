package dev.example.api.controller.product;


import dev.example.api.common.api.Api;
import dev.example.api.controller.product.dto.request.ProductCreateRequest;
import dev.example.api.service.product.ProductService;
import dev.example.api.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public Api<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return Api.OK(productService.createProduct(request));
    }

    @GetMapping("/api/vi/products/selling")
    public Api<List<ProductResponse>> getSellingProducts() {
        List<ProductResponse> sellingProducts = productService.getSellingProducts();
        return Api.OK(sellingProducts);
    }

}
