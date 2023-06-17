package com.api.aggregator.services;

import com.api.aggregator.model.Contact;
import com.api.aggregator.repositories.ContactRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  private final ContactRepository contactRepository;

  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  public List<Contact> getContacts() {
    return contactRepository.getContacts();
  }
}
