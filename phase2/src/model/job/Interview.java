package model.job;

import model.enums.InterviewStatus;
import model.exceptions.WrongInterviewStatusException;
import model.interfaces.Filterable;
import model.interfaces.ShowAble;
import model.storage.EmploymentCenter;
import model.user.Employee;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class {@code Interview} stores information about an interview an
 * {@code Applicant} has with an {@code Interviewer}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see model.user.Applicant
 * @see Employee
 * @since 2019-08-04
 */
public class Interview implements Filterable, Serializable, ShowAble {

    private static final long serialVersionUID = -1449779786766385357L;

    /**
     * The {@code Employee} that will do the interview.
     *
     * @see Employee
     * @see model.enums.UserType
     * @see #getInterviewer()
     * @see #match(Employee, String)
     */
    private Employee interviewer;

    /**
     * The {@code Application} which contains information about this
     * interviewee.
     *
     * @see Application
     * @see #getApplication()
     * @see #Interview(Application)
     */
    private Application application;

    /**
     * The recommendation interviewer has for the interviewee after the
     * interview has been finished.
     *
     * @see #setRecommendation(String)
     */
    private String recommendation;

    /**
     * The current status for this interview.
     *
     * @see InterviewStatus
     * @see #setStatus(InterviewStatus)
     * @see #getStatus()
     */
    private InterviewStatus status = InterviewStatus.UNMATCHED;

    /**
     * Constructor for {@code Interview}.
     *
     * @param application the {@code Application} whose holder is the interviewee
     * @see Application
     */
    public Interview(Application application) {
        this.application = application;
    }

    public Employee getInterviewer() {
        return interviewer;
    }

    public Application getApplication() {
        return application;
    }

    /**
     * Arrange the interview with the given interviewer.
     *
     * @param interviewer the interviewer that will do this interview
     * @param round the interview round this interview is in
     * @throws WrongInterviewStatusException the status of interview is not UNMATCHED, can not match
     * @see InterviewStatus
     * @see Employee#addFile(Object)
     */
    public void match(Employee interviewer, String round) throws WrongInterviewStatusException {
        if (status.equals(InterviewStatus.UNMATCHED)) {
            interviewer.addFile(this);
            this.interviewer = interviewer;
            setStatus(InterviewStatus.PENDING);
            application.addInterview(round, this);
            interviewer.receiveMessage("You got an new interview!");
        } else {
            throw new WrongInterviewStatusException();
        }
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public InterviewStatus getStatus() {
        return this.status;
    }

    /**
     * Update the {@code status} of this interview and notify all the holders.
     *
     * @param status the new {@code status} of this interview
     * @see #notifyHolders()
     * @see InterviewStatus
     */
    public void setStatus(InterviewStatus status) {
        this.status = status;
        notifyHolders();
    }

    /**
     * Cancel the interview if it has not ended yet. Remove from interviewer's list
     * if it has been arranged. This method is only used when the applicant wants
     * to cancel the application, and will not be used during the normal procedure.
     *
     * @see InterviewStatus
     * @see model.job.InterviewRound
     */
    void cancel() {
        if (status.equals(InterviewStatus.PENDING)) {
            interviewer.removeFile(this);
            setStatus(InterviewStatus.FAIL);
        } else if (status.equals(InterviewStatus.UNMATCHED)) {
            setStatus(InterviewStatus.FAIL);
        }
    }

    /**
     * Notify all the holders and remove this from interviewer's list if interview is finished.
     *
     * @see Application#update(Interview)
     * @see InterviewStatus
     * @see Employee#removeFile(Object)
     */
    private void notifyHolders() {
        application.update(this);
        if (status.equals(InterviewStatus.FAIL) || status.equals(InterviewStatus.PASS)) {
            interviewer.removeFile(this);
        }
    }

    /**
     * Return basic information about this interview and detailed information about applicant.
     *
     * @param employmentCenter the {@code EmploymentCenter} that contains all users
     * @return basic information about this interview and detailed information about applicant
     */
    public String detailedToStringForEmployee(EmploymentCenter employmentCenter) {
        String jobInfo = employmentCenter.getJobPosting(application.getJobPostingId()).toString();
        String applicantInfo = employmentCenter.getApplicant(application.getApplicantId()).toString();
        return getInfoString("JobPosting information", "\n" + jobInfo) +
                getInfoString("Applicant information", "\n" + applicantInfo);
    }

    /**
     * Overrides the method in interface {@code ShowAble}.
     *
     * @return a string that contains basic information about this application
     * @see ShowAble
     */
    @Override
    public String toString() {
        String jobPostingId = application.getJobPostingId();
        return getInfoString("JobPosting", jobPostingId.substring(0, jobPostingId.lastIndexOf("--"))) +
                getInfoString("Applicant", application.getApplicantId()) +
                getInfoString("Interviewer",
                        status.equals(InterviewStatus.UNMATCHED) ? "N/A" : interviewer.getUsername()) +
                getInfoString("Recommendation", recommendation) +
                getInfoString("Status", status.toString());
    }

    /**
     * Return a hash map of headings and corresponding values about this interview.
     *
     * @return a hash map of headings and corresponding values about this interview
     * @see Filterable
     */
    @Override
    public HashMap<String, String> getFilterMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("applicant", getApplication().getApplicantId());
        map.put("interviewer", getInterviewer() == null ? "N/A" : interviewer.getUsername());
        map.put("status", status.toString());
        return map;
    }

}
