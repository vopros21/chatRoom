package com.company.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Mike Kostenko on 23/04/2024
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatUser {
    private UUID id;
    private String name;
    private Instant lastVisit;
}
