package talkychefserver.services.impl;

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

@Service
public class CommandRecognitionServiceImpl implements CommandRecognitionService {
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(CommandRecognitionServiceImpl.class);

    public CommandRecognitionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CommandDto recognizeCommand(CommandRecognitionRequest request) {
        logger.info("Got request to recognize command: " + request.getValue());
        ResponseEntity<Integer> response = restTemplate.postForEntity(URI.create(Constants.RECOGNIZER_URL), request,
                                                                      Integer.class);
        if (response.getBody() == null) {
            throw new RuntimeException("Couldn't get command recognition value");
        }
        return new CommandDto(VoiceCommand.getByValue(response.getBody()));
    }
}
