package com.company.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Mike Kostenko on 23/04/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    private UUID id;
    private String message;
    private ChatUser user;
    private Instant time;
}
