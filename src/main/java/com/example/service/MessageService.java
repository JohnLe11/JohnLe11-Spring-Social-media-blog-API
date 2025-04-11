package com.example.service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * MessageService contains business logic for handling operations on Message entities.
 * It validates message inputs and ensures that operations such as create, update, delete, and retrieval
 * are executed properly.
 */
@Service
public class MessageService {

    // Inject the MessageRepository for CRUD operations on messages.
    @Autowired
    private MessageRepository messageRepository;

    // Inject the AccountRepository to verify that the account exists for a given postedBy.
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a new Message.
     * Validates that the message text is non-blank, within 255 characters, and that the postedBy account exists.
     * @param message the Message to be created.
     * @return the saved Message.
     */
    public Message createMessage(Message message) {
        if(message.getMessageText() == null || message.getMessageText().trim().isEmpty()){
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if(message.getMessageText().length() > 255){
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());
        if(!accountOptional.isPresent()){
            throw new IllegalArgumentException("PostedBy account does not exist.");
        }
        return messageRepository.save(message);
    }

    /**
     * Retrieves all messages.
     * @return a List of all Message entities.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId the ID of the Message.
     * @return the Message if found, or null otherwise.
     */
    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    /**
     * Deletes a Message by its ID.
     * If the message exists, deletes it and returns 1 (one row updated).
     * If the message does not exist, returns 0.
     * @param messageId the ID of the Message to delete.
     * @return the number of rows updated.
     */
    public int deleteMessage(Integer messageId) {
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    /**
     * Updates the message text for a given Message.
     * Validates that the new message text is non-blank and within 255 characters.
     * @param messageId the ID of the Message to update.
     * @param newMessageText the new message text.
     * @return 1 if the update was successful.
     */
    public int updateMessage(Integer messageId, String newMessageText) {
        if(newMessageText == null || newMessageText.trim().isEmpty()){
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if(newMessageText.length() > 255){
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1;
        } else {
            throw new IllegalArgumentException("Message does not exist.");
        }
    }

    /**
     * Retrieves all messages for a particular account.
     * @param accountId the ID of the account.
     * @return a List of Message entities posted by the account.
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
