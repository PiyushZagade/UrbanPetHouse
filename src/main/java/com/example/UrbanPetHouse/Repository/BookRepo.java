package com.example.UrbanPetHouse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UrbanPetHouse.Entity.Book;

@Repository
public interface BookRepo  extends JpaRepository<Book,Integer>{
    public List<Book> findByNameContaining(String name);
} 
