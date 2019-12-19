package model.exceptions;

public class NextRoundDoesNotExistException extends Exception {

    @Override
    public String getMessage() {
        return "Next round does not exist! Please add new round first!";
    }
}
