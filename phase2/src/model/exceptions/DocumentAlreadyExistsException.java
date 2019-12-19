package model.exceptions;

public class DocumentAlreadyExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Document already exists!";
    }
}
