package com.gensis.controller;

import com.gensis.model.Contact;
import com.gensis.repository.ContactRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    @Operation(summary = "Retrieve all Contacts", tags = "Contact")
    public List<Contact> getAllContact() {
        return contactRepository.findAll();
    }
    @Operation(summary = "Find a Contact by Id", tags = "Contact")
    @GetMapping("{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new Contact", tags = "Contact")
    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody Contact contact) {
        if (isContactFreelancerWithoutVAT(contact)) {
            return new ResponseEntity<>("VAT is mandatory for freelancer contact", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.OK);
        }
    }

    @Operation(summary = "Update a Contact", tags = "Contact")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        return contactRepository.findById(id)
                .map(contact -> {
                    contact.setFirstName(updatedContact.getFirstName());
                    contact.setLastName(updatedContact.getLastName());
                    contact.setAddress(updatedContact.getAddress());
                    contact.setEmployeeType(updatedContact.getEmployeeType());
                    contact.setVat(updatedContact.getVat());

                    if (isContactFreelancerWithoutVAT(contact)) {
                        return new ResponseEntity<>("VAT is mandatory for freelancer contact", HttpStatus.BAD_REQUEST);
                    } else {
                        return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.OK);
                    }
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a Contact by Id", tags = "Contact")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean isContactFreelancerWithoutVAT(Contact contact) {
        return contact.getEmployeeType().equals("FREELANCER") && contact.getVat().isBlank();
    }

}
