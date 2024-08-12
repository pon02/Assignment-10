package com.pon02.Assignment10.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
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

    //GETメソッドで指定したidのオーダーを1件取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void 指定したidのオーダーが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "carTypeId": 1,
                            "orderStatusId": 2,
                            "createdAt": "2024-05-02T09:00:00",
                            "updatedAt": "2024-05-02T09:05:00"
                        }
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

    // POSTメソッドで正しくリクエストした時に、オーダーが登録できステータスコード201とメッセージが返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @ExpectedDataSet(value = "datasets/orders/insert_order.yml")
    @Transactional
    void オーダーが登録できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeId": 4,
                                    "orderStatusId": 1
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
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーを登録時にcarTypeIdがが不正な値の場合400エラーが返されること(String str,String expectedMessage) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType("application/json")
                        .content("""
                                {
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
    @DataSet(value = "datasets/orders/orders.yml")
    @ExpectedDataSet(value = "datasets/orders/update_order.yml")
    @Transactional
    void オーダーが更新できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "id": 2,
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

    // PATCHメソッドでリクエストのidがnullや未登録の時に、ステータスコード400とエラーメッセージが返されること
    @ParameterizedTest
    @MethodSource("provideStringsForValidationId")
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーを更新時にidが不正な値の場合400エラーが返されること(String str,String expectedMessage) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "id": "%s",
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
                             "field": "id",
                             "message": "%s"
                         }
                     ]
                 }
                """.formatted(expectedMessage), response, true);
    }
    private static Stream<Arguments> provideStringsForValidationId() {
        return Stream.of(
            Arguments.of("", "必須項目です"),
            Arguments.of("99", "IDが存在しません")
        );
    }

    // PATCHメソッドでリクエストのorderStatusIdがnullの時に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーを更新時にorderStatusIdが不正な値の場合400エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                    "id": 2,
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
}
