package model.storage;

import gui.scenarios.userRegister.UserRegisterScenario;
import model.enums.UserType;
import model.exceptions.*;
import model.user.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class {@code UserFactory} is a factory class that creates {@code User}
 * for other classes.
 *
 * @author group 0120 of CSC207 summer 2019
 * @see UserType
 * @see User
 * @see Applicant
 * @see Employee
 * @see Company
 * @since 2019-08-04
 */
public class UserFactory implements Serializable {

    private static final long serialVersionUID = -6178992956146363626L;


    /**
     * The {@code employmentCenter} used to store all the users created.
     *
     * @see EmploymentCenter
     * @see #createUser(HashMap, UserType)
     */
    private EmploymentCenter employmentCenter;


    /**
     * Create a new user factory.
     *
     * @param userPool the {@code employmentCenter} used to store all users created
     * @see UserRegisterScenario
     */
    public UserFactory(EmploymentCenter userPool) {
        this.employmentCenter = userPool;
    }

    /**
     * Create and return a new user given his/her basic information and user type.
     *
     * @param infoMap      a hash map that stores all information needed to create a new user
     * @param registerType the user type of the new user being created
     * @return a new {@code User}
     * @throws UnmatchedPasswordException    see method {@code validValues(HashMap, UserType)}
     * @throws WrongEmailFormatException     see method {@code validValues(HashMap, UserType)}
     * @throws UserAlreadyExistsException    see method {@code validValues(HashMap, UserType)}
     * @throws CompanyAlreadyExistsException see method {@code validValues(HashMap, UserType)}
     * @throws CompanyDoesNotExistException  see method {@code validValues(HashMap, UserType)}
     * @see UserRegisterScenario
     * @see #validValues(HashMap, UserType)
     */
    public User createUser(HashMap<String, String> infoMap, UserType registerType)
            throws UnmatchedPasswordException, WrongEmailFormatException, UserAlreadyExistsException,
            CompanyAlreadyExistsException, CompanyDoesNotExistException {
        validValues(infoMap, registerType);
        User user;
        if (registerType.equals(UserType.APPLICANT)) user = createApplicant(infoMap);
        else if (registerType.equals(UserType.RECRUITER)) user = createRecruiter(infoMap);
        else if (registerType.equals(UserType.INTERVIEWER)) user = createInterviewer(infoMap);
        else if (registerType.equals(UserType.HIRING_MANAGER)) user = createHiringManagerAndCompany(infoMap);
        else user = new NullUser();

        if (!user.isNull()) {
            this.employmentCenter.register(user, registerType);
        }
        return user;
    }

    /**
     * Create a new applicant.
     *
     * @param infoMap the map storing basic information of the applicant
     * @return the new {@code Applicant}
     * @see UserFactory#createUser(HashMap, UserType)
     */
    private User createApplicant(HashMap<String, String> infoMap) {
        return new Applicant(infoMap);
    }

    /**
     * Create a new recruiter.
     *
     * @param infoMap the map storing basic information of the recruiter
     * @return the new {@code Employee} of type {@code UserType.RECRUITER}
     * @see UserFactory#createUser(HashMap, UserType)
     */
    private User createRecruiter(HashMap<String, String> infoMap) {
        String companyId = infoMap.get("Company id:");
        if (companyExists(companyId)) {
            Company company = employmentCenter.getCompany(companyId);
            company.addRecruiterId(infoMap.get("Username:"));
            return new Employee(infoMap, companyId, UserType.RECRUITER);
        } else {
            return new NullUser();
        }
    }

    /**
     * Create a new Interviewer.
     *
     * @param infoMap the map that contains basic information of the interviewer
     * @return the new {@code Employee} of type {@code UserType.INTERVIEWER}
     * @see UserFactory#createUser(HashMap, UserType)
     */
    private User createInterviewer(HashMap<String, String> infoMap) {
        String companyId = infoMap.get("Company id:");
        if (companyExists(companyId)) {
            Company company = employmentCenter.getCompany(companyId);
            company.addInterviewerId(infoMap.get("Username:"));
            return new Employee(infoMap, companyId, UserType.INTERVIEWER);
        } else {
            return new NullUser();
        }
    }

    /**
     * Create a new company and its hiring manager.
     *
     * @param infoMap the map that contains the basic information of the new company and its hiring manager
     * @return the new {@code Employee} of type {@code UserType.HIRING_MANAGER}
     * @see UserFactory#createUser(HashMap, UserType)
     */
    private User createHiringManagerAndCompany(HashMap<String, String> infoMap) {
        String companyId = infoMap.get("Company id:");
        if (!companyExists(companyId)) {
            HashMap<String, String> values = new HashMap<>();
            values.put("id", companyId);
            values.put("hiringManagerId", infoMap.get("Username:"));
            this.employmentCenter.registerCompany(new Company(values));
            return new Employee(infoMap, companyId, UserType.HIRING_MANAGER);
        } else {
            return new NullUser();
        }
    }

    /**
     * Return whether a company exists.
     * A helper method for createRecruiter, createInterviewer, createHiringManagerAndCompany.
     *
     * @param companyId the id of the company to be checked
     * @return whether the company exists
     * @see UserFactory#createRecruiter(HashMap)
     * @see UserFactory#createInterviewer(HashMap)
     * @see UserFactory#createHiringManagerAndCompany(HashMap)
     */
    private boolean companyExists(String companyId) {
        return employmentCenter.getCompany(companyId) != null;
    }

    /**
     * Check whether the information in {@code InfoMap} is valid to create a new user.
     * It is a helper method for createUser.
     *
     * @param infoMap      the map that consists of basic information about the user
     * @param registerType the type of the new user
     * @throws UnmatchedPasswordException    password does not match confirm password
     * @throws WrongEmailFormatException     email format wrong, has to end with .com
     * @throws UserAlreadyExistsException    user already exists, can not override
     * @throws CompanyAlreadyExistsException company already exists, can not override
     * @throws CompanyDoesNotExistException  can not become an employee of company that does not exist
     * @see UserFactory#createUser(HashMap, UserType)
     */
    private void validValues(HashMap<String, String> infoMap, UserType registerType)
            throws UnmatchedPasswordException, WrongEmailFormatException, UserAlreadyExistsException,
            CompanyAlreadyExistsException, CompanyDoesNotExistException {
        if (infoMap.get("Password:").equals("[]")) {
            throw new UnmatchedPasswordException();
        } else if (!infoMap.get("Email:").matches(".+@(.+\\.)com")) {
            throw new WrongEmailFormatException();
        } else if (!employmentCenter.getUser(infoMap.get("Username:"), registerType).isNull()) {
            throw new UserAlreadyExistsException();
        } else if (companyExists(infoMap.get("Company id:")) && registerType.equals(UserType.HIRING_MANAGER)) {
            throw new CompanyAlreadyExistsException();
        } else if (!companyExists(infoMap.get("Company id:")) &&
                (registerType.equals(UserType.RECRUITER) || registerType.equals(UserType.INTERVIEWER))) {
            throw new CompanyDoesNotExistException();
        }
    }
}
