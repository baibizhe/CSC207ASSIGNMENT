package model.exceptions;

public class NotEmployeeException extends Exception {

    @Override
    public String getMessage() {
        return "You are not an employee therefore cannot do whatever you are trying to do!";
    }
}
