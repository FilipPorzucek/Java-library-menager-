package pl.javastart.library.exception;

public class UserAlreadyExistsEsception extends RuntimeException{
    public UserAlreadyExistsEsception(String message) {
        super(message);
    }
}
