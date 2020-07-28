package com.org.controller;

import com.org.model.Fuel;
import com.org.repositories.FuelRepository;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

import static com.org.constants.AppConstants.DATE_FORMAT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//Order is important, cause firstly we need to load data and then check analytics
public class FuelControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuelRepository fuelRepository;

    @Test
    //Single registration test
    public void t1_registerTest() throws Exception {

        Fuel f = new Fuel(null, "96", BigDecimal.valueOf(100.25), BigDecimal.valueOf(10.47), LocalDate.now(), 120L);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fuelType", f.getFuelType());
        jsonObject.put("date", f.getDate().format(DATE_FORMAT));
        jsonObject.put("driverId", f.getDriverId());
        jsonObject.put("price", f.getPrice());
        jsonObject.put("volume", f.getVolume());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/consumption/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        /* retrieving entity to ensure that rest controller persists it to data base*/
        List<Fuel> fuelList = fuelRepository.getAllByDriverId(f.getDriverId());
        Assert.assertFalse(fuelList.isEmpty());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.fuelType").value(fuelList.get(0).getFuelType()))
                .andExpect(jsonPath("$.date").value(fuelList.get(0).getDate().format(DATE_FORMAT)))
                .andExpect(jsonPath("$.driverId").value(fuelList.get(0).getDriverId()))
                .andExpect(jsonPath("$.price").value(fuelList.get(0).getPrice()))
                .andExpect(jsonPath("$.volume").value(fuelList.get(0).getVolume()));
    }


    @Test
    //Bulk registration test
    public void t2_bulkRegisterTest() throws Exception {
        fuelRepository.deleteAll();
        Path p = Paths.get("src/test/resources/testing_sample.csv").toAbsolutePath();

        MockMultipartFile multipartFile = new MockMultipartFile("file", "testing_sample.csv",
                MediaType.MULTIPART_FORM_DATA.getType(), Files.newInputStream(p, StandardOpenOption.READ));

        mockMvc.perform(fileUpload("/consumption/register/bulk").file(multipartFile))
                .andExpect(status().isOk());
    }

    @Test
    public void t3_getTotalMoneyGroupByMonthTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/money/spent/bymonth")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value("2017-1"))
                .andExpect(jsonPath("$[0].totalAmount").value(2937.14))
                .andExpect(jsonPath("$[1].month").value("2017-5"))
                .andExpect(jsonPath("$[1].totalAmount").value(2611.14))
                .andExpect(jsonPath("$[2].month").value("2018-9"))
                .andExpect(jsonPath("$[2].totalAmount").value(8154));
    }

    @Test
    public void t4_getTotalMoneyGroupByMonthOfDriverTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/money/spent/bymonth/driver/1002")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value("2017-1"))
                .andExpect(jsonPath("$[0].totalAmount").value(597.8))
                .andExpect(jsonPath("$[1].month").value("2017-5"))
                .andExpect(jsonPath("$[1].totalAmount").value(1151.8))
                .andExpect(jsonPath("$[2].month").value("2018-9"))
                .andExpect(jsonPath("$[2].totalAmount").value(2040));
    }

    @Test
    public void t5_getSpecifiedMonthReportTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/consumption/year/2018/month/9")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fuelType").value("96"))
                .andExpect(jsonPath("$[0].volume").value(8))
                .andExpect(jsonPath("$[0].date").value("09.13.2018"))
                .andExpect(jsonPath("$[0].price").value(90))
                .andExpect(jsonPath("$[0].totalPrice").value(720))
                .andExpect(jsonPath("$[0].driverId").value(1001))

                .andExpect(jsonPath("$[1].fuelType").value("96"))
                .andExpect(jsonPath("$[1].volume").value(8))
                .andExpect(jsonPath("$[1].date").value("09.13.2018"))
                .andExpect(jsonPath("$[1].price").value(90))
                .andExpect(jsonPath("$[1].totalPrice").value(720))
                .andExpect(jsonPath("$[1].driverId").value(1002))

                .andExpect(jsonPath("$[2].fuelType").value("98"))
                .andExpect(jsonPath("$[2].volume").value(60))
                .andExpect(jsonPath("$[2].date").value("09.29.2018"))
                .andExpect(jsonPath("$[2].price").value(89.9))
                .andExpect(jsonPath("$[2].totalPrice").value(5394))
                .andExpect(jsonPath("$[2].driverId").value(1001))

                .andExpect(jsonPath("$[3].fuelType").value("98"))
                .andExpect(jsonPath("$[3].volume").value(30))
                .andExpect(jsonPath("$[3].date").value("09.29.2018"))
                .andExpect(jsonPath("$[3].price").value(44))
                .andExpect(jsonPath("$[3].totalPrice").value(1320))
                .andExpect(jsonPath("$[3].driverId").value(1002))
        ;
    }

    @Test
    public void t6_getSpecifiedMonthReportOfDriverTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/consumption/year/2018/month/9/driver/1001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fuelType").value("96"))
                .andExpect(jsonPath("$[0].volume").value(8))
                .andExpect(jsonPath("$[0].date").value("09.13.2018"))
                .andExpect(jsonPath("$[0].price").value(90))
                .andExpect(jsonPath("$[0].totalPrice").value(720))
                .andExpect(jsonPath("$[0].driverId").value(1001))

                .andExpect(jsonPath("$[1].fuelType").value("98"))
                .andExpect(jsonPath("$[1].volume").value(60))
                .andExpect(jsonPath("$[1].date").value("09.29.2018"))
                .andExpect(jsonPath("$[1].price").value(89.9))
                .andExpect(jsonPath("$[1].totalPrice").value(5394))
                .andExpect(jsonPath("$[1].driverId").value(1001));
    }

    @Test
    public void t7_eachMonthStatisticsGroupByFuelTypeTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/each/month/statistics")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())

                .andExpect(jsonPath("$.2017-1[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2017-1[0].volume").value(11.9))
                .andExpect(jsonPath("$.2017-1[0].averagePrice").value(75.875))
                .andExpect(jsonPath("$.2017-1[0].totalPrice").value(930.14))

                .andExpect(jsonPath("$.2017-1[1].fuelType").value("98"))
                .andExpect(jsonPath("$.2017-1[1].volume").value(14))
                .andExpect(jsonPath("$.2017-1[1].averagePrice").value(87.5))
                .andExpect(jsonPath("$.2017-1[1].totalPrice").value(1420))

                .andExpect(jsonPath("$.2017-1[2].fuelType").value("D"))
                .andExpect(jsonPath("$.2017-1[2].volume").value(8))
                .andExpect(jsonPath("$.2017-1[2].averagePrice").value(67.7))
                .andExpect(jsonPath("$.2017-1[2].totalPrice").value(587))

                .andExpect(jsonPath("$.2017-5[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2017-5[0].volume").value(12.8))
                .andExpect(jsonPath("$.2017-5[0].averagePrice").value(123.525))
                .andExpect(jsonPath("$.2017-5[0].totalPrice").value(1613.94))

                .andExpect(jsonPath("$.2017-5[1].fuelType").value("D"))
                .andExpect(jsonPath("$.2017-5[1].volume").value(13))
                .andExpect(jsonPath("$.2017-5[1].averagePrice").value(70.75))
                .andExpect(jsonPath("$.2017-5[1].totalPrice").value(997.2))

                .andExpect(jsonPath("$.2018-9[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2018-9[0].volume").value(16))
                .andExpect(jsonPath("$.2018-9[0].averagePrice").value(90))
                .andExpect(jsonPath("$.2018-9[0].totalPrice").value(1440))

                .andExpect(jsonPath("$.2018-9[1].fuelType").value("98"))
                .andExpect(jsonPath("$.2018-9[1].volume").value(90))
                .andExpect(jsonPath("$.2018-9[1].averagePrice").value(66.95))
                .andExpect(jsonPath("$.2018-9[1].totalPrice").value(6714));
    }

    @Test
    public void t8_eachMonthStatisticsGroupByFuelOfDriverTypeTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/report/each/month/statistics/driver/1001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.2017-1[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2017-1[0].volume").value(6.4))
                .andExpect(jsonPath("$.2017-1[0].averagePrice").value(107.55))
                .andExpect(jsonPath("$.2017-1[0].totalPrice").value(687.34))

                .andExpect(jsonPath("$.2017-1[1].fuelType").value("98"))
                .andExpect(jsonPath("$.2017-1[1].volume").value(10))
                .andExpect(jsonPath("$.2017-1[1].averagePrice").value(120))
                .andExpect(jsonPath("$.2017-1[1].totalPrice").value(1200))

                .andExpect(jsonPath("$.2017-1[2].fuelType").value("D"))
                .andExpect(jsonPath("$.2017-1[2].volume").value(5))
                .andExpect(jsonPath("$.2017-1[2].averagePrice").value(90.4))
                .andExpect(jsonPath("$.2017-1[2].totalPrice").value(452))

                .andExpect(jsonPath("$.2017-5[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2017-5[0].volume").value(6.4))
                .andExpect(jsonPath("$.2017-5[0].averagePrice").value(107.55))
                .andExpect(jsonPath("$.2017-5[0].totalPrice").value(687.34))

                .andExpect(jsonPath("$.2017-5[1].fuelType").value("D"))
                .andExpect(jsonPath("$.2017-5[1].volume").value(9))
                .andExpect(jsonPath("$.2017-5[1].averagePrice").value(85.2))
                .andExpect(jsonPath("$.2017-5[1].totalPrice").value(772))

                .andExpect(jsonPath("$.2018-9[0].fuelType").value("96"))
                .andExpect(jsonPath("$.2018-9[0].volume").value(8))
                .andExpect(jsonPath("$.2018-9[0].averagePrice").value(90))
                .andExpect(jsonPath("$.2018-9[0].totalPrice").value(720))

                .andExpect(jsonPath("$.2018-9[1].fuelType").value("98"))
                .andExpect(jsonPath("$.2018-9[1].volume").value(60))
                .andExpect(jsonPath("$.2018-9[1].averagePrice").value(89.9))
                .andExpect(jsonPath("$.2018-9[1].totalPrice").value(5394));
    }
}