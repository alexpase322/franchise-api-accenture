package com.accenture.franchise.usecase.branch;

import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import com.accenture.franchise.model.exception.NotFoundException;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class BranchUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Branch> createBranch(String franchiseId, String name){
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found")))
                .flatMap(franchise -> branchRepository.saveBranch(Branch.builder().id(UUID.randomUUID().toString()).name(name).franchiseId(franchise.getId()).build()));
    }

    public Mono<Branch> renameBranch(String branchId, String franchiseId, String newName){
        return branchRepository.getBranchById(franchiseId, branchId)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found")))
                .flatMap(branch -> branchRepository.saveBranch(Branch.builder().id(branch.getId()).franchiseId(branch.getFranchiseId()).name(newName).build()));
    }
}
