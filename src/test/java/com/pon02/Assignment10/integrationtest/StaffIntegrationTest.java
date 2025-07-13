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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaffIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void 全てのスタッフが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/staffs"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "fieldId": 1,
                                "sectionId": 1,
                                "staffStatusId": 2,
                                "createdAt": "2024-05-02T09:00:00",
                                "updatedAt": "2024-05-02T09:05:00"
                            },
                            {
                                "id": 2,
                                "fieldId": 1,
                                "sectionId": 2,
                                "staffStatusId": 1,
                                "createdAt": "2024-05-02T09:02:00",
                                "updatedAt": null
                            }
                        ]
                        """));
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void 指定したidのスタッフが取得できること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/staffs/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "fieldId": 1,
                            "sectionId": 1,
                            "staffStatusId": 2,
                            "createdAt": "2024-05-02T09:00:00",
                            "updatedAt": "2024-05-02T09:05:00"
                        }
                        """));
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void 存在しないIDのスタッフを取得しようとした場合404エラーが返されること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/staffs/100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
                {
                     "message": "Staff not found for fieldId: 1, staffId: 100",
                     "error": "Not Found",
                     "timestamp": "2025-07-10T12:00:00.000+09:00[Asia/Tokyo]",
                     "path": "/fields/1/staffs/100",
                     "status": "404"
                 }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/staffs/staff_empty.yml")
  @Transactional
  void スタッフが存在しない時に空のリストが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/fields/1/staffs"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));
  }

  @Test
  @DataSet(value = {"datasets/fields/fields.yml", "datasets/staffs/staffs.yml"})
  @ExpectedDataSet(value = "datasets/staffs/insert_staff.yml")
  @Transactional
  void スタッフが登録できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields/1/staffs")
            .contentType("application/json")
            .content("""
                                {
                                    "sectionId": 3
                                }
                                """))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(header().exists("Location"))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
                {
                    "message": "Staff created"
                }
                """, response, true);
  }

  // POSTメソッドでリクエストのsectionIdがnullや未登録の時に、ステータスコード400とエラーメッセージが返されること
  @ParameterizedTest
  @MethodSource("provideStringsForSectionIdValidation")
  @DataSet(value = {"datasets/fields/fields.yml", "datasets/staffs/staffs.yml"})
  @Transactional
  void スタッフ登録時にsectionIdが不正な値の場合400エラーが返されること(String input, String expectedMessage) throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/fields/1/staffs")
            .contentType("application/json")
            .content("""
                {
                    "sectionId": "%s"
                }
                """.formatted(input)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
            "status": "BAD_REQUEST",
            "message": "validation error",
            "errors": [
                {
                    "field": "sectionId",
                    "message": "%s"
                }
            ]
        }
        """.formatted(expectedMessage), response, true);
  }

  private static Stream<Arguments> provideStringsForSectionIdValidation() {
    return Stream.of(
        Arguments.of("", "必須項目です"),
        Arguments.of("99", "部署IDが存在しません")
    );
  }

  @Test
  @DataSet(value = {"datasets/fields/fields.yml", "datasets/staffs/staffs.yml"})
  @ExpectedDataSet(value = "datasets/staffs/update_staff.yml")
  @Transactional
  void スタッフが更新できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1/staffs/2")
            .contentType("application/json")
            .content("""
                                {
                                    "staffStatusId": 2
                                }
                                """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
                {
                    "message": "Staff updated"
                }
                """, response, true);
  }

  @Test
  @DataSet(value = {"datasets/fields/fields.yml", "datasets/staffs/staffs.yml"})
  @Transactional
  void スタッフを更新時にstaffStatusIdが不正な値の場合400エラーが返されること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/fields/1/staffs/2")
            .contentType("application/json")
            .content("""
                                {
                                    "staffStatusId": null
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
                             "field": "staffStatusId",
                             "message": "必須項目です"
                         }
                     ]
                 }
                """, response, true);
  }
}
