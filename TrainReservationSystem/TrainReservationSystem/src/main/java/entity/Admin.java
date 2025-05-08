package entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * Entity class representing an Admin.
 */
@Entity
@Table(name = "admin")
public class Admin {

    /** Primary key for Admin table */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Admin name */
    @Column(name = "name")
    private String name;

    /** Admin phone number */
    @Column(name = "phone")
    private String phone;

    /** Admin email (must be unique) */
    @Column(name = "email", unique = true)
    private String email;

    /** Default constructor */
    public Admin() {
    }

    /**
     * Constructor with parameters.
     * 
     * @param name  the name of the admin
     * @param phone the phone number of the admin
     * @param email the email address of the admin
     */
    public Admin(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters

    /**
     * @return the admin's ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the admin's ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the admin's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the admin's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the admin's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the admin's phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the admin's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the admin's email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
