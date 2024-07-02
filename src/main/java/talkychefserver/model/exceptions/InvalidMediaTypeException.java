package talkychefserver.model.exceptions;

public class InvalidMediaTypeException extends RuntimeException {
    public InvalidMediaTypeException(String type) {
        super("Unsupported/invalid media type: " + type);
    }
}
