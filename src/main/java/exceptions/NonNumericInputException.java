package exceptions;

public class NonNumericInputException extends Exception{
    public NonNumericInputException(){
        super("Unos nije broj");
    }

    public NonNumericInputException(String message){
        super(message);
    }

    public NonNumericInputException(String message, Throwable cause){
        super(message, cause);
    }

    public NonNumericInputException(Throwable cause){
        super(cause);
    }
}
