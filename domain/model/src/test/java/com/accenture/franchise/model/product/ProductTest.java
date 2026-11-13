package com.accenture.franchise.model.product;

import com.accenture.franchise.model.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {
    private Product.ProductBuilder validProduct(){
        return Product.builder().id("p1").branchId("b1").name("Product 1").stock(20);
    }

    @Test
    void shouldCreateProduct(){
        Product product = validProduct().build();
        assertEquals("b1", product.getBranchId());
        assertEquals("Product 1", product.getName());
        assertEquals(20, product.getStock());
    }

    @Test
    void shouldEliminateBlankSpacesWithTrimInName(){
        Product product = validProduct().name("    Product 1    ").build();
        assertEquals("Product 1", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNameIsNull(){
        assertThrows(InvalidDataException.class, ()-> validProduct().name(null).build());
    }

    @Test
    void shouldThrowExceptionWhenProductNameIsBlank(){
        assertThrows(InvalidDataException.class, ()-> validProduct().name("   ").build());
    }

    @Test
    void shouldThrowExceptionWhenBranchIdIsNull(){
        assertThrows(InvalidDataException.class, () -> validProduct().branchId(null).build());
    }

    @Test
    void shouldThrowExceptionWhenBranchIdIsBlank(){
        assertThrows(InvalidDataException.class, () -> validProduct().branchId("   ").build());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNull(){
        assertThrows(InvalidDataException.class, () -> validProduct().stock(null).build());
    }

    @Test
    void shouldThrowExceptionWhenStockIsLessThanZero(){
        assertThrows(InvalidDataException.class, ()-> validProduct().stock(-1).build());
    }
    @Test
    void shouldCreateWhenStockIsZero(){
        Product product = validProduct().stock(0).build();
        assertEquals(0, product.getStock());
    }

}
