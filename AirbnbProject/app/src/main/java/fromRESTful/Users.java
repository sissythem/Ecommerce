package fromRESTful;

/**
 * Created by sissy on 8/5/2017.
 */

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Date;
import util.Utils;

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
    private Date birthDate;
    private String host;

    public Users() {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static Users fromJSON (JSONObject obj){
        Users u = new Users();
        try {
            u.id = (Integer) obj.get("id");
            u.firstName = (String) obj.get("firstName");
            u.lastName = (String) obj.get("lastName");
            u.username = (String) obj.get("username");
            u.password = (String) obj.get("password");
            u.email = (String) obj.get("email");
            u.phoneNumber = (String) obj.get("phoneNumber");
            u.country = (String) obj.get("country");
            u.city = (String) obj.get("city");
            u.photo = (String) obj.get("photo");

            String dateString = (String)obj.get("registrationDate");
            u.registrationDate = Utils.ConvertStringToDate(dateString, "yyyy-MM-dd'T'hh:mm:ssz");

            u.about = (String) obj.get("about");

            String birthDateString = (String)obj.get("birthDate");
            u.birthDate = Utils.ConvertStringToDate(birthDateString, "yyyy-MM-dd'T'hh:mm:ssz");

            u.host = (String) obj.get("host");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "dbpackage.Users[ id=" + id + " ]";
    }

}
