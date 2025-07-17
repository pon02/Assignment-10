package com.pon02.Assignment10.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

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
        mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "fieldId": 1,
                                "carTypeId": 1,
                                "orderStatusId": 2,
                                "createdAt": "2024-05-02T09:00:00",
                                "updatedAt": "2024-05-02T09:05:00"
                            },
                            {
                                "id": 2,
                                "fieldId": 1,
                                "carTypeId": 2,
                                "orderStatusId": 1,
                                "createdAt": "2024-05-02T09:02:00",
                                "updatedAt": null
                            }
                        ]
                        """
                ));
    }

    //GETメソッドで指定したidのオーダーを1件取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void 指定したidのオーダーが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "fieldId": 1,
                            "carTypeId": 1,
                            "orderStatusId": 2,
                            "createdAt": "2024-05-02T09:00:00",
                            "updatedAt": "2024-05-02T09:05:00"
                        }
                        """
                ));
    }

    //GETメソッドで存在しないIDを指定した場合、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void 存在しないIDのオーダーを取得しようとした場合404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/orders/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Order not found for fieldId: 1, orderId: 100",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                     "path": "/fields/1/orders/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
            new Customization("timestamp", ((o1, o2) -> true))));
    }

    //GETメソッドでオーダーが存在しない場合、空のリストを取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/orders/order_empty.yml")
    @Transactional
    void オーダーが存在しない時に空のリストが返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    // POSTメソッドで正しくリクエストした時に、オーダーが登録できステータスコード201とメッセージが返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/car_types/car_types.yml", "datasets/orders/orders.yml"})
    @ExpectedDataSet(value = "datasets/orders/insert_order.yml")
    @Transactional
    void オーダーが登録できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields/1/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeId": 1
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                    "message": "Order created"
                }
                """, response, true);
    }

    // POSTメソッドでリクエストのcarTypeIdがnullや未登録の時に、ステータスコード400とエラーメッセージが返されること
    // （NotBlankのバリデーション確認,orderStatusIdは自動で1が入るようにしているため省略）
    @ParameterizedTest
    @MethodSource("provideStringsForValidation")
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @Transactional
    void オーダーを登録時にcarTypeIdがが不正な値の場合400エラーが返されること(String str,String expectedMessage) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields/1/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "fieldId": 1,
                                    "carTypeId": "%s",
                                    "orderStatusId": 1
                                }
                                """.formatted(str)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "status": "BAD_REQUEST",
                     "message": "validation error",
                     "errors": [
                         {
                             "field": "carTypeId",
                             "message": "%s"
                         }
                     ]
                 }
                """.formatted(expectedMessage), response, true);
    }
    private static Stream<Arguments> provideStringsForValidation() {
        return Stream.of(
            Arguments.of("", "必須項目です"),
            Arguments.of("99", "車種IDが存在しません")
        );
    }

    //PATCHメソッドで正しくリクエストした時に、オーダーが更新できステータスコード200とメッセージが返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @ExpectedDataSet(value = "datasets/orders/update_order.yml")
    @Transactional
    void オーダーが更新できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1/orders/2")
                        .contentType("application/json")
                        .content("""
                                {
                                    "orderStatusId": 2
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                    "message": "Order updated"
                }
                """, response, true);
    }

    // PATCHメソッドでリクエストのorderStatusIdがnullの時に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @Transactional
    void オーダーを更新時にorderStatusIdが不正な値の場合400エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1/orders/2")
                        .contentType("application/json")
                        .content("""
                                {
                                    "orderStatusId": null
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "status": "BAD_REQUEST",
                     "message": "validation error",
                     "errors": [
                         {
                             "field": "orderStatusId",
                             "message": "必須項目です"
                         }
                     ]
                 }
                """, response, true);
    }

    //PATCHメソッドで存在しないIDを指定した場合、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @Transactional
    void 存在しないIDのオーダーを更新しようとした場合404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1/orders/100")
                        .contentType("application/json")
                        .content("""
                                {
                                    "orderStatusId": 2
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Order not found for fieldId: 1 and orderId: 100",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                     "path": "/fields/1/orders/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
            new Customization("timestamp", ((o1, o2) -> true))));
    }

    //DELETEメソッドで正しくリクエストした時に、オーダーが削除できステータスコード204が返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @ExpectedDataSet(value = "datasets/orders/delete_order.yml")
    @Transactional
    void オーダーが削除できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/fields/1/orders/2"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    //DELETEメソッドで存在しないIDを指定した場合、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = {"datasets/fields/fields.yml", "datasets/orders/orders.yml"})
    @Transactional
    void 存在しないIDのオーダーを削除しようとした場合404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/fields/1/orders/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Order not found for fieldId: 1 and orderId: 100",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                     "path": "/fields/1/orders/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
            new Customization("timestamp", ((o1, o2) -> true))));
    }
}
