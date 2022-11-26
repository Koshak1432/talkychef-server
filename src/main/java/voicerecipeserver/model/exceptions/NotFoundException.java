package voicerecipeserver.model.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String itemName){
        super("Can't find " + itemName);
    }
}
