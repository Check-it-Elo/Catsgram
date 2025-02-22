package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    Long id; //уникальный идентификатор сообщения
    long authorId; //пользователь, который создал сообщение
    String description; //текстовое описание сообщения
    LocalDateTime postDate; //дата и время создания сообщения
}
