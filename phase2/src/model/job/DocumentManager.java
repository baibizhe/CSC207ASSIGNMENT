package model.job;

import model.exceptions.CanNotEditDocumentManagerException;
import model.exceptions.DocumentAlreadyExistsException;
import model.exceptions.EmptyDocumentNameException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class {@code DocumentManager} deals with all the documents for either
 * an {@code Applicant} or an {@code Application}.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see model.user.Applicant
 * @see Application
 * @since 2019-08-04
 */
public class DocumentManager implements Serializable {

    private static final long serialVersionUID = -1699054066935007390L;

    /**
     * An array list of documents.
     *
     * @see #getAllDocuments()
     * @see #addDocument(Document)
     * @see #removeDocument(Document)
     */
    private ArrayList<Document> documents = new ArrayList<>();

    /**
     * True if and only if the holder is able to add/remove document.
     * That happens if the holder is {@code Applicant} or if the holder
     * is {@code Application} and it has not been submitted yet.
     *
     * @see #isEditable()
     * @see #setEditable(boolean)
     */
    private boolean editable;


    /**
     * Create a new document manager.
     *
     * @param editable determines whether the holder is allowed to modify documents
     */
    public DocumentManager(boolean editable) {
        this.editable = editable;
    }

    private boolean isEditable() {
        return this.editable;
    }

    void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Add document if and only if this document's name is not empty and holder is allowed to
     * modify document manager.
     *
     * @param document a new document to add to this manager
     * @throws CanNotEditDocumentManagerException holder is not allowed to modify document manager
     * @throws EmptyDocumentNameException         document name is empty, can not add
     * @throws DocumentAlreadyExistsException     document already exists
     * @see #isEditable()
     */
    public void addDocument(Document document) throws CanNotEditDocumentManagerException, EmptyDocumentNameException,
            DocumentAlreadyExistsException {
        if (!this.isEditable()) {
            throw new CanNotEditDocumentManagerException();
        } else if (document.getDocumentName().equals("")) {
            throw new EmptyDocumentNameException();
        } else if (documents.contains(document)) {
            throw new DocumentAlreadyExistsException();
        } else {
            documents.add(document);
        }
    }

    /**
     * Remove the document from document manager.
     *
     * @param document the name of the document wished to remove
     */
    public void removeDocument(Document document) {
        documents.remove(document);
    }

    public ArrayList<Document> getAllDocuments() {
        return documents;
    }

    /**
     * Update all the documents it currently hold.
     *
     * @see Document#update()
     * @see Document#shouldDelete()
     */
    public void updateAllDocuments() {
        ArrayList<Document> temp = new ArrayList<>();
        for (Document document : getAllDocuments()) {
            document.update();
            if (!document.shouldDelete()) temp.add(document);
        }
        documents = temp;
    }
}
