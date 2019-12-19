package main;

import gui.general.LoginFrame;
import model.exceptions.CannotSaveSystemException;
import model.exceptions.NotIntegerException;
import model.job.Document;
import model.storage.EmploymentCenter;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;

/**
 * Class {@code Main} handles loading, saving, timing and provide {@code EmploymentCenter} to other class,
 * Also, system start at this point and create a new {@code LoginFrame}
 *
 * @author group 0120 of CSC207 summer 2019
 * @see LoginFrame
 * @see EmploymentCenter
 * @since 2019-08-08
 */
public class Main {

    // The location of where serialize file stores
    private static final String DATA_LOCATION = "\\phase2\\data.ser";

    /**
     * The Current Date, for testing purpose or if you want more authority to control time
     */
    private static LocalDate currentDate;

    /**
     * The {@code EmploymentCenter} that stores all employment related data,
     * in this class also do its loading and saving
     *
     * @see EmploymentCenter
     * @see #loadSystem()
     * @see #saveSystem()
     * @see #getEmploymentCenter()
     */
    private EmploymentCenter employmentCenter;

    /**
     * The login frame that when system runs, it shows up to use
     *
     * @see LoginFrame
     * @see #returnToLogin(JFrame)
     */
    private LoginFrame login;

    /**
     * The boolean of whether data is successfully loaded
     *
     * @see #isSuccessfullyLoaded()
     * @see #loadSystem()
     */
    private boolean successfullyLoaded = true;

    /**
     * Load system and create a new LoginFrame for user
     *
     * @see #loadSystem()
     * @see LoginFrame
     */
    public Main() {
        loadSystem();
        login = new LoginFrame(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    /**
     * Get current date, if it does not set, use real life date
     * @see  #setDaysElapse(String)
     * @see  Document#update()
     */
    public static LocalDate getCurrentDate() {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }
        return currentDate;
    }

    /**
     * Make login frame visible again
     *
     * @param frame the frame that is currently viewing
     */
    public void returnToLogin(JFrame frame) {
        frame.setVisible(false);
        login.setVisible(true);
    }

    /**
     * Make current day elapse with desired days
     *
     * @param daysElapse user input of how many days desired to elapse before restart
     * @throws NotIntegerException User is not pass in an correct correct int to elapse time
     */
    public static void setDaysElapse(String daysElapse) throws NotIntegerException {
        try {
            int days = Integer.parseInt(daysElapse);
            currentDate = getCurrentDate().plusDays(days);
        } catch (NumberFormatException e) {
            throw new NotIntegerException();
        }
    }

    public EmploymentCenter getEmploymentCenter() {
        return employmentCenter;
    }

    private String getPath() {
        return System.getProperty("user.dir") + DATA_LOCATION;
    }

    public boolean isSuccessfullyLoaded() {
        return successfullyLoaded;
    }

    /**
     * Load {@code EmploymentCenter} from serialize file and if something wrong happened set successfulLoaded be false
     * @see  Main()
     */
    private void loadSystem() {
        try {
            InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(getPath()));
            ObjectInput input = new ObjectInputStream(bufferedInputStream);
            employmentCenter = (EmploymentCenter) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            employmentCenter = new EmploymentCenter();
            successfullyLoaded = false;
        }
    }

    /**
     * Save{@code EmploymentCenter} to serialize file and if something wrong happened throw exception
     *
     * @throws CannotSaveSystemException Something wrong happened during saving therefore cannot save
     */
    public void saveSystem() throws CannotSaveSystemException {
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getPath()));
            ObjectOutput output = new ObjectOutputStream(bufferedOutputStream);
            output.writeObject(employmentCenter);
            output.close();
        } catch (IOException e) {
            throw new CannotSaveSystemException();
        }
    }
}
