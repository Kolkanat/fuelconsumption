package com.org.services;

import com.org.dto.MonthStatisticsReport;
import com.org.dto.MonthlyReport;
import com.org.dto.SpecifiedMonthReport;
import com.org.model.Fuel;
import com.org.repositories.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class FuelReportService {
    @Autowired
    private FuelRepository fuelRepository;

    public List<Fuel> getFuelByDriverId(Long driverId) {
        return fuelRepository.getAllByDriverId(driverId);
    }

    public List<MonthlyReport> getTotalSpentMoneyGroupByMonth() {
        return fuelRepository.getTotalSpentMoneyGroupByMonth();
    }

    public List<MonthlyReport> getTotalSpentMoneyGroupByMonthOfDriverId(Long driverId) {
        return fuelRepository.getTotalSpentMoneyGroupByMonthOfDriverId(driverId);
    }

    public List<SpecifiedMonthReport> getSpecifiedMonthReport(int year, int month) {
        return fuelRepository.getSpecifiedMonthReport(year, month);
    }

    public List<SpecifiedMonthReport> getSpecifiedMonthReportOfDriver(int year, int month, Long driverId) {
        return fuelRepository.getSpecifiedMonthReportOfDriver(year, month, driverId);
    }

    public Map<String, List<MonthStatisticsReport>> eachMonthStatisticsGroupByFuelType() {
        List<MonthStatisticsReport> report = fuelRepository.eachMonthStatisticsGroupByFuelType();
        Map<String, List<MonthStatisticsReport>> result = report.stream().collect(
                groupingBy(r -> String.format("%s-%s", r.getYear(), r.getMonth()), LinkedHashMap::new, toList()));
        return result;
    }

    public Map<String, List<MonthStatisticsReport>> eachMonthStatisticsGroupByFuelTypeOfDriver(Long driverId) {
        List<MonthStatisticsReport> report = fuelRepository.eachMonthStatisticsGroupByFuelTypeOfDriver(driverId);
        Map<String, List<MonthStatisticsReport>> result = report.stream().collect(
                groupingBy(r -> String.format("%s-%s", r.getYear(), r.getMonth()), LinkedHashMap::new, toList()));
        return result;
    }
}
