package model.user;

import model.enums.UserType;
import model.job.Application;
import model.storage.EmploymentCenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class {@code Company} contains all the information for a company including
 * its employees and all the job postings it has.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see Employee
 * @see model.job.JobPosting
 * @since 2019-08-04
 */
public class Company implements Serializable {

    private static final long serialVersionUID = 4339413084182647159L;

    /**
     * The name of the company, will be used as identification
     * for other classes.
     *
     * @see #getId()
     */
    private String id;

    /**
     * A hash map where the keys are {@code UserType} and the values are
     * corresponding users of that type.
     * Note that {@code HIRING_MANAGER} has only one corresponding user per
     * company.
     *
     * @see UserType
     * @see Employee
     * @see #addInterviewerId(String)
     * @see #addRecruiterId(String)
     * @see #getInterviewerIds()
     * @see #getRecruiterIds()
     */
    private HashMap<UserType, ArrayList<String>> workerIds;

    /**
     * An array list of id of all the job postings it has.
     *
     * @see model.job.JobPosting
     * @see #getJobPostingIds()
     * @see #addJobPostingId(String)
     */
    private ArrayList<String> jobPostingIds;

    /**
     * A hash map where the key is applicant's username and value is
     * all the applications this applicant has that were for this
     * company's job postings.
     *
     * @see Application
     * @see #receiveApplication(Application)
     * @see #cancelApplication(Application)
     * @see #getAllApplications()
     */
    private HashMap<String, ArrayList<Application>> applications;

    /**
     * Constructor for {@code Company}.
     *
     * @param values the values need to be pass in  include ID of company and hiringManagerId
     */
    public Company(HashMap<String, String> values) {
        this.id = values.get("id");
        this.workerIds = new HashMap<>();
        this.workerIds.put(UserType.HIRING_MANAGER, new ArrayList<>());
        this.workerIds.put(UserType.RECRUITER, new ArrayList<>());
        this.workerIds.put(UserType.INTERVIEWER, new ArrayList<>());
        this.workerIds.get(UserType.HIRING_MANAGER).add(values.get("hiringManagerId"));
        this.jobPostingIds = new ArrayList<>();
        this.applications = new HashMap<>();
    }

    public String getId() {
        return this.id;
    }

    /**
     * Return the ids for all {@code RECRUITER} in this company.
     *
     * @return the ids for all {@code RECRUITER} in this company
     */
    public ArrayList<String> getRecruiterIds() {
        return this.workerIds.get(UserType.RECRUITER);
    }

    /**
     * Return the ids for all {@code INTERVIEWER} in this company.
     *
     * @return the ids for all {@code INTERVIEWER} in this company
     */
    public ArrayList<String> getInterviewerIds() {
        return this.workerIds.get(UserType.INTERVIEWER);
    }

    public ArrayList<String> getJobPostingIds() {
        return this.jobPostingIds;
    }

    /**
     * Return all applications that this company received.
     *
     * @return all applications that this company received
     */
    public ArrayList<Application> getAllApplications() {
        ArrayList<Application> allApplications = new ArrayList<>();
        for (String applicantId : applications.keySet()) {
            allApplications.addAll(applications.get(applicantId));
        }
        return allApplications;
    }

    public void addRecruiterId(String id) {
        this.workerIds.get(UserType.RECRUITER).add(id);
    }

    public void addInterviewerId(String id) {
        this.workerIds.get(UserType.INTERVIEWER).add(id);
    }

    public void addJobPostingId(String id) {
        this.jobPostingIds.add(id);
    }

    /**
     * Add a new {@code Application} to this company.
     *
     * @param application the {@code Application} need to be added
     */
    public void receiveApplication(Application application) {
        String applicantId = application.getApplicantId();
        if (!this.applications.containsKey(applicantId)) {
            this.applications.put(applicantId, new ArrayList<>());
        }
        this.applications.get(applicantId).add(application);
    }

    /**
     * Delete the {@code Application} from this company. This only occurs when
     * the {@code Applicant} decides to cancel this {@code Application}.
     *
     * @param application the application that need to be deleted
     * @see model.job.JobPosting#applicationCancel(Application, EmploymentCenter)
     */
    public void cancelApplication(Application application) {
        String applicantId = application.getApplicantId();
        if (this.applications.containsKey(applicantId)) {
            this.applications.get(applicantId).remove(application);
        }
        if (this.applications.get(applicantId).isEmpty()) {
            this.applications.remove(applicantId);
        }
    }
}
