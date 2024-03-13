package av.iisproject.restapi.Utils;

public class CustomForbiddenException extends RuntimeException {

    public CustomForbiddenException() {
        super();
    }

    public CustomForbiddenException(String message) {
        super(message);
    }

    public CustomForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomForbiddenException(Throwable cause) {
        super(cause);
    }
}