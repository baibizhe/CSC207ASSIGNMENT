package model.exceptions;

public class WrongEmployeeTypeException extends Exception {

    private String task;

    public WrongEmployeeTypeException(String task) {
        this.task = task;
    }

    @Override
    public String getMessage() {
        return "Wrong employee type so that this employee cannot handle task: " + task;
    }
}
