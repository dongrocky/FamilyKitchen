/**
 * Controller for menu. 
 * Handles the CRUD operation for Menu related operation.
 *
 * Dynamic 
 * September, 2016
 */

/* URI: 
 * /menu/add/       -   get menu add page?
 * /menu/delete/    -   get menu delete page?
 * /menu/username/{username}/category/{category}    -   POST    create the category
 * /menu/username/{username}/category/{category}    -   PUT     update the category
 * /menu/username/{username}/category/{category}    -   GET     get the category
 * /menu/username/{username}/category/{category}    -   DELETE  delete the category
 */

package forester.familykitchen;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private static final Logger logger = 
        LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuDao dao;

    @RequestMapping(value="/username/{username}/category/{category}", 
            method=RequestMethod.GET)
    public ResponseEntity<Menu> getCategory(@PathVariable("username") 
            String username, 
            @PathVariable("category") String category) {
        Menu menu = null;

        /* TODO: Need to validate username and category */

        try {
            menu = dao.findByUserNameAndCategory(username, category);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to find user " + username + " category " 
                    + category);
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Find user: " + username + " category: " + category);

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @RequestMapping(value="/username/{username}", 
            method=RequestMethod.GET)
    public ResponseEntity<List<Menu>> getAllCategory(@PathVariable("username") 
            String username) {
        List<Menu> categories = null;

        /* TODO: Need to validate username */

        try {
            categories = dao.findByUserName(username);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to find all categories for user " + username);
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Find all categories for user: " + username);

        return new ResponseEntity(categories, HttpStatus.OK);
    }


    @RequestMapping(value="/username/{username}/category/{category}", 
            method=RequestMethod.POST)
    public ResponseEntity<Menu> createCategory(@RequestBody Menu menu) {
        if (menu == null) {
            logger.error("Failed to create category. Input is null");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String username = menu.getUserName();
        String category = menu.getCategory();
        String description = menu.getDescription();

        /* validate user */
        if(!Utils.validateString(username) || 
                !Utils.validateString(category)) {
            logger.error("Failed to create category since input is NULL");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Menu tmp = dao.findByUserNameAndCategory(username, category);
        if(tmp != null) {
            logger.error("Category " + category + " already exists. " + 
                    "User: " + username);
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        try {
            dao.save(menu); 
        } catch (IllegalArgumentException e) {
            logger.error("Failed to save category: " + category +
                    " User name: " + username);
            return new ResponseEntity("Failed", 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Saved category " + category + " User name: " + username);

        return new ResponseEntity(menu, HttpStatus.OK); 
    }
}
