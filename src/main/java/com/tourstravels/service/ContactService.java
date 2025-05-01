package com.tourstravels.service;



import com.tourstravels.entity.Contact;
import com.tourstravels.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;

    public Contact saveMessage(Contact contact) {
        return contactRepository.save(contact);
    }
}
