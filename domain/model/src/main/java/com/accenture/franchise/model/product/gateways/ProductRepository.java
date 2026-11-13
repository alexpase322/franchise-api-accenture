package com.accenture.franchise.model.product.gateways;

import com.accenture.franchise.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductRepository {
    Mono<Product> saveProduct(Product product);

    Mono<Void> deleteProductById(String branchId, String productId);

    Mono<Product> getProductById(String branchId, String productId);

    Flux<Product> getAllProductsByBranch(String branchId);
}
