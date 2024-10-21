package ru.fisher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.models.Sensor;


import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    // Поиск сенсора по названию
    Optional<Sensor> findByName(String name);

}
