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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SectionIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GETメソッドでセクションを全件取得しステータスコード200が返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 全てのセクションが取得できること() throws Exception {
    mockMvc.perform(get("/sections"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
                [
                  { "id": 1, "sectionName": "大道具" },
                  { "id": 2, "sectionName": "音響" }
                ]
                """));
  }

  // GETメソッドでセクションが存在しない場合、空のリストを取得しステータスコード200が返されること
  @Test
  @DataSet("datasets/sections/section_empty.yml")
  @Transactional
  void セクションが存在しないときに空のリストが返されること() throws Exception {
    mockMvc.perform(get("/sections"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  // GETメソッドで指定したIDのセクションを取得しステータスコード200が返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 指定したIDのセクションが取得できること() throws Exception {
    mockMvc.perform(get("/sections/1"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
                { "id": 1, "sectionName": "大道具" }
                """));
  }

  // GETメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 存在しないIDのセクションで404エラーが返ること() throws Exception {
    String response = mockMvc.perform(get("/sections/999"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
              "message": "Section not found for id: 999",
              "error": "Not Found",
              "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
              "path": "/sections/999",
              "status": "404"
            }
            """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }


  // GETメソッドで指定したセクション名のセクションを取得しステータスコード200が返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void セクション名でセクションが取得できること() throws Exception {
    mockMvc.perform(get("/sections?sectionName=大道具"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
                [ { "id": 1, "sectionName": "大道具" } ]
                """));
  }

  // GETメソッドで存在しないセクション名を指定した時に、404エラーが返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 存在しないセクション名を指定した時に404エラーが返されること() throws Exception {
    String response = mockMvc.perform(get("/sections?sectionName=美術"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
              "message": "Section not found for name: 美術",
              "error": "Not Found",
              "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
              "path": "/sections?sectionName=美術",
              "status": "404"
            }
            """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  // POSTメソッドで正しくリクエストした時に、セクションが登録できステータスコード201とメッセージが返されること
  @Test
  @DataSet(value = "datasets/sections/sections.yml",
  cleanBefore = true,
  executeStatementsBefore = "ALTER TABLE sections AUTO_INCREMENT = 1")
  @ExpectedDataSet("datasets/sections/insert_section.yml")
  @Transactional
  void セクションが登録できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/sections")
            .contentType("application/json")
            .content("""
                    {
                    "sectionName": "照明"
                     }
                    """))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
                "message": "Section created"
            }
            """, response, true);
  }

  // POSTメソッドでリクエストのsectionNameが空文字や50文字を超える場合、または登録済みのセクション名を入力された時に、
  // ステータスコード400とエラーメッセージが返されること
  @ParameterizedTest
  @MethodSource("provideBadNames")
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 新規登録時sectionNameが不正な場合400エラーが返されること(String name, String msg) throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.post("/sections")
            .contentType("application/json")
            .content("""
                    {
                        "sectionName": "%s"
                    }
                    """.formatted(name)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
                "status": "BAD_REQUEST",
                "message": "validation error",
                "errors": [
                    {
                        "field": "sectionName",
                        "message": "%s"
                    }
                ]
            }
            """.formatted(msg), response, true);
  }

  private static Stream<Arguments> provideBadNames() {
    return Stream.of(
        Arguments.of("", "必須項目です"),
        Arguments.of("A".repeat(51), "50文字以内で入力してください"),
        Arguments.of("大道具", "このセクション名はすでに登録されています")
    );
  }

  //PATCHメソッドで正しくリクエストした時に、カータイプが更新できステータスコード200とメッセージが返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @ExpectedDataSet("datasets/sections/update_section.yml")
  @Transactional
  void セクションが更新できること() throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/sections/2")
            .contentType("application/json")
            .content("""
                    {
                        "sectionName": "音響A"
                    }
                    """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
                "message": "Section updated"
            }
            """, response, true);
  }

  // PATCHメソッドでリクエストのsectionNameが空文字や50文字を超える場合、または登録済みのセクション名を入力された時に、
  // ステータスコード400とエラーメッセージが返されること
  @ParameterizedTest
  @MethodSource("provideBadNames")
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 更新時sectionNameが不正な場合400エラーが返されること(String name, String msg) throws Exception {
    String response = mockMvc.perform(MockMvcRequestBuilders.patch("/sections/1")
            .contentType("application/json")
            .content("""
                    {
                        "sectionName": "%s"
                    }
                    """.formatted(name)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
              "status": "BAD_REQUEST",
              "message": "validation error",
              "errors": [
                  {
                       "field": "sectionName",
                       "message": "%s" } ]
            }
            """.formatted(msg), response, true);
  }

  // DELETEメソッドで指定したIDのセクションを削除しステータスコード204が返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @ExpectedDataSet("datasets/sections/delete_section.yml")
  @Transactional
  void セクションが削除できること() throws Exception {
    mockMvc.perform(delete("/sections/2"))
        .andExpect(status().isNoContent());
  }

  // DELETEメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet("datasets/sections/sections.yml")
  @Transactional
  void 存在しないIDのセクションを削除しようとすると404エラーが返ること() throws Exception {
    String response = mockMvc.perform(delete("/sections/999"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
              "message": "Section not found for id: 999",
              "error": "Not Found",
              "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
              "path": "/sections/999",
              "status": "404"
            }
            """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  //DELETEメソッドでセクションを削除しようとした時に、セクションがすでに使用されている場合、
  // 例外がスローされ400エラーとメッセージが返されること
  @Test
  @DataSet(value = {"datasets/sections/sections.yml", "datasets/staffs/staffs.yml"})
  @Transactional
  void セクションが使用履歴がある場合に削除しようとすると400エラーが返されること() throws Exception {
    String response = mockMvc.perform(delete("/sections/1"))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    JSONAssert.assertEquals("""
            {
              "message": "Section with id 1 is in use and cannot be deleted",
              "error": "Bad Request",
              "timestamp": "2024-08-17T16:42:47.123237+09:00[Asia/Tokyo]",
              "path": "/sections/1",
              "status": "400"
            }
            """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }
}
