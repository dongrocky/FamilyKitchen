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
 * /menu/{username}/{category}    -   POST    create the category
 * /menu/{username}/{category}    -   PUT     update the category
 * /menu/{username}/{category}    -   GET     get the category
 * /menu/{username}/{category}    -   DELETE  delete the category
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

    /************************ Get *************************/
    @RequestMapping(value="/{username}/{category}", 
            method=RequestMethod.GET)
    public ResponseEntity<Menu> getCategory(@PathVariable("username") 
            String username, 
            @PathVariable("category") String category) {
        Menu menu = null;

        /* TODO: Need to validate username and category */
        if(!Utils.validateString(username) || 
                !Utils.validateString(category)) {
            logger.error("GET_CAT: Failed to get category since input is NULL");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            menu = dao.findByUserNameAndCategory(username, category);
        } catch (IllegalArgumentException e) {
            logger.error("GET_CAT: Failed to find user " + username + 
                    " category " + category);
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (menu == null) {
            logger.error("GET_CAT: can not find category " + category + 
                    " user: " + username);
            return new ResponseEntity<>(menu, HttpStatus.NOT_FOUND);
        }

        logger.debug("GET_CAT: Find user: " + username + " category: " + 
                category);

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @RequestMapping(value="/{username}", 
            method=RequestMethod.GET)
    public ResponseEntity<List<Menu>> getAllCategory(@PathVariable("username") 
            String username) {
        List<Menu> categories = null;

        /* TODO: Need to validate username */
        if(!Utils.validateString(username)) {
            logger.error("GET_CAT_ALL: Failed to get category since input " +
                    "is invalid");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            /* TODO: is it only one category or all categories? */
            categories = dao.findByUserName(username);
        } catch (IllegalArgumentException e) {
            logger.error("GET_CAT_ALL: Failed to find all categories for user "
                    + username);
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (categories.size() == 0) {
            logger.error("GET_CAT_ALL: can not find categories. user: " + 
                    username);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        logger.debug("GET_CAT_ALL: Find all categories for user: " + username);

        return new ResponseEntity(categories, HttpStatus.OK);
    }


    /************************ Create *************************/
    @RequestMapping(value="/{username}/{category}", 
            method=RequestMethod.POST)
    public ResponseEntity<Menu> createCategory(
            @PathVariable("username") String username,
            @PathVariable("category") String category,
            @RequestBody Menu menu) {
        if (menu == null || username == null || category == null) {
            logger.error("ADD_CAT: Failed to create category " + category +  
                    "user: " + username);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        /* TODO: validate input */
        if(!Utils.validateString(username) || 
                !Utils.validateString(category)) {
            logger.error("ADD_CAT: Failed to create category since input is " +
                    "invalid");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        menu.setUserName(username);
        menu.setCategory(category);
        return createOrUpdateCategoryInternal(menu, true);
    }

    /************************* Update ***************************/
    @RequestMapping(value="/{username}/{category}", 
            method=RequestMethod.PUT)
    public ResponseEntity<Menu> updateCategory(
            @PathVariable("username") String username,
            @PathVariable("category") String category,
            @RequestBody Menu menu) {
        if (menu == null || username == null || category == null) {
            logger.error("UPDATE_CAT: Failed to update category " + category +
                    "user: " + username);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        /* TODO: validate input */
        if(!Utils.validateString(username) || 
                !Utils.validateString(category)) {
            logger.error("UPDATE_CAT: Failed to update category since input " +
                    "is invalid");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        menu.setUserName(username);
        menu.setCategory(category);
        return createOrUpdateCategoryInternal(menu, false);
    }

    /********************** Delete *****************************/
    @RequestMapping(value="/{username}/{category}", 
            method=RequestMethod.DELETE)
    public ResponseEntity<Menu> deleteCategory(@PathVariable("username") 
            String username, 
            @PathVariable("category") String category) {
        /* TODO: validate input */
        if(!Utils.validateString(username) || 
                !Utils.validateString(category)) {
            logger.error("DEL_CAT: Failed to delete category since input is " +
                    "invalid");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Menu m = dao.findByUserNameAndCategory(username, category);
        if (m != null) {
            dao.delete(m);
            logger.debug("DEL_CAT: Category(%s) deleted. Username: %s",
                    category, username);
        } else {
            logger.debug("DEL_CAT: Category(%s) not found. User: %s",
                    category, username);
            return new ResponseEntity(HttpStatus.NOT_FOUND); 
        }

        return new ResponseEntity(HttpStatus.OK); 
    }

    @RequestMapping(value="/{username}", 
            method=RequestMethod.DELETE)
    public ResponseEntity<Menu> deleteAllCategory(@PathVariable("username") 
            String username) {
        /* TODO: validate input */
        if(!Utils.validateString(username)) {
            logger.error("CAT_DEL_ALL:Failed to delete category since input " +
                    "is NULL");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<Menu> menus = dao.findByUserName(username);
        
        for(Menu m : menus) {
            dao.delete(m);
        }

        logger.debug("CAT_DEL_ALL: User: " + username + " All categories have "
                + "been cleaned.");
        return new ResponseEntity(HttpStatus.OK);
    }
    protected ResponseEntity<Menu> createOrUpdateCategoryInternal(Menu menu,
                boolean create) {
        if (menu == null) {
            logger.error("CAT_ADD_UPDATE: Input is null");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String username = menu.getUserName();
        String category = menu.getCategory();
        String description = menu.getDescription();


        Menu tmp = dao.findByUserNameAndCategory(username, category);
        if(tmp != null && create) {
            logger.error("CAT_ADD_UPDATE: Category " + category + " already " +
                    "exists. " + "user: " + username);
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else if (tmp == null && !create) {
            logger.error("CAT_ADD_UPDATE: Category " + category + "does not " +
                    "exist for update." + "User: " + username);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {
            dao.save(menu); 
        } catch (IllegalArgumentException e) {
            logger.error("CAT_ADD_UPDATE: Failed to save category: " + 
                    category + " User name: " + username);
            return new ResponseEntity("Failed", 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("CAT_ADD_UPDATE: Created or updated category " + category 
                + " User name: " + username);

        return new ResponseEntity(menu, create ? 
                HttpStatus.CREATED : HttpStatus.OK); 
    }
}
