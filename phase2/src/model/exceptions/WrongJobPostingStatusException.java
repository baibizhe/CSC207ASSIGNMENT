package model.exceptions;

import model.enums.JobPostingStatus;

public class WrongJobPostingStatusException extends Exception {

    private JobPostingStatus status;

    public WrongJobPostingStatusException(JobPostingStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return "Wrong job posting status! Should be " + status.toString();
    }
}
