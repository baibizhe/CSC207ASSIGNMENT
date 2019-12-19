package model.interfaces;

import gui.panels.Filter;

import java.util.HashMap;

/**
 * Classes that implement interface {@code Filterable} would be able to
 * be filtered through.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see Filter
 * @since 2019-08-06
 */
public interface Filterable {

    /**
     * Return a hash map where each entry is a heading, search value pair.
     *
     * @return a hash map where each entry is a heading, search value pair
     */
    HashMap<String, String> getFilterMap();

}
