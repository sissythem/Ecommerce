package fromRESTful;

import java.io.Serializable;
import java.util.Date;

/** Users class to map the object from RESTful services **/
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String country;
    private String city;
    private String photo;
    private Date registrationDate;
    private String about;
    private String birthDate;

    public Users(){}

    /** For edit profile **/
    public Users(int userId, String firstName, String lastName, String username, String password, String email, String phoneNumber, String country, String city,
                 String about, String birthDate, Date registrationDate, String photo)
    {
        this.id             = userId;
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.username       = username;
        this.password       = password;
        this.email          = email;
        this.phoneNumber    = phoneNumber;
        this.country        = country;
        this.city           = city;
        this.about          = about;
        this.birthDate      = birthDate;
        this.registrationDate   = registrationDate;
        this.photo              = photo;
    }

    /** For Register **/
    public Users(String firstName, String lastName, String username, String password, String email, String phoneNumber, String bdate)
    {
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.username       = username;
        this.password       = password;
        this.email          = email;
        this.phoneNumber    = phoneNumber;
        this.birthDate      = bdate;
        this.country        = "";
        this.city           = "";
        this.about          = "";
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Users[ id=" + id + " ]";
    }

}