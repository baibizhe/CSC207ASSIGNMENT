package gui.scenarios.applicant;

import gui.general.Scenario;
import gui.general.UserMenuFrame;
import gui.panels.ButtonPanel;
import gui.panels.FilterPanel;
import model.exceptions.CanNotEditDocumentManagerException;
import model.exceptions.DocumentAlreadyExistsException;
import model.exceptions.EmptyDocumentNameException;
import model.job.Document;
import model.job.DocumentManager;
import model.user.Applicant;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class {@code DocumentManageScenario} deals with document managing.
 *
 * @see gui.general.MenuPanel
 * @see gui.scenarios.applicant
 * @since 2019-08-06
 */
public class DocumentManageScenario extends Scenario {
    /**
     * The {@code DocumentManager} for the {@code applicant}.
     *
     * @see #DocumentManageScenario(UserMenuFrame, DocumentManager)
     * @see #update()
     * @see AddDocumentListener
     * @see DeleteDocumentListener
     */
    private DocumentManager applicantDocumentManager;
    /**
     * The {@code DocumentManager} for an {@code application}.
     *
     * @see #update()
     * @see AddDocumentListener
     * @see DeleteDocumentListener
     */
    private DocumentManager applicationDocumentManager;
    /**
     * The {@code FilterPanel} that shows a list of {@code Document}s on upper left of the page.
     */
    private FilterPanel<Document> leftFilter; // contains application document
    /**
     * The {@code FilterPanel} that shows a list of {@code Document}s in the top middle of the page.
     */
    private FilterPanel<Document> rightFilter; // contains applicant document

    /**
     * Construct a new {@code DocumentManageScenario}.
     *
     * @param userMenuFrame       the {@code UserMenuFrame} for the new {@code DocumentManageScenario}
     * @param applicationDocument the {@code DocumentManager} that manages all documents for an application
     * @see gui.general.MenuPanel
     * @see ApplicationManageScenario.EditApplicationListener
     */
    public DocumentManageScenario(UserMenuFrame userMenuFrame, DocumentManager applicationDocument) {
        super(userMenuFrame, "Document Manager");
        Applicant applicant = (Applicant) getUserMenuFrame().getUser();
        this.applicantDocumentManager = applicant.getDocumentManager();
        this.applicationDocumentManager = applicationDocument;
        this.applicantDocumentManager.updateAllDocuments();
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
     * Initialize the {@code leftFilter} such that it shows all documents of a certain {@code application}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initLeftFilter() {
        leftFilter = new FilterPanel<>(LIST_SIZE, "Application Documents");
        addShowInfoListenerFor(leftFilter);
        add(leftFilter);
    }

    /**
     * Initialize the {@code rightFilter} such that it shows all documents of the {@code applicant}.
     * It is a helper method of {@link #initComponents()}.
     */
    protected void initRightFilter() {
        rightFilter = new FilterPanel<>(LIST_SIZE, "My Documents");
        addShowInfoListenerFor(rightFilter);
        add(rightFilter);
    }

    /**
     * Override the method {@code update()} in abstract class {@code Scenario}.
     * It updates the information shown on the user interface.
     *
     * @see AddDocumentListener
     * @see DeleteDocumentListener
     */
    @Override
    protected void update() {
        if (applicationDocumentManager != null) {
            leftFilter.setFilterContent(applicationDocumentManager.getAllDocuments());
        }
        rightFilter.setFilterContent(applicantDocumentManager.getAllDocuments());
    }

    /**
     * A helper method for {@link #initComponents()} that initializes all buttons shown on the {@code buttonPanel}.
     */
    protected void initButton() {
        ButtonPanel buttonPanel = new ButtonPanel(BUTTON_PANEL_SIZE);
        buttonPanel.addButton("Add", new AddDocumentListener());
        buttonPanel.addButton("Delete", new DeleteDocumentListener());
        add(buttonPanel);
    }


    /**
     * A helper function of {@link AddDocumentListener#actionPerformed(ActionEvent)}.
     * It gets the name of a chosen file.
     *
     * @return a string representing the path of the chosen file .
     */
    private String getSubmitFileName() {
        FileDialog fileDialog = new FileDialog(getUserMenuFrame());
        fileDialog.setVisible(true);
        return fileDialog.getDirectory() + "\\" + fileDialog.getFile();
    }


    /**
     * Class {@code AddDocumentListener} deals with the situation where "Add Document" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class AddDocumentListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * If the document-managing page is entered from clicking "My Documents" on {@code MenuPanel},
         * the system will allow the user to upload a file from local.
         * If the page is entered by clicking "Edit Application" on the page that manages applications,
         * the document selected from "My Documents" list will be added to the list "Application Documents".
         *
         * @param e the action event that "Add Document" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Document document;
            DocumentManager manager;
            if (applicationDocumentManager == null) {
                document = new Document(getSubmitFileName());
                manager = applicantDocumentManager;
            } else {
                document = rightFilter.getSelectObject();
                manager = applicationDocumentManager;
            }

            try {
                manager.addDocument(document);
                update();
                showMessage("Succeed!");
            } catch (CanNotEditDocumentManagerException | EmptyDocumentNameException | DocumentAlreadyExistsException e1) {
                showMessage(e1.getMessage());
            } catch (NullPointerException e1) {
                showMessage("No document selected!");
            }
        }
    }

    /**
     * Class {@code DeleteDocumentListener } deals with the situation where "Delete Document" button is clicked.
     *
     * @see #initButton()
     * @since 2019-08-06
     */
    private class DeleteDocumentListener implements ActionListener {
        /**
         * Override the method {@code actionPerformed} in the interface {@code ActionListener}.
         * If the document-managing page is entered from clicking "My Documents" on {@code MenuPanel},
         * the system will delete a document from list "My Documents" when the button is clicked.
         * If the page is entered by clicking "Edit Application" on the page that manages applications,
         * the selected document will be removed from list "Application Document" but it still appears on "My Documents".
         *
         * @param e the action event that "Delete Document" is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Document document;
            DocumentManager manager;
            if (applicationDocumentManager == null) {
                document = rightFilter.getSelectObject();
                manager = applicantDocumentManager;
            } else {
                document = leftFilter.getSelectObject();
                manager = applicationDocumentManager;
            }

            if (document == null) {
                showMessage("No document selected!");
            } else {
                manager.removeDocument(document);
                update();
                showMessage("Succeed!");
            }
        }
    }
}
