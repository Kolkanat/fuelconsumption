package com.org.repositories;

import com.org.dto.MonthStatisticsReport;
import com.org.dto.MonthlyReport;
import com.org.dto.SpecifiedMonthReport;
import com.org.model.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {
    List<Fuel> getAllByDriverId(Long driverId);

    @Query(value = "select new com.org.dto.MonthlyReport(concat(YEAR(f.date),'-', MONTH(f.date)),sum(f.price * f.volume)) " +
            "from Fuel f group by concat(YEAR(f.date),'-', MONTH(f.date)) order by concat(YEAR(f.date),'-', MONTH(f.date))")
    List<MonthlyReport> getTotalSpentMoneyGroupByMonth();

    @Query(value = "select new com.org.dto.MonthlyReport(concat(YEAR(f.date),'-', MONTH(f.date)),sum(f.price * f.volume)) " +
            "from Fuel f where f.driverId = ?1 " +
            "group by concat(YEAR(f.date),'-', MONTH(f.date)) order by concat(YEAR(f.date),'-', MONTH(f.date))")
    List<MonthlyReport> getTotalSpentMoneyGroupByMonthOfDriverId(Long driverId);

    @Query(value = "select new com.org.dto.SpecifiedMonthReport(f.fuelType, f.volume, f.date, f.price, " +
            "f.price * f.volume, f.driverId) from Fuel f where YEAR(f.date) = ?1 and MONTH(f.date) = ?2 order by f.date, f.driverId")
    List<SpecifiedMonthReport> getSpecifiedMonthReport(int year, int month);

    @Query(value = "select new com.org.dto.SpecifiedMonthReport(f.fuelType, f.volume, f.date, f.price, " +
            "f.price * f.volume, f.driverId) from Fuel f " +
            "where YEAR(f.date) = ?1 and MONTH(f.date) = ?2 and f.driverId = ?3 order by f.date, f.driverId")
    List<SpecifiedMonthReport> getSpecifiedMonthReportOfDriver(int year, int month, Long driverId);

    @Query(value = "select new com.org.dto.MonthStatisticsReport(f.fuelType,sum(f.volume),avg(f.price)," +
            "sum(f.price * f.volume), YEAR(f.date), MONTH(f.date)) from Fuel f " +
            "group by f.fuelType, YEAR(f.date), MONTH(f.date) order by YEAR(f.date), MONTH(f.date), f.fuelType")
    List<MonthStatisticsReport> eachMonthStatisticsGroupByFuelType();

    @Query(value = "select new com.org.dto.MonthStatisticsReport(f.fuelType,sum(f.volume),avg(f.price)," +
            "sum(f.price * f.volume), YEAR(f.date), MONTH(f.date)) from Fuel f where f.driverId = ?1 " +
            "group by f.fuelType, YEAR(f.date), MONTH(f.date) order by YEAR(f.date), MONTH(f.date), f.fuelType")
    List<MonthStatisticsReport> eachMonthStatisticsGroupByFuelTypeOfDriver(Long driverId);
}
