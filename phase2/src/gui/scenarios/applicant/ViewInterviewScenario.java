package gui.scenarios.applicant;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.FilterPanel;
import model.job.Interview;
import model.user.Applicant;

/**
 * Class {@code ViewInterviewScenario} deals with the case when an applicant wants to view all his/her interviews.
 *
 * @see gui.general.MenuPanel
 * @since 2019-08-06
 */
public class ViewInterviewScenario extends Scenario {

    /**
     * The {@code FilterPanel} that shows a list of ongoing {@code Interview}s on upper left of the page.
     *
     * @see #update()
     * @see #initLeftFilter()
     */
    private FilterPanel<Interview> leftFilter;

    /**
     * The {@code FilterPanel} that shows a list of past {@code Interview}s in the top middle of the page.
     *
     * @see #update()
     * @see #initRightFilter()
     */
    private FilterPanel<Interview> rightFilter;

    /**
     * Construct a new {@code ViewInterviewScenario}.
     *
     * @param userMenuFrame the {@code UserMenuFrame} for the new {@code JobSearchingScenario}
     * @see gui.general.MenuPanel
     */
    public ViewInterviewScenario(UserMenuFrame userMenuFrame) {
        super(userMenuFrame, "View Interview");
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
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     */
    @Override
    protected void update() {
        Applicant applicant = (Applicant) getUserMenuFrame().getUser();
        leftFilter.setFilterContent(applicant.getOngoingInterviews());
        rightFilter.setFilterContent(applicant.getPastInterviews());
    }

    /**
     * Initialize the {@code leftFilter} such that it shows all ongoing {@code Interviews}s of the {@code applicant}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "My Ongoing Interviews");
        addShowInfoListenerFor(leftFilter);
        add(leftFilter);
    }

    /**
     * Initialize the {@code rightFilter} such that it shows all past {@code Interviews}s of the {@code applicant}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "My Past Interviews");
        addShowInfoListenerFor(rightFilter);
        add(rightFilter);
    }
}
