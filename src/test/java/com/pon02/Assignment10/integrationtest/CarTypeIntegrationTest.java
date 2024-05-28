package com.pon02.Assignment10.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarTypeIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 全てのカータイプが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/car-types"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "carType": "セダン4人乗り",
                                "capacity": 4
                            },
                            {
                                "id": 2,
                                "carType": "ハコバン7人乗り",
                                "capacity": 7
                            }
                        ]
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 指定したIDのカータイプが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/car-types/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "carType": "セダン4人乗り",
                            "capacity": 4
                        }
                        """
                ));
    }

    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 存在しないIDのカータイプを取得しようとすると404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/car-types/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                    "timestamp": "2024-05-28T18:05:52.214069+09:00[Asia/Tokyo]",
                    "path": "/car-types/100",
                    "status": "404",
                    "error": "Not Found"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }
}
