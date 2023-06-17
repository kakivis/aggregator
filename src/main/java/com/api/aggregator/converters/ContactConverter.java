package com.api.aggregator.converters;

import com.api.aggregator.dto.ContactDto;
import com.api.aggregator.dto.HerokuContactDto;
import com.api.aggregator.model.Contact;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ContactConverter {

  public Contact toModel(HerokuContactDto dto, String source) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    return new Contact(
        dto.getId(),
        dto.getName(),
        dto.getEmail(),
        source,
        OffsetDateTime.parse(dto.getCreated_at(), formatter),
        OffsetDateTime.parse(dto.getUpdated_at(), formatter)
    );
  }

  public ContactDto toDto(Contact model) {
    return new ContactDto(
        model.getId(),
        model.getName(),
        model.getEmail(),
        model.getSource(),
        model.getCreatedAt(),
        model.getUpdatedAt()
    );
  }
}
