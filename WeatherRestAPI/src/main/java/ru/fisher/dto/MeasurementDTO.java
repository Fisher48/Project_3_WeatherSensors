package ru.fisher.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementDTO {

    @NotNull
    private SensorDTO sensorName;

    @NotNull
    @Min(value = -100, message = "value should be between -100 and 100 degrees")
    @Max(value = 100, message = "value should be between -100 and 100 degrees")
    private double value;

    @NotNull
    private boolean raining;

}
