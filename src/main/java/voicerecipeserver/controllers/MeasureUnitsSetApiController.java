//package voicerecipeserver.controllers;
//
//import voicerecipeserver.api.MeasureUnitsSetApi;
//import voicerecipeserver.model.dto.MeasureUnitSetDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
// 
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//  
//@RestController
//public class MeasureUnitsSetApiController implements MeasureUnitsSetApi {
//
//    private static final Logger log = LoggerFactory.getLogger(MeasureUnitsSetApiController.class);
//
//    private final ObjectMapper objectMapper;
//
//    private final HttpServletRequest request;
//
//    @org.springframework.beans.factory.annotation.Autowired
//    public MeasureUnitsSetApiController(ObjectMapper objectMapper, HttpServletRequest request) {
//        this.objectMapper = objectMapper;
//        this.request = request;
//    }
//
//    public ResponseEntity<MeasureUnitSetDto> measureUnitsSetIdGet(@Parameter(in = ParameterIn.PATH, description = "Идентификатор набора.", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<MeasureUnitSetDto>(objectMapper.readValue("{\n  \"units\" : [ \"Граммов\", \"Стаканов\" ]\n}", MeasureUnitSetDto.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<MeasureUnitSetDto>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
//
//        return new ResponseEntity<MeasureUnitSetDto>(HttpStatus.NOT_IMPLEMENTED);
//    }
//
//}
