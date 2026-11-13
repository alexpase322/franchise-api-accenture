package com.accenture.franchise.model.branch;


import com.accenture.franchise.model.exception.InvalidDataException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Branch {
    @EqualsAndHashCode.Include
    private final String id;
    private final String name;
    private final String franchiseId;

    @Builder(toBuilder = true)
    public Branch(String id, String name, String franchiseId) {
        if(name == null || name.isBlank()){
            throw new InvalidDataException("The name cannot be null or empty");
        }
        if(franchiseId == null || franchiseId.isBlank()){
            throw new InvalidDataException("The franchise ID cannot be null or empty");
        }
        this.id = id;
        this.name = name.trim();
        this.franchiseId = franchiseId;
    }
}
