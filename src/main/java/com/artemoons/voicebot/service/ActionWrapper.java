package com.artemoons.voicebot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обёртка над выполняемыми действиями.
 */
@Slf4j
@Service
public class ActionWrapper {

    /**
     * Сервис распознавания речи.
     */
    private final RecognitionService recognitionService;

    /**
     * Конструктор.
     *
     * @param service сервис распознавания речи
     */
    @Autowired
    public ActionWrapper(final RecognitionService service) {
        this.recognitionService = service;
    }

    /**
     * Метод для обработки входящих сообщений.
     *
     * @param update полученное сообщение
     * @return результат распознавания
     */
    public String processMessage(final Update update) {
        String asrResponse = recognitionService.sendToRecognition(update);
        String taskId = recognitionService.startVoiceRecognition(asrResponse);
        return recognitionService.pollForText(taskId);
    }


}
