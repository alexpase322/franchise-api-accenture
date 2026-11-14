package com.accenture.franchise.dynamodb.data;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class FranchiseData {
    private String id;
    private String name;

    public FranchiseData() {
    }

    public FranchiseData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
