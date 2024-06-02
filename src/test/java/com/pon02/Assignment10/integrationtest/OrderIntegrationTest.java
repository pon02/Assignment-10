package com.pon02.Assignment10.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    //GETメソッドでオーダーを全件取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void 全てのオーダーが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "carTypeId": 1,
                                "orderStatusId": 2,
                                "createdAt": "2024-05-02T09:00:00",
                                "updatedAt": "2024-05-02T09:05:00"
                            },
                            {
                                "id": 2,
                                "carTypeId": 2,
                                "orderStatusId": 1,
                                "createdAt": "2024-05-02T09:02:00",
                                "updatedAt": null
                            }
                        ]
                        """
                ));
    }

    //GETメソッドでオーダーが存在しない場合、空のリストを取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/orders/order_empty.yml")
    @Transactional
    void オーダーが存在しない時に空のリストが返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
}
