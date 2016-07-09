package forester.familykitchen;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// /user/{username}?type=1
// /menu?catogory=xxx&data=Mon
@RestController
@RequestMapping("/user")
class Controller {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger logger = 
        LoggerFactory.getLogger(Controller.class);

    @Autowired
    private UserDao userDao;

    @RequestMapping("/get/{username}")
    public User getUser(@PathVariable("username") String name) {
        User user = null;

        if(name == null || name.length() == 0) {
            logger.error("User name is empty.");
        }

        try {
	        user = userDao.findByUserName(name);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to get user " + name);
            return null;
        }

        return user;
    }

    @RequestMapping("/create/{username}")
    public ResponseEntity<?> createUser(@PathVariable("username") String name) {

        if(name == null || name.length() == 0) {
            logger.error("User name is empty.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.UNPROCESSABLE_ENTITY); 
        }

        /* check if the user name exists */
        if (userDao.findByUserName(name) != null) {
            logger.info("User name " + name + " already existed.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.UNPROCESSABLE_ENTITY); 
        }

        User user = new User(name);

        try {
	        user = userDao.save(user);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create user " + name);
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(), 
                                        HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        /* Build response */

        logger.info("Created user: " + name);

        return new ResponseEntity<>("Success",
                                    new HttpHeaders(),
                                    HttpStatus.CREATED); 
    }

    @RequestMapping("/delete/{username}")
    public boolean deleteUser(@PathVariable("username") String name) {

        if(name == null || name.length() == 0) {
            logger.error("User name is empty.");
        }

        User user = new User(name);

        try {
	        userDao.delete(user);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete user " + name);
            return false;
        }

        logger.info("Deleted user: " + name);

        return true;
    }
}
