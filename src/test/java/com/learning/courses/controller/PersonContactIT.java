package com.learning.courses.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.courses.AbstractIntegrationTest;
import com.learning.courses.dto.ContactDTO;
import com.learning.courses.dto.CreateContactDTO;
import com.learning.courses.dto.CreatePersonDTO;
import com.learning.courses.model.enums.Role;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PersonContactIT extends AbstractIntegrationTest {

  private Long createPersonViaApi(String identitySuffix) throws Exception {
    CreatePersonDTO dto = CreatePersonDTO.builder()
        .firstName("Jan")
        .lastName("Kowalski-" + identitySuffix)
        .identityNumber(identitySuffix)
        .role(Role.STUDENT)
        .build();
    HttpPost post = new HttpPost("/api/persons");
    initRequestWithBody(dto, post);
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), post);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      return retrieveResourceFromResponse(response, Long.class);
    }
  }

  private List<ContactDTO> readContactList(HttpResponse response) throws Exception {
    String json = EntityUtils.toString(response.getEntity());
    return objectMapper.readValue(json, new TypeReference<>() {});
  }

  @Test
  void contactCrud_happyPath() throws Exception {
    Long personId = createPersonViaApi("1111111111");

    CreateContactDTO create = CreateContactDTO.builder()
        .email("first@example.com")
        .address("ul. Test 1")
        .phone("+48111111111")
        .build();

    HttpPost postContact = new HttpPost("/api/persons/%d/contacts".formatted(personId));
    initRequestWithBody(create, postContact);

    ContactDTO created;
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), postContact);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      created = retrieveResourceFromResponse(response, ContactDTO.class);
    }

    assertThat(created.getId()).isNotNull();
    assertThat(created.getEmail()).isEqualTo("first@example.com");
    assertThat(created.getAddress()).isEqualTo("ul. Test 1");
    assertThat(created.getPhone()).isEqualTo("+48111111111");

    HttpGet listGet = new HttpGet("/api/persons/%d/contacts".formatted(personId));
    listGet.setHeader("Accept", "application/json");
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), listGet);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      List<ContactDTO> list = readContactList(response);
      assertThat(list).hasSize(1);
      assertThat(list.get(0).getId()).isEqualTo(created.getId());
    }

    HttpGet oneGet = new HttpGet("/api/persons/%d/contacts/%d".formatted(personId, created.getId()));
    oneGet.setHeader("Accept", "application/json");
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), oneGet);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      ContactDTO one = retrieveResourceFromResponse(response, ContactDTO.class);
      assertThat(one.getEmail()).isEqualTo("first@example.com");
    }

    CreateContactDTO update = CreateContactDTO.builder()
        .email("updated@example.com")
        .address("ul. Nowa 2")
        .phone("+48222222222")
        .build();
    HttpPut put = new HttpPut("/api/persons/%d/contacts/%d".formatted(personId, created.getId()));
    initRequestWithBody(update, put);
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), put);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      ContactDTO updated = retrieveResourceFromResponse(response, ContactDTO.class);
      assertThat(updated.getEmail()).isEqualTo("updated@example.com");
      assertThat(updated.getAddress()).isEqualTo("ul. Nowa 2");
      assertThat(updated.getPhone()).isEqualTo("+48222222222");
    }

    HttpDelete delete = new HttpDelete("/api/persons/%d/contacts/%d".formatted(personId, created.getId()));
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), delete);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    HttpGet listAfterDelete = new HttpGet("/api/persons/%d/contacts".formatted(personId));
    listAfterDelete.setHeader("Accept", "application/json");
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), listAfterDelete);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      assertThat(readContactList(response)).isEmpty();
    }
  }

  @Test
  void getContact_whenNotOwnedByPerson_returnsNotFound() throws Exception {
    Long personA = createPersonViaApi("2222222222");
    Long personB = createPersonViaApi("3333333333");

    CreateContactDTO create = CreateContactDTO.builder()
        .email("only-a@example.com")
        .address(null)
        .phone(null)
        .build();
    HttpPost postContact = new HttpPost("/api/persons/%d/contacts".formatted(personA));
    initRequestWithBody(create, postContact);

    ContactDTO created;
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), postContact);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
      created = retrieveResourceFromResponse(response, ContactDTO.class);
    }

    HttpGet wrongOwner = new HttpGet("/api/persons/%d/contacts/%d".formatted(personB, created.getId()));
    wrongOwner.setHeader("Accept", "application/json");
    try (var client = HttpClients.createDefault()) {
      var response = client.execute(getHttpHost(), wrongOwner);
      assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
  }
}
