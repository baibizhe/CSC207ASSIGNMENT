package gui.general;

import gui.panels.FilterPanel;
import gui.panels.OutputInfoPanel;
import gui.scenarios.hiringManager.JobPostingRegisterScenario;
import gui.scenarios.userRegister.UserRegisterScenario;
import main.Main;
import model.enums.UserType;
import model.exceptions.NotOverrideException;
import model.job.Document;
import model.job.DocumentManager;
import model.job.InterviewRound;
import model.job.JobPosting;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * Class {@code Scenario} setup the panel where main operation takes place and
 * contains useful shared methods that all child classes have
 *
 * @author group 0120 of CSC207 summer 2019
 * @see UserMenuFrame
 * @see OutputInfoPanel
 * @since 2019-08-05
 */
public abstract class Scenario extends JPanel {

    // Panel size
    private static final int WIDTH = UserMenuFrame.SCENARIO_SIZE.width;
    private static final int HEIGHT = UserMenuFrame.SCENARIO_SIZE.height;

    // Related dimension of components in certain ratio
    protected final Dimension REGULAR_INPUT_SIZE = getDimensionByRatio(1, 0.3);
    protected final Dimension REGISTER_INPUT_SIZE = getDimensionByRatio(1, 0.8);
    protected final Dimension LIST_SIZE = getDimensionByRatio(0.3, 0.5);
    protected final Dimension BUTTON_PANEL_SIZE = getDimensionByRatio(1, 0.2);
    private final Dimension OUTPUT_SIZE = getDimensionByRatio(0.4, 0.5);

    /**
     * The {@code UserMenuFrame} that will show in this scenario.
     *
     * @see #getUserMenuFrame()
     */
    private UserMenuFrame userMenuFrame;

    /**
     * The {@code OutputInfoPanel} for this scenario.
     *
     * @see #initOutputInfoPanel()
     * @see #setOutputText(String)
     * @see #showDocument(Document)
     */
    private OutputInfoPanel outputInfoPanel = new OutputInfoPanel(OUTPUT_SIZE);

    /**
     * True only when the scenario has been initialized.
     *
     * @see #init()
     */
    private boolean hasInit;

    /**
     * create a new {@code UserMenuFrame}with given {@code UserMenuFrame} and given title
     *
     * @param userMenuFrame the user menu that needs to be passed in
     * @param title         the title of this scenario
     * @see gui.scenarios.applicant.DocumentManageScenario#DocumentManageScenario(UserMenuFrame, DocumentManager)
     * @see gui.scenarios.applicant.ApplicationManageScenario#ApplicationManageScenario(UserMenuFrame)
     * @see UserRegisterScenario#UserRegisterScenario(UserMenuFrame, UserType)
     * @see UserRegisterScenario#UserRegisterScenario(UserMenuFrame)
     * @see gui.scenarios.applicant.JobSearchingScenario#JobSearchingScenario(UserMenuFrame)
     * @see gui.scenarios.applicant.ViewInterviewScenario#ViewInterviewScenario(UserMenuFrame)
     * @see JobPostingRegisterScenario#JobPostingRegisterScenario(UserMenuFrame)
     * @see gui.scenarios.hiringManager.ViewPostingScenario#ViewPostingScenario(UserMenuFrame)
     * @see gui.scenarios.interviewer.OngoingInterviewScenario#OngoingInterviewScenario(UserMenuFrame)
     * @see gui.scenarios.recruiter.ApplicationScenario#ApplicationScenario(UserMenuFrame)
     * @see gui.scenarios.recruiter.InterviewRoundScenario#InterviewRoundScenario(UserMenuFrame, InterviewRound, JobPosting)
     * @see gui.scenarios.recruiter.JobManageScenario#JobManageScenario(UserMenuFrame)
     * @see gui.scenarios.recruiter.MatchInterviewScenario#MatchInterviewScenario(UserMenuFrame, InterviewRound)
     */
    protected Scenario(UserMenuFrame userMenuFrame, String title) {
        this.userMenuFrame = userMenuFrame;
        setTitle(title);
    }

    /**
     * Initialize a scenario if it has never been initialized.
     *
     * @see UserMenuFrame#setScenario(Scenario)
     */
    void init() {
        if (!hasInit) {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setLayout(new FlowLayout());
            initComponents();
            hasInit = true;
        }
        update();
    }

    /**
     * A abstract helper function for {@code init()}.
     * It is implemented in subclasses of {@code Scenario}.
     *
     * @see #init()
     */
    abstract protected void initComponents();

    /**
     * A abstract helper function for {@code init()}.
     * It is implemented in subclasses of {@code Scenario}.
     *
     * @see #init()
     */
    abstract protected void update();

