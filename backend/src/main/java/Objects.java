package familykitchen;

import java.util.BitSet;

class User {

    private final String userName;
    // private final String password;
    private final String email;
    // private final BitSet type;
    private final String firstName;
    private final String lastName;
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

    public User(String name) {
	userName = name;
	email = "dummy@gmail.com";
	firstName = "David";
	lastName = "Jobs";
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
