package com.neu.webapp.models;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "USER")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private UUID id;

    @Column(name = "EMAIL_ID", unique = true)
    private String emailId;

    @Column(name = "PASSWORD")
    private String password;
}