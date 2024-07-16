package ru.pyrinoff.chatjobparser.model.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Ответ после загрузки файла")
public class UploadResponse {

    public UploadResponse(String message) {
        this.message = message;
    }

    @Schema(description = "Сообщение")
    private String message;

}
