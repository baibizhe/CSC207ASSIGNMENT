package gui.general;

import gui.scenarios.applicant.ApplicationManageScenario;
import gui.scenarios.applicant.DocumentManageScenario;
import gui.scenarios.applicant.JobSearchingScenario;
import gui.scenarios.applicant.ViewInterviewScenario;
import gui.scenarios.hiringManager.JobPostingRegisterScenario;
import gui.scenarios.hiringManager.ViewPostingScenario;
import gui.scenarios.interviewer.OngoingInterviewScenario;
import gui.scenarios.recruiter.ApplicationScenario;
import gui.scenarios.recruiter.JobManageScenario;
import gui.scenarios.userRegister.UserRegisterScenario;
import model.enums.UserType;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code MenuPanel} setup gui panel for buttons that varies in different types of user menu.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see UserMenuFrame
 * @since 2019-08-05
 */
public class MenuPanel extends JPanel {

    /**
     * The user menu that contains this panel.
     *
     * @see #setup()
     * @see #registerMenuSetup()
     * @see #interviewerMenuSetup()
     * @see #recruiterMenuSetup()
     * @see #hiringManagerMenuSetup()
     * @see #applicantMenuSetup()
     * @see MenuPanel.SwitchScenarioListener#actionPerformed(ActionEvent)
     */
    private UserMenuFrame userMenuFrame;

    /**
     * The {@code Dimension} that represents size of button.
     *
     * @see #addMenuButton(String, Scenario)
     */
    private Dimension buttonSize;

    /**
     * The {@code Dimension} that represents size of menus.
     *
     * @see #setup()
     */
    private Dimension menuSize;

    /**
     * Create a new {@code MenuPanel}.
     *
     * @param userMenuFrame {@code UserMenuFrame}
     * @param menuSize      dimension of the menuSize
     * @param buttonSize    dimension of the buttonSize
     * @see UserMenuFrame
     */
    public MenuPanel(UserMenuFrame userMenuFrame, Dimension menuSize, Dimension buttonSize) {
        this.userMenuFrame = userMenuFrame;
        this.menuSize = menuSize;
        this.buttonSize = buttonSize;
        setup();
    }

    /**
     * A helper method that sets up the {@code userMenuFrame} according to the user's type.
     * It sets up the register page if the user is a {@code NullUser}.
     *
     * @see #MenuPanel(UserMenuFrame, Dimension, Dimension)
     */
    private void setup() {
        setPreferredSize(menuSize);
        setLayout(new FlowLayout());
        User user = userMenuFrame.getUser();
        if (user.isNull()) registerMenuSetup();
        else if (user.getUserType().equals(UserType.APPLICANT)) applicantMenuSetup();
        else if (user.getUserType().equals(UserType.INTERVIEWER)) interviewerMenuSetup();
        else if (user.getUserType().equals(UserType.RECRUITER)) recruiterMenuSetup();
        else if (user.getUserType().equals(UserType.HIRING_MANAGER)) hiringManagerMenuSetup();
    }

    /**
     * A helper method that sets up the register frame for {@code setup()}.
     *
     * @see #setup()
     */
    private void registerMenuSetup() {
        addMenuButton("Applicant", new UserRegisterScenario(userMenuFrame, UserType.APPLICANT));
        addMenuButton("Employee", new UserRegisterScenario(userMenuFrame));
    }

    /**
     * A helper method for {@code setup()} that sets up the interviewer menu.
     *
     * @see #setup()
     */
    private void interviewerMenuSetup() {
        addMenuButton("Ongoing Interview", new OngoingInterviewScenario(userMenuFrame));
    }

    /**
     * A helper method for {@code setup()} that sets up the recruiter menu.
     *
     * @see #setup()
     */
    private void recruiterMenuSetup() {
        addMenuButton("All Applications", new ApplicationScenario(userMenuFrame));
        addMenuButton("JobManaging", new JobManageScenario(userMenuFrame));
    }

    /**
     * A helper method for {@code setup()} that sets up the hiring manager menu.
     *
     * @see #setup()
     */
    private void hiringManagerMenuSetup() {
        addMenuButton("Create Posting", new JobPostingRegisterScenario(userMenuFrame));
        addMenuButton("View Posting", new ViewPostingScenario(userMenuFrame));
    }

    /**
     * A helper method for {@code setup()} that sets up an applicant menu.
     *
     * @see #setup()
     */
    private void applicantMenuSetup() {
        addMenuButton("Upcoming Interviews", new ViewInterviewScenario(userMenuFrame));
        addMenuButton("Apply Jobs", new JobSearchingScenario(userMenuFrame));
        addMenuButton("Manage Application", new ApplicationManageScenario(userMenuFrame));
        addMenuButton("My Documents", new DocumentManageScenario(userMenuFrame, null));
    }

    /**
     * Add a new {@code JButton} for the given {@code Scenario}.
     *      * The page will be switched to that {@code Scenario} when this new button is clicked.
     *      *
     *      * @param buttonName a string representing button name
     *      * @param scenario   the {@code Scenario} to which the new button is linked
     *      * @see #registerMenuSetup()
     *      * @see #interviewerMenuSetup()
     *      * @see #recruiterMenuSetup()
     *      * @see #hiringManagerMenuSetup()
     *      * @see #applicantMenuSetup()
     */
    private void addMenuButton(String buttonName, Scenario scenario) {
        JButton button = new JButton(buttonName);
        button.setPreferredSize(buttonSize);
        button.addActionListener(new SwitchScenarioListener(scenario));
        add(button);
    }

    /**
     * Class {@code SwitchScenarioListener} implements interface {@code ActionListener}.
     * It is used to switch between different scenarios.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #addMenuButton(String, Scenario)
     * @since 2019-08-05
     */
    private class SwitchScenarioListener implements ActionListener {
        /**
         * The {@code Scenario} to be switched to.
         *
         * @see #SwitchScenarioListener(Scenario)#actionPerformed(ActionEvent)
         */
        private Scenario scenario;

        /**
         * Create a new {@code SwitchScenarioListener}.
         *
         * @param scenario the {@code Scenario} to be switched to
         * @see #addMenuButton(String, Scenario)
         */
        SwitchScenarioListener(Scenario scenario) {
            this.scenario = scenario;
        }

        /**
         * Override the method {@code actionPerformed} in interface {@code ActionListener}.
         *
         * @param e the event of clicking on related button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            userMenuFrame.setScenario(scenario);
        }
    }
}
