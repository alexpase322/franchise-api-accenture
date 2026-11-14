package com.accenture.franchise.api.config;

import com.accenture.franchise.api.Handler;
import com.accenture.franchise.api.RouterRest;
import com.accenture.franchise.usecase.branch.BranchUseCase;
import com.accenture.franchise.usecase.franchise.FranchiseUseCase;
import com.accenture.franchise.usecase.product.ProductUseCase;
import com.accenture.franchise.usecase.topstock.TopStockUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseUseCase franchiseUseCase;
    @MockitoBean
    private BranchUseCase branchUseCase;
    @MockitoBean
    private ProductUseCase productUseCase;
    @MockitoBean
    private TopStockUseCase topStockUseCase;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        when(topStockUseCase.getTopProducts("f1")).thenReturn(Flux.empty());
        webTestClient.get()
                .uri("/api/franchises/f1/top-stock-products")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().doesNotExist("Server")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}