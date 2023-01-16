package errorhandling;

public class InvalidUsernameException extends Exception {

    public InvalidUsernameException(String msg) {
        super(msg);
    }
}
