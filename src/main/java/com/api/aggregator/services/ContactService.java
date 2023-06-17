package com.api.aggregator.services;

import com.api.aggregator.model.Contact;
import com.api.aggregator.repositories.HerokuContactRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  private final HerokuContactRepository herokuContactRepository;

  public ContactService(HerokuContactRepository herokuContactRepository) {
    this.herokuContactRepository = herokuContactRepository;
  }

  public List<Contact> getContacts() {
    return herokuContactRepository.getContacts();
  }
}
