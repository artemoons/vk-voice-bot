package com.artemoons.voicebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Todo.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsrResult {

    /**
     * todo.
     */
    @JsonProperty("response")
    private TextResponse response;

}
