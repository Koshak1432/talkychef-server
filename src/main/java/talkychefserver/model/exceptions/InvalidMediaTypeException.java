package voicerecipeserver.model.exceptions;

public class InvalidMediaTypeException extends Exception{
    public InvalidMediaTypeException(String type){
        super("Unsupported/invalid media type: " + type);
    }
}
