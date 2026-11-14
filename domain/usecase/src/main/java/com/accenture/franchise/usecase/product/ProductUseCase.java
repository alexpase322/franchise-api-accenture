package com.accenture.franchise.usecase.product;

import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.product.Product;
import com.accenture.franchise.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductUseCase {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Product> createProduct(String franchiseId, String branchId, String name, Integer stock) {
        return findBranch(franchiseId, branchId)
                .map(branch -> Product.builder().id(UUID.randomUUID().toString()).name(name).branchId(branchId).stock(stock).build())
                .flatMap(productRepository::saveProduct);
    }

    public Mono<Product> updateStock(String franchiseId, String branchId, String productId, Integer newStock) {
        return findProductById(franchiseId, branchId, productId)
                .map(product -> product.toBuilder().stock(newStock).build())
                .flatMap(productRepository::saveProduct);
    }

    public Mono<Product> renameProduct(String franchiseId, String branchId, String productId, String newName) {
        return findProductById(franchiseId, branchId, productId)
                .map(product -> product.toBuilder().name(newName).build())
                .flatMap(productRepository::saveProduct);
    }

    public Mono<Void> deleteProduct(String franchiseId, String branchId, String productId) {
        return findProductById(franchiseId, branchId, productId)
                .flatMap(product -> productRepository.deleteProductById(branchId, productId));
    }

    private Mono<Product> findProductById(String franchiseId, String branchId, String productId) {
        return findBranch(franchiseId, branchId)
                .flatMap(branch->productRepository.getProductById(branchId, productId))
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found" )));
    }

    private Mono<Branch> findBranch(String franchiseId, String branchId) {
        return branchRepository.getBranchById(franchiseId, branchId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")));
    }
}
