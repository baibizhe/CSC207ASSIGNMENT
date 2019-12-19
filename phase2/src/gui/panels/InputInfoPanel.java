package gui.panels;

import gui.general.LoginFrame;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class {@code InputInfoPanel} setup gui panel to interfaces output text to users.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.Scenario
 * @see LoginFrame
 * @see ComponentFactory
 * @since 2019-08-05
 */
public class InputInfoPanel extends JScrollPane {

    /**
     * The container of showed components.
     *
     * @see #addComponent(String, JComponent)
     * @see #setup(Dimension, boolean)
     */
    private Container container;

    /**
     * The ComponentFactory that responsible for adding components.
     *
     * @see ComponentFactory
     * @see #addComponent(String, JComponent)
     * @see #factorySetup(Dimension, boolean)
     */
    private ComponentFactory componentFactory;

    /**
     * The password fields that handles user entered password.
     *
     * @see #addComponent(String, JComponent)
     * @see #passwordMatched()
     * @see #getPassword()
     */
    private JPasswordField[] passwordFields = new JPasswordField[2];

    /**
     * The collection of components that handles user input.
     *
     * @see #addComponent(String, JComponent)
     * @see #clear()
     * @see #getInfoMap()
     */
    private HashMap<String, JComponent> componentMap = new HashMap<>();

    /**
     * Create a panel for input information with given dimension.
     *
     * @param dimension dimension of the panel
     */
    public InputInfoPanel(Dimension dimension) {
        setup(dimension, false);
        factorySetup(dimension, false);
    }

    /**
     * Create a {@code InputInfoPanel} for input information with given dimension and
     * a boolean of whether it is a vertical panel.
     * @param dimension the dimension of the panel
     * @param vertical boolean of whether the panel is vertical or not
     */
    public InputInfoPanel(Dimension dimension, boolean vertical) {
        setup(dimension, vertical);
        factorySetup(dimension, vertical);
    }

    /**
     * Helper function for constructor to create a panel.
     * If the passed in boolean is true, then create a vertical box.
     * If the passed in boolean is false, then create a new panel with the given dimension.
     * @param dimension the dimension of the panel
     * @param vertical boolean of whether the panel is vertical or not
     */
    public void setup(Dimension dimension, boolean vertical) {
        setPreferredSize(dimension);
        if (vertical) {
            container = Box.createVerticalBox();
        } else {
            container = new JPanel();
            container.setPreferredSize(new Dimension(dimension.width - 20, dimension.height - 20));
        }
        setViewportView(container);
    }

    /**
     * Create components for the input information panel.
     * @param dimension dimension of the panel
     * @param vertical boolean deciding whether it is vertical
     * @see #componentFactory
     */
    private void factorySetup(Dimension dimension, boolean vertical) {
        if (vertical) {
            componentFactory = new ComponentFactory(this, dimension.width * 3 / 4);
        } else {
            componentFactory = new ComponentFactory(this, dimension.width / 2);
        }
    }

    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Add components to the panel.
     * If the given component is password field, create a field for user to type in the password.
     * If the given component is not password field, create a component of what it is.
     * @param name the description of the component
     * @param component the type of the component
     * @see #passwordFields
     * @see #componentMap
     */
    void addComponent(String name, JComponent component) {
        JPanel panel = new JPanel();
        panel.add(componentFactory.getLabel(name));
        panel.add(component);
        container.add(panel);
        if (component instanceof JPasswordField) {
            if (passwordFields[0] == null) passwordFields[0] = (JPasswordField) component;
            else if (passwordFields[1] == null) passwordFields[1] = (JPasswordField) component;
        } else if (component instanceof JScrollPane) {
            componentMap.put(name, (JComponent) ((JScrollPane) component).getViewport().getView());
        } else {
            componentMap.put(name, component);
        }
    }

    /**
     * If given component is text field, return what is typed in the text field.
     * If given component is a combo box, return what is selected.
     * @param component the type of component
     * @return the text in the given component
     */
    private String getText(JComponent component) {
        if (component instanceof JTextComponent) {
            return ((JTextComponent) component).getText();
        } else if (component instanceof JComboBox) {
            Object object = ((JComboBox) component).getSelectedItem();
            if (object instanceof String) return (String) object;
        }
        return "";
    }

    /**
     * Check if the passwords in the two fields the user typed in matches.
     * @return true if the password of two fields matches and false if they don't match
     */
    private boolean passwordMatched() {
        JPasswordField passwordField = passwordFields[0];
        JPasswordField confirmPasswordField = passwordFields[1];
        if (passwordField == null) {
            return false;
        } else if (confirmPasswordField == null) {
            return true;
        } else {
            return Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword());
        }
    }

    /** Return the password the user typed in if the two fields have the same password.
     * @return the password the user typed in if the two fields have the same password
     */
    public char[] getPassword() {
        if (passwordMatched()) return passwordFields[0].getPassword();
        else return new char[0];
    }


    /**
     * Clear text field.
     * If the type of component is text, clear the text field, else do nothing.
     */
    public void clear() {
        for (JComponent component : componentMap.values()) {
            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText("");
            }
        }
    }

    /**
     * Return a HashMap which its key is a string of the name of the component and the value
     * is the text in the correspond component.
     * @return a HashMap which its key and value are both String
     */
    public HashMap<String, String> getInfoMap() {
        HashMap<String, String> infoMap = new HashMap<>();
        for (String componentName : componentMap.keySet()) {
            JComponent component = componentMap.get(componentName);
            infoMap.put(componentName, getText(component));
        }
        return infoMap;
    }
}
