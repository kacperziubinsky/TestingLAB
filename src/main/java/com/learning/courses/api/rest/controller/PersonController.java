package com.learning.courses.api.rest.controller;

import com.learning.courses.dto.ContactDTO;
import com.learning.courses.dto.CreateContactDTO;
import com.learning.courses.dto.CreatePersonDTO;
import com.learning.courses.dto.DegreeDTO;
import com.learning.courses.dto.PersonDTO;
import com.learning.courses.service.PersonCourseService;
import com.learning.courses.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "PersonController", description = "Person API")
@RequestMapping("/api/persons")
class PersonController {

  private final PersonService personService;
  private final PersonCourseService personCourseService;

  @PostMapping
  @Operation(summary = "Create person")
  public Long createPerson(@Valid @RequestBody CreatePersonDTO createPersonDTO) {
    return personService.createPerson(createPersonDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get person")
  public PersonDTO getPerson(@PathVariable Long id) {
    return personService.getPerson(id);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update person")
  public PersonDTO updatePerson(@PathVariable Long id, @RequestBody PersonDTO updatedPerson) {
    return personService.updatePerson(id, updatedPerson);
  }

  @PatchMapping("/grade")
  @Operation(summary = "Grade a student")
  public void gradeStudent(@RequestBody @Valid DegreeDTO degreeDTO) {
    personCourseService.gradeStudent(degreeDTO);
  }

  @GetMapping("/{personId}/contacts")
  @Operation(summary = "List contacts for person")
  public List<ContactDTO> getContactsForPerson(@PathVariable Long personId) {
    return personService.getContactsForPerson(personId);
  }

  @GetMapping("/{personId}/contacts/{contactId}")
  @Operation(summary = "Get contact for person")
  public ContactDTO getContactForPerson(
      @PathVariable Long personId, @PathVariable Long contactId) {
    return personService.getContactForPerson(personId, contactId);
  }

  @PostMapping("/{personId}/contacts")
  @Operation(summary = "Add contact for person")
  public ContactDTO addContactForPerson(
      @PathVariable Long personId,
      @Valid @RequestBody CreateContactDTO createContactDTO) {
    return personService.addContactForPerson(personId, createContactDTO);
  }

  @PutMapping("/{personId}/contacts/{contactId}")
  @Operation(summary = "Update contact for person")
  public ContactDTO updateContactForPerson(
      @PathVariable Long personId,
      @PathVariable Long contactId,
      @Valid @RequestBody CreateContactDTO createContactDTO) {
    return personService.updateContactForPerson(personId, contactId, createContactDTO);
  }

  @DeleteMapping("/{personId}/contacts/{contactId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete contact for person")
  public void deleteContactForPerson(@PathVariable Long personId, @PathVariable Long contactId) {
    personService.deleteContactForPerson(personId, contactId);
  }

}
