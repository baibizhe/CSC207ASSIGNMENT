package gui.scenarios.recruiter;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.FilterPanel;
import model.enums.InterviewRoundStatus;
import model.enums.JobPostingStatus;
import model.exceptions.CurrentRoundUnfinishedException;
import model.exceptions.JobPostingAlreadyFilledException;
import model.exceptions.WrongApplicationStatusException;
import model.exceptions.WrongJobPostingStatusException;
import model.job.*;
import model.user.Applicant;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code ViewPostingScenario} handles the situation where the hiring manager can view the interview rounds of
 * the applicants.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see JobManageScenario
 */
public class InterviewRoundScenario extends Scenario {

    /**
     * The {@code InterviewRound} that is being shown in this scenario.
     *
     * @see #update()
     * @see HireListener
     * @see MatchInterviewListener
     */
    private InterviewRound interviewRound;

    /**
     * The {@code InterviewRoundManager} that manages this {@code InterviewRound}.
     *
     * @see HireListener
     * @see MatchInterviewListener
     */
    private InterviewRoundManager manager;

    /**
     * The {@code FilterPanel} that shows a list of {@code Application}s on upper left of the page.
     *
     * @see #update()
     * @see #initLeftFilter()
     * @see LeftFilterListener
     * @see HireListener
     */
    private FilterPanel<Application> leftFilter;

    /**
     * The {@code FilterPanel} that shows a list of {@code Interviews}s in the top middle of the page.
     *
     * @see #update()
     * @see LeftFilterListener
     */
    private FilterPanel<Interview> rightFilter;

    /**
     * Create a new {@code InterviewRoundScenario} that is a {@code Scenario} with title "Interview Round Manager".
     *
     * @param userMenuFrame  the {@code userMenuFrame} that sets up the gui framework
     * @param interviewRound the {@code InterviewRound} that is concerned
     * @param jobPosting     the {@code JobPosting} that is concerned
     * @see JobManageScenario
     */
    public InterviewRoundScenario(UserMenuFrame userMenuFrame, InterviewRound interviewRound, JobPosting jobPosting) {
        super(userMenuFrame, "Interview Round Manager");
        this.interviewRound = interviewRound;
        this.manager = jobPosting.getInterviewRoundManager();
    }

    /**
     * Override method {@code initComponents()} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initLeftFilter();
        initRightFilter();
        initOutputInfoPanel();
        initButton();
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Start Matching", new MatchInterviewListener());
        buttonPanel.addButton("Hire", new HireListener());
        add(buttonPanel);
    }


    /**
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     */
    @Override
    protected void update() {
        leftFilter.setFilterContent(interviewRound.getCurrentRoundApplications());
    }

    /**
     * Initialize the {@code leftFilter} such that it shows all remaining applications of the company.
     * It is a helper method of {@link #initComponents()}.
     * <p>
     * An application will be shown only when it's not of status {@code ApplicationStatus.REJECTED}.
     *
     * @see HireListener
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "Remaining Applications");
        leftFilter.addSelectionListener(new LeftFilterListener());
        add(leftFilter);
    }

    /**
     * A helper function for {@link #initComponents()}
     * that initializes the {@code rightFilter}.
     * It enables the {@code rightFilter} to show a list of interviews that relates to a selected {@code Application}.
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "Application Interviews");
        addShowInfoListenerFor(rightFilter);
        add(rightFilter);
    }

    /**
     * Class{@code LeftFilterListener} implements {@code ListSelectionListener}.
     * It deals with the situation where an {@code Application} is selected on left {@code FilterPanel}.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initLeftFilter()
     * @since 2019-08-05
     */
    private class LeftFilterListener implements ListSelectionListener {
        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * It shows all interviews of the selected application on the screen.
         *
         * @param e the action event that an application is selected from "Remaining Applications".
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Application application = leftFilter.getSelectObject();
            if (application != null) {
                rightFilter.setFilterContent(application.getInterviews());
                setOutputText(application.toString());
            }
        }
    }

    /**
     * Class {@code HireListener} implements the interface {@code ActionListener}.
     * It handles the situation where a recruiter is going to hire an applicant and click "Hire" button.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class HireListener implements ActionListener {
        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * The hiring request can be processed only when the selected {@code Application} is of status
         * {@code ApplicationStatus.PENDING} and the job posting is {@code JobPostingStatus.PROCESSING}.
         *
         * @param e the action event that "Hire" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (interviewRound == manager.getCurrentInterviewRound()) {
                Application application = leftFilter.getSelectObject();
                try {
                    manager.hire(application);
                    Applicant applicant = application.getApplicant(getMain().getEmploymentCenter());
                    showMessage("Succeed!");
                    applicant.receiveMessage("You got hired by a job!");
                    initLeftFilter();
                } catch (NullPointerException e1) {
                    showMessage("No application selected!");
                } catch (WrongJobPostingStatusException e1) {
                    showMessage("The status of JobPosting is not PROCESSING, can not hire!");
                } catch (WrongApplicationStatusException e1) {
                    showMessage("The status of Application is not PENDING, can not hire!");
                } catch (CurrentRoundUnfinishedException | JobPostingAlreadyFilledException e1) {
                    showMessage(e1.getMessage());
                }
            } else {
                showMessage("Can only hire people in the most recent interview round!");
            }
        }
    }

    /**
     * Class {@code HireListener} implements the interface {@code ActionListener}.
     * It handles the situation where a recruiter is going to match an interview with an interviewer and click on
     * "Match Interview" button.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class MatchInterviewListener implements ActionListener {
        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * The matching request can be approved only when the job is {@code JobPostingStatus.PROCESSING} and the
         * {@code interviewRound} has status {@code InterviewRoundStatus.MATCHING}.
         * The system will switch to {@code MatchInterviewScenario} if the request is approved.
         *
         * @param e the action event that "Match Interview" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            UserMenuFrame menu = getUserMenuFrame();
            if (!manager.getJobPosting().getStatus().equals(JobPostingStatus.PROCESSING)) {
                showMessage("JobPosting already finished!");
            } else if (!interviewRound.getStatus().equals(InterviewRoundStatus.MATCHING)) {
                showMessage("Current interview round is not in the matching stage, can not match!");
            } else {
                menu.setScenario(new MatchInterviewScenario(menu, interviewRound));
            }
        }
    }
}
