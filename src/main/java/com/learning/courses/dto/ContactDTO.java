package com.learning.courses.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Valid
public class ContactDTO {

  private Long id;
  private String email;
  private String address;
  private String phone;
}
