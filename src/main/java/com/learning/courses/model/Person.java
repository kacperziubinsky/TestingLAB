package com.learning.courses.model;

import com.learning.courses.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
  @SequenceGenerator(name = "person_id_seq", sequenceName = "person_id_seq", allocationSize = 1)
  private Long id;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String identityNumber;

  @ManyToMany
  @JoinTable(name = "person_course",
      joinColumns = @JoinColumn(name = "person_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private List<Course> assignedCourses;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "tutor")
  private List<Course> tutoringCourses;

  @ManyToMany
  @JoinTable(name = "person_contact",
  joinColumns = @JoinColumn(name = "person_id"),
  inverseJoinColumns = @JoinColumn(name = "contact_id") )
  private List<Contact> studentContact;
}
