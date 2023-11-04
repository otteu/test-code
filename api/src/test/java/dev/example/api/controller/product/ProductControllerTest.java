package dev.example.api.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.example.api.controller.product.dto.request.ProductCreateRequest;
import dev.example.api.service.product.ProductService;
import dev.example.db.domain.product.ProductSellingStatus;
import dev.example.db.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

import static dev.example.db.domain.product.ProductSellingStatus.*;
import static dev.example.db.domain.product.ProductType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void creatProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .price(5000)
                .name("아메리카노")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void creatProduct_parameter_validation_without_type() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
//                .type(HANDMADE)
                .sellingStatus(SELLING)
                .price(5000)
                .name("아메리카노")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultCode == '%s')]", 400).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultMessage == '%s')]","파라미터 값 검증 실패").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultDescription== '%s')]", "상품 타입은 필수입니다.").exists())
                ;
    }

    @DisplayName("신규 상품을 등록할 때 상풐 가격은 양수여야 합니다.")
    @Test
    void creatProduct_parameter_validation_without_price() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
//                .price(5000)
                .name("아메리카노")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultCode == '%s')]", 400).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultMessage == '%s')]","파라미터 값 검증 실패").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultDescription== '%s')]", "상풐 가격은 양수여야 합니다.").exists())
        ;
    }

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        when(productService.getSellingProducts()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vi/products/selling")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultCode == '%s')]", 200).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultMessage == '%s')]","성공").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$..result[?(@.resultDescription== '%s')]", "성공").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                ;
    }
}