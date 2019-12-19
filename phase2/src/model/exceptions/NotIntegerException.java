package model.exceptions;

public class NotIntegerException extends Exception {

    @Override
    public String getMessage() {
        return "You does not enter a valid int therefore nothing happened!";
    }
}
