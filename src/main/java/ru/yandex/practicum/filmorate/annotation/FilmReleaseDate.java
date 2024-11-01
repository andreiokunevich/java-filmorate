package ru.yandex.practicum.filmorate.annotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FilmReleaseValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface FilmReleaseDate {
    String message() default "Дата должна быть после 1895-12-28";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}