package dev.example.api.service.order;

import dev.example.api.controller.order.request.OrderCreateRequest;
import dev.example.api.service.order.request.OrderCreateServiceRequest;
import dev.example.api.service.order.response.OrderResponse;
import dev.example.db.domain.order.Order;
import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductType;
import dev.example.db.domain.stock.Stock;
import dev.example.db.repository.order.OrderRepository;
import dev.example.db.repository.product.ProductRepository;
import dev.example.db.repository.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * TODO : optimistic lock / pessimistic lock
     */

    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {

        // 제품 리스트 출력 조회
        // in 절이여서 중복은 생략 된다.
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductBy(productNumbers);

        deductStockQuantity(products);

        // order 생성
        Order order = Order.create(products, registeredDateTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);
    }

    private void deductStockQuantity(List<Product> products) {
        // 재고 차감이 필요한 상품 조회
        List<String> stockProductNumbers = extractStockProductNumber(products);
        // 재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        // 상품별 counting
        Map<String, Long> productCountingMap = createStockCounting(stockProductNumbers);

        // 재고 차감
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)) {
                // TODO : 예외 처리
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            // TODO : 예외 처리 stock 내부 예외 처리 필요
            stock.deductQuantity(quantity);
        }
    }

    private static Map<String, Long> createStockCounting(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        return stockMap;
    }

    private static List<String> extractStockProductNumber(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        return stockProductNumbers;
    }

    private List<Product> findProductBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAll_Where_ProductNumber_In(productNumbers);

        Map<String, Product> productMap = products.stream().
                collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicateProducts = productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .collect(Collectors.toList());
        return duplicateProducts;
    }

}
