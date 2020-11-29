package com.gmail.tthiagoaze.route.model;

import com.gmail.tthiagoaze.route.util.Constants;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    private String departure;
    private String arrival;
    private Integer price;

    @Setter(value = AccessLevel.NONE)
    private Node nodeFrom;

    @Setter(value = AccessLevel.NONE)
    private Node noteTo;

    public Node getNodeFrom() {
        return Node.builder().name(departure).value(Constants.ZERO).build();
    }

    public Node getNoteTo() {
        return Node.builder().name(arrival).value(price).build();
    }

    public String getRouteToFile() {
        return String.join(Constants.COMMA_DELIMITER, departure, arrival, price.toString());
    }
}
