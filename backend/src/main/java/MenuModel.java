/**
 * Data model for menus 
 *
 * Dynamic
 * September, 2016
 */
package forester.familykitchen;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;
import forester.familykitchen.MenuPK;

@Entity
@Table(name="menus")
@IdClass(MenuPK.class)
class Menu {
    @Id
    private String userName;
    @Id
    private String category;
    private String description;

    public Menu() {}

    public Menu(String user) {
        this.userName = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


