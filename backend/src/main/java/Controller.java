package forester.familykitchen;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;


// /user/{username}?type=1
// /menu?catogory=xxx&data=Mon
@RestController
@RequestMapping("/user")
class UserController {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger logger = 
        LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;

    @RequestMapping(value="/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("username") String name) {
        User user = null;

        /* This is throw exception if the user does not exist. */
        // validateUser(name);

        try {
	        user = userDao.findByUserName(name);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to get user " + name);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public ResponseEntity<?> getCreateUserForm() {
        //FIXME: return the form
        return new ResponseEntity<>("Success",
                                    new HttpHeaders(),
                                    HttpStatus.CREATED); 
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, 
                                        UriComponentsBuilder ucBuilder) {

        String name = user.getUserName();

        if(name.isEmpty()) {
            logger.error("Empty name is not allowed.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.BAD_REQUEST); 
        }

        /* check if the user name exists */
        if (userDao.findByUserName(name) != null) {
            logger.info("User name " + name + " already existed.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.CONFLICT); 
        }

        try {
	        user = userDao.save(user);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create user " + name);
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(), 
                                        HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        /* Build response */

        logger.debug("Created user: " + name);

        HttpHeaders header = new HttpHeaders();
        header.setLocation(ucBuilder.path("/user/{id}")
                                .buildAndExpand(name).toUri());
        return new ResponseEntity<>("Success",
                                    header,
                                    HttpStatus.CREATED); 
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String name = user.getUserName();

        if(!validateUser(name)) {
            logger.error("User name " + name + " is not valid.");
            return new ResponseEntity<>("Failure",
                                        new HttpHeaders(),
                                        HttpStatus.BAD_REQUEST); 
        }

        try {
            user = userDao.save(user);
        } catch(IllegalArgumentException e) {
            logger.error("Failed to update user " + name + " Error: " + e.toString());
            return new ResponseEntity<>("Failure",
                                        new HttpHeaders(),
                                        HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        return new ResponseEntity<>("Success",
                                    new HttpHeaders(),
                                    HttpStatus.OK); 
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("username") String name) {

        if(!validateUser(name)) {
            logger.error("User name " + name + " is not valid for delete");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = new User(name);

        try {
	        userDao.delete(user);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete user " + name);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Deleted user: " + name);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean validateUser(String name) {
        if(name == null || name.length() == 0) {
            logger.error("User name is empty.");
            return false;
        }

        User user = userDao.findByUserName(name);

        if (user == null) {
            return false;
        }

        return true;

    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userName) {
        super("could not find user '" + userName + "'.");
    }
}
