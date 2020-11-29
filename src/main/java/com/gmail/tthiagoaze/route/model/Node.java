package com.gmail.tthiagoaze.route.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Node {

    private String name;

    @EqualsAndHashCode.Exclude
    private Integer value;

}
