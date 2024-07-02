package talkychefserver.services.interfaces;

import talkychefserver.model.dto.CommandDto;
import talkychefserver.model.dto.CommandRecognitionRequest;

public interface CommandRecognitionService {
    CommandDto recognizeCommand(CommandRecognitionRequest request);
}
