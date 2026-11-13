package com.accenture.franchise.usecase.product;

import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.InvalidDataException;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.product.Product;
import com.accenture.franchise.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BranchRepository branchRepository;
    @InjectMocks
    private ProductUseCase productUseCase;

    private final Branch branch = Branch.builder().id("b1").name("Branch 1").franchiseId("f1").build();
    private final Product product = Product.builder().id("p1").name("Product 1").branchId("b1").stock(10).build();

    private void branchExists() {
        when(branchRepository.getBranchById("f1", "b1")).thenReturn(Mono.just(branch));
    }

    private void productExists() {
        branchExists();
        when(productRepository.getProductById("b1", "p1")).thenReturn(Mono.just(product));
    }

    private void saveReturnsArgument() {
        when(productRepository.saveProduct(any(Product.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    void shouldCreateProductInExistingBranch() {
        branchExists();
        saveReturnsArgument();

        StepVerifier.create(productUseCase.createProduct("f1", "b1", "Product 1", 5))
                .assertNext(created -> {
                    assertNotNull(created.getId());
                    assertEquals("Product 1", created.getName());
                    assertEquals("b1", created.getBranchId());
                    assertEquals(5, created.getStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldFailToCreateProductWhenBranchDoesNotExist() {
        when(branchRepository.getBranchById("f1", "b1")).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.createProduct("f1", "b1", "Product 1", 5))
                .expectError(NotFoundException.class)
                .verify();

        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void shouldUpdateStockOfExistingProduct() {
        productExists();
        saveReturnsArgument();

        StepVerifier.create(productUseCase.updateStock("f1", "b1", "p1", 50))
                .assertNext(updated -> {
                    assertEquals(50, updated.getStock());
                    assertEquals("p1", updated.getId());
                    assertEquals("Product 1", updated.getName());
                })
                .verifyComplete();
    }

    @Test
    void shouldFailToUpdateStockWhenNegative() {
        productExists();

        StepVerifier.create(productUseCase.updateStock("f1", "b1", "p1", -1))
                .expectError(InvalidDataException.class)
                .verify();

        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void shouldFailToUpdateStockWhenProductDoesNotExist() {
        branchExists();
        when(productRepository.getProductById("b1", "p1")).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateStock("f1", "b1", "p1", 50))
                .expectError(NotFoundException.class)
                .verify();

        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void shouldRenameExistingProduct() {
        productExists();
        saveReturnsArgument();

        StepVerifier.create(productUseCase.renameProduct("f1", "b1", "p1", "New name"))
                .assertNext(renamed -> {
                    assertEquals("New name", renamed.getName());
                    assertEquals("p1", renamed.getId());
                    assertEquals(10, renamed.getStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldDeleteExistingProduct() {
        productExists();
        when(productRepository.deleteProductById("b1", "p1")).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct("f1", "b1", "p1"))
                .verifyComplete();

        verify(productRepository).deleteProductById("b1", "p1");
    }

    @Test
    void shouldFailToDeleteWhenProductDoesNotExist() {
        branchExists();
        when(productRepository.getProductById("b1", "p1")).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct("f1", "b1", "p1"))
                .expectError(NotFoundException.class)
                .verify();

        verify(productRepository, never()).deleteProductById(any(), any());
    }
}
