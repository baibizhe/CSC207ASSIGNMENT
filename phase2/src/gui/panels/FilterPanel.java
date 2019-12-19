package gui.panels;

import model.interfaces.Filterable;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class {@code FilterPanel} setup gui panel for showing a list of instances(filterable)
 * and be able to search and filter through instances.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.general.Scenario
 * @since 2019-08-05
 */
public class FilterPanel<T extends Filterable> extends JPanel {

    // Settings related to components sizes
    private static final int TITLE_HEIGHT = 30;
    private static final int SEARCH_BUTTON_WIDTH = 80;
    private static final int SEARCH_BUTTON_HEIGHT = 30;

    /**
     * The filter that calculate remaining instance.
     *
     * @see Filter
     * @see SearchListener
     * @see #getFilter()
     * @see #update()
     * @see #setFilterContent(ArrayList)
     */
    private Filter<T> filter = new Filter<>();

    /**
     * The filter table that shows results to users
     *
     * @see #getFilterTable()
     * @see #filterTableSetup(Dimension)
     * @see #getSelectObject()
     * @see #addSelectionListener(ListSelectionListener)
     */
    private JTable filterTable = new JTable();

    /**
     * The table model that updates result in filter table
     *
     * @see NotEditableTableModel
     * @see #update()
     * @see #filterTableSetup(Dimension)
     */
    private DefaultTableModel tableModel = new NotEditableTableModel();

    /**
     * Create a {@code FilterPanel} with given dimension and title.
     *
     * @param dimension the dimension of the filter
     * @param title     title of the filter
     */
    public FilterPanel(Dimension dimension, String title) {
        setup(dimension, title);
    }

    private JTable getFilterTable() {
        return filterTable;
    }

    private DefaultTableModel getTableModel() {
        return tableModel;
    }

    private Filter<T> getFilter() {
        return filter;
    }

    /**
     * Update the content of filter panel.
     */
    private void update() {
        getTableModel().setRowCount(0);
        Filter<T> filter = getFilter();
        filter.filter();
        String[] headings = filter.getHeadings();
        if (headings == null) return;
        getTableModel().setColumnIdentifiers(headings);
        for (T result : getFilter().getResults()) {
            getTableModel().addRow(filter.getSearchValues(result, headings));
        }
        updateUI();
    }

    /**
     * Set up a new filter with given dimension and title of what the filter is for.
     * @param dimension dimension of filter
     * @param title title of filter
     */
    public void setup(Dimension dimension, String title) {
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        int width = dimension.width - 10;
        titleSectionSetup(new Dimension(width, TITLE_HEIGHT), title);
        searchSectionSetup(width);
        int tableHeight = dimension.height - TITLE_HEIGHT - SEARCH_BUTTON_HEIGHT - 30;
        filterTableSetup(new Dimension(width, tableHeight));
    }


    /**
     * Set up the title for the filter.
     * @param dimension dimension of the filter
     * @param title title of filter
     */
    private void titleSectionSetup(Dimension dimension, String title) {
        JLabel label = new JLabel(title);
        label.setPreferredSize(dimension);
        add(label);
    }

    /**
     * Add a search section with a new JButton with a new SearchListener in it.
     *
     * @param width the width of the searchSection
     * @see #setup(Dimension, String)
     */
    private void searchSectionSetup(int width) {
        JTextField textField = new JTextField("XXX; XX; XXX");
        textField.setPreferredSize(new Dimension(width - SEARCH_BUTTON_WIDTH - 10, SEARCH_BUTTON_HEIGHT));

        JButton button = new JButton("Search");
        button.setPreferredSize(new Dimension(SEARCH_BUTTON_WIDTH, SEARCH_BUTTON_HEIGHT));
        button.addActionListener(new SearchListener(textField));

        add(button);
        add(textField);
    }

    /**
     * Set up the filter table with given dimension.
     *
     * @param dimension the size of the filter table
     */
    private void filterTableSetup(Dimension dimension) {
        JTable filterTable = getFilterTable();
        filterTable.setModel(getTableModel());
        JScrollPane scrollPane = new JScrollPane(filterTable);
        scrollPane.setPreferredSize(dimension);
        add(scrollPane);
    }

    /**
     * Return the selected object in the filter table, if nothing is selected, return {@code null}.
     * @return the selected object in the filter table, if nothing is selected, return {@code null}
     */
    public T getSelectObject() {
        int index = getFilterTable().getSelectedRow();
        if (index == -1) return null;
        return getFilter().getSelectedItem(index);
    }

    public void addSelectionListener(ListSelectionListener listener) {
        getFilterTable().getSelectionModel().addListSelectionListener(listener);
    }

    public void setFilterContent(ArrayList<T> filterContent) {
        getFilter().setFilterContent(filterContent);
        update();
    }

    private class NotEditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Class {@code SearchListener} is the {@code ActionListener} for searching.
     */
    private class SearchListener implements ActionListener {

        private JTextField textField;

        private SearchListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getFilter().setFilterString(textField.getText());
            update();
        }
    }
}