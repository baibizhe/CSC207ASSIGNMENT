package model.enums;

/**
 * Enum {@code JobPostingStatus} contains different status of {@code JobPosting}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see model.job.JobPosting
 * @since 2019-08-04
 */

public enum JobPostingStatus {

    /**
     * {@code OPEN} means the {@code JobPosting} is currently open for {@code Applicant}
     * to apply for.
     */
    OPEN,

    /**
     * {@code PROCESSING} means the {@code JobPosting} has been closed and no further
     * {@code Application} is accepted, and it is in the processing stage.
     */
    PROCESSING,

    /**
     * {@code FINISHED} means the {@code JobPosting} has fully ended.
     */
    FINISHED

}
