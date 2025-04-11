package com.example.dto;

/**
 * UpdateMessageRequest is a Data Transfer Object (DTO) used to capture the input from a PATCH request.
 * It contains only the field necessary for updating a Message's text.
 */
public class UpdateMessageRequest {
    // The new message text that will replace the current one.
    private String messageText;

    // Default no-argument constructor required for JSON deserialization.
    public UpdateMessageRequest() {}

    /**
     * Getter for messageText.
     * @return the new message text.
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Setter for messageText.
     * @param messageText the new message text.
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
