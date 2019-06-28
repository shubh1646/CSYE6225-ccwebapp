package com.neu.webapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Cover {
    @Id
    @GeneratedValue
    @Column(name = "cover_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String url;

    public Cover() {
    }

    public Cover(String url) {
        this.url = url;
    }

    public Cover(UUID id, String url) {
        this.id = id;
        this.url = url;
    }
}
