package com.accenture.franchise.model.franchise;

import com.accenture.franchise.model.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FranchiseTest {
    private Franchise.FranchiseBuilder validFranchise(){
        return Franchise.builder().name("Franchise 1");
    }


    @Test
    void shouldCreateFranchise(){
        Franchise franchise = validFranchise().build();
        assertEquals("Franchise 1", franchise.getName());
    }

    @Test
    void shouldEliminateBlankSpacesWithTrimInName(){
        Franchise franchise = validFranchise().name("    Franchise 1    ").build();
        assertEquals("Franchise 1", franchise.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull(){
        assertThrows(InvalidDataException.class, () -> validFranchise().name(null).build());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank(){
        assertThrows(InvalidDataException.class, () -> validFranchise().name("    ").build());
    }
}
