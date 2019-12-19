package model.enums;

import model.job.Application;

/**
 * Enum {@code ApplicationStatus} contains different status of {@code Application}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see Application
 * @since 2019-08-04
 */
public enum ApplicationStatus {

    /**
     * {@code DRAFT} means the {@code Application} has not been submitted yet,
     * and is allowed to modify.
     */
    DRAFT,

    /**
     * {@code PENDING} means the {@code Application} has been submitted but is
     * still waiting for final result.
     */
    PENDING,

    /**
     * {@code HIRE} means the {@code Applicant} that holds the {@code Application}
     * has been hired by the company.
     */
    HIRED,

    /**
     * {@code REJECTED} means the {@code Applicant} that holds the {@code Application}
     * has been rejected by the company.
     */
    REJECTED

}
