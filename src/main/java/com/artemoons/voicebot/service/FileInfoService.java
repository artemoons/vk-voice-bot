package com.artemoons.voicebot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для взаимодействия с сервисом получения файлов из Telegram.
 */
public interface FileInfoService {

    /**
     * Метод для получения ссылки на скачивание голосового сообщения.
     *
     * @param message сообщение
     * @return ссылка на файл
     */
    String getFileUrl(Update message);

    /**
     * Метод для скачивания голосового сообщения в оперативную память.
     *
     * @param message сообщение
     * @return содержимое файла
     */
    byte[] downloadFile(Update message);

}
