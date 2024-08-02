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
                                "carTypeName": "セダン4人",
                                "capacity": 4
                            },
                            {
                                "id": 2,
                                "carTypeName": "ハコバン7人",
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
                            "carTypeName": "セダン4人",
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
                    "timestamp": "2024-05-28T18:05:52.214069+09:00[Asia/Tokyo]",
                    "path": "/car-types/100",
                    "status": "404",
                    "error": "Not Found"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }

    // POSTメソッドで正しくリクエストした時に、カータイプが登録できステータスコード201とメッセージが返されること
    @Test
    @DataSet(cleanBefore = true, cleanAfter = true, value = "datasets/car_types/car_types.yml")
    @ExpectedDataSet(value = "datasets/car_types/insert_car_type.yml")
    @Transactional
    void カータイプが登録できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/car-types")
                        .contentType("application/json")
                        .content("""
                                {
                                    "carTypeName": "ハイエース9人",
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
    @ParameterizedTest
    @MethodSource("provideStringsForValidation")
    @Transactional
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
    private static Stream<Arguments> provideStringsForValidation() {
        return Stream.of(
                Arguments.of("", "必須項目です"),
                Arguments.of("123456789012345678901234567890123456789012345678901", "50文字以内で入力してください"),
                Arguments.of("セダン4人", "この車種名はすでに登録されています")
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
                                    "carTypeName": "ハイエース9人",
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
}
