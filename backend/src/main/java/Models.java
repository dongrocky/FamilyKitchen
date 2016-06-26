package forester.familykitchen;

import java.util.BitSet;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="users")
class User {

    @Id
    private String userName;
    // private final String password;

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

    public User() {}

    public User(String name) {
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
}
