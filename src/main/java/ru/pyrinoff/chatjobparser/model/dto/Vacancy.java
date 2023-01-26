package ru.pyrinoff.chatjobparser.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vacancy", schema = "public")
public class Vacancy {

    protected static final long serialVersionUID = 1;

    @Id
    private @NotNull Integer messageId;

    @Column
    protected @NotNull Date date;

    @Column
    protected @Nullable Integer salaryFrom;

    @Column
    protected @Nullable Integer salaryTo;

    @Column
    @Enumerated(EnumType.STRING)
    protected @Nullable CurrencyEnum currency;

    @Column
    protected @NotNull Boolean withPrediction;

    @ElementCollection
    @CollectionTable(name = "vacancy_markers")
    @Column
    protected @NotNull Set<String> markers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "vacancy_words")
    @Column
    protected @NotNull Set<String> words = new HashSet<>();

}
