package ru.pyrinoff.chatjobparser.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ElementCollection
    @CollectionTable(name = "vacancy_markers")
    @JoinColumn(foreignKey = @ForeignKey(name = "messageId"))
    protected @NotNull Set<String> markers = new HashSet<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ElementCollection
    @CollectionTable(name = "vacancy_words")
    @JoinColumn(foreignKey = @ForeignKey(name = "messageId"))
    protected @NotNull Set<String> words = new HashSet<>();

}
