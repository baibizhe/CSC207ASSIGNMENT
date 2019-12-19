package model.job;

import gui.panels.Filter;
import model.enums.InterviewRoundStatus;
import model.enums.InterviewStatus;
import model.interfaces.Filterable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class {@code InterviewRound} stores information about a specific round for a
 * {@code JobPosting} and deals with all the events happened within this round.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see JobPosting
 * @see InterviewRoundStatus
 * @see InterviewRoundManager
 * @see Interview
 * @since 2019-08-04
 */
public class InterviewRound implements Filterable, Serializable {

    private static final long serialVersionUID = -9079019848528946667L;

    /**
     * The name of this interview round.
     *
     * @see #getRoundName()
     */
    private String roundName;

    /**
     * All the applications that made into this interview round.
     *
     * @see #getInterviews()
     * @see #getCurrentRoundApplications()
     * @see #getUnmatchedApplications()
     * @see #getApplicationsByStatus(InterviewStatus)
     */
    private ArrayList<Application> applications;

    /**
     * The status of this interview round, values are from {@code InterviewRoundStatus}.
     *
     * @see InterviewRoundStatus
     * @see #getStatus()
     * @see #setStatus(InterviewRoundStatus)
     * @see #checkStatus()
     */
    private InterviewRoundStatus status;


    /**
     * Create a new interviewRound.
     *
     * @param roundName name of the new interview round
     */
    public InterviewRound(String roundName) {
        this.roundName = roundName;
        this.status = InterviewRoundStatus.EMPTY;
        this.applications = new ArrayList<>();
    }

    public String getRoundName() {
        return this.roundName;
    }

    public ArrayList<Application> getCurrentRoundApplications() {
        return this.applications;
    }

    /**
     * Return the {@code ArrayList<Application>} which contains applications that has an unmatched interview.
     *
     * @return the {@code ArrayList<Application>} which contains applications that has an unmatched interview
     */
    public ArrayList<Application> getUnmatchedApplications() {
        return getApplicationsByStatus(InterviewStatus.UNMATCHED);
    }

    /**
     * Get all applications of a certain interview status. It is a helper method for {@code getUnmatchedApplications}.
     *
     * @param status the interview status of wanted interviews
     * @return the {@code ArrayList<Application} containing all applications of a given status
     */
    private ArrayList<Application> getApplicationsByStatus(InterviewStatus status) {
        ArrayList<Application> passedApplications = new ArrayList<>();
        for (Application application : this.applications) {
            if (application.getInterviewByRound(this.roundName).getStatus().equals(status)) {
                passedApplications.add(application);
            }
        }
        return passedApplications;
    }

    public InterviewRoundStatus getStatus() {
        return this.status;
    }

    public void setStatus(InterviewRoundStatus status) {
        this.status = status;
    }

    /**
     * Check whether the status of this interview round is consistent with interview status of applications stored in
     * {@code applications}. If it used not to be, correct it. If there is at least one application has an interview
     * status of unmatched, then the interview round status should be {@code InterviewRoundStatus.MATCHING}.
     * If there exists one interview corresponding to current interview round which has a status of {@code InterviewStatus.PENDING},
     * then the interview round status should be set {@code InterviewRoundStatus.PENDING}. Otherwise, check whether
     * {@code this.applications} is non empty. Set the status to {@code InterviewRoundStatus.Finished} if the answer is yes.
     *
     * @see InterviewRoundManager#checkStatus()
     */
    void checkStatus() {
        ArrayList<Interview> interviews = this.getInterviews();
        boolean finished = true;
        for (Interview interview : interviews) {
            if (interview.getStatus().equals(InterviewStatus.UNMATCHED)) {
                this.setStatus(InterviewRoundStatus.MATCHING);
                finished = false;
                break;
            } else if (interview.getStatus().equals(InterviewStatus.PENDING)) {
                this.setStatus(InterviewRoundStatus.PENDING);
                finished = false;
                break;
            }
        }
        if (finished && !this.applications.isEmpty()) {
            this.setStatus(InterviewRoundStatus.FINISHED);
        }
    }

    /**
     * Get all interviews in the current round. This is a helper function for {@code checkStatus}.
     *
     * @return the {@code ArrayList<Interview>} that consists of all interviews in the current round
     */
    private ArrayList<Interview> getInterviews() {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Application application : this.applications) {
            interviews.add(application.getInterviewByRound(this.roundName));
        }
        return interviews;
    }

    /**
     * Start the new interview round. All {@code applications} in the list that is passed in are going to be appended to
     * the field {@code this.applications} and will be added a new interview on.
     *
     * @param applications the applications that wait for a new interview
     * @see InterviewRoundManager#nextRound()
     */
    void start(ArrayList<Application> applications) {
        this.setStatus(InterviewRoundStatus.MATCHING);
        for (Application application : applications) {
            this.applications.add(application);
            application.addInterview(this.roundName, new Interview(application));
        }
    }

    /**
     * Cancel the application that is passed in as input and its interview in this round.
     *
     * @param application the application that is about to be canceled
     * @see InterviewRoundManager#applicationCancel(Application)
     */
    void applicationCancel(Application application) {
        applications.remove(application);
        Interview interview = application.getInterviewByRound(roundName);
        interview.cancel();
    }

    /**
     * Return a hash map of headings and corresponding values about this interview round.
     *
     * @return a hash map of headings and corresponding values about this interview round
     * @see Filterable
     * @see Filter
     */
    @Override
    public HashMap<String, String> getFilterMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("round name", getRoundName());
        map.put("remaining applications", Integer.toString(this.getCurrentRoundApplications().size()));
        map.put("status", status.toString());
        return map;
    }
}
