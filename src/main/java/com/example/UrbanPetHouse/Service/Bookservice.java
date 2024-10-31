package com.example.UrbanPetHouse.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UrbanPetHouse.Entity.Book;
import com.example.UrbanPetHouse.Repository.BookRepo;

@Service
public class Bookservice {
    
    @Autowired
    BookRepo bookRepo;

    public void savedata(Book book){
        bookRepo.save(book);
    }

    public Optional<Book> getbyid(int id){
        return bookRepo.findById(id);
    }

    public Book findById(int id) {
        Optional<Book> bookOptional = bookRepo.findById(id);
        return bookOptional.orElse(null); 
    }
   
    public List<Book> getAllBooking(){
        return bookRepo.findAll();
    }

    public void deleteBookbyid(int id){
        Book book = bookRepo.findById(id).get();
        bookRepo.delete(book);
    }
}
