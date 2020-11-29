package com.gmail.tthiagoaze.route.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteResponseDTO {

    private String description;
    private Integer price;

}
