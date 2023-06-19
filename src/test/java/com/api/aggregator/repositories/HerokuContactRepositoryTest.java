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
  void testGettingOnePageListShouldReturnFullList() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Current-Page", "1"),
        Map.entry("Page-Items", "20"),
        Map.entry("Total-Pages", "1"),
        Map.entry("Total-Count", "18")
    );

    List<HerokuContactDto> pageOneContacts = generateRandomContactsList(18);

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(18, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(1)).getContacts(Mockito.anyInt());
  }

  @Test
  void testGettingTwoPagesListShouldReturnFullList() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Current-Page", "1"),
        Map.entry("Page-Items", "20"),
        Map.entry("Total-Pages", "2"),
        Map.entry("Total-Count", "32")
    );

    List<HerokuContactDto> pageOneContacts = generateRandomContactsList(20);

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<HerokuContactDto> pageTwoContacts = generateRandomContactsList(12);

    Mockito.when(herokuClientMock.getContacts(2)).thenReturn(buildResponse(pageValues, pageTwoContacts));


    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(32, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(2)).getContacts(Mockito.anyInt());
  }

  @Test
  void testResponseWithWrongPaginationInfoShouldCallClientAsInfoSuggests() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Current-Page", "1"),
        Map.entry("Page-Items", "20"),
        Map.entry("Total-Pages", "1"),
        Map.entry("Total-Count", "28")
    );

    List<HerokuContactDto> pageOneContacts = generateRandomContactsList(20);

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<HerokuContactDto> pageTwoContacts = generateRandomContactsList(7);

    Mockito.when(herokuClientMock.getContacts(2)).thenReturn(buildResponse(pageValues, pageTwoContacts));


    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(20, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(1)).getContacts(Mockito.anyInt());
  }

  @Test
  void testResponseWithoutPaginationInfoShouldNotKeepCallingTheClient() {
    Map<String, String> pageValues = Map.ofEntries();

    List<HerokuContactDto> pageOneContacts = generateRandomContactsList(3);

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    Assertions.assertThrows(RuntimeException.class, () -> sut.getContacts());

    Mockito.verify(herokuClientMock, Mockito.times(1)).getContacts(Mockito.anyInt());
  }

  @Test
  void testResponseWithoutContactsShouldCallClientOnce() {
    Map<String, String> pageValues = Map.ofEntries(
        Map.entry("Current-Page", "1"),
        Map.entry("Page-Items", "20"),
        Map.entry("Total-Pages", "1"),
        Map.entry("Total-Count", "0")
    );

    List<HerokuContactDto> pageOneContacts = new ArrayList<>();

    Mockito.when(herokuClientMock.getContacts(1)).thenReturn(buildResponse(pageValues, pageOneContacts));

    List<Contact> contacts = sut.getContacts();

    Assertions.assertEquals(0, contacts.size());
    Mockito.verify(herokuClientMock, Mockito.times(1)).getContacts(Mockito.anyInt());
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

  private List<HerokuContactDto> generateRandomContactsList(Integer numberOfContacts) {
    List<HerokuContactDto> list = new ArrayList<>();
    for(int i = 0; i< numberOfContacts; i++) {
      list.add(generateRandomContact());
    }
    return list;
  }
}
