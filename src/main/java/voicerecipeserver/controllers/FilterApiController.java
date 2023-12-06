//package voicerecipeserver.controllers;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Positive;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import voicerecipeserver.model.exceptions.NotFoundException;
//
//
//@CrossOrigin(maxAge = 1440)
//@RestController
//public class  FilterApiController implements FilterApi {
//    private final FilterService service;
//
//    public FilterApiController(FilterService service) {
//        this.service = service;
//    }
//
//    @Override
//    public ResponseEntity<FilterDto> getUserCollections(String login, Integer limit, Integer page) throws NotFoundException {
//        return service.getCollections(login, limit, page);
//    }
//
//    @Override
//    public ResponseEntity<FilterDto> addCollection(CollectionDto body) throws NotFoundException {
//        return service.addCollection(body);
//    }
//}
