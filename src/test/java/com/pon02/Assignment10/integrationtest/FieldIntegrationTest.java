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

import static java.lang.reflect.Array.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FieldIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GET: 全てのフィールドが取得できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void 全てのフィールドが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "fieldName": "2025/01/29AM",
                                "dateOfUse": "2025-01-29",
                                "createdAt": "2025-01-28T09:00:00"
                            },
                            {
                                "id": 2,
                                "fieldName": "2025/01/29PM",
                                "dateOfUse": "2025-01-29",
                                "createdAt": "2025-01-29T09:00:00"
                            }
                        ]
                        """
        ));
  }

  // GET: 指定したIDのフィールドが取得できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void 指定したIDのフィールドが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields/1"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "fieldName": "2025/01/29AM",
                            "dateOfUse": "2025-01-29",
                            "createdAt": "2025-01-28T09:00:00"
                        }
                        """
        ));
  }

  // GET: 存在しないIDで404が返ること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void 存在しないIDのフィールドで404エラーが返ること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.get("/fields/999"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field not found for id: 999",
                    "error": "Not Found",
                    "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                    "path": "/fields/999",
                    "status": "404"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  // GET: フィールドが存在しないとき空配列を返す
  @Test
  @DataSet(value = "datasets/fields/field_empty.yml")
  @Transactional
  void フィールドが存在しないときに空のリストが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));
  }

  // GET: フィールド名でフィールドが取得できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド名でフィールドが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields/name").param("value", "2025/01/29AM"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "fieldName": "2025/01/29AM",
                                "dateOfUse": "2025-01-29",
                                "createdAt": "2025-01-28T09:00:00"
                            }
                        ]
                        """
        ));
  }

  // GET: フィールド名で存在しないフィールドを取得し404が返ること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド名で存在しないフィールドを取得し404エラーが返ること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.get("/fields/name").param("value", "2026/01/29AM"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field not found for name: 2026/01/29AM",
                    "error": "Not Found",
                    "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                    "path": "/fields/name",
                    "status": "404"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  // GET: フィールド使用日でフィールドが取得できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド使用日でフィールドが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields")
            .param("dateOfUse", "2025-01-29"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "fieldName": "2025/01/29AM",
                                "dateOfUse": "2025-01-29",
                                "createdAt": "2025-01-28T09:00:00"
                            },
                            {
                                "id": 2,
                                "fieldName": "2025/01/29PM",
                                "dateOfUse": "2025-01-29",
                                "createdAt": "2025-01-29T09:00:00"
                            }
                        ]
                        """
        ));
  }

  // GET: フィールド使用日で存在しない日付を指定した場合に404が返ること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド使用日で存在しない日付を指定した場合404エラーが返ること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.get("/fields")
            .param("dateOfUse", "2026-01-29"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field not found for date of use: 2026-01-29",
                    "error": "Not Found",
                    "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                    "path": "/fields",
                    "status": "404"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  // POST: フィールドが登録できること
  @Test
  @DataSet(value = "datasets/fields/field_empty.yml",
      cleanBefore = true,
      executeStatementsBefore = "ALTER TABLE fields AUTO_INCREMENT = 1")
  @ExpectedDataSet(value = "datasets/fields/insert_field.yml", ignoreCols = "created_at")
  @Transactional
  void フィールドが登録できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields")
            .contentType("application/json")
            .content("""
                                {
                                    "fieldName": "2125/01/30AM",
                                    "dateOfUse": "2125-01-30"
                                }
                                """))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field created"
                }
                """, response, true);
  }

  // POSTメソッドでリクエストのfieldNameやdateOfUseがnullや未登録の時に、ステータスコード400とエラーメッセージが返されること
  @ParameterizedTest
  @MethodSource("provideFieldValidationData")
  @DataSet(value = "datasets/fields/field_empty.yml")
  @Transactional
  void フィールドを登録時に不正な値の場合400エラーが返されること(String fieldName, String dateOfUse, String expectedField, String expectedMessage) throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields")
        .contentType("application/json")
        .content("""
            {
                "fieldName": "%s",
                "dateOfUse": "%s"
            }
        """.formatted(
            null == fieldName ? "" : fieldName,
            null == dateOfUse ? "" : dateOfUse
        )))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
             "status": "BAD_REQUEST",
             "message": "validation error",
             "errors": [
                 {
                     "field": "%s",
                     "message": "%s"
                 }
             ]
         }
    """.formatted(expectedField, expectedMessage), response, true);
  }

  private static Stream<Arguments> provideFieldValidationData() {
    return Stream.of(
        Arguments.of("", "2025-08-01", "fieldName", "必須項目です"),
        Arguments.of(null, "2025-08-01", "fieldName", "必須項目です"),
        Arguments.of("2025/08/AM", null, "dateOfUse", "必須項目です")
    );
  }

  // PATCH: フィールドが更新できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @ExpectedDataSet(value = "datasets/fields/update_field.yml")
  @Transactional
  void フィールドが更新できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1")
            .contentType("application/json")
            .content("""
                                {
                                    "fieldName": "2125/01/30AM",
                                    "dateOfUse": "2125-01-30"
                                }
                                """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field updated"
                }
                """, response, true);
  }

  // PATCH: フィールド更新時に不正値で400が返ること
  @ParameterizedTest
  @MethodSource("provideFieldValidationData")
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド更新時に不正な値で400エラーが返ること(String fieldName, String dateOfUse, String expectedField, String expectedMessage) throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1")
            .contentType("application/json")
            .content("""
            {
                "fieldName": "%s",
                "dateOfUse": "%s"
            }
        """.formatted(
                null == fieldName ? "" : fieldName,
                null == dateOfUse ? "" : dateOfUse
            )))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
             "status": "BAD_REQUEST",
             "message": "validation error",
             "errors": [
                 {
                     "field": "%s",
                     "message": "%s"
                 }
             ]
         }
    """.formatted(expectedField, expectedMessage), response, true);
  }

  // DELETE: フィールドが削除できること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @ExpectedDataSet(value = "datasets/fields/delete_field.yml")
  @Transactional
  void フィールドが削除できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/fields/2"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  // DELETE: 存在しないフィールドIDで404が返ること
  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void 存在しないフィールドIDで404エラーが返ること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.delete("/fields/999"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
                {
                    "message": "Field not found for id: 999",
                    "error": "Not Found",
                    "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
                    "path": "/fields/999",
                    "status": "404"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }
}
