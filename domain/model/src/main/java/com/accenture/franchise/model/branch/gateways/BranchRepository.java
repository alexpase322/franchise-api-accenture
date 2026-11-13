package com.accenture.franchise.model.branch.gateways;

import com.accenture.franchise.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BranchRepository {
    Mono<Branch> saveBranch(Branch branch);

    Mono<Branch> getBranchById(String franchiseId, String branchId);

    Flux<Branch> getAllBranchesByFranchise(String franchiseId);
}
