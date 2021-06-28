package com.projectx.ProjectX.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericError {

    private Date timestamp;
    private String message;
    private String cause;

    public GenericError(String message, String cause) {
        this.timestamp = new Date();
        this.message = message;
        this.cause = cause;
    }
}
