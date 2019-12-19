package model.exceptions;

public class CompanyDoesNotExistException extends Exception {

    @Override
    public String getMessage() {
        return "Company does not exists!";
    }
}
