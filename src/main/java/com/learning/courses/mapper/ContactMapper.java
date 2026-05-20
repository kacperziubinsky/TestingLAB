package com.learning.courses.mapper;

import com.learning.courses.dto.ContactDTO;
import com.learning.courses.model.Contact;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ContactMapper {

  ContactDTO toDTO(Contact contact);

  List<ContactDTO> toDTO(List<Contact> contacts);
}
