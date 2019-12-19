package gui.scenarios.hiringManager;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.FilterPanel;
import model.job.Application;
import model.job.JobPosting;
import model.storage.EmploymentCenter;
import model.user.Company;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

/**
 * Class {@code ViewPostingScenario} handles the situation where the hiring manager wants to view job postings
 * of the company.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see #initLeftFilter()
 * @see #initRightFilter()
 * @see gui.general.MenuPanel
 * @since 2019-08-05
 */
public class ViewPostingScenario extends Scenario {

    /**
     * The {@code FilterPanel} located leftmost in the interface. It contains a list of {@code JobPosting}s.
     *
     * @see #initLeftFilter()
     * @see #update()
     * @see LeftFilterListener
     */
    private FilterPanel<JobPosting> leftFilter;

    /**
     * The {@code FilterPanel} located middle in this interface. It contains a list of {@code Application}s.
     *
     * @see #initRightFilter()
     * @see #update()
     * @see LeftFilterListener
     * @see RightFilterListener
     */
    private FilterPanel<Application> rightFilter;

    /**
     * Create a new {@code ViewPostingScenario} that is a {@code Scenario} with title "View Company Job Postings".
     *
     * @param userMenuFrame the {@code userMenuFrame} that sets up the gui framework
     * @see gui.general.MenuPanel
     */
    public ViewPostingScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "View Company Job Postings");
    }

    /**
     * Override method {@code initComponents()} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initLeftFilter();
        initRightFilter();
        initOutputInfoPanel();
    }

    /**
     * Initialize the {@code leftFilter} such that it shows all job postings in the company.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "All JobPostings");
        leftFilter.addSelectionListener(new ViewPostingScenario.LeftFilterListener());
        add(leftFilter);
    }

    /**
     * Initialize the {@code rightFilter} such that it shows all applications of the {@code JobPosting} selected
     * on the left {@code FilterPanel}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "JobPosting Applications");
        rightFilter.addSelectionListener(new ViewPostingScenario.RightFilterListener());
        add(rightFilter);
    }

    /**
     * Override {@code update()} in abstract class {@code Scenario}.
     */
    @Override
    protected void update() {
        Company company = getUserMenuFrame().getCompany();
        EmploymentCenter EmploymentCenter = getMain().getEmploymentCenter();
        ArrayList<JobPosting> jobPostings = EmploymentCenter.getJobPostingsByIds(company.getJobPostingIds());
        leftFilter.setFilterContent(jobPostings);
        JobPosting jobPosting = leftFilter.getSelectObject();
        if (jobPosting != null) {
            rightFilter.setFilterContent(jobPosting.getApplications());
        }
    }

    /**
     * Class{@code LeftFilterListener} implements ListSelectionListener.
     * It deals with the case when a job posting is selected on the list "All JobPostings" shown on left filter panel.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initLeftFilter()
     * @since 2019-08-05
     */
    private class LeftFilterListener implements ListSelectionListener {

        /**
         * Override {@code valueChanged} in interface {@code ListSelectionListener}.
         * All applications of the job posting will be shown on the right {@code FilterPanel} when a job is selected.
         *
         * @param e the action event of selecting an object from list "All JobPostings".
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JobPosting jobPosting = leftFilter.getSelectObject();
            if (jobPosting != null) {
                rightFilter.setFilterContent(jobPosting.getApplications());
                setOutputText(jobPosting.toString());
            }
        }
    }

    /**
     * Class{@code RightFilterListener} implements {@code ListSelectionListener}.
     * It deals with the occasion when an {@code Application} is selected from list "JobPosting Applications".
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initRightFilter()
     * @since 2019-08-05
     */
    private class RightFilterListener implements ListSelectionListener {

        /**
         * Override {@code ListSelectionEvent} in interface {@code ListSelectionListener}.
         * The detailed information about the selected {@code Application} will be shown on top right of the page.
         *
         * @param e the action event of selecting an application from "JobPosting Applications"
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Application application = rightFilter.getSelectObject();
            if (application != null) {
                setOutputText(application.detailedToStringForEmployee(getMain().getEmploymentCenter()));
            }
        }
    }

}
