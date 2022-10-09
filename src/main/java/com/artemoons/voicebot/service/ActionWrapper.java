package com.artemoons.voicebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Thread.sleep;

/**
 * Обёртка над выполняемыми действиями.
 */
@Service
public class ActionWrapper {

    /**
     * todo: Задержка перед запросом результатов распознавания.
     */
    public static final int TIME_TO_SLEEP = 2000;
    /**
     * Сервис распознавания речи.
     */
    private final AsrService asrService;

    /**
     * Конструктор.
     *
     * @param service сервис распознавания речи
     */
    @Autowired
    public ActionWrapper(final AsrService service) {
        this.asrService = service;
    }

    /**
     * Метод для обработки входящих сообщений.
     *
     * @param update полученное сообщение
     * @return результат распознавания
     */
    public String processMessage(final Update update) {

        String asrResponse = asrService.sendToAsr(update);

        String taskId = asrService.processVoice(asrResponse);
        try {
            sleep(TIME_TO_SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return asrService.getText(taskId);
    }


}
