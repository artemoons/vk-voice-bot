package com.artemoons.voicebot.dto.tg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.File;

/**
 * todo.
 */
@Data
public class FileResponse {

    /**
     * todo.
     */
    @JsonProperty("ok")
    private String status;

    /**
     * todo.
     */
    private File result;

}
