package errorhandling;

public class InvalidDateException extends Exception{
    public InvalidDateException(String msg) {
        super(msg);
    }
}
