package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.entities.Media;
import talkychefserver.model.entities.MediaType;
import talkychefserver.model.exceptions.InvalidMediaTypeException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respositories.MediaRepository;
import talkychefserver.respositories.MediaTypeRepository;
import talkychefserver.services.interfaces.MediaService;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final MediaTypeRepository mediaTypeRepository;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, MediaTypeRepository mediaTypeRepository) {
        this.mediaTypeRepository = mediaTypeRepository;
        this.mediaRepository = mediaRepository;
    }

    //TODO тип медиа не проверяется
    @Override
    public ResponseEntity<byte[]> getMediaById(Long id) {
        log.info("Processing get media [{}] by id request", id);
        Media media = mediaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Couldn't find media with id: " + id));

        byte[] data = media.getFileData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.valueOf(media.getMediaType().getMimeType()));
        headers.setContentLength(data.length);
        log.info("Processed get media by id request");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }


    //todo кешнуть поддерживаемые типы при инициализации и потом их юзать. Эксепшены кидать при отсутствии типа.
    @Override
    @Transactional
    public ResponseEntity<IdDto> addMedia(String contentTypeHeader, byte[] data) {
        log.info("Processing add media request");
        int endOfTypeInd = contentTypeHeader.indexOf(';');
        String mimeType;
        if (endOfTypeInd == -1) {
            mimeType = contentTypeHeader;
        } else {
            mimeType = contentTypeHeader.substring(0, endOfTypeInd);
        }

        MediaType mediaType = mediaTypeRepository.findByMimeType(mimeType).orElseThrow(
                () -> new InvalidMediaTypeException(mimeType));
        Media media = new Media();
        media.setFileData(data);
        media.setMediaType(mediaType);

        Media savedMedia = mediaRepository.save(media);
        log.info("Added media");
        return ResponseEntity.ok(new IdDto().id(savedMedia.getId()));
    }

}
