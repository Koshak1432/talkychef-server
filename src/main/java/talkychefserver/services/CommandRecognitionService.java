package talkychefserver.services;

import talkychefserver.model.dto.CommandDto;

public interface CommandRecognitionService {
    CommandDto recognizeCommand(String text);
}
