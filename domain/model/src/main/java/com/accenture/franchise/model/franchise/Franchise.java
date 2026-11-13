package com.accenture.franchise.model.franchise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Franchise {
    @EqualsAndHashCode.Include
    private final String id;
    private final String name;

}
