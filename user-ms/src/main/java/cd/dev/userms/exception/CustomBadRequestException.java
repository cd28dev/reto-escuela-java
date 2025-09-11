package cd.dev.userms.exception;

public class CustomBadRequestException extends RuntimeException {
    private final int errorCode;

    public CustomBadRequestException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}