package com.tourstravels.controller;



import com.tourstravels.entity.Contact;
import com.tourstravels.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tours_travel/contacts")
@CrossOrigin(origins = "*") 
public class ContactController {

    @Autowired
    private ContactService contactService;

    
    @PostMapping
    public Contact submitContactForm(@RequestBody Contact contact) {
        System.out.println("Received contact form submission: " + contact);
        return contactService.saveMessage(contact);
    }

}
