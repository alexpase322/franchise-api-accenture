package com.accenture.franchise.model.product;

import com.accenture.franchise.model.exception.InvalidDataException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @EqualsAndHashCode.Include
    private final String id;
    private final String name;
    private final String branchId;
    private final Integer stock;

    @Builder(toBuilder = true)
    public Product(String id, String name, String branchId, Integer stock) {
        if(name == null || name.isBlank()){
            throw new InvalidDataException("Product name cannot be null or empty");
        }
        if(branchId == null || branchId.isBlank()){
            throw new InvalidDataException("The branch ID cannot be null or empty");
        }
        if(stock == null || stock < 0){
            throw new InvalidDataException("The stock cannot be null or less than 0");
        }
        this.id = id;
        this.name = name.trim();
        this.branchId = branchId;
        this.stock = stock;
    }
}
