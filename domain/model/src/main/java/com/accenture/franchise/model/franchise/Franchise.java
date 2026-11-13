package com.accenture.franchise.model.franchise;

import com.accenture.franchise.model.exception.InvalidDataException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Franchise {
    @EqualsAndHashCode.Include
    private final String id;
    private final String name;

    @Builder(toBuilder = true)
    public Franchise(String id, String name) {
        if(name == null || name.isBlank()){
            throw new InvalidDataException("Franchise name cannot be null or empty");
        }
        this.id = id;
        this.name = name.trim();
    }
}
