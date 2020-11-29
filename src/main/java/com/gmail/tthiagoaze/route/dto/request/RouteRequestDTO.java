package com.gmail.tthiagoaze.route.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class RouteRequestDTO {

    @NotNull(message = "departure is required")
    @Size(min = 3, max = 3, message = "departure size must be 3")
    private String departure;

    @NotNull(message = "arrival is required")
    @Size(min = 3, max = 3, message = "arrival size must be 3")
    private String arrival;

    @NotNull(message = "price is required")
    private Integer price;

}
