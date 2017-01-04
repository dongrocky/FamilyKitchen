package forester.familykitchen;
import forester.familykitchen.Account;
import forester.familykitchen.Menu;
import forester.familykitchen.MenuPK;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Transactional
interface AccountDao extends CrudRepository<Account, Long> {

      /**
       * This method will find an Account instance in the database by its email.
       * Note that this method is not implemented and its working code will be
       * automagically generated from its signature by Spring Data JPA.
       */
      public Account findByEmail(String email);

      public Account findByUserName(String name);

}

@Transactional
interface MenuDao extends CrudRepository<Menu, Long> {
    public Menu findByUserNameAndCategory(String name, String category);

    public List<Menu> findByUserName(String username);
}
