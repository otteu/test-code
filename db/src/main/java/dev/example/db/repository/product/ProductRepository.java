package dev.example.db.repository.product;

import dev.example.db.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom{

//    @Query(value = "select p.product_number form product p order by id desc limit 1", nativeQuery = true)
//    String findLatestProductNumber();

}
