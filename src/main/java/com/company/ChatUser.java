package com.company;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

/**
 * @author Mike Kostenko on 23/04/2024
 */
@AllArgsConstructor
public class ChatUser {
    private UUID id;
    @Getter
    private String name;
    private Date lastVisit;
}
