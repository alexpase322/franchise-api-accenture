package com.accenture.franchise.model.branch;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Branch {
    @EqualsAndHashCode.Include
    private final String id;
    private final String name;
    private final String franchiseId;

}
