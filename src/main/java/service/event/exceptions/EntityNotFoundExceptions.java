package service.event.exceptions;

public class EntityNotFoundExceptions extends RuntimeException {
    public EntityNotFoundExceptions(String message) {
        super(message);
    }
}
