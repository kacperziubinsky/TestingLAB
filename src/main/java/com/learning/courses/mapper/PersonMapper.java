package com.learning.courses.mapper;

import com.learning.courses.dto.CreatePersonDTO;
import com.learning.courses.dto.PersonDTO;
import com.learning.courses.model.Person;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PersonMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "assignedCourses", ignore = true)
  @Mapping(target = "tutoringCourses", ignore = true)
  @Mapping(target = "studentContact", ignore = true)
  Person toEntity(CreatePersonDTO createPersonDTO);

  PersonDTO toDTO(Person person);

  List<PersonDTO> toDTO(List<Person> personList);
}
