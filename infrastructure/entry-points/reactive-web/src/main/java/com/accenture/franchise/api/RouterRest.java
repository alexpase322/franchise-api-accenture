package com.accenture.franchise.api;

import com.accenture.franchise.model.exception.InvalidDataException;
import com.accenture.franchise.model.exception.NotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import com.accenture.franchise.api.dto.ErrorResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .POST("/api/franchises", handler::createFranchise)
                .PATCH("/api/franchises/{franchiseId}", handler::renameFranchise)
                .POST("/api/franchises/{franchiseId}/branches", handler::createBranch)
                .PATCH("/api/franchises/{franchiseId}/branches/{branchId}", handler::renameBranch)
                .POST("/api/franchises/{franchiseId}/branches/{branchId}/products", handler::createProduct)
                .PATCH("/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::renameProduct)
                .PATCH("/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock", handler::updateStock)
                .DELETE("/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::deleteProduct)
                .GET("/api/franchises/{franchiseId}/top-stock-products", handler::getTopProducts)
                .onError(NotFoundException.class, (error, request) -> errorResponse(HttpStatus.NOT_FOUND, error))
                .onError(InvalidDataException.class, (error, request) -> errorResponse(HttpStatus.BAD_REQUEST, error))
                .build();
    }

    private Mono<ServerResponse> errorResponse(HttpStatus status, Throwable error) {
        return ServerResponse.status(status)
                .bodyValue(new ErrorResponse(status.value(), status.getReasonPhrase(), error.getMessage()));
    }
}
