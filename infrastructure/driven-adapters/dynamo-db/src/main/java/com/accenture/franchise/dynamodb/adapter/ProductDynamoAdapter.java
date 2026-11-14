package com.accenture.franchise.dynamodb.adapter;

import com.accenture.franchise.dynamodb.data.ProductData;
import com.accenture.franchise.dynamodb.helper.TemplateAdapterOperations;
import com.accenture.franchise.model.product.Product;
import com.accenture.franchise.model.product.gateways.ProductRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class ProductDynamoAdapter
    extends TemplateAdapterOperations<Product, String, ProductData>
    implements ProductRepository {

    public ProductDynamoAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper, @Value(("${aws.dynamodb.tables.products}")) String tableName) {
        super(client, mapper, productData -> mapper.mapBuilder(productData, Product.ProductBuilder.class).build(), tableName);
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return save(product);
    }

    @Override
    public Mono<Void> deleteProductById(String branchId, String productId) {
        return deleteByKey(branchId, productId);
    }

    @Override
    public Mono<Product> getProductById(String branchId, String productId) {
        return getByKey(branchId, productId);
    }

    @Override
    public Flux<Product> getAllProductsByBranch(String branchId) {
        return queryByPartition(branchId);
    }

}
