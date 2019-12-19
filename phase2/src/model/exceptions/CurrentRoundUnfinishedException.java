package model.exceptions;

public class CurrentRoundUnfinishedException extends Exception {

    @Override
    public String getMessage() {
        return "Current interview round has not been finished yet!";
    }
}
