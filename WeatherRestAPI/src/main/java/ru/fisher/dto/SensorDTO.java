package ru.fisher.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDTO {

    @NotEmpty
    @Size(min = 3, max = 30, message = "Sensor name should be between 3 and 30 characters")
    private String name;

}
