package com.artemoons.voicebot.enums;

/**
 * Перечислитель статусов от сервиса распознавания ВК.
 *
 * @author <a href="mailto:github@eeel.ru">Artem Utkin</a>
 */
public enum ResultStatus {

    /**
     * Аудиозапись обрабатывается.
     */
    PROCESSING("processing"),

    /**
     * Обработка аудиозаписи закончена.
     */
    FINISHED("finished"),

    /**
     * Внутренние ошибки сервиса распознавания речи ВКонтакте.
     */
    INTERNAL_ERROR("internal_error"),

    /**
     * Ошибка перекодирования аудиозаписи во внутренний формат.
     */
    TRANSCODING_ERROR("transcoding_error"),

    /**
     * Ошибка распознавания речи, сложности в распознавании.
     */
    RECOGNITION_ERROR("recognition_error");

    /**
     * Получение кода статуса ВК.
     *
     * @return код статуса
     */
    public String getStatus() {
        return status;
    }

    /**
     * Код ошибки.
     */
    private final String status;

    /**
     * Конструктор.
     *
     * @param newStatus статус
     */
    ResultStatus(final String newStatus) {
        this.status = newStatus;
    }

}
