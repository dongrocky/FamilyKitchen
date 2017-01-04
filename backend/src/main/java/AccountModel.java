package forester.familykitchen;

import java.util.BitSet;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="accounts")
class Account {

    @Id
    private String userName;
    private String password;

    private String email;
    // private final BitSet type;

    private String firstName;
    private String lastName;
    /*
    private final String phone;
    private final String address;
    private final String city;
    private final String state;
    private final int 	 zip;
    private final String description;
    private final String tag;
    private final String monStart;
    private final String monEnd;
    private final String tueStart;
    private final String tueEnd;
    private final String wedStart;
    private final String wedEnd;
    private final String thuStart;
    private final String thuEnd;
    private final String friStart;
    private final String firEnd;
    private final String satStart;
    private final String satEnd;
    private final String sunStart;
    private final String sunEnd;
    private final float  rating;
    */

    public Account() {}

    public Account(String name) {
	    userName = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String first) {
        this.firstName = first;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String last) {
        lastName = last;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }
}

@Entity
@Table(name="dishes")
class Dish {
    @Id
    private long    did;
    private String  userName;
    private String  category;
    private String  dishName;
    private float   price;
    private String  description;
    private boolean status;
    private boolean monAvail;
    private boolean tueAvail;
    private boolean wedAvail;
    private boolean thuAvail;
    private boolean friAvail;
    private boolean satAvail;
    private boolean sunAvail;

    // FIXME
    // private char photo;

    public Dish() {}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String name) {
        this.dishName = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getMonAvail() {
        return monAvail;
    }

    public void setMonAvail(boolean av) {
        this.monAvail = av;
    }

    public boolean getTueAvail() {
        return tueAvail;
    }

    public void setTueAvail(boolean av) {
        this.tueAvail = av;
    }

    public boolean getWedAvail() {
        return wedAvail;
    }

    public void setWedAvail(boolean wedAvail) {
        this.wedAvail = wedAvail;
    }

    public boolean getThuAvail() {
        return thuAvail;
    }

    public void setThuAvail(boolean thuAvail) {
        this.thuAvail = thuAvail;
    }

    public boolean getFriAvail() {
        return friAvail;
    }

    public void setCategory(boolean av) {
        this.friAvail = av;
    }

    public boolean getSatAvail() {
        return satAvail;
    }

    public void setSatAvail(boolean av) {
        this.satAvail = av;
    }

    public boolean getSunAvail() {
        return sunAvail;
    }

    public void setSunAvail(boolean av) {
        this.sunAvail = av;
    }
}
