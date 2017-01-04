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


// /account/{username}?type=1
// /menu?catogory=xxx&data=Mon
@RestController
@RequestMapping("/account")
class AccountController {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger logger = 
        LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value="/{username}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccount(@PathVariable("username") String name) {
        Account account = null;

        /* This is throw exception if the account does not exist. */
        // validateUser(name);

        try {
	        account = accountDao.findByUserName(name);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to get account " + name);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public ResponseEntity<?> getCreateAccountForm() {
        //FIXME: return the form
        return new ResponseEntity<>("Success",
                                    new HttpHeaders(),
                                    HttpStatus.CREATED); 
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(@RequestBody Account account, 
                                        UriComponentsBuilder ucBuilder) {

        String name = account.getUserName();

        if(name.isEmpty()) {
            logger.error("Empty name is not allowed.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.BAD_REQUEST); 
        }

        /* check if the account name exists */
        if (accountDao.findByUserName(name) != null) {
            logger.info("Account name " + name + " already existed.");
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(),
                                        HttpStatus.CONFLICT); 
        }

        try {
	        account = accountDao.save(account);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create account " + name);
            return new ResponseEntity<>("Failed",
                                        new HttpHeaders(), 
                                        HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        /* Build response */

        logger.debug("Created account: " + name);

        HttpHeaders header = new HttpHeaders();
        header.setLocation(ucBuilder.path("/account/{id}")
                                .buildAndExpand(name).toUri());
        return new ResponseEntity<>("Success",
                                    header,
                                    HttpStatus.CREATED); 
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@RequestBody Account account) {
        String name = account.getUserName();

        if(!validateAccount(name)) {
            logger.error("Account name " + name + " is not valid.");
            return new ResponseEntity<>("Failure",
                                        new HttpHeaders(),
                                        HttpStatus.BAD_REQUEST); 
        }

        try {
            account = accountDao.save(account);
        } catch(IllegalArgumentException e) {
            logger.error("Failed to update account " + name + " Error: " + e.toString());
            return new ResponseEntity<>("Failure",
                                        new HttpHeaders(),
                                        HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        return new ResponseEntity<>("Success",
                                    new HttpHeaders(),
                                    HttpStatus.OK); 
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAccount(@PathVariable("username") String name) {

        if(!validateAccount(name)) {
            logger.error("Account name " + name + " is not valid for delete");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Account account = new Account(name);

        try {
	        accountDao.delete(account);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete account " + name);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Deleted account: " + name);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean validateAccount(String name) {
        if(name == null || name.length() == 0) {
            logger.error("Account name is empty.");
            return false;
        }

        Account account = accountDao.findByUserName(name);

        if (account == null) {
            return false;
        }

        return true;

    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String userName) {
        super("could not find Account '" + userName + "'.");
    }
}
