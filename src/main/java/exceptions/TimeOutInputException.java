package exceptions;

public class TimeOutInputException extends RuntimeException {
    public TimeOutInputException(){
        super("Iskoritili ste sve pokusaje");
    }

    public TimeOutInputException(String message){
        super(message);
    }

    public TimeOutInputException(String message, Throwable cause){
        super(message, cause);
    }

    public TimeOutInputException(Throwable cause){
        super(cause);
    }
}
