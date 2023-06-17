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
  private static final String SOURCE = "KENECT_LABS";
  public static final String TOTAL_PAGES = "Total-Pages";

  public HerokuContactRepository(HerokuClient client) {
    this.client = client;
  }

  public List<Contact> getContacts() {
    List<Contact> list = new ArrayList<>();
    ContactConverter converter = new ContactConverter();

    ResponseEntity<HerokuContactListDto> response = client.getContacts(1);

    if (response.getBody() == null) return list;

    List<HerokuContactDto> contactResponseList = response.getBody().getContacts();
    list.addAll(contactResponseList.stream().map(c -> converter.toModel(c, SOURCE)).toList());

    Integer totalPages = getIntFromHeader(response, TOTAL_PAGES);

    if (totalPages == null || totalPages < 2) return list;

    for (int i = 2; i <= totalPages; i++) {
      response = client.getContacts(i);

      if (response.getBody() == null) return list;

      contactResponseList = response.getBody().getContacts();
      list.addAll(contactResponseList.stream().map(c -> converter.toModel(c, SOURCE)).toList());
    }

    return list;
  }

  private Integer getIntFromHeader(ResponseEntity<HerokuContactListDto> response, String headerKey) {
    String totalPagesValue = response.getHeaders().getFirst(headerKey);

    if (totalPagesValue == null) {
      return null;
    }

    return Integer.parseInt(totalPagesValue);
  }
}
