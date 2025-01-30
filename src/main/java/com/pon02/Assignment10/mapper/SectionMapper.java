package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Section;
import com.pon02.Assignment10.validation.existsId.ExistChecker;
import com.pon02.Assignment10.validation.uniqueName.UniqueNameChecker;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SectionMapper extends ExistChecker, UniqueNameChecker {
    @Select("SELECT * FROM sections")
    List<Section> findAllSections();

    @Select("SELECT * FROM sections WHERE id = #{id}")
    Optional<Section> findSectionById(Integer id);

    @Select("SELECT * FROM sections WHERE LOWER(section_name) LIKE LOWER(CONCAT('%', #{sectionName}, '%'))")
    List<Section> findSectionByName(String sectionName);

    @Select("SELECT EXISTS(SELECT 1 FROM sections WHERE section_name = #{sectionName})")
    boolean existsByName(String sectionName);

    @Select("SELECT EXISTS(SELECT 1 FROM sections WHERE id = #{id})")
    boolean existsById(Integer id);

    @Insert("INSERT INTO sections (section_name) VALUES (#{sectionName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertSection(Section section);

    @Update("UPDATE sections " +
        "SET section_name = #{sectionName} " +
        "WHERE id = #{id}")
    void updateSection(Section section);

    @Delete("DELETE FROM sections WHERE id = #{id}")
    void deleteSection(Integer id);
}
