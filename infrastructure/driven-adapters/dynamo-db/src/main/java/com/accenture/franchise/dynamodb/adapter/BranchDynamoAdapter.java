package com.accenture.franchise.dynamodb.adapter;

import com.accenture.franchise.dynamodb.data.BranchData;
import com.accenture.franchise.dynamodb.helper.TemplateAdapterOperations;
import com.accenture.franchise.model.branch.Branch;
import com.accenture.franchise.model.branch.gateways.BranchRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;



@Repository
public class BranchDynamoAdapter
        extends TemplateAdapterOperations<Branch, String, BranchData>
        implements BranchRepository {

    public BranchDynamoAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper, @Value(("${aws.dynamodb.tables.branches}")) String tableName) {
        super(client, mapper, branchData -> mapper.mapBuilder(branchData, Branch.BranchBuilder.class).build(), tableName);
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        return save(branch);
    }

    @Override
    public Mono<Branch> getBranchById(String franchiseId, String branchId) {
        return getByKey(franchiseId, branchId);
    }

    @Override
    public Flux<Branch> getAllBranchesByFranchise(String franchiseId) {
        return queryByPartition(franchiseId);
    }


}
