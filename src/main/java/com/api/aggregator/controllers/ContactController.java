package com.api.aggregator.controllers;

import com.api.aggregator.converters.ContactConverter;
import com.api.aggregator.dto.ContactDto;
import com.api.aggregator.model.Contact;
import com.api.aggregator.services.ContactService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactController {

	private final ContactService contactService;

	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@GetMapping()
	public List<ContactDto> getContacts() {
		List<Contact> list = contactService.getContacts();
		ContactConverter converter = new ContactConverter();
		return list.stream().map(converter::toDto).toList();
	}

}
