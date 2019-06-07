package com.neu.webapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String emailId;

    private String password;

    public User() {}

    public User(UUID id, String emailId, String password) {
        this.id = id;
        this.emailId = emailId;
        this.password = password;
    }
}