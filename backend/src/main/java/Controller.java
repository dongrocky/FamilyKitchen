package familykitchen;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// /user/{username}?type=1
// /menu?catogory=xxx&data=Mon
@RestController
class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/user")
    public User getUser(@RequestParam(value="username", defaultValue="World") String name) {
	User user = new User(name);
        return user;
    }
}
