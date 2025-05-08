package entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * Entity class representing a Passenger in the system.
 */
@Entity
@Table(name = "passenger")
public class Passenger {

    /** Unique identifier for the passenger (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private int passengerId;

    /** Name of the passenger (cannot be null) */
    @Column(name = "name", nullable = false)
    private String name;

    /** Email of the passenger (must be unique and not null) */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /** Age of the passenger (not null) */
    @Column(name = "age", nullable = false)
    private int age;

    /** Gender of the passenger (not null) */
    @Column(name = "gender", nullable = false)
    private String gender;

    /** Phone number of the passenger (not null) */
    @Column(name = "phone", nullable = false)
    private String phone;

    /** Password for the passenger account (not null) */
    @Column(name = "password", nullable = false)
    private String password;

    /** Default constructor */
    public Passenger() {}

    /**
     * Parameterized constructor
     * 
     * @param name     Passenger's name
     * @param email    Passenger's email
     * @param age      Passenger's age
     * @param gender   Passenger's gender
     * @param phone    Passenger's phone number
     * @param password Passenger's password
     */
    public Passenger(String name, String email, int age, String gender, String phone, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.password = password;
    }

    // Getters and Setters

    /**
     * @return the passenger ID
     */
    public int getPassengerId() {
        return passengerId;
    }

    /**
     * @param passengerId the ID to set
     */
    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    /**
     * @return the passenger name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the passenger email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the passenger age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the passenger gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the passenger phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the passenger password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
