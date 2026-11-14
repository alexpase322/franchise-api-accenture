package com.accenture.franchise.api;

import com.accenture.franchise.api.dto.CreateProductRequest;
import com.accenture.franchise.api.dto.NameRequest;
import com.accenture.franchise.api.dto.StockRequest;
import com.accenture.franchise.model.topstock.BranchTopProduct;
import com.accenture.franchise.usecase.branch.BranchUseCase;
import com.accenture.franchise.usecase.franchise.FranchiseUseCase;
import com.accenture.franchise.usecase.product.ProductUseCase;
import com.accenture.franchise.usecase.topstock.TopStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final BranchUseCase branchUseCase;
    private final ProductUseCase productUseCase;
    private final TopStockUseCase topStockUseCase;

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(NameRequest.class)
                .flatMap(body -> franchiseUseCase.saveFranchise(body.name()))
                .flatMap(franchise -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(franchise));
    }

    public Mono<ServerResponse> renameFranchise(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("franchiseId");
        return serverRequest.bodyToMono(NameRequest.class)
                .flatMap(body->franchiseUseCase.renameFranchise(id, body.name()))
                .flatMap(franchise -> ServerResponse.status(HttpStatus.OK).bodyValue(franchise));
    }

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        return serverRequest.bodyToMono(NameRequest.class)
                .flatMap(body->branchUseCase.createBranch(franchiseId, body.name()))
                .flatMap(branch -> ServerResponse.status(HttpStatus.CREATED).bodyValue(branch));
    }

    public Mono<ServerResponse> renameBranch(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        String branchId = serverRequest.pathVariable("branchId");
        return serverRequest.bodyToMono(NameRequest.class)
                .flatMap(body->branchUseCase.renameBranch(franchiseId, branchId, body.name()))
                .flatMap(branch -> ServerResponse.status(HttpStatus.OK).bodyValue(branch));
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        String branchId = serverRequest.pathVariable("branchId");
        return serverRequest.bodyToMono(CreateProductRequest.class)
                .flatMap(body->productUseCase.createProduct(franchiseId, branchId, body.name(), body.stock()))
                .flatMap(product -> ServerResponse.status(HttpStatus.CREATED).bodyValue(product));
    }

    public Mono<ServerResponse> renameProduct(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        String branchId = serverRequest.pathVariable("branchId");
        String productId = serverRequest.pathVariable("productId");
        return serverRequest.bodyToMono(NameRequest.class)
                .flatMap(body->productUseCase.renameProduct(franchiseId, branchId, productId, body.name()))
                .flatMap(product -> ServerResponse.status(HttpStatus.OK).bodyValue(product));
    }

    public Mono<ServerResponse> updateStock(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        String branchId = serverRequest.pathVariable("branchId");
        String productId = serverRequest.pathVariable("productId");
        return serverRequest.bodyToMono(StockRequest.class)
                .flatMap(body->productUseCase.updateStock(franchiseId, branchId, productId, body.stock()))
                .flatMap(stock -> ServerResponse.status(HttpStatus.OK).bodyValue(stock));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        String branchId = serverRequest.pathVariable("branchId");
        String productId = serverRequest.pathVariable("productId");
        return productUseCase.deleteProduct(franchiseId, branchId, productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getTopProducts(ServerRequest serverRequest) {
        String franchiseId = serverRequest.pathVariable("franchiseId");
        return ServerResponse.ok()
                .body(topStockUseCase.getTopProducts(franchiseId), BranchTopProduct.class);
    }
}
