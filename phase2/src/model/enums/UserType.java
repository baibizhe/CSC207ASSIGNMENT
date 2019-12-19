package model.enums;

/**
 * Enum {@code UserType} contains different types of {@code User}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see model.user.User
 * @see model.storage.UserFactory
 * @since 2019-08-04
 */

public enum UserType {

    /**
     * {@code APPLICANT} means the {@code User} is an {@code Applicant} and
     * can apply for {@code JobPosting}.
     */
    APPLICANT,

    /**
     * {@code INTERVIEWER} means the {@code User} is an {@code Employee} whose
     * duty is to interview people and give feedback.
     */
    INTERVIEWER,

    /**
     * {@code HIRING_MANAGER} means the {@code User} is an {@code Employee}
     * whose duty is to post new {@code JobPosting} and assign each to a
     * {@code RECRUITER} to process.
     */
    HIRING_MANAGER,

    /**
     * {@code RECRUITER} means the {@code User} is an {@code Employee} whose
     * duty is to arrange {@code Interview} between applicants and interviews,
     * and manage the procedure of each {@code JobPosting}.
     */
    RECRUITER

}
