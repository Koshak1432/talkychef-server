package talkychefserver.model.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String msg){
        super(msg);
    }
}
