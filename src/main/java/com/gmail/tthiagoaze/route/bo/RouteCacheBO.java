package com.gmail.tthiagoaze.route.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteCacheBO {

    private String departure;
    private String arrival;

}
