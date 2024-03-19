package talkychefserver.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CommandDto;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/recognition")
public interface CommandRecognitionApi {

    @PostMapping
    CommandDto recognizeCommand(@RequestBody String s2text);

}
