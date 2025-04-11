package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * AccountRepository is a Spring Data JPA repository for Account entities.
 * It provides built-in CRUD operations and a custom method to find an account by username.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Method to find an Account by its username.
    Optional<Account> findByUsername(String username);
}
