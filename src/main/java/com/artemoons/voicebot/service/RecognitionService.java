package com.artemoons.voicebot.service;

import com.artemoons.voicebot.dto.vk.TextResponse;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для взаимодействия с сервисов распознавания речи от ВК.
 */
public interface RecognitionService {

    /**
     * Метод для получения ссылки для отправки голосового сообщения.
     *
     * @return URL на который необходимо отправлять запрос для распознавания файла
     */
    String getUploadURL();

    /**
     * Метод для отправки голосового сообщения.
     *
     * @param voiceMessage сообщение из Telegram, которое содержит голосовое сообщение
     * @return результат отправки голосового сообщения
     */
    String sendToRecognition(Update voiceMessage);

    /**
     * Метод для запуска процесса распознавания голосового сообщения.
     *
     * @param messageInfo информация о голосовом сообщении, полученная от ВК
     * @return ИД задачи
     */
    String startVoiceRecognition(String messageInfo);

    /**
     * Метод для получения реузльтатов распознавания.
     *
     * @param taskId идентификатор задачи
     * @return текст сообщения
     */

    TextResponse getResponseFromService(String taskId);

    /**
     * Метод для опроса контроллера получения результатов распознавания.
     *
     * @param taskId идентификатор задачи
     * @return текст сообщения
     */
    String pollForText(String taskId);

}
