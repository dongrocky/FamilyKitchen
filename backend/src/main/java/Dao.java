package forester.familykitchen;
import forester.familykitchen.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
interface UserDao extends CrudRepository<User, Long> {

      /**
       *    * This method will find an User instance in the database by its email.
       *       * Note that this method is not implemented and its working code will be
       *          * automagically generated from its signature by Spring Data JPA.
       *             */
      public User findByEmail(String email);

      public User findByUserName(String name);

}
