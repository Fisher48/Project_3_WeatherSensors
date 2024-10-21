package ru.fisher.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "measurement")
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(value = -100, message = "value should be between -100 and 100 degrees")
    @Max(value = 100, message = "value should be between -100 and 100 degrees")
    @Column(name = "value")
    private Double value;

    @NotNull
    @Column(name = "raining")
    private Boolean raining;

    @NotNull
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "sensor", referencedColumnName = "name")
    private Sensor sensor;

}
