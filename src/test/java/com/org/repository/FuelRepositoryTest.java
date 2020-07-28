package com.org.repository;

import com.org.dto.MonthStatisticsReport;
import com.org.dto.MonthlyReport;
import com.org.dto.SpecifiedMonthReport;
import com.org.model.Fuel;
import com.org.repositories.FuelRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.org.utils.TestConstants.*;
import static com.org.utils.TestUtils.addOneRecord;
import static com.org.utils.TestUtils.addSeveralRecords;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FuelRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private FuelRepository fuelRepository;

    @Test
    public void saveFuelTest() {
        Fuel fuel = addOneRecord(testEntityManager);
        Fuel found = fuelRepository.findOne(fuel.getId());

        Assert.assertEquals(found.getFuelType(), fuel.getFuelType());
        Assert.assertTrue(found.getPrice().compareTo(fuel.getPrice()) == 0);
        Assert.assertTrue(found.getVolume().compareTo(fuel.getVolume()) == 0);
        Assert.assertEquals(found.getDate(), fuel.getDate());
        Assert.assertEquals(found.getDriverId(), fuel.getDriverId());

    }

    @Test
    public void saveFuelListTest() {
        List<Fuel> fuelList = Arrays.asList(f1_d_2018_02_12th, f2_98_2018_02_27th, f3_98_2018_02_9th);
        fuelRepository.save(fuelList);
        fuelRepository.flush();

        for (Fuel f : fuelList) {
            Fuel fromDb = testEntityManager.find(Fuel.class, f.getId());
            Assert.assertTrue(f.equals(fromDb));
        }
    }

    @Test
    public void getTotalSpentMoneyGroupByMonthTest() {
        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<MonthlyReport> result = fuelRepository.getTotalSpentMoneyGroupByMonth();
        BigDecimal sum = fuelList
                .stream()
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.assertEquals(sum.compareTo(result.get(0).getTotalAmount()), 0);
        Assert.assertEquals("2018-2", result.get(0).getMonth());
    }

    @Test
    public void getTotalSpentMoneyGroupByMonthOfDriverTest() {
        Long driverId = 120L;
        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<MonthlyReport> result = fuelRepository.getTotalSpentMoneyGroupByMonthOfDriverId(driverId);
        BigDecimal sum = fuelList
                .stream()
                .filter(f -> f.getDriverId().equals(120L))
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.assertEquals(sum.compareTo(result.get(0).getTotalAmount()), 0);
        Assert.assertEquals("2018-2", result.get(0).getMonth());
    }

    @Test
    public void getSpecifiedMonthReportTest() {
        int year = 2018;
        int month = 2;

        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<SpecifiedMonthReport> result = fuelRepository.getSpecifiedMonthReport(year, month);

        Assert.assertEquals(result.get(0).getFuelType(), f3_98_2018_02_9th.getFuelType());
        Assert.assertEquals(result.get(0).getDate(), f3_98_2018_02_9th.getDate());
        Assert.assertEquals(result.get(0).getDriverId(), f3_98_2018_02_9th.getDriverId());
        Assert.assertEquals(result.get(0).getVolume().compareTo(f3_98_2018_02_9th.getVolume()), 0);
        Assert.assertEquals(result.get(0).getPrice().compareTo(f3_98_2018_02_9th.getPrice()), 0);
        Assert.assertEquals(result.get(0).getTotalPrice()
                .compareTo(f3_98_2018_02_9th.getPrice().multiply(f3_98_2018_02_9th.getVolume())), 0);


        Assert.assertEquals(result.get(1).getFuelType(), f1_d_2018_02_12th.getFuelType());
        Assert.assertEquals(result.get(1).getDate(), f1_d_2018_02_12th.getDate());
        Assert.assertEquals(result.get(1).getDriverId(), f1_d_2018_02_12th.getDriverId());
        Assert.assertEquals(result.get(1).getVolume().compareTo(f1_d_2018_02_12th.getVolume()), 0);
        Assert.assertEquals(result.get(1).getPrice().compareTo(f1_d_2018_02_12th.getPrice()), 0);
        Assert.assertEquals(result.get(1).getTotalPrice()
                .compareTo(f1_d_2018_02_12th.getPrice().multiply(f1_d_2018_02_12th.getVolume())), 0);

        Assert.assertEquals(result.get(2).getFuelType(), f2_98_2018_02_27th.getFuelType());
        Assert.assertEquals(result.get(2).getDate(), f2_98_2018_02_27th.getDate());
        Assert.assertEquals(result.get(2).getDriverId(), f2_98_2018_02_27th.getDriverId());
        Assert.assertEquals(result.get(2).getVolume().compareTo(f2_98_2018_02_27th.getVolume()), 0);
        Assert.assertEquals(result.get(2).getPrice().compareTo(f2_98_2018_02_27th.getPrice()), 0);
        Assert.assertEquals(result.get(2).getTotalPrice()
                .compareTo(f2_98_2018_02_27th.getPrice().multiply(f2_98_2018_02_27th.getVolume())), 0);

    }

    @Test
    public void getSpecifiedMonthReportOfDriverTest() {
        int year = 2018;
        int month = 2;
        Long driverId = 130L;

        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<SpecifiedMonthReport> result = fuelRepository.getSpecifiedMonthReportOfDriver(year, month, driverId);

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getFuelType(), f3_98_2018_02_9th.getFuelType());
        Assert.assertEquals(result.get(0).getDate(), f3_98_2018_02_9th.getDate());
        Assert.assertEquals(result.get(0).getDriverId(), f3_98_2018_02_9th.getDriverId());
        Assert.assertEquals(result.get(0).getVolume().compareTo(f3_98_2018_02_9th.getVolume()), 0);
        Assert.assertEquals(result.get(0).getPrice().compareTo(f3_98_2018_02_9th.getPrice()), 0);
        Assert.assertEquals(result.get(0).getTotalPrice()
                .compareTo(f3_98_2018_02_9th.getPrice().multiply(f3_98_2018_02_9th.getVolume())), 0);
    }

    @Test
    public void eachMonthStatisticsGroupByFuelTypeTest() {
        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<MonthStatisticsReport> result = fuelRepository.eachMonthStatisticsGroupByFuelType();
        //total price calculation for fuel type D
        BigDecimal totalPriceFor_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total price calculation for fuel type 98
        BigDecimal totalPriceFor_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total volume calculation for fuel type D
        BigDecimal volume_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .map(Fuel::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total volume calculation for fuel type
        BigDecimal volume_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .map(Fuel::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //avg price calculation for fuel type D
        Double avg_price_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .mapToDouble(f -> f.getPrice().doubleValue())
                .average().orElse(0.0);
        //avg price calculation for fuel type 98
        Double avg_price_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .mapToDouble(f -> f.getPrice().doubleValue())
                .average().orElse(0.0);

        Assert.assertEquals(result.get(0).getFuelType(), "98");
        Assert.assertEquals(result.get(0).getVolume().compareTo(volume_98), 0);
        Assert.assertEquals(result.get(0).getAveragePrice().compareTo(avg_price_98), 0);
        Assert.assertEquals(result.get(0).getTotalPrice().compareTo(totalPriceFor_98), 0);

        Assert.assertEquals(result.get(1).getFuelType(), "D");
        Assert.assertEquals(result.get(1).getVolume().compareTo(volume_D), 0);
        Assert.assertEquals(result.get(1).getAveragePrice().compareTo(avg_price_D), 0);
        Assert.assertEquals(result.get(1).getTotalPrice().compareTo(totalPriceFor_D), 0);
    }

    @Test
    public void eachMonthStatisticsGroupByFuelTypeOfDriverTest() {
        Long driverId = 120L;
        List<Fuel> fuelList = addSeveralRecords(testEntityManager);
        List<MonthStatisticsReport> result = fuelRepository.eachMonthStatisticsGroupByFuelTypeOfDriver(driverId);
        //total price calculation for fuel type D and driver 120
        BigDecimal totalPriceFor_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .filter(f -> f.getDriverId().equals(driverId))
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total price calculation for fuel type 98 and driver 120
        BigDecimal totalPriceFor_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .filter(f -> f.getDriverId().equals(driverId))
                .map(f -> f.getVolume().multiply(f.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total volume calculation for fuel type D and driver 120
        BigDecimal volume_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .filter(f -> f.getDriverId().equals(driverId))
                .map(Fuel::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //total volume calculation for fuel type 98 and driver 120
        BigDecimal volume_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .filter(f -> f.getDriverId().equals(driverId))
                .map(Fuel::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //avg price calculation for fuel type D and driver 120
        Double avg_price_D = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("D"))
                .filter(f -> f.getDriverId().equals(driverId))
                .mapToDouble(f -> f.getPrice().doubleValue())
                .average().orElse(0.0);
        //avg price calculation for fuel type 98 and driver 120
        Double avg_price_98 = fuelList
                .stream()
                .filter(f -> f.getFuelType().equals("98"))
                .filter(f -> f.getDriverId().equals(driverId))
                .mapToDouble(f -> f.getPrice().doubleValue())
                .average().orElse(0.0);

        Assert.assertEquals(result.get(0).getFuelType(), "98");
        Assert.assertEquals(result.get(0).getVolume().compareTo(volume_98), 0);
        Assert.assertEquals(result.get(0).getAveragePrice().compareTo(avg_price_98), 0);
        Assert.assertEquals(result.get(0).getTotalPrice().compareTo(totalPriceFor_98), 0);

        Assert.assertEquals(result.get(1).getFuelType(), "D");
        Assert.assertEquals(result.get(1).getVolume().compareTo(volume_D), 0);
        Assert.assertEquals(result.get(1).getAveragePrice().compareTo(avg_price_D), 0);
        Assert.assertEquals(result.get(1).getTotalPrice().compareTo(totalPriceFor_D), 0);
    }
}
