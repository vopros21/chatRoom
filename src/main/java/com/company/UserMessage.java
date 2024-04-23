package com.company;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @author Mike Kostenko on 23/04/2024
 */
@AllArgsConstructor
public class UserMessage {
    private UUID id;
    private String message;
    private ChatUser user;
    private Date time;
}
