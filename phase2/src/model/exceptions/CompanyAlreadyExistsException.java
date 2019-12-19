package model.exceptions;

public class CompanyAlreadyExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Company name already exists!";
    }
}
