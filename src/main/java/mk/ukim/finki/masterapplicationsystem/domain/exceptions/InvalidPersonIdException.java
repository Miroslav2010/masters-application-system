package mk.ukim.finki.masterapplicationsystem.domain.exceptions;

public class InvalidPersonIdException extends RuntimeException{
    public InvalidPersonIdException() {
        super("Invalid id provided");
    }
}
