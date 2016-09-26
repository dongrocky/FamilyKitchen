/**
 * Primary
 *
 * Dynamic
 * September, 2016
 */
package forester.familykitchen;
import java.io.Serializable;

public class MenuPK implements Serializable {
    protected String userName;
    protected String category;

    public MenuPK() {}

    @Override
    public boolean equals(Object obj) {
        MenuPK key = (MenuPK) obj;
        return this.userName.equals(key.userName) &&
            this.category.equals(key.category);
    }

    @Override
    public int hashCode() {
        return this.userName.hashCode() ^ this.category.hashCode();
    }
}
