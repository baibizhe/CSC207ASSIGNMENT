package gui.general;

import main.Main;
import model.exceptions.NotEmployeeException;
import model.storage.EmploymentCenter;
import model.user.Company;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code UserMenuFrame} setup gui frame for user menu with common buttons and provide getters of information.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see Main
 * @see User
 * @see MenuPanel
 * @see Scenario
 * @since 2019-08-05
 */
public class UserMenuFrame extends JFrame {

    // Frame Size
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;

    // Related dimension of components in certain ratio
    static final Dimension SCENARIO_SIZE = new Dimension(WIDTH * 4 / 5 - 20, HEIGHT - 50);
    private static final Dimension MENU_SIZE = new Dimension(WIDTH / 5 - 20, HEIGHT - 50);
    private static final Dimension BUTTON_SIZE = new Dimension(WIDTH / 6, 50);

    /**
     * The {@code Main} that the program is based on.
     *
     * @see Main
     * @see #getMain()
     * @see #logout()
     */
    private Main main;

    /**
     * The user.
     *
     * @see User
     * @see #getUser()
     * @see #getCompany()
     * @see #addLogoutButton()
     */
    private User user;

    /**
     * The panel contains all buttons.
     *
     * @see MenuPanel
     * @see #setup()
     * @see #addLogoutButton()
     */
    private MenuPanel menu;

    /**
     * The panel be able to do main operations.
     *
     * @see Scenario
     * @see #clearScenario()
     * @see #setScenario(Scenario)
     */
    private Scenario scenario;

    public UserMenuFrame() {
    }

    /**
     * Create a new {@code UserMenuFrame}, and then setup for this menu.
     *
     * @param main given java.main
     * @param user given user
     */
    public UserMenuFrame(Main main, User user) {
        this.main = main;
        this.user = user;
        setup();
        showColor();
    }

    private void showColor() {
        menu.setBackground(Color.BLUE);
    }

    /**
     * Setup the width and height, set the layout to flow layout and set visible to true.
     *
     * @see #UserMenuFrame(Main, User)
     */
    private void setup() {
        setSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new FlowLayout());
        setResizable(false);
        menu = new MenuPanel(this, MENU_SIZE, BUTTON_SIZE);
        menu.setBackground(Color.BLUE);
        addLogoutButton();
        add(menu);
        ((JButton) menu.getComponent(0)).doClick();
        setVisible(true);
    }

    /**
     * Return a {@code Company} that this {@code User} belongs to.
     *
     * @return a {@code Company} that this {@code User} belongs to
     */
    public Company getCompany() {
        EmploymentCenter employmentCenter = getMain().getEmploymentCenter();
        try {
            return employmentCenter.getCompany(user.getCompanyId());
        } catch (NotEmployeeException e) {
            return null;
        }
    }

    /**
     * Remove scenario from JFrame.
     */
    private void clearScenario() {
        if (scenario != null) remove(this.scenario);
    }

    public void setScenario(Scenario scenario) {
        clearScenario();
        this.scenario = scenario;
        scenario.init();
        add(scenario);
        scenario.updateUI();
    }

    public User getUser() {
        return user;
    }

    Main getMain() {
        return main;
    }

    /**
     * Add the LogoutButton to {@code UserMenuFrame} if the user is not a {@code NullUser}.
     */
    private void addLogoutButton() {
        String text;
        if (user.isNull()) text = "Back";
        else text = "Logout";
        JButton button = new JButton(text);
        button.setPreferredSize(BUTTON_SIZE);
        button.addActionListener(new LogoutListener());
        menu.add(button);
    }

    /**
     * Set this {@code UserMenuFrame} to be invisible and return to login scenario.
     */
    private void logout() {
        this.setVisible(false);
        getMain().returnToLogin(this);
    }

    /**
     * Class {@code LogoutListener} is the listener used for logout event.
     *
     * @author group 0120 of CSC207 summer 2019
     * @see UserMenuFrame
     * @since 2019-08-05
     */
    private class LogoutListener implements ActionListener {
        /**
         * Log out.
         *
         * @param e the actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }
    }
}
