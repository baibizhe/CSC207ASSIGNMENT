package gui.scenarios.applicant;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.FilterPanel;
import model.exceptions.ApplicationAlreadyExistsException;
import model.job.Application;
import model.job.JobPosting;
import model.storage.EmploymentCenter;
import model.user.Applicant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code JobSearchingScenario} deals with the scenario when an applicant is doing job searching.
 *
 * @see gui.general.MenuPanel
 * @since 2019-08-06
 */
public class JobSearchingScenario extends Scenario {
    /**
     * The {@code FilterPanel} that shows a list of {@code JobPosting}s on upper left of the page.
     *
     * @see #update()
     * @see #initLeftFilter()
     */
    private FilterPanel<JobPosting> leftFilter;

    /**
     * Construct a new {@code JobSearchingScenario}.
     *
     * @param userMenuFrame the {@code UserMenuFrame} for the new {@code JobSearchingScenario}
     */
    public JobSearchingScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "Job Searching");
    }

    /**
     * Override method {@code initComponents()} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initLeftFilter();
        initOutputInfoPanel();
        initButton();
    }

    /**
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     */
    @Override
    protected void update() {
        EmploymentCenter EmploymentCenter = getMain().getEmploymentCenter();
        leftFilter.setFilterContent(EmploymentCenter.getOpenJobPostings());
    }

    /**
     * Initialize the {@code leftFilter} such that it shows all {@code JobPosting}s
     * that are in {@code JobPostingStatus.OPEN} status.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "Open Jobs");
        addShowInfoListenerFor(leftFilter);
        add(leftFilter);
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Create Application", new CreateApplicationListener());
        add(buttonPanel);
    }

    /**
     * Class {@code CreateApplicationListener} deals with the situation where "Create Application" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class CreateApplicationListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * The application can be created if and only if a job on job listing is selected
         * and no such application exists.
         *
         * @param e the action event that "Create Application" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            Applicant applicant = (Applicant) getUserMenuFrame().getUser();
            try {
                applicant.addApplication(jobPosting.getJobId(), new Application(applicant, jobPosting));
                ApplicationManageScenario scenario = new ApplicationManageScenario(getUserMenuFrame());
                switchScenario(scenario);
            } catch (NullPointerException e1) {
                showMessage("No job posting selected!");
            } catch (ApplicationAlreadyExistsException e1) {
                showMessage(e1.getMessage());
            }
        }
    }
}

