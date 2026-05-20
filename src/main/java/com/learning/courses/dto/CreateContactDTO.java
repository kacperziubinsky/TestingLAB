package com.learning.courses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactDTO implements Serializable {

  @NotBlank
  @Email
  @Schema(example = "jan.kowalski@example.com")
  private String email;

  @Schema(example = "ul. Lipowa 10, 00-001 Warszawa")
  private String address;

  @Schema(example = "+48111222333")
  private String phone;
}
