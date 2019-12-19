package model.exceptions;

public class EmptyDocumentNameException extends Exception {

    @Override
    public String getMessage() {
        return "The name of this document can not be empty!";
    }
}
