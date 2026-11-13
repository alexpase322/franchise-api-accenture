package com.accenture.franchise.model.branch;

import com.accenture.franchise.model.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BranchTest {
    private Branch.BranchBuilder validBranch(){
        return Branch.builder().id("b1").franchiseId("f1").name("Branch 1");
    }

    @Test
    void shouldCreateBranch(){
        Branch branch = validBranch().build();
        assertEquals("Branch 1", branch.getName());
        assertEquals("f1", branch.getFranchiseId());
    }
    @Test
    void shouldEliminateBlankSpacesWithTrimInName(){
        Branch branch = validBranch().name("    Branch 1   ").build();
        assertEquals("Branch 1", branch.getName());
    }

    @Test
    void shouldThrowsExceptionWithNullName(){
        assertThrows(InvalidDataException.class, () -> validBranch().name(null).build());
    }
    @Test
    void shouldThrowsExceptionWithBlankName(){
        assertThrows(InvalidDataException.class, ()-> validBranch().name("  ").build());
    }

    @Test
    void shouldThrowsExceptionWithNullFranchiseId(){
        assertThrows(InvalidDataException.class, () -> validBranch().franchiseId(null).build());
    }

    @Test
    void shouldThrowsExceptionWithBlankFranchiseId(){
        assertThrows(InvalidDataException.class, () -> validBranch().franchiseId("  ").build());
    }
}
