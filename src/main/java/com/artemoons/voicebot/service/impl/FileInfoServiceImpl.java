package com.artemoons.voicebot.service.impl;

import com.artemoons.voicebot.dto.tg.FileResponse;
import com.artemoons.voicebot.service.FileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Сервис для скачивания файлов из Telegram.
 */
@Service
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {

    /**
     * Токен доступа бота.
     */
    @Value("${integration.telegram.bot-token}")
    private String telegramBotToken;

    /**
     * Ссылка на API Telegram.
     */
    @Value("${integration.telegram.api-url}")
    private String telegramApiUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileUrl(final Update message) {

        String fileId = message.getMessage().getVoice().getFileId();


        RestTemplate restTemplate = new RestTemplate();
        FileResponse fileInfo = restTemplate.getForEntity(String.format(telegramApiUrl + "/bot%s/getFile?file_id=%s",
                        telegramBotToken,
                        fileId),
                FileResponse.class).getBody();

        String fileUrl = fileInfo.getResult().getFileUrl(telegramBotToken, fileInfo.getResult().getFilePath());

        log.info(MessageFormat.format("Telegram file URL: {0}", fileUrl));
        return fileUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] downloadFile(final Update message) {
        byte[] fileContent;
        try {
            log.info("Downloading file to byte array...");
            fileContent = IOUtils.toByteArray(new URL(getFileUrl(message)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("File downloaded successfully");
        return fileContent;
    }
}
