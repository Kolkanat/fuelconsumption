package com.org.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.org.FuelAppException;
import com.org.model.Fuel;
import com.org.repositories.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.org.constants.AppConstants.*;

@Service
public class FuelConsumptionService {
    @Autowired
    private FuelRepository fuelRepository;

    public Fuel saveFuel(Fuel fuel) {
        return fuelRepository.save(fuel);
    }

    public List<Fuel> saveFuelFromFile(MultipartFile file) throws IOException, FuelAppException {
        checkFile(file);
        try (MappingIterator<Fuel> readValues = objectReader.readValues(file.getInputStream())) {
            List<Fuel> fuelList = new ArrayList<>();
            int row = 1;
            while (readValues.hasNext()) {
                fuelList.add(checkFuel(readValues.nextValue(), row++));
            }
            if (fuelList.isEmpty()) {
                throw new IOException("File doesn't contain data to register!");
            }
            return fuelRepository.save(fuelList);
        }
    }

    private void checkFile(@NotNull MultipartFile file) throws FuelAppException {
        String fileName = file.getOriginalFilename();
        if (Objects.isNull(fileName)) {
            throw new FuelAppException("Can't get file name, please check file!");
        } else if (fileName.length() < 5) {
            throw new FuelAppException("Incorrect file name!");
        } else if (!FILE_EXTENSION.equals(fileName.substring(fileName.length() - 3))) {
            throw new FuelAppException(
                    String.format("Incorrect file extension, file must be in example.%s format", FILE_EXTENSION));
        } else {
            try {
                if (file.getBytes().length > 1024 * MAX_FILE_SIZE_IN_KILOBYTES) {
                    throw new FuelAppException(
                            String.format("File size bigger than %s kilobytes", MAX_FILE_SIZE_IN_KILOBYTES));
                }
            } catch (IOException e) {
                throw new FuelAppException("Can't access file");
            }
        }
    }

    private Fuel checkFuel(Fuel fuel, int row) throws FuelAppException {
        try{
            Objects.requireNonNull(fuel.getFuelType(), String.format(NULL_CHECK_MSG, row, "fuelType"));
            if (fuel.getFuelType().isEmpty())
                throw new FuelAppException(String.format(NULL_CHECK_MSG, row, "fuelType"));
            Objects.requireNonNull(fuel.getVolume(), String.format(NULL_CHECK_MSG, row, "volume"));
            Objects.requireNonNull(fuel.getPrice(), String.format(NULL_CHECK_MSG, row, "price"));
            Objects.requireNonNull(fuel.getDate(), String.format(NULL_CHECK_MSG, row, "date"));
            Objects.requireNonNull(fuel.getDriverId(), String.format(NULL_CHECK_MSG, row, "driverId"));
        } catch (NullPointerException npe) {
            throw new FuelAppException(npe.getMessage());
        }
        return fuel;
    }
}
