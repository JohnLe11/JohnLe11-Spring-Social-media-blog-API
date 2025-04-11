package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MessageRepository is a Spring Data JPA repository for Message entities.
 * It provides built-in CRUD operations and a custom method to retrieve all messages posted by a specific account.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Method to find messages based on the postedBy field.
    List<Message> findByPostedBy(Integer postedBy);
}
