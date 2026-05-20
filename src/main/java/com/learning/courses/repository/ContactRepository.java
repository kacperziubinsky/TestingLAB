package com.learning.courses.repository;

import com.learning.courses.model.Contact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

  List<Contact> findByStudent_Id(Long studentId);

  Optional<Contact> findByIdAndStudent_Id(Long id, Long studentId);
}
