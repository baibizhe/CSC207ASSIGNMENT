package gui.panels;

import model.interfaces.Filterable;
import model.job.JobPosting;
import model.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Class {@code Filter} filters through a list of objects that implemented {@code Filterable}
 * and stores the objects after filtration.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see gui.panels.FilterPanel
 * @see Filterable
 * @since 2019-08-05
 */
public class Filter<T extends Filterable> {

    /**
     * The list of instances to filter through. All instances need to implement
     * {@code Filterable} interface.
     *
     * @see #getHeadings()
     * @see #setFilterContent(ArrayList)
     * @see #filter()
     */
    private ArrayList<T> filterContent;

    /**
     * The resulting list of instances(filterable) after filtration.
     *
     * @see #getResults()
     * @see #getSelectedItem(int)
     * @see #filter()
     */
    private ArrayList<T> results = new ArrayList<>();

    /**
     * Keywords for filtering. It has format "(.+;)*(.+)".
     *
     * @see #getFilterValues()
     * @see #setFilterString(String)
     */
    private String filterString = "";

    public void setFilterContent(ArrayList<T> filterContent) {
        this.filterContent = filterContent;
    }

    /**
     * Separate the string into String[] where each element is a keyword for filtering.
     *
     * @return String[] where each element is a keyword for filtering
     */
    private String[] getFilterValues() {
        return filterString.split("[;]");
    }

    void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    /**
     * Return true if and only if the beginning of {@code value} matches {@code filterValue}.
     *
     * @param value       the value for determining whether match {@code filterValue} or not
     * @param filterValue the sample to be filtered against
     * @return true if and only if the beginning of {@code value} matches {@code filterValue}
     */
    private boolean isValueMatched(String value, String filterValue) {
        return value.toLowerCase().startsWith(filterValue.toLowerCase());
    }

    /**
     * Return true if and only if at least one value in the map given by {@code filterable}
     * matches one of the values in {@code filterString}
     * @param filterable    a class that implemented {@code Filterable} interface
     * @return true if and only if at least one value in the map given by {@code filterable}
     * matches one of the values in {@code filterString}
     * @see #getFilterValues()
     */
    private boolean isMatched(T filterable) {
        for (String value : filterable.getFilterMap().values()) {
            for (String filterValue : getFilterValues()) {
                if (isValueMatched(value, filterValue)) return true;
            }
        }
        return false;
    }

    /**
     * Filter through all the objects in {@code filterContent} and store those
     * that matches in {@code result}.
     * @see #isMatched(Filterable)
     */
    void filter() {
        results.clear();
        if (filterContent == null) return;
        for (T filterable : filterContent) {
            if (isMatched(filterable)) results.add(filterable);
        }
    }

    T getSelectedItem(int index) {
        return getResults().get(index);
    }

    ArrayList<T> getResults() {
        return results;
    }

    /**
     * Get headings from objects in {@code filterContent}.
     * @return headings from objects in {@code filterContent}
     * @see Filterable#getFilterMap()
     */
    String[] getHeadings() {
        if (filterContent.size() != 0) {
            Collection<String> headingCollection = filterContent.get(0).getFilterMap().keySet();
            return new ArrayList<>(headingCollection).toArray(new String[0]);
        } else return null;
    }

    /**
     * Get search values from {@code filterable} given headings.
     * @param filterable    an object that implements {@code Filterable} interface
     * @param headings  the heading for all the search values needed from {@code Filterable}
     * @return search values from {@code filterable}
     * @see Filterable#getFilterMap()
     */
    String[] getSearchValues(T filterable, String[] headings) {
        String[] searchValues = new String[headings.length];
        for (int i = 0; i < headings.length; i++) {
            searchValues[i] = filterable.getFilterMap().get(headings[i]);
        }
        return searchValues;
    }}


