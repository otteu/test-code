package dev.example.db.repository.product;


import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.example.db.domain.product.Product;
import dev.example.db.domain.product.ProductSellingStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static dev.example.db.domain.product.QProduct.*;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory query;

    public ProductRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> findAll_Where_SellingStatus_In(List<ProductSellingStatus> sellingStatuses) {
        return query.selectFrom(product)
                .where(product.sellingStatus.in(sellingStatuses))
                .fetch()
                ;
    }

    @Override
    public List<Product> findAll_Where_ProductNumber_In(List<String> productNumbers) {
        return query.selectFrom(product)
                .where(product.productNumber.in(productNumbers))
                .fetch()
                ;
    }

    @Override
    public String findOne_Latest_ProductNumber() {
        return query.select(product.productNumber)
                .from(product)
                .orderBy(product.productNumber.desc())
                .limit(1)
                .fetchOne()
                ;
    }
}
