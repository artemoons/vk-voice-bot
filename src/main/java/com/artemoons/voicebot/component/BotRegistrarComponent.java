package com.artemoons.voicebot.component;

import com.artemoons.voicebot.service.ActionWrapper;
import com.artemoons.voicebot.service.BotInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

/**
 * Компонент для регистрации бота.
 */
@Slf4j
@Component
public class BotRegistrarComponent {

    /**
     * Обёртка над исполнителем действий.
     */
    private final ActionWrapper wrapper;

    /**
     * Токен доступа бота Telegram.
     */
    @Value("${integration.telegram.bot-token}")
    private String telegramBotToken;

    /**
     * Имя Telegram бота.
     */
    @Value("${telegram.bot.name}")
    private String telegramBotName;

    /**
     * Конструктор.
     *
     * @param actionWrapper обёртка над исполнителем действий
     */
    @Autowired
    public BotRegistrarComponent(final ActionWrapper actionWrapper) {
        this.wrapper = actionWrapper;
    }

    /**
     * Метод для регистрации бота.
     */
    @PostConstruct
    private void registerBot() {
        log.info("Trying to register bot...");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotInitializer(wrapper, telegramBotToken, telegramBotName));
            log.info("Bot registered!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
