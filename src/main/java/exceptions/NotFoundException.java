package exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(){
        super("Ne postoji entitet po s unesenim ID-jem");
    }

    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NotFoundException(Throwable cause){
        super(cause);
    }
}
