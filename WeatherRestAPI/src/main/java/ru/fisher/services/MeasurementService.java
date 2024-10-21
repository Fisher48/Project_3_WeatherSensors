package ru.fisher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.models.Measurement;
import ru.fisher.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }


    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void saveValues(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
        // мы должны сами найти сенсор из БД по имени и вставить объект из Hibernate persistence context'а
        measurement.setSensor(sensorService.findByName(measurement.getSensor().getName()).get());
        measurement.setRegisterDate(LocalDateTime.now());
    }

    // вернуть список из дат дождливых дней
    public List<LocalDateTime> rainyDays() {
       return measurementRepository.findAll().stream()
               .filter(Measurement::getRaining)
               .map(Measurement::getRegisterDate)
               .toList();
    }
}
