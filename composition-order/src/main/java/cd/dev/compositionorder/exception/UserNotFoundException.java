package cd.dev.compositionorder.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Usuario no encontrado con ID: " + userId);
    }
}
