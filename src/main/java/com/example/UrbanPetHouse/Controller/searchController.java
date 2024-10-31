package com.example.UrbanPetHouse.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.UrbanPetHouse.Entity.Book;
import com.example.UrbanPetHouse.Repository.BookRepo;
import com.example.UrbanPetHouse.Service.Bookservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class searchController {

    @Autowired
    private BookRepo bookRepo;

    

    @GetMapping("/search/{query}")
    public ResponseEntity<?> getsearch(@PathVariable("query") String query) {
        List<Book> book = bookRepo.findByNameContaining(query);
        return ResponseEntity.ok(book);
    }
    
    
}
