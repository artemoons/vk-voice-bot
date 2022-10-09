package com.artemoons.voicebot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для взаимодействия с сервисов распознавания речи от ВК.
 */
public interface AsrService {

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
    String sendToAsr(Update voiceMessage);

    /**
     * Метод для запуска процесса распознавания голосового сообщения.
     *
     * @param messageInfo информация о голосовом сообщении, полученная от ВК
     * @return ИД задачи
     */
    String processVoice(String messageInfo);

    /**
     * Метод для получения реузльтатов распознавания.
     *
     * @param taskId идентификатор задачи
     * @return текст сообщения
     */
    String getText(String taskId);

}
