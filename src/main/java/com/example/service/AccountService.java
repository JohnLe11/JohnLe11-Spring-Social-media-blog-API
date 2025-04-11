package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountService contains business logic for account operations
 * such as registration and login. It performs validations before delegating to the repository.
 */
@Service
public class AccountService {

    // Inject the AccountRepository to perform database operations.
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Registers a new Account.
     * Validates that the username is not blank, the password has at least 4 characters,
     * and that the username is not already taken.
     * @param account the Account to be registered.
     * @return the saved Account with a generated accountId.
     */
    public Account register(Account account) {
        if(account.getUsername() == null || account.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        if(account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new IllegalArgumentException("Account with this username already exists.");
        }
        return accountRepository.save(account);
    }
    
    /**
     * Processes a login request.
     * Validates credentials and compares the provided password with the stored password.
     * @param account an Account object containing login credentials.
     * @return the matching Account if successful.
     */
    public Account login(Account account) {
        if(account.getUsername() == null || account.getUsername().trim().isEmpty() ||
           account.getPassword() == null){
            throw new IllegalArgumentException("Invalid credentials.");
        }
        Account existing = accountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        if(!existing.getPassword().equals(account.getPassword())){
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return existing;
    }
}
