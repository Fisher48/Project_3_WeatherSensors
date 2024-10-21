package ru.fisher.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.fisher.dto.MeasurementDTO;
import ru.fisher.models.Measurement;
import ru.fisher.services.MeasurementService;
import ru.fisher.utils.ErrorResponse;
import ru.fisher.utils.MeasurementException;
import ru.fisher.utils.MeasurementValidator;
import ru.fisher.utils.SensorNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/rainyDaysCount")
    public List<LocalDateTime> getRainyDays() {
        return measurementService.rainyDays();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> putValues(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                BindingResult bindingResult) {
        Measurement measurementToAdd = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurementToAdd, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotFoundException(errorMsg.toString());
        }
        measurementService.saveValues(measurementToAdd);

        // отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private MeasurementDTO convertToDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotFoundException e){
        ErrorResponse response = new ErrorResponse(
                "Sensor with this id not found",
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementException e){
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус
    }

}
