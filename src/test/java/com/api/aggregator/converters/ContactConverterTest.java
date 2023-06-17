package com.api.aggregator.converters;

import com.api.aggregator.dto.ContactDto;
import com.api.aggregator.dto.HerokuContactDto;
import com.api.aggregator.model.Contact;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContactConverterTest {

  @Test
  void testToModelConversion() {
    HerokuContactDto herokuDto = new HerokuContactDto(
        1,
        "jmadsen",
        "jmadsen@kenect.com",
        "2020-06-25T02:29:23.755Z",
        "2020-06-25T04:22:23Z"
    );
    OffsetDateTime expectedCreatedAt = OffsetDateTime.of(2020, 6, 25, 2, 29, 23, 755 * 1000000, ZoneOffset.UTC);
    OffsetDateTime expectedUpdatedAt = OffsetDateTime.of(2020, 6, 25, 4, 22, 23, 0, ZoneOffset.UTC);

    ContactConverter converter = new ContactConverter();
    Contact converted = converter.toModel(herokuDto, "TEST_SOURCE");
    Assertions.assertEquals(herokuDto.getId(), converted.getId());
    Assertions.assertEquals(herokuDto.getName(), converted.getName());
    Assertions.assertEquals(herokuDto.getEmail(), converted.getEmail());
    Assertions.assertEquals("TEST_SOURCE", converted.getSource());
    Assertions.assertEquals(expectedCreatedAt, converted.getCreatedAt());
    Assertions.assertEquals(expectedUpdatedAt, converted.getUpdatedAt());


  }

  @Test
  void testToDtoConversion() {
    Contact contact = new Contact(
        1,
        "jmadsen",
        "jmadsen@kenect.com",
        "TEST_SOURCE",
        OffsetDateTime.of(2020, 6, 25, 2, 29, 23, 755 * 1000000, ZoneOffset.UTC),
        OffsetDateTime.of(2020, 6, 25, 4, 22, 23, 0, ZoneOffset.UTC)
    );

    ContactConverter converter = new ContactConverter();
    ContactDto converted = converter.toDto(contact);

    Assertions.assertEquals(contact.getId(), converted.getId());
    Assertions.assertEquals(contact.getName(), converted.getName());
    Assertions.assertEquals(contact.getEmail(), converted.getEmail());
    Assertions.assertEquals(contact.getUpdatedAt(), converted.getUpdatedAt());
    Assertions.assertEquals(contact.getCreatedAt(), converted.getCreatedAt());
    Assertions.assertEquals(contact.getSource(), converted.getSource());

  }
}
