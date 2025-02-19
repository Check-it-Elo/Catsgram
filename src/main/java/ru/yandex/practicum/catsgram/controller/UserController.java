package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();


    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }


    @PostMapping
    public User createUser(@RequestBody User user) {

        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }

        User existingUser = users.get(user.getId());
        if (existingUser != null && existingUser.getEmail().equals(user.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }

        user.setId(getNextUserId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);

        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User newUser) {

        if (newUser.getId() == null) {
            throw new ConditionsNotMetException ("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            boolean emailAlreadyUsed = false;

            for (User u : users.values()) {
                if (u != null && u.getEmail().equals(newUser.getEmail()) && !u.getId().equals(newUser.getId())) {
                    emailAlreadyUsed = true;
                    break;
                }
            }
            if (emailAlreadyUsed) {
                throw new DuplicatedDataException("Этот email уже используется");
            }
        }
        User oldUser = users.get(newUser.getId());

        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getUsername()!= null) {
            oldUser.setUsername(newUser.getUsername());
        }

        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }

        return oldUser;
    }





    //Метод для генерации id нового пользователя
    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }



}
