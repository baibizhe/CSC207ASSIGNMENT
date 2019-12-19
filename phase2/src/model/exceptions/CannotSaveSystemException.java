package model.exceptions;

public class CannotSaveSystemException extends Exception {

    @Override
    public String getMessage() {
        return "Cannot save system!";
    }
}
