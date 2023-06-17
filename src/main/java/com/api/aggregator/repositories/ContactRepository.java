package com.api.aggregator.repositories;

import com.api.aggregator.model.Contact;
import java.util.List;

public interface ContactRepository {
  List<Contact> getContacts();
}
