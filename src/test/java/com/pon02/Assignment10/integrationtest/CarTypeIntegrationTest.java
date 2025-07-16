package com.pon02.Assignment10.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
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
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarTypeIntegrationTest {

    @Autowired
    MockMvc mockMvc;


    //GETメソッドでカータイプを全件取得しステータスコード200が返されること
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
                                "carTypeName": "セダン4人乗り",
                                "capacity": 4
                            },
                            {
                                "id": 2,
                                "carTypeName": "ハコバン7人乗り",
                                "capacity": 7
                            }
                        ]
                        """
                ));
    }

    //GETメソッドでカータイプが存在しない場合、空のリストを取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/car_types/car_type_empty.yml")
    @Transactional
    void カータイプが存在しない時に空のリストが返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/car-types"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    //GETメソッドで指定したIDのカータイプを取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 指定したIDのカータイプが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/car-types/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "carTypeName": "セダン4人乗り",
                            "capacity": 4
                        }
                        """
                ));
    }

    // GETメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 存在しないIDのカータイプを取得しようとすると404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/car-types/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Car type not found for id: 100",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:28:02.663990+09:00[Asia/Tokyo]",
                     "path": "/car-types/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }

    // GETメソッドで指定したカータイプ名のカータイプを取得しステータスコード200が返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 指定したカータイプ名のカータイプが取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/car-types?carTypeName=セダン4人"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "carTypeName": "セダン4人乗り",
                                "capacity": 4
                            }
                        ]
                        """
                ));
    }

    // GETメソッドで存在しないカータイプ名を指定した時に、404エラーが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 存在しないカータイプ名を指定した時に404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/car-types?carTypeName=セダン10人乗り"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Car type not found for name: セダン10人乗り",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:28:02.663990+09:00[Asia/Tokyo]",
                     "path": "/car-types?carTypeName=セダン10人乗り",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }

    // POSTメソッドで正しくリクエストした時に、カータイプが登録できステータスコード201とメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @ExpectedDataSet(value = "datasets/car_types/insert_car_type.yml")
    @Transactional
    void カータイプが登録できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/car-types")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "ハイエース9人乗り",
                                    "capacity": 9
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                    "message": "CarType created"
                }
                """, response, true);
    }

    // POSTメソッドでリクエストのcarTypeNameが空文字や50文字を超える場合、または登録済みの車種名を入力された時に、
    // ステータスコード400とエラーメッセージが返されること
    @Transactional
    @DataSet(value = "datasets/car_types/car_types.yml")
    @ParameterizedTest
    @MethodSource("provideStringsForPost")
    void 新規登録時carTypeNameが不正な値の場合400エラーが返されること(String str,String expectedMessage) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/car-types")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "%s",
                                    "capacity": 4
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
                             "field": "carTypeName",
                             "message": "%s"
                         }
                     ]
                 }
                """.formatted(expectedMessage), response, true);
    }
    private static Stream<Arguments> provideStringsForPost() {
        return Stream.of(
                Arguments.of("", "必須項目です"),
                Arguments.of("123456789012345678901234567890123456789012345678901", "50文字以内で入力してください"),
                Arguments.of("セダン4人乗り", "この車種名はすでに登録されています")
        );
    }

    // POSTメソッドでリクエストのcapacityがnullの場合に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 新規登録時capacityが不正な値の場合400エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/car-types")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "ハイエース9人乗り",
                                    "capacity": null
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
                             "field": "capacity",
                             "message": "必須項目です"
                         }
                     ]
                 }
                """, response, true);
    }

    //PATCHメソッドで正しくリクエストした時に、カータイプが更新できステータスコード200とメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @ExpectedDataSet(value = "datasets/car_types/update_car_type.yml")
    @Transactional
    void カータイプが更新できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/car-types/1")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "セダン4人",
                                    "capacity": 4
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                    "message": "CarType updated"
                }
                """, response, true);
    }

    // PATCHメソッドでリクエストのcarTypeNameが空文字や50文字を超える場合、または登録済みの車種名を入力された時に、
    // ステータスコード400とエラーメッセージが返されること
    @ParameterizedTest
    @MethodSource("provideStringsForPatchCarType")
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void カータイプを更新時にcarTypeNameが不正な値の場合400エラーが返されること(String str,String expectedMessage) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/car-types/1")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "%s",
                                    "capacity": 4
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
                             "field": "carTypeName",
                             "message": "%s"
                         }
                     ]
                 }
                """.formatted(expectedMessage), response, true);
    }
    private static Stream<Arguments> provideStringsForPatchCarType() {
        return Stream.of(
                Arguments.of("", "必須項目です"),
                Arguments.of("123456789012345678901234567890123456789012345678901", "50文字以内で入力してください"),
                Arguments.of("ハコバン7人乗り", "この車種名はすでに登録されています")
        );
    }

    // PATCHメソッドでリクエストのcapacityがnullの場合に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void カータイプを更新時にcapacityが不正な値の場合400エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/car-types/1")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "セダン4人",
                                    "capacity": null
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
                             "field": "capacity",
                             "message": "必須項目です"
                         }
                     ]
                 }
                """, response, true);
    }

    // DELETEメソッドで指定したIDのカータイプを削除しステータスコード204が返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @ExpectedDataSet(value = "datasets/car_types/delete_car_type.yml")
    @Transactional
    void 指定したIDのカータイプが削除できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/car-types/2"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // DELETEメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 存在しないIDのカータイプを削除しようとすると404エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/car-types/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Car type not found for id: 100",
                     "error": "Not Found",
                     "timestamp": "2024-08-17T16:28:02.663990+09:00[Asia/Tokyo]",
                     "path": "/car-types/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }

    //DELETEメソッドでカータイプを削除しようとした時に、カータイプがすでに使用されている場合、
    // 例外がスローされ400エラーとメッセージが返されること
    @Test
    @DataSet(value = {"datasets/car_types/car_types.yml", "datasets/orders/orders.yml"})
    @Transactional
    void カータイプが使用履歴がある場合に削除しようとすると400エラーが返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/car-types/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals("""
                {
                     "message": "Car type with id 1 is in use and cannot be deleted",
                     "error": "Bad Request",
                     "timestamp": "2024-08-17T16:28:02.663990+09:00[Asia/Tokyo]",
                     "path": "/car-types/1",
                     "status": "400"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }
}
