package com.accenture.franchise.dynamodb.adapter;

import com.accenture.franchise.dynamodb.data.FranchiseData;
import com.accenture.franchise.dynamodb.helper.TemplateAdapterOperations;
import com.accenture.franchise.model.franchise.Franchise;
import com.accenture.franchise.model.franchise.gateways.FranchiseRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class FranchiseDynamoAdapter
        extends TemplateAdapterOperations<Franchise, String, FranchiseData>
        implements FranchiseRepository {

    public FranchiseDynamoAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper,
                                  @Value("${aws.dynamodb.tables.franchises}") String tableName) {
        super(client, mapper, data -> mapper.mapBuilder(data, Franchise.FranchiseBuilder.class).build(), tableName);
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return save(franchise);
    }

    @Override
    public Mono<Franchise> getFranchiseById(String id) {
        return getById(id);
    }
}
