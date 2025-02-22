package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User createUser(User user) {

        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }

        boolean emailExists = users.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));

        if (emailExists) {
            throw new DuplicatedDataException("Этот email уже используется");
        }

        user.setId(getNextUserId());
        user.setRegistrationDate(LocalDateTime.now());
        users.put(user.getId(), user);

        return user;
    }

    public User updateUser(User newUser) {

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


    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

}
