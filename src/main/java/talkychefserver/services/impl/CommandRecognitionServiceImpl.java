package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CommandDto;
import talkychefserver.model.dto.CommandRecognitionRequest;
import talkychefserver.model.dto.VoiceCommand;
import talkychefserver.services.interfaces.CommandRecognitionService;

import java.net.URI;

@Slf4j
@Service
public class CommandRecognitionServiceImpl implements CommandRecognitionService {
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(CommandRecognitionServiceImpl.class);

    public CommandRecognitionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CommandDto recognizeCommand(String text) {
        log.info("Processing recognize command request. Text to recognize: {}", text);
        CommandRecognitionRequest request = new CommandRecognitionRequest(text);
        ResponseEntity<Integer> response = restTemplate.postForEntity(URI.create(Constants.RECOGNIZER_URL), request,
                                                                      Integer.class);
        if (response.getBody() == null) {
            log.error("Couldn't get command, check recognizer");
            throw new RuntimeException("Couldn't get command recognition value");
        }
        VoiceCommand command = VoiceCommand.getByValue(response.getBody());
        log.info("Recognized command [{}]", command.name());
        return new CommandDto(command);
    }
}
