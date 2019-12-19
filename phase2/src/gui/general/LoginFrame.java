package gui.general;

import gui.panels.ButtonPanel;
import gui.panels.ComponentFactory;
import gui.panels.InputInfoPanel;
import main.Main;
import model.enums.UserType;
import model.exceptions.CannotSaveSystemException;
import model.exceptions.NotIntegerException;
import model.user.NullUser;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Class {@code LoginFrame} setup gui frame for login and deal with user input.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see InputInfoPanel
 * @see ButtonPanel
 * @see Main
 * @since 2019-08-05
 */
public class LoginFrame extends JFrame {

    // Frame size
    private static final int WIDTH = 520;
    private static final int HEIGHT = 300;

    // Related dimension of components in certain ratio
    private static final Dimension INPUT_SIZE = new Dimension(WIDTH - 20, HEIGHT - 100);
    private static final Dimension BUTTON_PANEL_SIZE = new Dimension(WIDTH - 20, 50);

    // LoginFrame user types
    private static final String[] USER_TYPE = new String[]{"Applicant", "Hiring_Manager", "Recruiter", "Interviewer"};

    /**
     * The {@code Main} that the program is based on.
     *
     * @see #getMain()
     * @see #login(User)
     */
    private Main main;

    /**
     * The {@code InputInfoPanel} that contains all text fields in this frame.
     *
     * @see InputInfoPanel
     * @see LoginListener
     * @see #getInputInfoPanel()
     * @see #infoPanelSetup()
     * @see #getUser()
     */
    private InputInfoPanel inputInfoPanel = new InputInfoPanel(INPUT_SIZE, true);

    /**
     * The {@code ButtonPanel} that contains all buttons in this frame.
     *
     * @see ButtonPanel
     * @see #buttonPanelSetup()
     */
    private ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);

    /**
     * Construct a new {@code LoginFrame}.
     *
     * @param main the {@code Main} that the program is based on
     */
    public LoginFrame(Main main) {
        this.main = main;
        if (!getMain().isSuccessfullyLoaded()) {
            JOptionPane.showMessageDialog(this, "Warning! Not successfully loaded data!");
        }
        setup();
    }

    /**
     * A helper method for the constructor.
     * Set up the title, size, layout, infoPanel, buttonPanel and visibility of LoginFrame.
     *
     * @see #LoginFrame(Main)
     */
    private void setup() {
        setTitle();
        setResizable(false);
        setSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new FlowLayout());
        infoPanelSetup();
        buttonPanelSetup();
        setVisible(true);
    }

    private void setTitle() {
        setTitle("LoginFrame" + Main.getCurrentDate().toString());
    }

    /**
     * A helper method for setup() that adds "Login", "Register", "Restart" buttons to button panel.
     *
     * @see #setup()
     */
    private void buttonPanelSetup() {
        buttonPanel.addButton("Login", new LoginListener());
        buttonPanel.addButton("Register", new RegisterListener());
        buttonPanel.addButton("Restart", new SaveSystemListener());
        add(buttonPanel);
    }

    /**
     * A helper method for setup() that sets up the {@code InputInfoPanel}.
     * This panel allows a user to choose the user type as well as enter his/her username and password.
     *
     * @see #setup()
     */
    private void infoPanelSetup() {
        ComponentFactory factory = getInputInfoPanel().getComponentFactory();
        factory.addTextField("Username:");
        factory.addPasswordField("Password:");
        factory.addComboBox("UserType:", USER_TYPE, "Applicant", false);
        add(getInputInfoPanel());
    }

    /**
     * Get the {@code Main}.
     *
     * @return {@code main}
     * @see #LoginFrame(Main)
     * @see #getUser()
     * @see #login(User)
     * @see LoginListener
     * @see SaveSystemListener
     */
    public Main getMain() {
        return main;
    }


    private InputInfoPanel getInputInfoPanel() {
        return inputInfoPanel;
    }

    /**
     * A helper function for {@code actionPerformed} in LoginListener.
     * It searches and returns the {@code User} whose username and user type
     * match inputs of text fields behind "UserType:" and "Username:".
     *
     * @see LoginListener#actionPerformed(ActionEvent)
     */
    private User getUser() {
        HashMap<String, String> infoMap = getInputInfoPanel().getInfoMap();
        String userType = infoMap.get("UserType:").toUpperCase();
        String userName = infoMap.get("Username:");
        return getMain().getEmploymentCenter().getUser(userName, UserType.valueOf(userType));
    }

    /**
     * A helper function for {@code actionPerformed} in LoginListener.
     * Check and return whether a user is entering correct password.
     *
     * @param user     the {@code User} whose password is waiting to be checked
     * @param password the {@code char[]} that the user enters as the password
     * @see LoginListener#actionPerformed(ActionEvent)
     */
    private boolean checkUser(User user, char[] password) {
        if (!user.isNull() && user.matchPassword(password)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Sorry, user or password is incorrect!");
            return false;
        }
    }

    /**
     * A helper method that hides the log in page and shows a new {@code UserMenuFrame} for the {@code User}.
     *
     * @param user the {@code User} who is logging in
     * @see LoginListener#actionPerformed(ActionEvent)
     * @see RegisterListener#actionPerformed(ActionEvent)
     */
    private void login(User user) {
        this.setVisible(false);
        JFrame frame = new UserMenuFrame(getMain(), user);
        String message = user.getMessage();
        if (!message.equals("")) {
            JOptionPane.showMessageDialog(frame, message);
        }
    }

    /**
     * Class {@code LoginListener} implements {@code ActionListener}.
     * It deals with the case when "Login" button is clicked.
     *
     * @see LoginFrame#buttonPanelSetup()
     */
    private class LoginListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in interface {@code ActionListener}.
         *
         * @param e the {@code ActionEvent} of clicking on button "Log in"
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = getUser();
            char[] password = getInputInfoPanel().getPassword();
            if (checkUser(user, password)) {
                getMain().getEmploymentCenter().updateOpenJobPostings();
                login(user);
            }
        }
    }

    /**
     * Class {@code RegisterListener} implements {@code ActionListener}.
     * It deals with the situation where "Register" button is clicked.
     *
     * @see LoginFrame#buttonPanelSetup()
     */
    private class RegisterListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in interface {@code ActionListener}.
         * A {@code NullUser} is created when action occurs.
         *
         * @param e the {@code ActionEvent} of clicking on button "Register"
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            login(new NullUser());
        }
    }

    /**
     * Class {@code SaveSystemListener} implements {@code ActionListener}.
     * It deals with the situation where "Restart" button is clicked.
     *
     * @see LoginFrame#buttonPanelSetup()
     */
    private class SaveSystemListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in interface {@code ActionListener}.
         * When clicking "Restart", the system saves data generated from this round and allows users to reset the date.
         *
         * @param e the {@code ActionEvent} of clicking on button "Restart"
         */

        @Override
        public void actionPerformed(ActionEvent e) {
            Main main = getMain();
            String message = "How many days elapse before restart system";
            try {
                main.saveSystem();
                Main.setDaysElapse(JOptionPane.showInputDialog(LoginFrame.this, message));
            } catch (CannotSaveSystemException | NotIntegerException e1) {
                JOptionPane.showMessageDialog(LoginFrame.this, e1.getMessage());
            }
            JOptionPane.showMessageDialog(LoginFrame.this, Main.getCurrentDate());
            setTitle("LoginFrame" + Main.getCurrentDate().toString());
        }
    }
}
