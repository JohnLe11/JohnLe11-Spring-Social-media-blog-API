package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.dto.UpdateMessageRequest;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

/**
 * This REST controller handles all HTTP endpoints for the Social Media API.
 * It provides endpoints for user registration, login, and all CRUD operations on messages.
 * The controller delegates business logic to its service classes.
 */
@RestController
public class SocialMediaController {

    // Inject the AccountService bean. This service contains business logic for account operations.
    @Autowired
    private AccountService accountService;

    // Inject the MessageService bean. This service contains business logic for message operations.
    @Autowired
    private MessageService messageService;

    /**
     * Endpoint: POST /register
     * Description: Registers a new user account.
     * Validates that the username is non-blank, the password is at least 4 characters,
     * and that no duplicate username exists.
     * On success, returns the created Account with a generated accountId.
     * If a duplicate username is detected, returns HTTP 409.
     * Any other validation error returns HTTP 400.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account created = accountService.register(account);
            return new ResponseEntity<>(created, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Check if the error message indicates a duplicate username.
            if(e.getMessage().contains("already exists")) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // For any unexpected errors, return 400 (client error in our spec).
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: POST /login
     * Description: Authenticates a user with provided credentials.
     * Expects a JSON Account (without accountId). If credentials match,
     * returns the Account entity; otherwise returns HTTP 401.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account loggedIn = accountService.login(account);
            return new ResponseEntity<>(loggedIn, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: POST /messages
     * Description: Creates a new message.
     * Validates that messageText is non-blank, under 255 characters,
     * and that the postedBy field references an existing Account.
     * On success, returns the saved Message entity.
     */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message created = messageService.createMessage(message);
            return new ResponseEntity<>(created, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: GET /messages
     * Description: Retrieves all messages in the system.
     * Always returns an HTTP 200 status with a JSON list (which may be empty).
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        try {
            List<Message> messages = messageService.getAllMessages();
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: GET /messages/{messageId}
     * Description: Retrieves a specific message by its ID.
     * If the message does not exist, it returns an empty body with HTTP 200.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        try {
            Message message = messageService.getMessageById(messageId);
            if(message == null)
                return new ResponseEntity<>(null, HttpStatus.OK);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: DELETE /messages/{messageId}
     * Description: Deletes a message with the given ID.
     * If the message is found and deleted, returns the number of rows updated (1).
     * If not found, returns an empty body with HTTP 200.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        try {
            int rowsUpdated = messageService.deleteMessage(messageId);
            if(rowsUpdated == 0) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: PATCH /messages/{messageId}
     * Description: Updates the text of an existing message.
     * The request body should contain a JSON with the new "messageText" value.
     * On success, returns the number of rows updated (1). If the update fails for any reason,
     * returns HTTP 400.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId,
                                           @RequestBody UpdateMessageRequest updateRequest) {
        try {
            int rowsUpdated = messageService.updateMessage(messageId, updateRequest.getMessageText());
            return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: GET /accounts/{accountId}/messages
     * Description: Retrieves all messages posted by a specific user.
     * Always returns an HTTP 200 status with a JSON list (empty if no messages).
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable Integer accountId) {
        try {
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}