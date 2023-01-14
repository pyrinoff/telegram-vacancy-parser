package ru.pyrinoff.chatjobparser.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vacancy", schema = "public")
public class Vacancy extends AbstractDTOModel {

    private static final long serialVersionUID = 1;

    @Column
    private @NotNull Date date;

    @Column
    private @Nullable Integer salaryFrom;

    @Column
    private @Nullable Integer salaryTo;

    @Column
    @Enumerated(EnumType.STRING)
    private @Nullable CurrencyEnum currency;

    @Column
    private @NotNull Boolean withPrediction;

    @ElementCollection
    @CollectionTable(name = "vacancy_markers")
    @Column
    private @NotNull Set<String> markers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "vacancy_words")
    @Column
    private @NotNull Set<String> words = new HashSet<>();

}
