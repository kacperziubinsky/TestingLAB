package com.learning.courses.service;

import com.learning.courses.dto.ContactDTO;
import com.learning.courses.dto.CreateContactDTO;
import com.learning.courses.dto.CreatePersonDTO;
import com.learning.courses.dto.PersonDTO;
import com.learning.courses.exception.EntityNotFoundException;
import com.learning.courses.mapper.ContactMapper;
import com.learning.courses.mapper.PersonMapper;
import com.learning.courses.model.Contact;
import com.learning.courses.model.Person;
import com.learning.courses.repository.ContactRepository;
import com.learning.courses.repository.PersonRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;
  private final ContactRepository contactRepository;
  private final PersonMapper personMapper;
  private final ContactMapper contactMapper;

  @Transactional
  public Long createPerson(CreatePersonDTO createPersonDTO) {
    final Person person = personMapper.toEntity(createPersonDTO);

    return personRepository.save(person).getId();
  }

  @Transactional(readOnly = true)
  public PersonDTO getPerson(@NotNull @Positive Long id) {
    return personRepository.findById(id)
        .map(personMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException(id, Person.class.getSimpleName()));
  }

  @Transactional(readOnly = true)
  public Person getPersonEntity(@NotNull @Positive Long id) {
    return personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, Person.class.getSimpleName()));
  }

  @Transactional
  public PersonDTO updatePerson(Long id, PersonDTO updatedPerson) {
    var person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Person.class.getSimpleName()));
    person.setRole(updatedPerson.getRole());
    person.setFirstName(updatedPerson.getFirstName());
    person.setLastName(updatedPerson.getLastName());
    person.setIdentityNumber(updatedPerson.getIdentityNumber());
    person = personRepository.save(person);
    return personMapper.toDTO(person);
  }

  @Transactional
  public ContactDTO addContactForPerson(@NotNull @Positive Long personId, @Valid CreateContactDTO createContactDTO) {
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new EntityNotFoundException(personId, Person.class.getSimpleName()));

    Contact contact = Contact.builder()
        .email(createContactDTO.getEmail())
        .address(createContactDTO.getAddress())
        .phone(createContactDTO.getPhone())
        .student(person)
        .build();
    contact = contactRepository.save(contact);

    if (person.getStudentContact() == null) {
      person.setStudentContact(new ArrayList<>());
    }
    person.getStudentContact().add(contact);
    personRepository.save(person);

    return contactMapper.toDTO(contact);
  }

  @Transactional(readOnly = true)
  public List<ContactDTO> getContactsForPerson(@NotNull @Positive Long personId) {
    if (!personRepository.existsById(personId)) {
      throw new EntityNotFoundException(personId, Person.class.getSimpleName());
    }
    return contactMapper.toDTO(contactRepository.findByStudent_Id(personId));
  }

  @Transactional(readOnly = true)
  public ContactDTO getContactForPerson(@NotNull @Positive Long personId, @NotNull @Positive Long contactId) {
    if (!personRepository.existsById(personId)) {
      throw new EntityNotFoundException(personId, Person.class.getSimpleName());
    }
    return contactRepository.findByIdAndStudent_Id(contactId, personId)
        .map(contactMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException(contactId, Contact.class.getSimpleName()));
  }

  @Transactional
  public ContactDTO updateContactForPerson(
      @NotNull @Positive Long personId,
      @NotNull @Positive Long contactId,
      @Valid CreateContactDTO createContactDTO) {
    Contact contact = contactRepository.findByIdAndStudent_Id(contactId, personId)
        .orElseThrow(() -> new EntityNotFoundException(contactId, Contact.class.getSimpleName()));
    contact.setEmail(createContactDTO.getEmail());
    contact.setAddress(createContactDTO.getAddress());
    contact.setPhone(createContactDTO.getPhone());
    return contactMapper.toDTO(contactRepository.save(contact));
  }

  @Transactional
  public void deleteContactForPerson(@NotNull @Positive Long personId, @NotNull @Positive Long contactId) {
    Contact contact = contactRepository.findByIdAndStudent_Id(contactId, personId)
        .orElseThrow(() -> new EntityNotFoundException(contactId, Contact.class.getSimpleName()));
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new EntityNotFoundException(personId, Person.class.getSimpleName()));
    if (person.getStudentContact() != null) {
      person.getStudentContact().remove(contact);
      personRepository.save(person);
    }
    contactRepository.delete(contact);
  }

}
