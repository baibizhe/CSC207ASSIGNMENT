package gui.scenarios.applicant;

import gui.general.MenuPanel;
import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.FilterPanel;
import model.enums.ApplicationStatus;
import model.exceptions.ApplicationAlreadyExistsException;
import model.exceptions.WrongApplicationStatusException;
import model.exceptions.WrongJobPostingStatusException;
import model.job.Application;
import model.job.Document;
import model.user.Applicant;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code  ApplicationManageScenario} sets up the scenario use for managing application of an applicant.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.MenuPanel
 * @see gui.scenarios.applicant
 * @since 2019-08-06
 */
public class ApplicationManageScenario extends Scenario {
    /**
     * The {@code Applicant} whose applications are managed in this scenario.
     *
     * @see #update()
     * @see DeleteApplicationListener
     */
    private Applicant applicant;
    /**
     * The {@code FilterPanel} that shows a list of {@code Application}s on upper left of the page.
     *
     * @see #initLeftFilter()
     * @see #update()
     * @see ApplyListener
     * @see DeleteApplicationListener
     * @see EditApplicationListener
     * @see WithdrawListener
     */
    private FilterPanel<Application> leftFilter;
    /**
     * The {@code FilterPanel} that shows a list of {@code Document}s in the top middle of the page.
     *
     * @see #update()
     * @see #initRightFilter()
     * @see ViewDocumentListener
     */
    private FilterPanel<Document> rightFilter;

    /**
     * Construct a new {@code ApplicationManageScenario}.
     *
     * @param userMenuFrame the {@code UserMenuFrame} for the new {@code ApplicationManageScenario}
     * @see MenuPanel
     * @see gui.scenarios.applicant.ApplicationManageScenario
     * @see JobSearchingScenario
     */
    public ApplicationManageScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "Application Manager");
        this.applicant = (Applicant) getUserMenuFrame().getUser();
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
     * Initialize the {@code leftFilter} such that it shows all applications of the attribute {@code applicant}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "My Applications");
        addShowInfoListenerFor(leftFilter);
        add(leftFilter);
    }

    /**
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     *
     * @see ApplyListener
     * @see DeleteApplicationListener
     * @see WithdrawListener
     */
    @Override
    protected void update() {
        leftFilter.setFilterContent(applicant.getApplications());
    }

    /**
     * Update right filter according to left filter selected application
     *
     * @see LeftFilterListener
     */
    private void updateRightFilter() {
        Application application = leftFilter.getSelectObject();
        if (application == null) return;
        rightFilter.setFilterContent(application.getDocumentManager().getAllDocuments());
    }

    /**
     * A helper function for {@link #initComponents()}
     * that initializes the {@code rightFilter} and names it as "Application Documents".
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "Application Documents");
        add(rightFilter);
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Edit Application", new EditApplicationListener());
        buttonPanel.addButton("Delete Application", new DeleteApplicationListener());
        buttonPanel.addButton("Apply", new ApplyListener());
        buttonPanel.addButton("Withdraw", new WithdrawListener());
        buttonPanel.addButton("View Document", new ViewDocumentListener());
        add(buttonPanel);
    }

    /**
     * Class {@code ViewDocumentListener } deals with the situation where "View Document" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class ViewDocumentListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * It shows the content of the selected document on the screen.
         *
         * @param e the action event that "View Document" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Document document = rightFilter.getSelectObject();
            if (document != null) {
                showDocument(document);
            } else {
                showMessage("No document selected.");
            }
        }
    }

    /**
     * Class {@code  EditApplicationListener} deals with the case when "Edit Application" is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    class EditApplicationListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * The page is switched to {@code DocumentManageScenario}
         * only when the selected application is of status {@code ApplicationStatus.DRAFT}.
         *
         * @param e the action event that "Edit Application" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Application application = leftFilter.getSelectObject();
            if (application == null) {
                showMessage("No application selected!");
            } else if (!application.getStatus().equals(ApplicationStatus.DRAFT)) {
                showMessage("Application status is not DRAFT, can not edit!");
            } else {
                DocumentManageScenario scenario = new DocumentManageScenario(getUserMenuFrame(),
                        application.getDocumentManager());
                switchScenario(scenario);
            }
        }
    }

    /**
     * Class {@code ApplyListener } deals with the situation where "Apply" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class ApplyListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * The selected application can be submitted if and only if
         * it is an {@code ApplicationStatus.DRAFT} application to an open job posting and
         * the {@code applicant} has never applied to the job before.
         *
         * @param e the action event that "Apply" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Application application = leftFilter.getSelectObject();
            try {
                application.apply(getMain().getEmploymentCenter());
                update();
                showMessage("Succeed!");
            } catch (NullPointerException e1) {
                showMessage("No application selected!");
            } catch (WrongApplicationStatusException e1) {
                showMessage("Application status is not DRAFT, can not apply!");
            } catch (ApplicationAlreadyExistsException e1) {
                showMessage("You already submitted an application for this job posting!");
            } catch (WrongJobPostingStatusException e1) {
                showMessage("The job posting is not open, can not apply!");
            }
        }
    }

    /**
     * Class {@code WithdrawListener } deals with the situation where "Withdraw" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class WithdrawListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * The selected application can be withdrawn if and only if
         * it is an application of status {@code ApplicationStatus.PENDING}.
         *
         * @param e the action event that "Withdraw" button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Application application = leftFilter.getSelectObject();
            try {
                application.cancel(getMain().getEmploymentCenter());
                showMessage("Withdrawal succeeds!");
                update();
            } catch (NullPointerException e1) {
                showMessage("No application selected!");
            } catch (WrongApplicationStatusException e1) {
                showMessage("This application is not pending, can not cancel!");
            }
        }
    }

    /**
     * Class {@code DeleteApplicationListener } deals with the situation where "Delete Application" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class DeleteApplicationListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * The selected application can be deleted only when its status is {@code ApplicationStatus.DRAFT}.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Application application = leftFilter.getSelectObject();
            try {
                applicant.deleteApplication(application);
                update();
            } catch (NullPointerException e1) {
                showMessage("No application selected!");
            } catch (WrongApplicationStatusException e1) {
                showMessage("Status for this application is not DRAFT, can not delete.");
            }
        }
    }

    /**
     * Class {@code LeftFilterListener } deals with the situation where left filter is being selected to some value.
     *
     * @see #updateRightFilter()
     * @since 2019-08-08
     */
    private class LeftFilterListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            updateRightFilter();
        }
    }
}
