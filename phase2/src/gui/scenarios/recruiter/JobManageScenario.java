package gui.scenarios.recruiter;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.ComponentFactory;
import gui.panels.FilterPanel;
import gui.panels.InputInfoPanel;
import model.enums.JobPostingStatus;
import model.exceptions.NextRoundDoesNotExistException;
import model.exceptions.WrongEmployeeTypeException;
import model.exceptions.WrongInterviewRoundStatusException;
import model.exceptions.WrongJobPostingStatusException;
import model.job.InterviewRound;
import model.job.JobPosting;
import model.user.Employee;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class {@code JobManageScenario} handles the case when a recruiter is managing jobs.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.MenuPanel
 * @since 2019-08-05
 */
public class JobManageScenario extends Scenario {

    /**
     * The {@code FilterPanel} that shows a list of {@code JobPosting}s on upper left of the page.
     *
     * @see #initLeftFilter()
     * @see #update()
     * @see ViewEditListener
     * @see AddRoundListener
     * @see NextRoundListener
     * @see EndJobPostingListener
     * @see LeftFilterListener
     */
    private FilterPanel<JobPosting> leftFilter;
    /**
     * The {@code FilterPanel} that shows a list of {@code InterviewRound}s in the top middle of the page.
     *
     * @see #initRightFilter()
     * @see #update()
     * @see ViewEditListener
     * @see LeftFilterListener
     */
    private FilterPanel<InterviewRound> rightFilter;
    /**
     * The {@code InputInfoPanel} that allows typing on the screen.
     *
     * @see #initInput()
     * @see AddRoundListener
     */
    private InputInfoPanel infoPanel;

