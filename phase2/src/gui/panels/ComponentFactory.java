package gui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Class {@code ComponentFactory} adds desired components to InputInfoPanel.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see InputInfoPanel
 * @since 2019-08-05
 */
public class ComponentFactory {
    // Enum of all component types
    private enum ComponentType {LABEL, COMBO_BOX, AREA, FIELD}

    // Component height for all created components(except text area)
    private static final int COMPONENT_HEIGHT = 45;

    /**
     * The location where all created components are added to.
     *
     * @see InputInfoPanel
     * @see #addComboBox(String, String[], String, boolean)
     * @see #addTextField(String, String, boolean)
     * @see #addPasswordField(String)
     * @see #addTextArea(String, String, boolean)
     */
    private InputInfoPanel infoPanel;

    /**
     * Store the sizes of all components.
     *
     * @see #setComponentSize(int)
     * @see #getComponentSize(ComponentType)
     */
    private HashMap<ComponentType, Dimension> sizes = new HashMap<>();

    /**
     * Constructor for {@code ComponentFactory}.
     * @param infoPanel the destination of all created components
     * @param width the desired width of each component group(the total width of a JLabel
     *              with another component handle user input )
     * @see InputInfoPanel
     */
    ComponentFactory(InputInfoPanel infoPanel, int width) {
        this.infoPanel = infoPanel;
        setComponentSize(width);
    }

    /**
     * Set the size for the components with the given width.
     * @param width the width of the components
     */
    private void setComponentSize(int width) {
        int labelWidth = width / 3;
        int inputComponentWidth = width * 3 / 5;
        sizes.put(ComponentType.LABEL, new Dimension(labelWidth, COMPONENT_HEIGHT));
        sizes.put(ComponentType.COMBO_BOX, new Dimension(inputComponentWidth, COMPONENT_HEIGHT));
        sizes.put(ComponentType.FIELD, new Dimension(inputComponentWidth, COMPONENT_HEIGHT));
        sizes.put(ComponentType.AREA, new Dimension(inputComponentWidth, COMPONENT_HEIGHT * 2));
    }

    /**
     * Return the size of the given component type.
     * @param type the type of the component
     * @return the size of the given component type
     */
    private Dimension getComponentSize(ComponentType type) {
        return sizes.get(type);

    }


    /**
     * Create a text field and specify what the textField is for.
     * Set the textField to be editable by user.
     * @param name name of the text field
     */
    public void addTextField(String name) {
        addTextField(name, "", true);
    }


    /**
     * Create a combo box  and specify what it is for.
     * Set the combo box to be uneditable.
     * @param name the name of what the combo box is for
     * @param options the options for the combo box
     */
    public void addComboBox(String name, String[] options) {
        addComboBox(name, options, null, false);
    }

    /**
     * Create a text area and specify what the area is for.
     * Make the area editable by user.
     * @param name the name of what the text area is for
     */
    public void addTextArea(String name) {
        addTextArea(name, "", true);
    }


    /**
     * Return a component from the given component type name.
     * @param name the name of the component
     * @return component from the given component type name
     */
    Component getLabel(String name) {
        JLabel label = new JLabel(name);
        label.setPreferredSize(getComponentSize(ComponentType.LABEL));
        return label;
    }

    /**
     * Create a combo box with a set of options.
     * Specify what the combo box is for and decide whether it is editable by the boolean it is passed in.
     * @param name the name of the combo box
     * @param options the options in the combo box
     * @param defaultValue the default selected value
     * @param editable boolean of whether the combo box is editable
     * @see #infoPanel
     */
    public void addComboBox(String name, String[] options, String defaultValue, boolean editable) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedItem(defaultValue);
        comboBox.setEditable(editable);
        comboBox.setPreferredSize(getComponentSize(ComponentType.COMBO_BOX));
        infoPanel.addComponent(name, comboBox);
    }

    /**
     * Create a text field.
     * @param name the name of text field
     * @param defaultValue set the output of the text as default value
     * @param editable boolean for whether the textField is editable
     */
    private void addTextField(String name, String defaultValue, boolean editable) {
        JTextField textField = new JTextField();
        textField.setText(defaultValue);
        textField.setEditable(editable);
        textField.setPreferredSize(getComponentSize(ComponentType.FIELD));
        infoPanel.addComponent(name, textField);
    }


    /**
     * Create a JPasswordFiled with given name.
     * @param name the name of the field, which would be password
     */
    public void addPasswordField(String name) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(getComponentSize(ComponentType.FIELD));
        infoPanel.addComponent(name, passwordField);
    }

    /**
     * Add a  text area with scroll pane.
     * @param name the name of the text area
     * @param defaultValue the output of the area, which is default value
     * @param editable boolean of whether the area is editable
     */

    private void addTextArea(String name, String defaultValue, boolean editable) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(OutputInfoPanel.FONT);
        textArea.setLineWrap(true);
        textArea.setEditable(editable);
        textArea.setText(defaultValue);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(getComponentSize(ComponentType.AREA));
        infoPanel.addComponent(name, scrollPane);
    }
}
