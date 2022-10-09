package com.artemoons.voicebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * todo.
 */
@Data
public class ProcessVoice {

    /**
     * todo.
     */
    private String audio;

    /**
     * todo.
     */
    private String model;

    /**
     * todo.
     */
    @JsonProperty("access_token")
    private String token;

    /**
     * todo.
     */
    @JsonProperty("v")
    private String apiVersion;


}
