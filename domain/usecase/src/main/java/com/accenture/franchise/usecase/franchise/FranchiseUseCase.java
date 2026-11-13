package com.accenture.franchise.usecase.franchise;

import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.franchise.Franchise;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveFranchise(String name) {
        return Mono.fromCallable(()->Franchise.builder().id(UUID.randomUUID().toString()).name(name).build())
                .flatMap(franchiseRepository::saveFranchise);
    }

    public Mono<Franchise> renameFranchise(String franchiseId, String newName) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")))
                .flatMap(franchise1 -> franchiseRepository.saveFranchise(franchise1.toBuilder().name(newName).build()));
    }
}
