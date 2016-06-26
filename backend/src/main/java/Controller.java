package forester.familykitchen;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

// /user/{username}?type=1
// /menu?catogory=xxx&data=Mon
@RestController
@RequestMapping("/user")
class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private UserDao userDao;

    @RequestMapping("/get/{username}")
    public User getUser(@PathVariable("username") String name) {
        User user = null;

        if(name == null || name.length() == 0) {
            System.out.println("User name is empty.");
        }

        try {
	        user = userDao.findByUserName(name);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to get user " + name);
            return null;
        }

        return user;
    }

    @RequestMapping("/create/{username}")
    public boolean createUser(@PathVariable("username") String name) {

        if(name == null || name.length() == 0) {
            System.out.println("User name is empty.");
        }

        User user = new User(name);

        try {
	        user = userDao.save(user);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create user " + name);
            return false;
        }

        return true;
    }

    @RequestMapping("/delete/{username}")
    public boolean deleteUser(@PathVariable("username") String name) {

        if(name == null || name.length() == 0) {
            System.out.println("User name is empty.");
        }

        User user = new User(name);

        try {
	        userDao.delete(user);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete user " + name);
            return false;
        }

        return true;
    }
}
