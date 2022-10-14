package com.artemoons.voicebot.service.impl;

import com.artemoons.voicebot.dto.vk.AsrResponse;
import com.artemoons.voicebot.dto.vk.AsrResult;
import com.artemoons.voicebot.dto.vk.Response;
import com.artemoons.voicebot.dto.vk.TextResponse;
import com.artemoons.voicebot.enums.ResultStatus;
import com.artemoons.voicebot.service.FileInfoService;
import com.artemoons.voicebot.service.RecognitionService;
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

import java.util.Objects;

import static java.lang.Thread.sleep;

/**
 * Сервис, взаимодействующий с сервисом распознавания речи.
 */
@Service
@Slf4j
public class RecognitionServiceImpl implements RecognitionService {

    /**
     * Задержка перед запросом результатов распознавания.
     */
    @Value("${integration.vk.poll-interval}")
    private int pollInterval;

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
    public RecognitionServiceImpl(final FileInfoService fileService) {
        this.fileInfoService = fileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUploadURL() {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = prepareRequestParams();

        ResponseEntity<Response> response = sendRequest(restTemplate, map);
        String uploadUrl = response.getBody().getResponse().getUploadUrl();

        log.info(String.format("Upload URL: %s", uploadUrl));
        return uploadUrl;
    }

    private ResponseEntity<Response> sendRequest(final RestTemplate restTemplate,
                                                 final MultiValueMap<String, String> map) {
        ResponseEntity<Response> response = restTemplate.postForEntity(vkApiUrl + "/method/asr.getUploadUrl",
                map,
                Response.class);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sendToRecognition(final Update message) {
        RestTemplate restTemplate = new RestTemplate();
        ByteArrayResource fileData = prepareFileData(message);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", fileData);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(getUploadURL(), map, String.class);

        return stringResponseEntity.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String startVoiceRecognition(final String messageInfo) {

        MultiValueMap<String, String> map = prepareRequestParams();
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
    public TextResponse getResponseFromService(final String taskId) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = prepareRequestParams();
        map.add("task_id", taskId);

        ResponseEntity<AsrResult> response = restTemplate.postForEntity(vkApiUrl + "/method/asr.checkStatus",
                map,
                AsrResult.class);
        return response.getBody().getResponse();
    }

    /**
     * {@inheritDoc}
     */
    public String pollForText(final String taskId) {
        Boolean continueChecks = Boolean.TRUE;
        TextResponse result = new TextResponse();

        result = pollService(taskId, continueChecks, result);

        return handleServiceResponse(result);
    }

    /**
     * Вспомогательный метод для обработки ответов сервиса.
     *
     * @param response ответ сервиса
     * @return результат распознавания, либо сообщение об ошибке
     */
    private static String handleServiceResponse(final TextResponse response) {
        if (response.getStatus().equals(ResultStatus.INTERNAL_ERROR.getStatus())) {
            return "Внутренняя ошибки сервиса распознавания речи ВКонтакте";
        }
        if (response.getStatus().equals(ResultStatus.RECOGNITION_ERROR.getStatus())) {
            return "Ошибка распознавания речи, сложности в распознавании. "
                    + "Попробуйте говорить чётче или снизить фоновые шумы";
        }
        if (response.getStatus().equals(ResultStatus.TRANSCODING_ERROR.getStatus())) {
            return "Ошибка перекодирования аудиозаписи во внутренний формат. "
                    + "Попробуйте загрузить аудиозапись в другом поддерживаемом формате";
        }
        if (Objects.equals(response.getText(), "")
                && response.getStatus().equals(ResultStatus.FINISHED.getStatus())) {
            return "Мне не удалось распознать голос, попробуйте другой файл.";
        }

        return response.getText();
    }

    /**
     * Вспомогательный метод для опроса сервиса распознавания.
     *
     * @param taskId         идентификатор задачи
     * @param continueChecks флаг продолжения опроса
     * @param result         результат
     * @return ответ сервиса типа {@link TextResponse}
     */
    private TextResponse pollService(final String taskId, final Boolean continueChecks, final TextResponse result) {
        TextResponse response = result;
        Boolean process = continueChecks;

        while (Boolean.TRUE.equals(process)) {
            log.info("Распозначание файла в процессе...");
            response = getResponseFromService(taskId);

            try {
                sleep(pollInterval);
            } catch (InterruptedException ex) {
                log.error("Ошибка при попытке поставить поток на паузу");
                ex.printStackTrace();
            }

            if (response != null && !response.getStatus().equals(ResultStatus.PROCESSING.getStatus())) {
                process = Boolean.FALSE;
                log.info("Распознавание файла завершено");
            }
        }
        return response;
    }

    /**
     * Вспомогательный метод для подготовки голосового файла к отправке.
     *
     * @param message сообщение
     * @return объект типа {@link ByteArrayResource}
     */
    private ByteArrayResource prepareFileData(final Update message) {
        return new ByteArrayResource(fileInfoService.downloadFile(message)) {
            @Override
            public String getFilename() {
                return "filename";
            }
        };
    }

    /**
     * Вспомогательный метод для подготовки параметров запроса.
     *
     * @return карта параметров
     */
    private MultiValueMap<String, String> prepareRequestParams() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", vkApiToken);
        map.add("v", vkApiVersion);
        return map;
    }
}
