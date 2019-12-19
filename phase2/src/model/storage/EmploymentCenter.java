package model.storage;

import gui.general.LoginFrame;
import gui.general.UserMenuFrame;
import gui.scenarios.applicant.JobSearchingScenario;
import gui.scenarios.hiringManager.JobPostingRegisterScenario;
import gui.scenarios.hiringManager.ViewPostingScenario;
import gui.scenarios.recruiter.MatchInterviewScenario;
import model.enums.JobPostingStatus;
import model.enums.UserType;
import model.job.Application;
import model.job.Interview;
import model.job.JobPosting;
import model.user.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class {@code EmploymentCenter} stores all the {@code User}, {@code Company} and
 * {@code JobPosting} for classes who need them.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see User
 * @see Company
 * @see JobPosting
 * @since 2019-08-04
 */
public class EmploymentCenter implements Serializable {

    private static final long serialVersionUID = -6864049380257653797L;

    /**
     * A hash map whose key is {@code UserType} and value is an array list
     * of the corresponding users.
     *
     * @see UserType
     * @see Applicant
     * @see Employee
     * @see #getUser(String, UserType)
     * @see #register(User, UserType)
     */
    private HashMap<UserType, ArrayList<User>> users = new HashMap<>();

    /**
     * An array list containing all the companies registered.
     *
     * @see Company
     * @see #getCompany(String)
     * @see #registerCompany(Company)
     */
    private ArrayList<Company> companies = new ArrayList<>();

    /**
     * An array list containing all the job postings created.
     *
     * @see JobPosting
     * @see #getJobPosting(String)
     * @see #getJobPostingsByIds(ArrayList)
     * @see #getOpenJobPostings()
     * @see #addJobPosting(JobPosting)
     */
    private ArrayList<JobPosting> jobPostings = new ArrayList<>();


    /**
     * Create a new {@code EmploymentCenter}.
     *
     * @see main.Main
     */
    public EmploymentCenter() {
        users.put(UserType.APPLICANT, new ArrayList<>());
        users.put(UserType.RECRUITER, new ArrayList<>());
        users.put(UserType.HIRING_MANAGER, new ArrayList<>());
        users.put(UserType.INTERVIEWER, new ArrayList<>());
    }

    /**
     * Add a new user to its corresponding list.
     *
     * @param user     the new registered user
     * @param userType type of the new user
     * @see UserFactory#createUser(HashMap, UserType)
     */
    void register(User user, UserType userType) {
        this.users.get(userType).add(user);
    }

    /**
     * Add a new company to {@code companies}.
     *
     * @param company the new company to be added
     * @see UserFactory#createUser(HashMap, UserType)
     */
    void registerCompany(Company company) {
        this.companies.add(company);
    }

    /**
     * Get a user by his/her username and type.
     *
     * @param userName the username of the user
     * @param userType the type of the user
     * @return the target {@code User}
     * @see EmploymentCenter#getApplicant(String)
     * @see EmploymentCenter#getEmployee(String, UserType)
     * @see LoginFrame
     */
    public User getUser(String userName, UserType userType) {
        for (User user : users.get(userType)) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return new NullUser();
    }

    /**
     * Get an applicant by its username.
     *
     * @param username the name of the applicant
     * @return the target {@code Applicant}
     * @see Application#getApplicant(EmploymentCenter)
     * @see Application#detailedToStringForEmployee(EmploymentCenter)
     * @see Interview#detailedToStringForEmployee(EmploymentCenter)
     */
    public Applicant getApplicant(String username) {
        try {
            return (Applicant) getUser(username, UserType.APPLICANT);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Get the company by its id.
     *
     * @param companyId the id of the company
     * @return the target {@code Company}
     * @see JobPosting#applicationSubmit(Application, EmploymentCenter)
     * @see JobPosting#applicationCancel(Application, EmploymentCenter)
     * @see UserFactory
     * @see UserMenuFrame#getCompany()
     * @see main.Main#main(String[])
     */
    public Company getCompany(String companyId) {
        for (Company company : companies) {
            if (company.getId().equals(companyId)) {
                return company;
            }
        }
        return null;
    }

    /**
     * Get an employee using his/her username and type.
     *
     * @param username the username of the employee
     * @param userType the type of the employee
     * @return the target {@code Employee}
     * @see EmploymentCenter#getInterviewers(ArrayList)
     * @see JobPostingRegisterScenario
     */
    public Employee getEmployee(String username, UserType userType) {
        try {
            return (Employee) getUser(username, userType);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Get all interviewers stored in the storage.
     *
     * @param usernameList a list of interviews' user names
     * @return {@code ArrayList<Employee>} that contains all interviewers
     * @see MatchInterviewScenario
     */
    public ArrayList<Employee> getInterviewers(ArrayList<String> usernameList) {
        ArrayList<Employee> interviewers = new ArrayList<>();
        for (String username : usernameList) {
            interviewers.add(getEmployee(username, UserType.INTERVIEWER));
        }
        return interviewers;
    }

    /**
     * Get an list of all open job postings.
     *
     * @return {@code ArrayList<JobPosting>} containing all job postings with status {@code JobPostingStatus.OPEN}
     * @see EmploymentCenter#updateOpenJobPostings()
     * @see JobSearchingScenario
     */
    public ArrayList<JobPosting> getOpenJobPostings() {
        ArrayList<JobPosting> openJobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.jobPostings) {
            if (jobPosting.getStatus().equals(JobPostingStatus.OPEN)) {
                openJobPostings.add(jobPosting);
            }
        }
        return openJobPostings;
    }

    /**
     * Get job postings by their id's.
     *
     * @param ids a list that contains id's of job postings that are wanted
     * @return the ArrayList of target {@code JobPosting}
     * @see ViewPostingScenario
     */
    public ArrayList<JobPosting> getJobPostingsByIds(ArrayList<String> ids) {
        ArrayList<JobPosting> listJobPostings = new ArrayList<>();
        for (String id : ids) {
            listJobPostings.add(getJobPosting(id));
        }
        return listJobPostings;
    }

    /**
     * Get the job posting using its job id.
     *
     * @param id the job id of target job posting
     * @return the target {@code JobPosting}
     * @see Application#apply(EmploymentCenter)
     * @see Application#cancel(EmploymentCenter)
     * @see EmploymentCenter#getJobPostingsByIds(ArrayList)
     */
    public JobPosting getJobPosting(String id) {
        for (JobPosting jobPosting : jobPostings) {
            if (jobPosting.getJobId().equals(id)) {
                return jobPosting;
            }
        }
        return null;
    }

    /**
     * Add a new job posting to {@code jobPostings}
     *
     * @param jobPosting the {@code JobPosting} to be added
     * @see JobPostingRegisterScenario
     */
    public void addJobPosting(JobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }

    /**
     * Update all open job postings stored here by calling startProcessing() on it
     *
     * @see LoginFrame
     */
    public void updateOpenJobPostings() {
        ArrayList<JobPosting> jobPostings = this.getOpenJobPostings();
        for (JobPosting jobPosting : jobPostings) {
            jobPosting.startProcessing();
        }
    }

}
