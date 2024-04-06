package talkychefserver.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.CommandRecognitionApi;
import talkychefserver.model.dto.CommandDto;
import talkychefserver.services.interfaces.CommandRecognitionService;

@CrossOrigin(maxAge = 1440)
@RestController
public class CommandRecognitionController implements CommandRecognitionApi {
    private final CommandRecognitionService service;

    public CommandRecognitionController(CommandRecognitionService service) {
        this.service = service;
    }

    @Override
    public CommandDto recognizeCommand(String s2text) {
        return service.recognizeCommand(s2text);
    }
}
