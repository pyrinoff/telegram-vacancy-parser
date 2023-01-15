package ru.pyrinoff.chatjobparser.model.parser;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ParserServiceResult extends Vacancy {

    private @Nullable String text;

    private @Nullable Set<String> textWords;

    public @NotNull String getText() {
        if (text == null) throw new ParsedTextEmpty();
        return text;
    }

    public @NotNull Set<String> getTextWords() {
        if (textWords == null) {
            if (text == null) throw new ParsedTextEmpty();
            return TextUtil.getSetOfWords(text);
        }
        return textWords;
    }

}
