package av.iisproject.restapi.Utils;

public class CustomAlreadyExistsException extends RuntimeException{
    public CustomAlreadyExistsException(String message) {
        super(message);
    }
}