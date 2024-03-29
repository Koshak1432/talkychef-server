package talkychefserver.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import talkychefserver.config.Constants;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/recognition")
public interface CommandRecognitionApi {

    @PostMapping
    String recognizeCommand(@RequestBody String s2text);

}
