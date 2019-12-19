package model.enums;

/**
 * Enum {@code InterviewRoundStatus} contains different status of {@code InterviewRound}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see model.job.InterviewRound
 * @since 2019-08-04
 */
public enum InterviewRoundStatus {

    /**
     * {@code EMPTY} means the {@code InterviewRound} has been initialized but
     * there is nothing in it, and is still waiting to begin.
     */
    EMPTY,

    /**
     * {@code MATCHING} means all the interviews in the {@code InterviewRound}
     * are either being matched or have been matched by recruiter.
     */
    MATCHING,

    /**
     * {@code PENDING} means all interviews have been arranged and is waiting
     * for the results from interviewers.
     */
    PENDING,

    /**
     * {@code FINISHED} means the current {@code InterviewRound} has been
     * finished.
     */
    FINISHED

}
