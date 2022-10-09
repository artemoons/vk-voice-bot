package com.artemoons.voicebot.service.impl;

import com.artemoons.voicebot.dto.AsrResponse;
import com.artemoons.voicebot.dto.AsrResult;
import com.artemoons.voicebot.dto.Response;
import com.artemoons.voicebot.service.AsrService;
import com.artemoons.voicebot.service.FileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Сервис, взаимодействующий с сервисом распознавания речи.
 */
@Service
@Slf4j
public class AsrServiceImpl implements AsrService {

    /**
     * Сервис получения информации о файле из Telegram.
     */
    private final FileInfoService fileInfoService;

    /**
     * VK API URL.
     */
    @Value("${integration.vk.api-url}")
    private String vkApiUrl;

    /**
     * Токен.
     */
    @Value("${integration.vk.api-token}")
    private String vkApiToken;

    /**
     * Версия API сервиса распознавания речи.
     */
    @Value("${integration.vk.api-version}")
    private String vkApiVersion;

    /**
     * Конструктор.
     *
     * @param fileService сервис получения информации о файле из Telegram
     */
    @Autowired
    public AsrServiceImpl(final FileInfoService fileService) {
        this.fileInfoService = fileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUploadURL() {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", vkApiToken);
        map.add("v", vkApiVersion);

        ResponseEntity<Response> response = restTemplate.postForEntity(vkApiUrl + "/method/asr.getUploadUrl",
                map,
                Response.class);
        String uploadUrl = response.getBody().getResponse().getUploadUrl();

        log.info(String.format("Upload URL: %s", uploadUrl));
        return uploadUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sendToAsr(final Update message) {
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileInfoService.downloadFile(message)) {
            @Override
            public String getFilename() {
                return "filename";
            }
        };

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", byteArrayResource);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(getUploadURL(), map, String.class);

        return stringResponseEntity.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String processVoice(final String messageInfo) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", vkApiToken);
        map.add("v", vkApiVersion);
        map.add("model", "spontaneous");
        map.add("audio", messageInfo);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AsrResponse> response = restTemplate.postForEntity(vkApiUrl + "/method/asr.process",
                map,
                AsrResponse.class);


        return response.getBody().getResponse().getTaskId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText(final String taskId) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", vkApiToken);
        map.add("v", vkApiVersion);
        map.add("task_id", taskId);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AsrResult> response = restTemplate.postForEntity(vkApiUrl + "/method/asr.checkStatus",
                map,
                AsrResult.class);


        return response.getBody().getResponse().getText();
    }
}