    /**
     * Set the frame size by ratios.
     *
     * @param horizontalRatio the horizontal ratio
     * @param verticalRatio   the vertical ratio
     * @return the {@code Dimension} matching the ratios
     */
    private Dimension getDimensionByRatio(double horizontalRatio, double verticalRatio) {
        return new Dimension((int) (WIDTH * horizontalRatio) - 5, (int) (HEIGHT * verticalRatio) - 5);
    }

    /**
     * Add an {@code outputInfoPanel} to this frame.
     * It can be overridden in its subclasses.
     */
    protected void initOutputInfoPanel() {
        add(outputInfoPanel);
    }

    /**
     * Switch to another {@code Scenario}.
     *
     * @param scenario the {@code Scenario} that the system will switch to
     */
    protected void switchScenario(Scenario scenario) {
        getUserMenuFrame().setScenario(scenario);
    }


    protected UserMenuFrame getUserMenuFrame() {
        return userMenuFrame;
    }

    protected Main getMain() {
        return getUserMenuFrame().getMain();
    }

    /**
     * Show message in the {@code outputInfoPanel}.
     *
     * @param text the string need to be shown
     * @see ShowInfoListener#valueChanged(ListSelectionEvent)
     */
    protected void setOutputText(String text) {
        outputInfoPanel.setOutputText(text);
    }

    /**
     * Show content of a certain document in the user interface.
     *
     * @param document the {@code Document} that needs to be shown
     */
    protected void showDocument(Document document) {
        outputInfoPanel.showDocument(document);
    }

    /**
     * Add a new {@code showInfoListener} to the given {@code filterPanel}.
     *
     * @param filterPanel the {@code filterPanel} to which a new {@code ShowInfoListener} will be added
     */
    protected void addShowInfoListenerFor(FilterPanel filterPanel) {
        filterPanel.addSelectionListener(new ShowInfoListener(filterPanel));
    }

    /**
     * Display a pop-up message on the screen.
     *
     * @param message the message that needs to be shown
     */
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(getUserMenuFrame(), message, "Message Dialog", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Return whether an action is confirmed.
     *
     * @return true if and only if the action is confirmed
     * @see JobPostingRegisterScenario
     */
    protected boolean withdrawAction() {
        return 0 != JOptionPane.showConfirmDialog(getUserMenuFrame(), "Are you sure?",
                "Confirm Dialog", JOptionPane.YES_NO_OPTION);
    }

    protected void setTitle(String title) {
        getUserMenuFrame().setTitle(title + Main.getCurrentDate().toString());
    }

    /**
     * A method that wishes whenever desired then override with same signature(therefore do not want to make abstract)
     *
     * @throws NotOverrideException Children class uses this method without override.
     */
    protected void initLeftFilter() throws NotOverrideException {
        throw new NotOverrideException();
    }

    /**
     * A method that wishes whenever desired then override with same signature(therefore do not want to make abstract)
     *
     * @throws NotOverrideException Children class uses this method without override.
     */
    protected void initRightFilter() throws NotOverrideException {
        throw new NotOverrideException();
    }

    /**
     * A method that wishes whenever desired then override with same signature(therefore do not want to make abstract)
     *
     * @throws NotOverrideException Children class uses this method without override.
     */
    protected void initButton() throws NotOverrideException {
        throw new NotOverrideException();
    }

    /**
     * A method that wishes whenever desired then override with same signature(therefore do not want to make abstract)
     *
     * @throws NotOverrideException Children class uses this method without override.
     */
    protected void initInput() throws NotOverrideException {
        throw new NotOverrideException();
    }

    /**
     * Class {@code ShowInfoListener} implements {@code ListSelectionListener}.
     * It handles the situation where related information of the selected value should be shown.
     *
     * @see Scenario#addShowInfoListenerFor(FilterPanel)
     * @since 2019-08-06
     */
    private class ShowInfoListener implements ListSelectionListener {
        /**
         * The {@code FilterPanel} containing the list where a selection is made.
         *
         * @see #valueChanged(ListSelectionEvent)
         */
        private FilterPanel filterPanel;

        /**
         * Create a new {@code ShowInfoListener} for the given {@code FilterPanel}.
         *
         * @param filterPanel the {@code FilterPanel} where selection is made
         */
        private ShowInfoListener(FilterPanel filterPanel) {
            this.filterPanel = filterPanel;
        }

        /**
         * Override {@code valueChanged} in interface{@code ListSelectionListener}.
         * It shows the information of selected object when an action is performed.
         *
         * @param e the event of selecting from a list
         * @see #setOutputText(String)
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (filterPanel.getSelectObject() != null) {
                String info = filterPanel.getSelectObject().toString();
                setOutputText(info);
            }
        }
    }
}
