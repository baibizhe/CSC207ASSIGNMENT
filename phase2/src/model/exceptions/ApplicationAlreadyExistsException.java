package model.exceptions;

public class ApplicationAlreadyExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Application has already exist!";
    }
}
