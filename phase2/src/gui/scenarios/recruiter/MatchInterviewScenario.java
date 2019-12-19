package gui.scenarios.recruiter;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.FilterPanel;
import model.exceptions.WrongInterviewStatusException;
import model.job.Application;
import model.job.Interview;
import model.job.InterviewRound;
import model.storage.EmploymentCenter;
import model.user.Applicant;
import model.user.Company;
import model.user.Employee;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class {@code MatchInterviewScenario} handles the situation of matching interview.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.MenuPanel
 * @since 2019-08-05
 */
public class MatchInterviewScenario extends Scenario {

    /**
     * An {@code InterviewRound} for this scenario.
     *
     * @see #update()
     * @see MatchListener
     */
    private InterviewRound interviewRound;

    /**
     * The {@code LeftFilterPanel} in this scenario.
     *
     * @see #initComponents()
     * @see #initLeftFilter()
     * @see #update()
     * @see MatchListener
     */
    private FilterPanel<Application> leftFilter;

    /**
     * The {@code RightFilterPanel} in this scenario.
     *
     * @see #update()
     * @see #initRightFilter()
     * @see MatchListener
     */
    private FilterPanel<Employee> rightFilter;

    /**
     * Create a new {@code MatchInterviewScenario} that is a {@code Scenario} with title "Match Interview".
     *
     * @param userMenuFrame  the {@code userMenuFrame} that sets up the gui framework
     * @param interviewRound interview round that is matching
     */
    public MatchInterviewScenario(UserMenuFrame userMenuFrame, InterviewRound interviewRound) {
        super(userMenuFrame, "Match Interview");
        this.interviewRound = interviewRound;
    }

    /**
     * Override {@code initComponents} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initLeftFilter();
        initRightFilter();
        initOutputInfoPanel();
        initButton();
    }

    /**
     * A helper method for {@code initComponents()} that initializes and add the new {@code ButtonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Match", new MatchListener());
        add(buttonPanel);
    }

    /**
     * A helper method for {@code initComponents()} that initializes {@code leftFilter}.
     *
     * @see #initComponents()
     */

    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "Unmatched Applications");
        add(leftFilter);
    }

    /**
     * Override {@code update()} in abstract class {@code Scenario}.
     */

    @Override
    protected void update() {
        leftFilter.setFilterContent(interviewRound.getUnmatchedApplications());
        EmploymentCenter EmploymentCenter = getMain().getEmploymentCenter();
        Company company = getUserMenuFrame().getCompany();
        ArrayList<Employee> interviewers = EmploymentCenter.getInterviewers(company.getInterviewerIds());
        rightFilter.setFilterContent(interviewers);
    }

    /**
     * A helper method for {@code initComponents()} that initializes {@code rightFilter}.
     *
     * @see #initComponents()
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "Company Interviewers");
        add(rightFilter);
    }

    /**
     * Class{@code MatchListener} implements ActionListener. It deals with actions related to matching interviews.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initLeftFilter()
     * @since 2019-08-05
     */
    private class MatchListener implements ActionListener {
        /**
         * Match the application with a corresponding interviewer.
         *
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Employee interviewer = rightFilter.getSelectObject();
            Application application = leftFilter.getSelectObject();
            Interview interview;
            try {
                interview = application.getInterviewByRound(interviewRound.getRoundName());
                interview.match(interviewer, interviewRound.getRoundName());
                Applicant applicant = application.getApplicant(getMain().getEmploymentCenter());
                applicant.receiveMessage("You received a new interview!");
                showMessage("Succeed!");
                update();
            } catch (NullPointerException e1) {
                showMessage("Please select one application and one interviewer!");
            } catch (WrongInterviewStatusException e1) {
                showMessage("Status of interview is not UNMATCHED, can not match!");
            }
        }
    }
}
