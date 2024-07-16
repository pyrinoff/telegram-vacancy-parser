package ru.pyrinoff.chatjobparser.model.endpoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Schema(description = "Линия данных")
public class OneLineRequest {

    @Schema(description = "Список маркеров")
    private List<String> markers;

    @Schema(description = "SQL оператор (OR/AND) для маркеров")
    private SqlOperatorEnum markersOperator;

    @Schema(description = "Список слов")
    private List<String> words;

    @Schema(description = "SQL оператор (OR/AND) для слов")
    private SqlOperatorEnum wordsOperator;

    @Schema(description = "Период, от")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date periodFrom;

    @Schema(description = "Период, до")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date periodTo;

    @Schema(description = "Данные по ЗП")
    private SalarySettings salary;

    @Override public String toString() {
        return super.toString();
    }

}
