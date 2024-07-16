package ru.pyrinoff.chatjobparser.model.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintenanceRequest {

    @Schema(name = "Флаг включения режима")
    public Boolean enabled;

}
