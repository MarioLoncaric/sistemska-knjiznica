package exceptions;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException(){
        super("Nije dozvoljeno unositi negativnu vrijednost");
    }

    public NegativeValueException(String message){
        super(message);
    }

    public NegativeValueException(String message, Throwable cause){
        super(message, cause);
    }

    public NegativeValueException(Throwable cause){
        super(cause);
    }
}
