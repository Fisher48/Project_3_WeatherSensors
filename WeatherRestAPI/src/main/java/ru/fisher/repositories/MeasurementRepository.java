package ru.fisher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    // Подсчет количества дождливых дней (где raining = true)
    long countByRainingTrue();

}
