package model.exceptions;

public class NotOverrideException extends Exception {

    @Override
    public String getMessage() {
        return "Warning! Call undesired method without override!";
    }
}
