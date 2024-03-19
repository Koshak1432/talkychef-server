package talkychefserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandRecognitionRequest {
    private String value;
}
