package gui.panels;

import model.job.Document;

import javax.swing.*;
import java.awt.*;

/**
 * Class {@code OutputInfoPanel} setup gui panel to interfaces output text to users.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.Scenario
 * @since 2019-08-05
 */
public class OutputInfoPanel extends JPanel {

    // The font that is used
    static final Font FONT = new Font("Times New Roman", Font.PLAIN, 15);

    // The dimension of frames showing document content
    private static final Dimension DOCUMENT_FRAME_SIZE = new Dimension(600, 400);

    /**
     * The text area to interfaces text.
     *
     * @see #setup(Dimension)
     * @see #setOutputText(String)
     */
    private JTextArea textArea = new JTextArea();

    /**
     * Create a new {@code OutputInfoPanel} with given dimension of its size.
     *
     * @param dimension Dimension of the output panel.
     */
    public OutputInfoPanel(Dimension dimension) {
        setup(dimension);
    }

    /**
     * Set up an output info panel.
     * Set the layout and scroll panel for text area.
     * @param dimension the dimension of output info panel
     */
    public void setup(Dimension dimension) {
        setPreferredSize(dimension);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(dimension);
        add(scrollPane);

        textArea.setFont(FONT);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
    }

    public void setOutputText(String text) {
        textArea.setText(text);
    }

    /**
     * Show the given document on the panel by getting its information from the toString method.
     * @param document the document needed to be performed
     */
    public void showDocument(Document document) {
        OutputInfoPanel outputInfo = new OutputInfoPanel(DOCUMENT_FRAME_SIZE);
        outputInfo.setOutputText(document.getContent());
        new DocumentFrame(document.getDocumentName(), outputInfo);
    }

    /**
     * Class {@code DocumentFrame} extends JFrame.
     * It creates a frame to interfaces the information for given document.
     */
    private class DocumentFrame extends JFrame {

        /**
         * Constructor for {@code DocumentFrame}.
         * @param title he name of the document want to be shown
         * @param document he document wanted to be shown
         */
        DocumentFrame(String title, JPanel document) {
            setTitle(title);
            setResizable(false);
            setSize(new Dimension(DOCUMENT_FRAME_SIZE.width + 20, DOCUMENT_FRAME_SIZE.height + 45));
            setLayout(new FlowLayout());
            add(document);
            setVisible(true);
        }
    }
}
