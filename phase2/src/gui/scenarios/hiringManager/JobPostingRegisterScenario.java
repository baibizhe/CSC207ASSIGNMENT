package gui.scenarios.hiringManager;

import gui.general.MenuPanel;
import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.ComponentFactory;
import gui.panels.InputInfoPanel;
import main.Main;
import model.enums.UserType;
import model.job.JobPosting;
import model.storage.EmploymentCenter;
import model.user.Company;
import model.user.Employee;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

/**
 * Class {@code JobPostingRegisterScenario} handles the situation where the hiring manager wants to create a new job posting.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.MenuPanel
 * @since 2019-08-05
 */
public class JobPostingRegisterScenario extends Scenario {

    /**
     * An {@code InputInfoPanel} that allows text input in this scenario.
     *
     * @see #update()
     * @see #initInput()
     * @see #createJobInfoMap()
     * @see CreateJobPostingListener
     */
    private InputInfoPanel infoPanel;

    /**
     * Create a new {@code JobPostingRegisterScenario} that is a {@code Scenario} with title "Create Job Posting".
     *
     * @param userMenuFrame the {@code userMenuFrame} that sets up the gui framework
     * @see MenuPanel
     */
    public JobPostingRegisterScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "Create Job Posting");
    }

    /**
     * Override method {@code initComponents()} in abstract class {@code Scenario}.
     */
    @Override
    protected void initComponents() {
        initInput();
        initButton();
    }

    /**
     * Override {@code update()} in abstract class {@code Scenario}.
     */
    @Override
    protected void update() {
        infoPanel.clear();
    }

    /**
     * A helper method for {@code initComponents()} that initializes the {@code infoPanel}.
     *
     * @see #initComponents()
     */
    protected void initInput() {
        infoPanel = new InputInfoPanel(REGISTER_INPUT_SIZE, true);
        ComponentFactory factory = infoPanel.getComponentFactory();
        String[] recruiters = getUserMenuFrame().getCompany().getRecruiterIds().toArray(new String[0]);
        factory.addComboBox("Recruiter:", recruiters);
        factory.addTextField("Position name:");
        factory.addTextField("Num of positions:");
        factory.addTextField("Close date:");
        String[] documentsOption = new String[]{"Required", "Optional"};
        String[] extraDocumentsOption = new String[]{"Not allowed", "Allowed 1", "Allowed up to 3", "No restriction"};
        factory.addComboBox("CV:", documentsOption);
        factory.addComboBox("Cover letter:", documentsOption);
        factory.addComboBox("Reference:", documentsOption);
        factory.addComboBox("Extra document:", extraDocumentsOption);
        add(infoPanel);
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Post job", new CreateJobPostingListener());
        add(buttonPanel);
    }

    /**
     * A helper function for {@link  CreateJobPostingListener#actionPerformed(ActionEvent)} that
     * creates a map which contains basic job information obtained from gui.
     *
     * @return a map that contains the basic job information obtained from the user interface
     */
    private HashMap<String, String> createJobInfoMap() {
        HashMap<String, String> infoMap = infoPanel.getInfoMap();
        Company company = getUserMenuFrame().getCompany();
        infoMap.put("Post date:", Main.getCurrentDate().toString());
        infoMap.put("Company id:", company.getId());
        infoMap.put("Job id:", company.getId() + "--" + infoMap.get("Position name:") + "--" +
                LocalDateTime.now());
        return infoMap;
    }

    /**
     * Check and return whether an integer is valid, that is, whether it starts from a non-zero digit.
     * It is a helper method for {@link #isValidJobInfoMap(HashMap)}.
     *
     * @param integer the integer to be checked
     * @return true if and only if the integer does not begin with zero
     */
    private boolean isValidInt(String integer) {
        return integer.matches("[1-9][0-9]*");
    }

    /**
     * Check and return whether a date is valid, that is return whether the date is today or after today.
     * It is a helper method for {@link #isValidJobInfoMap(HashMap)}.
     *
     * @param date the date to be checked
     * @return true only when the date passed in is no earlier than now
     */
    private boolean isValidDate(String date) {
        try {
            return !LocalDate.parse(date).isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * A helper method for {@link CreateJobPostingListener#actionPerformed(ActionEvent)} that checks validity
     * and returns a message that will be shown on gui indicating corresponding information.
     *
     * @param map a hash map containing the information entered into gui
     * @return a message about which part of the information is missing; "Good" when the input is valid
     */
    private String isValidJobInfoMap(HashMap<String, String> map) {
        if (map.containsValue("")) {
            return "Please fill all information";
        } else if (!isValidInt(map.get("Num of positions:"))) {
            return "Please type in right form of Number of positions";
        } else if (!isValidDate(map.get("Close date:"))) {
            return "Please type in right form of Close date";
        }
        return "Good";
    }

    /**
     * Class{@code CreateJobPostingListener} implements {@code ActionListener}.
     * It deals with the situation in which the button "Post job" is clicked.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see #initButton()
     * @since 2019-08-05
     */
    private class CreateJobPostingListener implements ActionListener {

        /**
         * Override {@code actionPerformed} in interface {@code ActionListener}.
         * A job will be successfully posted when all required information is filled.
         *
         * @param e the action event of clicking "Post Job"
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (withdrawAction()) return;
            HashMap<String, String> values = createJobInfoMap();
            EmploymentCenter employmentCenter = getMain().getEmploymentCenter();
            Employee employee = employmentCenter.getEmployee(values.get("Recruiter:"), UserType.RECRUITER);
            Company company = getUserMenuFrame().getCompany();
            if (isValidJobInfoMap(values).equals("Good")) {
                JobPosting jobPosting = new JobPosting(values);
                employmentCenter.addJobPosting(jobPosting);
                company.addJobPostingId(jobPosting.getJobId());
                employee.addFile(jobPosting);
                employee.receiveMessage("You got a new Job Posting to manage!");
                showMessage("Successfully post job!");
                infoPanel.clear();
            } else {
                showMessage(isValidJobInfoMap(values));
            }
        }
    }
}
