package com.artemoons.voicebot.service;

import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Сервис для инициализации бота.
 */
@Service
public class BotInitializer extends TelegramLongPollingBot {

    /**
     * Обёртка над исполнителем действий.
     */
    private final ActionWrapper actionWrapper;

    /**
     * Токен доступа бота Telegram.
     */
    private final String telegramBotToken;

    /**
     * Имя Telegram бота.
     */
    private final String telegramBotName;


    /**
     * Конструктор.
     *
     * @param wrapper обёртка над исполнителем действий
     * @param token   токен
     * @param name    имя бота
     */
    public BotInitializer(final ActionWrapper wrapper, final String token, final String name) {
        this.actionWrapper = wrapper;
        this.telegramBotToken = token;
        this.telegramBotName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().getVoice() != null) {
            String voiceToText = actionWrapper.processMessage(update);
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(voiceToText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBotUsername() {
        return telegramBotName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBotToken() {
        return telegramBotToken;
    }
}
