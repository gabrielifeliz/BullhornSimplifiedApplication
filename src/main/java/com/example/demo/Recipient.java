package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String recipientName;

    @ManyToMany(mappedBy = "recipients")
    private Set<Message> messages;

    public Recipient() {
        messages = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

}