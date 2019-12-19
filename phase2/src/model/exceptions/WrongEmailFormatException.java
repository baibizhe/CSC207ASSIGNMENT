package model.exceptions;

public class WrongEmailFormatException extends Exception {

    @Override
    public String getMessage() {
        return "Wrong email format! Only accept email addresses ending with .com!";
    }
}
