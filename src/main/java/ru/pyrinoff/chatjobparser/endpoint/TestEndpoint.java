package ru.pyrinoff.chatjobparser.endpoint;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class TestEndpoint {

    @PostMapping("/test")
    public String post(@RequestBody TestRequest request) {
        return request.toString();
    }

    @GetMapping("/test")
    public DatasetRequest get() {
        DatasetRequest datasetRequest = new DatasetRequest();

        OneLineRequest oneLineRequest = new OneLineRequest();
        oneLineRequest.setMarkers(new ArrayList<>(){{
            add("some");
            add("markers");
        }});
        oneLineRequest.setWords(new ArrayList<>(){{
            add("some");
            add("words");
        }});
        SalarySettings salarySettings = new SalarySettings();
        salarySettings.setSalaryFrom(10);
        salarySettings.setSalaryTo(10);
        salarySettings.setToBorder(true);
        salarySettings.setFromBorder(true);
        salarySettings.setAllowPredicted(true);
        salarySettings.setBothBorders(true);
        oneLineRequest.setSalary(salarySettings);

        oneLineRequest.setPeriodFrom(new Date());
        oneLineRequest.setPeriodTo(new Date());

        datasetRequest.setLines(new ArrayList<OneLineRequest>(){{
            add(oneLineRequest);
            }
        });
        return datasetRequest;

    }

}
