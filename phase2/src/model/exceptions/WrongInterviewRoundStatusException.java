package model.exceptions;

public class WrongInterviewRoundStatusException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong interview round status therefore cannot execute desired operation!";
    }
}
