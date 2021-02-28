
package com.assignment.spring.api.model;

import lombok.Data;

@Data
public class Main {

    private Double temp;
    private Integer pressure;
    private Integer humidity;
    private Double tempMin;
    private Double tempMax;
}
