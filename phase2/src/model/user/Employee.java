package model.user;

import model.enums.UserType;
import model.exceptions.WrongEmployeeTypeException;
import model.interfaces.Filterable;
import model.job.Interview;
import model.job.JobPosting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class {@code Employee} is a type of {@code User} that works for a company.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see UserType
 * @see User
 * @see Company
 * @since 2019-08-04
 */
public class Employee extends User implements Filterable, Serializable {

    private static final long serialVersionUID = -351421824333122154L;

    /**
     * The name of {@code Company} this {@code Employee} works for.
     *
     * @see Company
     * @see #getCompanyId()
     */
    private String companyId;

    /**
     * All the files this employee are responsible for. Different type
     * of employee may have different type of files to handle.
     * For {@code INTERVIEWER}, the files are interviews.
     * For {@code RECRUITER} and {@code HIRING_MANAGER}, the files are
     * job postings.
     *
     * @see UserType
     * @see User
     * @see Interview
     * @see JobPosting
     * @see #addFile(Object)
     * @see #removeFile(Object)
     * @see #getInterviews()
     * @see #getJobPostings()
     */
    private ArrayList<Object> files = new ArrayList<>();

    /**
     * Constructor for {@code Employee}.
     *
     * @param values    the information needed to create this {@code Employee}
     * @param companyId the name of the company this {@code Employee} works for
     * @param userType  represent the type of this {@code Employee}
     * @see UserType
     * @see User#User(HashMap, UserType)
     * @see Company#getId()
     */
    public Employee(HashMap<String, String> values, String companyId, UserType userType) {
        super(values, userType);
        this.companyId = companyId;
    }

    /**
     * return the company ID of this employee
     *
     * @return a string represent the ID of this company
     * @see null
     */
    @Override
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Return all job postings this {@code Employee} is responsible for.
     * This method is called only when {@code Employee} is a recruiter or a hiring manager.
     *
     * @return all job postings this {@code Employee} is responsible for
     * @throws WrongEmployeeTypeException type of {@code Employee} is not recruiter or hiring manager
     */
    public ArrayList<JobPosting> getJobPostings() throws WrongEmployeeTypeException {
        try {
            ArrayList<JobPosting> jobPostings = new ArrayList<>();
            for (Object file : files) {
                jobPostings.add((JobPosting) file);
            }

            return jobPostings;
        } catch (ClassCastException e) {
            throw new WrongEmployeeTypeException("JobPosting");
        }
    }

    /**
     * Return all interviews this {@code Employee} is responsible for.
     * This method is called only when {@code Employee} is an interviewer.
     *
     * @return all interviews this {@code Employee} is responsible for
     * @throws WrongEmployeeTypeException type of {@code Employee} is not interviewer
     */
    public ArrayList<Interview> getInterviews() throws WrongEmployeeTypeException {
        try {
            ArrayList<Interview> interviews = new ArrayList<>();
            for (Object file : files) {
                interviews.add((Interview) file);
            }
            return interviews;
        } catch (ClassCastException e) {
            throw new WrongEmployeeTypeException("Interview");
        }
    }

    public void addFile(Object file) {
        this.files.add(file);
    }

    public void removeFile(Object file) {
        this.files.remove(file);
    }

    /**
     * Return a hash map of headings and corresponding values about this employee.
     *
     * @return a hash map of headings and corresponding values about this employee
     * @see Filterable
     */
    @Override
    public HashMap<String, String> getFilterMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", getUsername());
        map.put("real name", getRealName());
        map.put("company", getCompanyId());
        return map;
    }
}
