package com.accenture.franchise.usecase.topstock;

import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
import com.accenture.franchise.model.product.gateways.ProductRepository;
import com.accenture.franchise.model.topstock.BranchTopProduct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class TopStockUseCase {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Flux<BranchTopProduct> getTopProducts(String franchiseId) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")))
                .flatMapMany(franchise -> branchRepository.getAllBranchesByFranchise(franchise.getId()))
                .flatMapSequential(branch -> productRepository.getAllProductsByBranch(branch.getId())
                        .reduce((a, b) -> a.getStock() >= b.getStock() ? a : b)
                        .map(product -> BranchTopProduct.builder()
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .productId(product.getId())
                                .productName(product.getName())
                                .productStock(product.getStock())
                                .build()));
    }
}
