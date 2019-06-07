package com.neu.webapp.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookAdditionStatus {
    private String titleError;
    private String authorError;
    private String isbnError;
    private String quantityError;

    public BookAdditionStatus() {
        titleError = "-";
        authorError = "-";
        isbnError = "-";
        quantityError = "-";
    }

    public BookAdditionStatus(String titleError, String authorError, String isbnError, String quantityError) {
        this.titleError = titleError;
        this.authorError = authorError;
        this.isbnError = isbnError;
        this.quantityError = quantityError;
    }
}
