package com.accenture.franchise.usecase.franchise;


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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FranchiseUseCaseTest {
    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    @Test
    void shouldCreateFranchiseWithGeneratedId() {
        when(franchiseRepository.saveFranchise(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(franchiseUseCase.saveFranchise("Franchise 1"))
                .assertNext(franchise -> {
                    assertEquals("Franchise 1", franchise.getName());
                })
                .verifyComplete();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        StepVerifier.create(franchiseUseCase.saveFranchise("   "))
                .expectError(InvalidDataException.class)
                .verify();

        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        StepVerifier.create(franchiseUseCase.saveFranchise(null))
                .expectError(InvalidDataException.class)
                .verify();
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void shouldRenameExistingFranchise() {
        Franchise existing = Franchise.builder().id("f1").name("old name").build();
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.just(existing));
        when(franchiseRepository.saveFranchise(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(franchiseUseCase.renameFranchise("f1", "new name"))
                .assertNext(franchise -> {
                    assertEquals("new name", franchise.getName());
                    assertEquals("f1", franchise.getId());
                })
                .verifyComplete();
    }

    @Test
    void shouldNotRenameFranchiseNotExisting(){
        when(franchiseRepository.getFranchiseById("f1")).thenReturn(Mono.empty());
        StepVerifier.create(franchiseUseCase.renameFranchise("f1", "new name"))
                .expectError(NotFoundException.class)
                .verify();

        verify(franchiseRepository, never()).saveFranchise(any());
    }
}
