package com.api.aggregator.repositories;

import com.api.aggregator.client.HerokuClient;
import com.api.aggregator.dto.HerokuContactDto;
import com.api.aggregator.dto.HerokuContactListDto;
import com.api.aggregator.model.Contact;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class HerokuContactRepositoryTest {

  private final HerokuClient herokuClientMock = Mockito.mock(HerokuClient.class);
  private HerokuContactRepository sut = new HerokuContactRepository(herokuClientMock);

  @Test
  void testGettingOnePageList() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Current-Page", "1"),
        Map.entry("Page-Items", "4"),
        Map.entry("Total-Pages", "1"),
        Map.entry("Total-Count", "2")
    );

    List<HerokuContactDto> pageOneContacts = new ArrayList<>();
    pageOneContacts.add(generateRandomContact());
    pageOneContacts.add(generateRandomContact());

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(2, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(1)).getContacts(Mockito.anyInt());
  }

  @Test
  void testGettingTwoPagesList() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Page-Items", "4"),
        Map.entry("Total-Pages", "2"),
        Map.entry("Total-Count", "7")
    );

    List<HerokuContactDto> pageOneContacts = new ArrayList<>();
    pageOneContacts.add(generateRandomContact());
    pageOneContacts.add(generateRandomContact());
    pageOneContacts.add(generateRandomContact());
    pageOneContacts.add(generateRandomContact());

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<HerokuContactDto> pageTwoContacts = new ArrayList<>();
    pageTwoContacts.add(generateRandomContact());
    pageTwoContacts.add(generateRandomContact());
    pageTwoContacts.add(generateRandomContact());

    Mockito.when(herokuClientMock.getContacts(2)).thenReturn(buildResponse(pageValues, pageTwoContacts));


    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(7, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(2)).getContacts(Mockito.anyInt());
  }

  private ResponseEntity<HerokuContactListDto> buildResponse(Map<String, String> headers, List<HerokuContactDto> contacts) {
    HttpHeaders mockHeaders = new HttpHeaders();
    headers.forEach(mockHeaders::add);

    HerokuContactListDto mockBody = new HerokuContactListDto(contacts);
    return new ResponseEntity<>(mockBody, mockHeaders, HttpStatus.OK);
  }

  private HerokuContactDto generateRandomContact() {
    return new HerokuContactDto(
        (int) (Math.random()*1000),
        RandomString.make(),
        RandomString.make(),
        OffsetDateTime.now().toString(),
        OffsetDateTime.now().toString()
    );
  }
}
