package com.artemoons.voicebot.dto;

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
public class TextResponse {

    /**
     * todo.
     */
    private String id;

    /**
     * todo.
     */
    private String status;

    /**
     * todo.
     */
    private String text;

}
