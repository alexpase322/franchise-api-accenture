package com.accenture.franchise.usecase.branch;

import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.InvalidDataException;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.franchise.Franchise;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
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
class BranchUseCaseTest {
    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private BranchRepository branchRepository;
    @InjectMocks
    private BranchUseCase branchUseCase;

    private final Franchise franchise = Franchise.builder().id("f1").name("Franchise 1").build();
    private final Branch branch = Branch.builder().id("b1").name("Branch 1").franchiseId("f1").build();

    private void saveReturnsArgument() {
        when(branchRepository.saveBranch(any(Branch.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    void shouldCreateBranchInExistingFranchise() {
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.just(franchise));
        saveReturnsArgument();

        StepVerifier.create(branchUseCase.createBranch("f1", "Branch 1"))
                .assertNext(created -> {
                    assertNotNull(created.getId());
                    assertEquals("Branch 1", created.getName());
                    assertEquals("f1", created.getFranchiseId());
                })
                .verifyComplete();
    }

    @Test
    void shouldFailToCreateBranchWhenFranchiseDoesNotExist() {
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.createBranch("f1", "Branch 1"))
                .expectError(NotFoundException.class)
                .verify();

        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void shouldFailToCreateBranchWithBlankName() {
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(branchUseCase.createBranch("f1", "   "))
                .expectError(InvalidDataException.class)
                .verify();

        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void shouldRenameExistingBranch() {
        when(branchRepository.getBranchById("f1", "b1")).thenReturn(Mono.just(branch));
        saveReturnsArgument();

        StepVerifier.create(branchUseCase.renameBranch("f1", "b1", "New name"))
                .assertNext(renamed -> {
                    assertEquals("New name", renamed.getName());
                    assertEquals("b1", renamed.getId());
                    assertEquals("f1", renamed.getFranchiseId());
                })
                .verifyComplete();
    }

    @Test
    void shouldFailToRenameWhenBranchDoesNotExist() {
        when(branchRepository.getBranchById("f1", "b1")).thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.renameBranch("f1", "b1", "New name"))
                .expectError(NotFoundException.class)
                .verify();

        verify(branchRepository, never()).saveBranch(any());
    }
}
