package model.interfaces;

/**
 * Classes implement interface {@code ShowAble} will interfaces information to
 * users in GUI.
 *
 * @author group 0120 of CSC207 summer 2019
 * @since 2019-08-06
 */
public interface ShowAble {

    String toString();

    /**
     * Helper method for {@code toString} method.
     *
     * @param header      header
     * @param description value for each header
     * @return a structured line of header and value pair
     */
    default String getInfoString(String header, String description) {
        return header + ": " + description + "\n";
    }
}
