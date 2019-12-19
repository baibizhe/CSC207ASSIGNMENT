package model.job;

import gui.scenarios.hiringManager.JobPostingRegisterScenario;
import main.Main;
import model.enums.ApplicationStatus;
import model.enums.JobPostingStatus;
import model.exceptions.ApplicationAlreadyExistsException;
import model.exceptions.WrongJobPostingStatusException;
import model.interfaces.Filterable;
import model.interfaces.ShowAble;
import model.storage.EmploymentCenter;
import model.user.Company;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class {@code JobPosting} contains open position (with their descriptions and requirements)
 * so that applicants who wish to work in this position may apply.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see InterviewRoundManager
 * @see JobPostingStatus
 * @since 2019-08-04
 */
public class JobPosting implements Filterable, Serializable, ShowAble {

    private static final long serialVersionUID = -3212798175590179109L;


    /**
     * A hash map that contains all the details for this job posting.
     * Keys are fixed and can be found in {@code JobPostingRegisterScenario}.
     *
     * @see JobPostingRegisterScenario
     * @see #toString()
     */
    private HashMap<String, String> jobDetails;

    /**
     * The {@code InterviewRoundManager} for this job posting that deals
     * with events related to interview rounds.
     * It should be {@code null} when the job posting is opened.
     *
     * @see InterviewRoundManager
     * @see InterviewRound
     * @see JobPostingStatus
     * @see #startProcessing()
     */
    private InterviewRoundManager interviewRoundManager;

    /**
     * An array list that contains all the applications applied for
     * this job posting initially.
     *
     * @see #applicationSubmit(Application, EmploymentCenter)
     * @see #applicationCancel(Application, EmploymentCenter)
     * @see #getApplications()
     */
    private ArrayList<Application> applications;

    /**
     * Keep track of the status of this {@code JobPosting}.
     *
     * @see JobPostingStatus
     * @see #getStatus()
     */
    private JobPostingStatus status;


    /**
     * Create a new job posting.
     *
     * @param jobDetails a hash map containing all details about the job
     */
    public JobPosting(HashMap<String, String> jobDetails) {
        this.jobDetails = jobDetails;
        this.applications = new ArrayList<>();
        this.status = JobPostingStatus.OPEN;
    }

    public InterviewRoundManager getInterviewRoundManager() {
        return interviewRoundManager;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public String getJobId() {
        return jobDetails.get("Job id:");
    }

    int getNumOfPositions() {
        return Integer.parseInt(jobDetails.get("Num of positions:"));
    }

    /**
     * Return whether the close date has been passed or not. This is a helper function for {@code startProcessing}.
     *
     * @return whether the close date has been passed.
     * @see JobPosting#startProcessing()
     */
    private boolean shouldClose() {
        LocalDate closeDate;
        try {
            closeDate = LocalDate.parse(jobDetails.get("Close date:"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return closeDate.isBefore(Main.getCurrentDate());
    }

    /**
     * Create an {@code InterviewRoundManager} if status equals {@code JobPosting.OPEN} and the job should be closed.
     *
     * @see EmploymentCenter#updateOpenJobPostings()
     */
    public void startProcessing() {
        if (isOpen() && shouldClose()) {
            status = JobPostingStatus.PROCESSING;
            interviewRoundManager = new InterviewRoundManager(this, applications);
        }
    }

    /**
     * The method is called when the job posting process is finished. It sets the status to {@code JobPostingStatus.FINISHED}
     * and empty {@code remainingApplications} list of the interview round manager.
     *
     * @see gui.scenarios.recruiter.JobManageScenario
     */
    public void endJobPosting() {
        status = JobPostingStatus.FINISHED;
        if (interviewRoundManager != null) interviewRoundManager.end();
    }

    /**
     * It is a helper function for {@code applicationSubmit}. Return the existence of a certain application.
     *
     * @param application the application of which existence will be checked
     * @return whether the application already exists in the list of applications: {@code applications}
     * @see JobPosting#applicationSubmit(Application, EmploymentCenter)
     */
    private boolean hasApplication(Application application) {
        for (Application app : applications) {
            if (app.getApplicantId().equals(application.getApplicantId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Submit an application and check whether this application submission process succeeds.
     * Here we need to update {@code applications} as well as notify the company to which the job posting belongs
     *
     * @param application      the application that is ready to be submitted
     * @param employmentCenter the place where the company information is stored
     * @see Application#apply(EmploymentCenter)
     */
    public void applicationSubmit(Application application, EmploymentCenter employmentCenter)
            throws ApplicationAlreadyExistsException, WrongJobPostingStatusException {
        if (hasApplication(application)) {
            throw new ApplicationAlreadyExistsException();
        } else if (!isOpen()) {
            throw new WrongJobPostingStatusException(JobPostingStatus.OPEN);
        } else {
            Company company = employmentCenter.getCompany(jobDetails.get("Company id:"));
            company.receiveApplication(application);
            this.applications.add(application);
        }
    }

    /**
     * Cancel an application by removing it from {@code applications} and notifying the company and {@code interviewRoundManager}.
     *
     * @param application      the application that waits to be canceled
     * @param employmentCenter the place where the company information is stored
     * @see Application#cancel(EmploymentCenter)
     */
    public void applicationCancel(Application application, EmploymentCenter employmentCenter) {
        applications.remove(application);
        Company company = employmentCenter.getCompany(jobDetails.get("Company id:"));
        company.cancelApplication(application);
        if (interviewRoundManager != null) interviewRoundManager.applicationCancel(application);
    }

    public void notifyAllFailedApplicant(EmploymentCenter employmentCenter) {
        for (Application application : applications) {
            if (application.getStatus() == ApplicationStatus.REJECTED) {
                application.getApplicant(employmentCenter).receiveMessage("Sorry! You are rejected by a Job Posting!");
            }
        }
    }

    public boolean isOpen() {
        return status == JobPostingStatus.OPEN;
    }

    /**
     * Overrides the method {@code toString}
     *
     * @return a string that contains basic information about the job posting
     * @see gui.scenarios.hiringManager.ViewPostingScenario
     * @see gui.scenarios.recruiter.JobManageScenario
     */
    @Override
    public String toString() {
        return getInfoString("Company", jobDetails.get("Company id:")) +
                getInfoString("Position name", jobDetails.get("Position name:")) +
                getInfoString("Num of positions", jobDetails.get("Num of positions:")) +
                getInfoString("Post date", jobDetails.get("Post date:")) +
                getInfoString("Close date", jobDetails.get("Close date:")) +
                getInfoString("CV", jobDetails.get("CV:")) +
                getInfoString("Cover letter", jobDetails.get("Cover letter:")) +
                getInfoString("Reference", jobDetails.get("Reference:")) +
                getInfoString("Extra document", jobDetails.get("Extra document:")) +
                getInfoString("Status", status.toString());
    }

    /**
     * Return a hash map of headings and corresponding values about this job posting.
     *
     * @return a hash map of headings and corresponding values about this job posting
     */
    @Override
    public HashMap<String, String> getFilterMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("company", jobDetails.get("Company id:"));
        map.put("position (no.)", jobDetails.get("Position name:") + "(" + jobDetails.get("Num of positions:") + ")");
        map.put("close date", jobDetails.get("Close date:"));
        return map;
    }
}
