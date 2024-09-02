package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"name", "birthday"})
public class User {
    private Long id;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Email задан неверно.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
