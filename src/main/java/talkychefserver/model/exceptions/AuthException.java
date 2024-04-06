package talkychefserver.model.exceptions;

public class AuthException extends RuntimeException {
    public AuthException(String string) {
        super(string);
    }
}
