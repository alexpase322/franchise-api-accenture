package com.accenture.franchise.usecase.topstock;

import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.franchise.Franchise;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
import com.accenture.franchise.model.product.Product;
import com.accenture.franchise.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopStockUseCaseTest {
    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private TopStockUseCase topStockUseCase;

    private final Franchise franchise = Franchise.builder().id("f1").name("Franchise 1").build();
    private final Branch branch1 = Branch.builder().id("b1").name("Branch 1").franchiseId("f1").build();
    private final Branch branch2 = Branch.builder().id("b2").name("Branch 2").franchiseId("f1").build();

    private Product product(String id, String branchId, int stock) {
        return Product.builder().id(id).name("Product " + id).branchId(branchId).stock(stock).build();
    }

    private void franchiseWithBranches(Branch... branches) {
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.just(franchise));
        when(branchRepository.getAllBranchesByFranchise("f1")).thenReturn(Flux.just(branches));
    }

    @Test
    void shouldReturnProductWithHighestStockPerBranch() {
        franchiseWithBranches(branch1, branch2);
        when(productRepository.getAllProductsByBranch("b1"))
                .thenReturn(Flux.just(product("p1", "b1", 5), product("p2", "b1", 20)));
        when(productRepository.getAllProductsByBranch("b2"))
                .thenReturn(Flux.just(product("p3", "b2", 7)));

        StepVerifier.create(topStockUseCase.getTopProducts("f1"))
                .assertNext(top -> {
                    assertEquals("b1", top.getBranchId());
                    assertEquals("Branch 1", top.getBranchName());
                    assertEquals("p2", top.getProductId());
                    assertEquals(20, top.getProductStock());
                })
                .assertNext(top -> {
                    assertEquals("b2", top.getBranchId());
                    assertEquals("p3", top.getProductId());
                    assertEquals(7, top.getProductStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldSkipBranchesWithoutProducts() {
        franchiseWithBranches(branch1, branch2);
        when(productRepository.getAllProductsByBranch("b1"))
                .thenReturn(Flux.just(product("p1", "b1", 5)));
        when(productRepository.getAllProductsByBranch("b2")).thenReturn(Flux.empty());

        StepVerifier.create(topStockUseCase.getTopProducts("f1"))
                .assertNext(top -> assertEquals("b1", top.getBranchId()))
                .verifyComplete();
    }

    @Test
    void shouldKeepFirstProductWhenStockIsTied() {
        franchiseWithBranches(branch1);
        when(productRepository.getAllProductsByBranch("b1"))
                .thenReturn(Flux.just(product("p1", "b1", 10), product("p2", "b1", 10)));

        StepVerifier.create(topStockUseCase.getTopProducts("f1"))
                .assertNext(top -> assertEquals("p1", top.getProductId()))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenFranchiseHasNoBranches() {
        franchiseWithBranches();

        StepVerifier.create(topStockUseCase.getTopProducts("f1"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.empty());

        StepVerifier.create(topStockUseCase.getTopProducts("f1"))
                .expectError(NotFoundException.class)
                .verify();

        verify(branchRepository, never()).getAllBranchesByFranchise(any());
    }
}
