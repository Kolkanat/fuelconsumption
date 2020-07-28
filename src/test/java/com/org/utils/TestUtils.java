package com.org.utils;

import com.org.model.Fuel;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.org.utils.TestConstants.*;

public class TestUtils {

    public static Fuel addOneRecord(TestEntityManager em) {
        Fuel f = em.persist(getCopy(f1_d_2018_02_12th));
        em.flush();
        return f;
    }

    public static Fuel getCopy(Fuel f) {
        return Fuel
                .builder()
                .fuelType(f.getFuelType())
                .date(f.getDate())
                .driverId(f.getDriverId())
                .price(f.getPrice())
                .volume(f.getVolume())
                .build();
    }

    public static List<Fuel> addSeveralRecords(TestEntityManager em) {
        List<Fuel> fuelList = new ArrayList<>();
        fuelList.add(em.persist(getCopy(f1_d_2018_02_12th)));
        fuelList.add(em.persist(getCopy(f2_98_2018_02_27th)));
        fuelList.add(em.persist(getCopy(f3_98_2018_02_9th)));
        em.flush();
        return fuelList;
    }

}
