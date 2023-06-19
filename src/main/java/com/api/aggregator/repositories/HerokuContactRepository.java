package com.api.aggregator.repositories;

import com.api.aggregator.client.HerokuClient;
import com.api.aggregator.converters.ContactConverter;
import com.api.aggregator.dto.HerokuContactDto;
import com.api.aggregator.dto.HerokuContactListDto;
import com.api.aggregator.model.Contact;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class HerokuContactRepository implements ContactRepository {

  private final HerokuClient client;
  private static final String SOURCE_NAME = "KENECT_LABS";
  public static final String TOTAL_PAGES_KEY = "Total-Pages";

  public HerokuContactRepository(HerokuClient client) {
    this.client = client;
  }

  public List<Contact> getContacts() {
    ResponseEntity<HerokuContactListDto> response = client.getContacts(1);

    validateResponse(response);

    List<Contact> list = new ArrayList<>();
    extractContactsFromResponse(list, response);

    Integer totalPages = getPageNumberFromHeader(response);

    if (totalPages < 2) return list;

    for (int i = 2; i <= totalPages; i++) {
      response = client.getContacts(i);

      validateResponse(response);

      extractContactsFromResponse(list, response);
    }

    return list;
  }

  private Integer getPageNumberFromHeader(ResponseEntity<HerokuContactListDto> response) {
    String totalPagesValue = response.getHeaders().getFirst(TOTAL_PAGES_KEY);

    if (totalPagesValue == null) {
      return null;
    }

    return Integer.parseInt(totalPagesValue);
  }
  
  private void validateResponse(ResponseEntity<HerokuContactListDto> response) {
    if (response.getBody() == null || getPageNumberFromHeader(response) == null) {
      throw new RuntimeException("Could not fetch contacts info.");
    }
  }

  private void extractContactsFromResponse(List<Contact> list, ResponseEntity<HerokuContactListDto> response) {
    ContactConverter converter = new ContactConverter();
    List<HerokuContactDto> contactResponseList = response.getBody().getContacts();
    list.addAll(contactResponseList.stream().map(c -> converter.toModel(c, SOURCE_NAME)).toList());
  }

}