    /**
     * Create a new {@code JobManageScenario} that is a {@code Scenario} with title "Job Manager"
     *
     * @param userMenuFrame the {@code userMenuFrame} that sets up the gui framework
     * @see gui.general.MenuPanel
     */
    public JobManageScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "Job Manager");
    }

    /**
     * Override method {@code initComponents()} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initLeftFilter();
        initRightFilter();
        initOutputInfoPanel();
        initInput();
        initButton();
    }

    /**
     * A helper method for {@link #initComponents()} that initializes the {@code infoPanel}.
     * <p>
     * The user can enter the name of a new {@code InterviewRound} in the text field.
     */
    protected void initInput() {
        infoPanel = new InputInfoPanel(REGULAR_INPUT_SIZE);
        ComponentFactory factory = infoPanel.getComponentFactory();
        factory.addTextField("Round name:");
        add(infoPanel);
    }

    /**
     * A helper method for {@link #initComponents()} that initializes {@code leftFilter}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "Jobs I Responsible to:");
        leftFilter.addSelectionListener(new JobManageScenario.LeftFilterListener());
        add(leftFilter);
    }

    /**
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     */
    @Override
    protected void update() {
        Employee recruiter = (Employee) getUserMenuFrame().getUser();
        try {
            leftFilter.setFilterContent(recruiter.getJobPostings());
        } catch (WrongEmployeeTypeException e) {
            leftFilter.setFilterContent(new ArrayList<>());
        }
    }

    /**
     * Update the right filter when left filter is selected
     *
     * @see AddRoundListener
     * @see EndJobPostingListener
     * @see NextRoundListener
     */
    private void updateRightFilter() {
        JobPosting jobPosting = leftFilter.getSelectObject();
        if (jobPosting != null) {
            rightFilter.setFilterContent(jobPosting.getInterviewRoundManager().getInterviewRounds());
        } else {
            rightFilter.setFilterContent(new ArrayList<>());
        }
    }

    /**
     * A helper method for {@link #initComponents()} that initializes {@code rightFilter}.
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "Job Interview Rounds");
        add(rightFilter);
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("View/Edit", new JobManageScenario.ViewEditListener());
        buttonPanel.addButton("Add Round", new JobManageScenario.AddRoundListener());
        buttonPanel.addButton("Next Round", new JobManageScenario.NextRoundListener());
        buttonPanel.addButton("End JobPosting", new JobManageScenario.EndJobPostingListener());
        add(buttonPanel);
    }

    /**
     * Class{@code LeftFilterListener} implements ListSelectionListener.
     * It deals with the situation where a {@code JobPosting} is selected on list "Jobs I Responsible to:".
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initLeftFilter()
     * @since 2019-08-05
     */
    private class LeftFilterListener implements ListSelectionListener {
        /**
         * Implement the method {@code valueChanged} in the interface {@code ListSelectionListener}.
         * It shows all {@code InterviewRound}s of the job posting selected on the screen.
         *
         * @param e the action event that a {@code jobPosting} is selected from "Jobs I Responsive to:".
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            if (jobPosting != null) {
                setOutputText(jobPosting.toString());
                if (!jobPosting.isOpen()) {
                    jobPosting.getInterviewRoundManager().checkStatus();
                    rightFilter.setFilterContent(jobPosting.getInterviewRoundManager().getInterviewRounds());
                }
            }
        }
    }

    /**
     * Class {@code ViewEditListener} implements {@code ActionListener}.
     * It handles the situation when "View/Edit" is clicked.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class ViewEditListener implements ActionListener {
        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * The user can view and edit the selected {@code InterviewRound}.
         *
         * @param e the action event that "View/Edit" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            InterviewRound interviewRound = rightFilter.getSelectObject();
            if (interviewRound == null || jobPosting == null) {
                showMessage("No interviewRound selected!");
            } else {
                InterviewRoundScenario interviewRoundScenario = new InterviewRoundScenario(
                        getUserMenuFrame(), interviewRound, jobPosting);
                switchScenario(interviewRoundScenario);
            }
        }
    }

    /**
     * Class {@code AddRoundListener} implements {@code ActionListener}.
     * It deals with situation where "Add Round" is clicked.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class AddRoundListener implements ActionListener {

        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * A new {@code InterviewRound} can be added to the selected job if and only if the job has status
         * {@code JobPostingStatus.PROCESSING}.
         *
         * @param e the action event that "Add Round" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            String roundName = infoPanel.getInfoMap().get("Round name:");
            if (jobPosting == null) {
                showMessage("No job posting selected!");
            } else if (!jobPosting.getStatus().equals(JobPostingStatus.PROCESSING)) {
                showMessage("The status of job posting is not PROCESSING, can not add round!");
            } else {
                jobPosting.getInterviewRoundManager().addInterviewRound(new InterviewRound(roundName));
                showMessage("Succeed!");
                updateRightFilter();
            }
        }
    }

    /**
     * Class {@code NextRoundListener} implements {@code ActionListener}.
     * It handles the case when the recruiter wants to proceed a job to the next {@code InterviewRound} and thus
     * clicks on "Next Round" button.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class NextRoundListener implements ActionListener {

        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * The request can be proceeded only when the job selected is {@code JobPostingStatus.PROCESSING} and current
         * {@code InterviewRound} is {@code InterviewRoundStatus.FINISHED}.
         *
         * @param e the action event that "Next Round" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            try {
                jobPosting.getInterviewRoundManager().nextRound();
                updateRightFilter();
                showMessage("Succeeds");
            } catch (NullPointerException e1) {
                showMessage("No job posting selected!");
            } catch (WrongJobPostingStatusException e1) {
                showMessage("The status of job posting is not PROCESSING, can not start next round!");
            } catch (WrongInterviewRoundStatusException e1) {
                showMessage("The status of current round is not FINISHED, can not start next round!");
            } catch (NextRoundDoesNotExistException e1) {
                showMessage(e1.getMessage());
            }
        }
    }

    /**
     * Class {@code EndJobPostingListener} implements {@code ActionListener}.
     * It handles the case when the recruiter wants to end the recruiting process for the job and thus
     * clicks on "End JobPosting" button.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class EndJobPostingListener implements ActionListener {

        /**
         * Implement the method {@code actionPerformed} in the interface {@code ActionListener}.
         * <p>
         * Close a job if the selected job is not of status {@code JobPostingStatus.FINISHED}.
         *
         * @param e the action event that "End JobPosting" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            if (jobPosting == null) {
                showMessage("No job posting selected!");
            } else if (jobPosting.getStatus().equals(JobPostingStatus.FINISHED)) {
                showMessage("The job posting has already closed!");
            } else {
                jobPosting.endJobPosting();
                jobPosting.notifyAllFailedApplicant(getMain().getEmploymentCenter());
                updateRightFilter();
                showMessage("The jobPosting is now closed.");
            }
        }
    }
}
