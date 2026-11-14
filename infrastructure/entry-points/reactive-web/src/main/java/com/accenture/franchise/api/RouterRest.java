package com.accenture.franchise.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
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
                .build();
    }
}
