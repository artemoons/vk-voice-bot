package com.artemoons.voicebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс запуска бота.
 */
@SpringBootApplication
public class TelegramVoiceRecognitionApplication {

    /**
     * Метод для запуска Spring Boot приложения.
     *
     * @param args аргументы
     */
    public static void main(final String[] args) {
        SpringApplication.run(TelegramVoiceRecognitionApplication.class, args);
    }

}
