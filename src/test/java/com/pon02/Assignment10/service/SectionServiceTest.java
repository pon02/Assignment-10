package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Section;
import com.pon02.Assignment10.exception.SectionNotFoundException;
import com.pon02.Assignment10.mapper.SectionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

  @InjectMocks
  private SectionService sectionService;

  @Mock
  private SectionMapper sectionMapper;

  @Test
  public void 全てのセクションを取得できる() {
    doReturn(List.of(
        new Section(1, "大道具"),
        new Section(2, "音響")
    )).when(sectionMapper).findAllSections();

    List<Section> actual = sectionService.findAllSections();

    assertThat(actual).isEqualTo(List.of(
        new Section(1, "大道具"),
        new Section(2, "音響")
    ));
    verify(sectionMapper, times(1)).findAllSections();
  }

  @Test
  public void セクションIDでセクションを取得できる() {
    doReturn(Optional.of(new Section(1, "大道具")))
        .when(sectionMapper).findSectionById(1);

    Section actual = sectionService.findSectionById(1);

    assertThat(actual).isEqualTo(new Section(1, "大道具"));
    verify(sectionMapper, times(1)).findSectionById(1);
  }

  @Test
  public void セクションIDが存在しない場合例外がスローされること() {
    doReturn(Optional.empty()).when(sectionMapper).findSectionById(100);

    assertThatThrownBy(() -> sectionService.findSectionById(100))
        .isInstanceOfSatisfying(SectionNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Section not found for id: 100");
        });

    verify(sectionMapper, times(1)).findSectionById(100);
  }

  @Test
  public void セクション名でセクションを取得できる() {
    doReturn(List.of(new Section(1, "大道具")))
        .when(sectionMapper).findSectionByName("大道具");

    List<Section> actual = sectionService.findSectionByName("大道具");

    assertThat(actual).isEqualTo(List.of(new Section(1, "大道具")));
    verify(sectionMapper, times(1)).findSectionByName("大道具");
  }

  @Test
  public void セクション名が存在しない場合例外がスローされること() {
    doReturn(List.of()).when(sectionMapper).findSectionByName("美術");

    assertThatThrownBy(() -> sectionService.findSectionByName("美術"))
        .isInstanceOfSatisfying(SectionNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Section not found for name: 美術");
        });

    verify(sectionMapper, times(1)).findSectionByName("美術");
  }

  @Test
  public void セクションが正しく1件追加されること() {
    Section section = new Section(null, "照明");
    doNothing().when(sectionMapper).insertSection(section);

    Section actual = sectionService.insertSection("照明");

    assertThat(actual).isEqualTo(section);
    verify(sectionMapper, times(1)).insertSection(section);
  }

  @Test
  public void セクションが正しく1件更新されること() {
    Section existingSection = new Section(1, "大道具");
    Section updatedSection = new Section(1, "大道具・装飾");

    doReturn(Optional.of(existingSection)).when(sectionMapper).findSectionById(1);
    doNothing().when(sectionMapper).updateSection(updatedSection);

    Section actual = sectionService.updateSection(1, "大道具・装飾");

    assertThat(actual).isEqualTo(updatedSection);

    verify(sectionMapper, times(1)).findSectionById(1);
    verify(sectionMapper, times(1)).updateSection(updatedSection);
  }
}
