package fromRESTful;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import util.Utils;

import static util.Utils.DATABASE_DATETIME;
import static util.Utils.DATABASE_DATE_FORMAT;

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

    public Users() {}

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
            u.country = "";
            if(Utils.isFieldOK(obj, "country")) u.country = (String) obj.get("country");
            u.city = "";
            if(Utils.isFieldOK(obj, "city")) u.city = (String) obj.get("city");
            u.photo = "";
            if(Utils.isFieldOK(obj, "photo")) u.photo = (String) obj.get("photo");

            String dateString;
            if(Utils.isFieldOK(obj,"registrationDate")) {
                dateString = (String) obj.get("registrationDate");
                u.registrationDate = Utils.ConvertStringToDate(dateString, DATABASE_DATETIME);
            }
            u.about="";
            if(Utils.isFieldOK(obj, "about")) u.about = (String) obj.get("about");
            String birthDateString;

            if(Utils.isFieldOK(obj,"birthDate")) {
                birthDateString = (String)obj.get("birthDate");
                u.birthDate = Utils.ConvertStringToDate(birthDateString, DATABASE_DATE_FORMAT);
            }
            u.host = (String) obj.get("host");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return u;
    }

    public JSONObject toJSON (){
        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", id.toString());
            jsonUser.put("firstName", firstName);
            jsonUser.put("lastName", lastName);
            jsonUser.put("username", username);
            jsonUser.put("password", password);
            jsonUser.put("email", email);
            jsonUser.put("phoneNumber", phoneNumber);
            jsonUser.put("country", country);
            jsonUser.put("city", city);
            jsonUser.put("photo", photo);
            String rdate = Utils.ConvertDateToString(registrationDate, DATABASE_DATETIME);
            jsonUser.put("registrationDate", rdate);
            jsonUser.put("about", about);
            String bdate = Utils.ConvertDateToString(birthDate, DATABASE_DATE_FORMAT);
            jsonUser.put("birthDate", bdate);
            jsonUser.put("host", host);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonUser;
    }

    //    public String[] getUserDetails (String username)
//    {
//        String[] userdetails = new String[10];
//        userdetails[0] = this.firstName;
//        userdetails[1] = this.lastName;
//        userdetails[2] = this.username;
//        userdetails[3] = this.password;
//        userdetails[4] = this.email;
//        userdetails[5] = this.phoneNumber;
//        userdetails[6] = this.country;
//        userdetails[7] = this.city;
//        userdetails[8] = this.about;
//        userdetails[9] = this.birthDate.toString();
//        return userdetails;
//    }
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