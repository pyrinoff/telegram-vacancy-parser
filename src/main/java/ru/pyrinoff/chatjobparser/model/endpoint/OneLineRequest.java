package ru.pyrinoff.chatjobparser.model.endpoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.SqlOperatorEnum;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OneLineRequest {

    private List<String> markers;

    private SqlOperatorEnum markersOperator;

    private List<String> words;

    private SqlOperatorEnum wordsOperator;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date periodFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date periodTo;

    private SalarySettings salary;

    @Override public String toString() {
        return super.toString();
    }

}
