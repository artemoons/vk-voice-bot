package com.artemoons.voicebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * todo.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /**
     * todo.
     */
    @JsonProperty("response")
    private UploadURL response;

}
