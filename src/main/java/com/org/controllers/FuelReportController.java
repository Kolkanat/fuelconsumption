package com.org.controllers;

import com.org.dto.MonthStatisticsReport;
import com.org.dto.MonthlyReport;
import com.org.dto.SpecifiedMonthReport;
import com.org.services.FuelReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class FuelReportController {

    @Autowired
    private FuelReportService fuelReportService;

    @GetMapping("/money/spent/bymonth")
    public List<MonthlyReport> getTotalMoneyGroupByMonth() {
        return fuelReportService.getTotalSpentMoneyGroupByMonth();
    }

    @GetMapping("/money/spent/bymonth/driver/{driverId}")
    public List<MonthlyReport> getTotalSpentMoneyGroupByMonthOfDriverId(@PathVariable("driverId") Long driverId) {
        return fuelReportService.getTotalSpentMoneyGroupByMonthOfDriverId(driverId);
    }

    @GetMapping("/consumption/year/{year}/month/{month}")
    public List<SpecifiedMonthReport> getSpecifiedMonthReport(@PathVariable("year") int year,
                                                              @PathVariable("month") int month) {
        return fuelReportService.getSpecifiedMonthReport(year, month);
    }

    @GetMapping("/consumption/year/{year}/month/{month}/driver/{driverId}")
    public List<SpecifiedMonthReport> getSpecifiedMonthReportOfDriver(@PathVariable("year") int year,
                                                                      @PathVariable("month") int month,
                                                                      @PathVariable("driverId") Long driverId) {
        return fuelReportService.getSpecifiedMonthReportOfDriver(year, month, driverId);
    }

    @GetMapping("/each/month/statistics")
    public Map<String, List<MonthStatisticsReport>> eachMonthStatisticsGroupByFuelType() {
        return fuelReportService.eachMonthStatisticsGroupByFuelType();
    }

    @GetMapping("/each/month/statistics/driver/{driverId}")
    public Map<String, List<MonthStatisticsReport>> eachMonthStatisticsGroupByFuelTypeOfDriver(
            @PathVariable("driverId") Long driverId) {
        return fuelReportService.eachMonthStatisticsGroupByFuelTypeOfDriver(driverId);
    }
}

