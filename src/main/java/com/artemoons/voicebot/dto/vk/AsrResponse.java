package com.artemoons.voicebot.dto.vk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO ответа от сервиса распознавания речи.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsrResponse {

    /**
     * Ответ.
     */
    @JsonProperty("response")
    private TaskInfo response;

}
