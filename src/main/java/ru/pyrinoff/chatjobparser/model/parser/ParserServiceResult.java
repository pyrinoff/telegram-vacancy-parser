package ru.pyrinoff.chatjobparser.model.parser;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.util.SimilarityUtil;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class ParserServiceResult extends Vacancy {

    private @Nullable String text;

    private @Nullable Set<String> textWords;

    public static Float SIMILARITY_PERCENT = 95.0F;

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

    @Override public boolean equals(Object obj) {
        if(this == obj) return true;                                                              //ссылки
        if(!(obj instanceof ParserServiceResult)) return false;                                   //разные классы
        ParserServiceResult second = (ParserServiceResult) obj;
        if(this.getText() == null || second.getText() == null) return false;                      //пустые тексты
        if(this.getText().equals(second.getText())) return true;                                  //эквивалентность текста
        if(this.hashCode() != second.hashCode()) return false;                                    //значения с парсинга
        /*
        if(this.getSalaryFrom()!=null && second.getSalaryFrom()==null || this.getSalaryFrom()==null && second.getSalaryFrom()!=null) return false;
        if(!this.getSalaryFrom().equals(second.getSalaryFrom())) return false;
        if(this.getSalaryTo()!=null && second.getSalaryTo()==null || this.getSalaryTo()==null && second.getSalaryTo()!=null) return false;
        if(!this.getSalaryTo().equals(second.getSalaryTo())) return false;
        if(this.getWithPrediction() != second.getWithPrediction()) return false;
        if(!this.getMarkers().equals(second.getMarkers())) return false;
        if(!this.getTextWords().equals(second.getTextWords())) return false;
        */
        if(this.getText().equals(second.getText())) return true;                                  //одинаковый текст
        if(SimilarityUtil.getSimilarityPercent(this.getText(), second.getText())>=SIMILARITY_PERCENT) return true; //схожесть текстов
        return false;
    }

    @Override public int hashCode() {
        //System.out.println("SIMILARITY: "+SIMILARITY_PERCENT);
        return Objects.hash(salaryFrom, salaryTo, currency, withPrediction, markers, textWords);
    }

}
