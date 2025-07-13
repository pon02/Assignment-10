package com.pon02.Assignment10.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.Section;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SectionMapperTest {

  @Autowired
  SectionMapper sectionMapper;

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void 全てのセクションが取得できること() {
    List<Section> sections = sectionMapper.findAllSections();
    assertThat(sections)
        .hasSize(2)
        .contains(
            new Section(1, "大道具"),
            new Section(2, "音響")
        );
  }

  @Test
  @DataSet(value = "datasets/sections/section_empty.yml")
  @Transactional
  void セクションがない時に空のリストが返されること() {
    assertThat(sectionMapper.findAllSections()).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void セクションIDでセクションが取得できること() {
    Optional<Section> section = sectionMapper.findSectionById(1);
    assertThat(section).contains(new Section(1, "大道具"));
  }

  @Test
  @DataSet(value = "datasets/sections/section_empty.yml")
  @Transactional
  void 存在しないセクションIDを指定した時に空で返すこと() {
    Optional<Section> section = sectionMapper.findSectionById(100);
    assertThat(section).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void セクション名でセクションが取得できること() {
    List<Section> sections = sectionMapper.findSectionByName("大道具");
    assertThat(sections)
        .hasSize(1)
        .contains(new Section(1, "大道具"));
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void 存在しないセクション名を指定した時に空で返すこと() {
    List<Section> sections = sectionMapper.findSectionByName("特効");
    assertThat(sections).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void セクションIDでセクションが存在するかどうかが確認できること() {
    assertThat(sectionMapper.existsById(1)).isTrue();
    assertThat(sectionMapper.existsById(100)).isFalse();
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void セクション名がすでに存在するかどうかが確認できること() {
    assertThat(sectionMapper.existsByName("大道具")).isTrue();
    assertThat(sectionMapper.existsByName("音響")).isTrue();
    assertThat(sectionMapper.existsByName("特効")).isFalse();
  }

  @Test
  @DataSet(value = "datasets/sections/section_empty.yml")
  @Transactional
  void セクションが追加されること() {
    Section section = new Section(null, "照明");
    sectionMapper.insertSection(section);
    List<Section> sections = sectionMapper.findAllSections();
    assertThat(sections)
        .hasSize(1)
        .isEqualTo(List.of(section));
  }

  @Test
  @DataSet(value = "datasets/sections/sections.yml")
  @Transactional
  void セクションが更新されること() {
    Section existingSection = sectionMapper.findSectionById(1).get();
    Section updatedSection = new Section(existingSection.getId(), "大道具A");
    sectionMapper.updateSection(updatedSection);
    List<Section> sections = sectionMapper.findAllSections();
    assertThat(sections)
        .hasSize(2)
        .contains(
            updatedSection,
            new Section(2, "音響")
        );
  }
}
