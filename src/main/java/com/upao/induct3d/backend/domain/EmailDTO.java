package com.upao.induct3d.backend.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDTO {

    private String[] toUser;
    private String subject;
    private String message;

    public String[] getToUser() {return toUser;}
    public String getSubject() {return subject;}
    public String getMessage() {return message;}
}