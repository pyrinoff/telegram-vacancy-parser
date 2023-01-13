package ru.pyrinoff.chatjobparser.exception.service.parser;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import ru.pyrinoff.chatjobparser.exception.AbstractException;

public class VacancyNotCorrect extends AbstractException {

    public VacancyNotCorrect(@NotNull final String message) {
        super(message);
    }

}
