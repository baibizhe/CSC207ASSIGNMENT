package model.exceptions;

public class WrongInterviewStatusException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong interview status so that cannot do desired changes!";
    }
}
