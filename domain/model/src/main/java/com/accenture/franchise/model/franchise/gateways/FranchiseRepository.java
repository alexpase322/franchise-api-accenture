package com.accenture.franchise.model.franchise.gateways;

import com.accenture.franchise.model.franchise.Franchise;
import reactor.core.publisher.Mono;


public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);

    Mono<Franchise> getFranchiseById(String id);
}
