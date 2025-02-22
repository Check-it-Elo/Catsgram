package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    Long id;
    String username;
    String email;
    String password;
    LocalDateTime registrationDate;
}
