package gui.scenarios.userRegister;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.ComponentFactory;
import gui.panels.InputInfoPanel;
import model.enums.UserType;
import model.exceptions.*;
import model.storage.UserFactory;
import model.user.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class {@code UserRegisterScenario} setup a Scenario that deal with user register
 *
 * @author group 0120 of CSC207 summer 2019
 * @see UserType
 * @see InputInfoPanel
 * @see ButtonPanel
 * @see Scenario
 * @since 2019-08-05
 */
public class UserRegisterScenario extends Scenario {

    /**
     * The type of user that going to create
     *
     * @see UserType
     * @see #createUserAndRegister()
     * @see #initInput()
     */
    private UserType registerType;

    /**
     * The panel deal with all user input
     *
     * @see InputInfoPanel
     * @see #createUserAndRegister()
     * @see #initInput()
     */
    private InputInfoPanel infoPanel;

    /**
     * Constructor for new {@code UserRegisterScenario}.
     *
     * @param userMenuFrame given UserMenu
     * @param registerType  given {@code UserType}
     */
    public UserRegisterScenario(UserMenuFrame userMenuFrame, UserType registerType) {
        super(userMenuFrame, "User Register");
        this.registerType = registerType;
    }

    /**
     * Constructor for new {@code UserRegisterScenario}.
     *
     * @param userMenuFrame given user menu
     */
    public UserRegisterScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "User Register");
    }

    @Override
    protected void initComponents() {
        initInput();
        initButton();
    }

    @Override
    protected void update() {
        infoPanel.clear();
    }

    protected void initInput() {
        infoPanel = new InputInfoPanel(REGISTER_INPUT_SIZE, true);
        ComponentFactory factory = infoPanel.getComponentFactory();
        initUserInput(factory);
        if (registerType == null) initEmployeeInput(factory);
        else initApplicantInput(factory);
        add(infoPanel);
    }

    private void initUserInput(ComponentFactory factory) {
        factory.addTextField("Username:");
        factory.addPasswordField("Password:");
        factory.addPasswordField("Confirm Password:");
        factory.addTextField("First name:");
        factory.addTextField("Last/Family name:");
        factory.addTextField("Email:");
    }

    /**
     * Initial the information that need to be filled by {@code Applicant} in order to
     * create a new {@code Applicant}.
     *
     * @param factory the factory that construct the page
     */
    private void initApplicantInput(ComponentFactory factory) {
        String[] employmentStatus = new String[]{"Student", "Employee", "Other"};
        factory.addComboBox("Employment status:", employmentStatus);
        String[] experiences = new String[]{"0-3 years", "3-5 years", "5-8 years", "8+ years"};
        factory.addComboBox("Work experiences:", experiences);
        String[] diplomas = new String[]{"High School", "Bachelor", "Master", "Doctor", "Other"};
        factory.addComboBox("Education background:", diplomas);
        String[] major = new String[]{
                "Math", "CompScience", "Economics", "Finance", "Education", "Art", "Engineer", "Statistics", "Other"
        };
        factory.addComboBox("Major in:", major);
    }

    /**
     * Initial the information that need to be filled by {@code Employee} in order to
     * create a new {@code Employee}.
     *
     * @param factory the factory that construct the page
     */
    private void initEmployeeInput(ComponentFactory factory) {
        String[] positions = new String[]{"Interviewer", "Recruiter", "Hiring_Manager"};
        factory.addComboBox("Position:", positions);
        factory.addTextField("Company id:");
    }

    /**
     * Initial the buttonPanel and create a new button called "Create User" with a new {@code CreateUserListener}.
     *
     * @see #initComponents()
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Create User", new CreateUserListener());
        add(buttonPanel);
    }

    /**
     * Create user and try to register, throw exceptions if something goes wrong
     *
     * @throws UnmatchedPasswordException  password is not matched
     * @throws WrongEmailFormatException   email is not in correct format
     * @throws UserAlreadyExistsException  create an user that already exist
     * @throws CompanyAlreadyExistsException if this worker is the hiring manager of company,
     *                                       cannot create an account with pre-exist company id
     * @throws CompanyDoesNotExistException  if this worker is other worker rather than hiring manager,
     *                                       should register into a pre-exist company
     * @see CreateUserListener
     */
    private User createUserAndRegister()
            throws UnmatchedPasswordException, WrongEmailFormatException, UserAlreadyExistsException,
            CompanyAlreadyExistsException, CompanyDoesNotExistException {
        HashMap<String, String> infoMap = infoPanel.getInfoMap();
        infoMap.put("Password:", Arrays.toString(infoPanel.getPassword()));
        if (registerType == null) registerType = UserType.valueOf(infoMap.get("Position:").toUpperCase());
        return new UserFactory(getMain().getEmploymentCenter()).createUser(infoMap, registerType);
    }

    /**
     * Class {@code CreateUserListener} Listener to register a new user.
     *
     * @see UserRegisterScenario
     * @since 2019-08-05
     */
    private class CreateUserListener implements ActionListener {
        /**
         * Create a user given input values.
         *
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                createUserAndRegister();
                showMessage("Successfully registered!");
                getUserMenuFrame().setVisible(false);
                getMain().returnToLogin(getUserMenuFrame());
            } catch (UnmatchedPasswordException | WrongEmailFormatException | UserAlreadyExistsException |
                    CompanyAlreadyExistsException | CompanyDoesNotExistException e1) {
                showMessage(e1.getMessage());
            }
        }
    }
}
