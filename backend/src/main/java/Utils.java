/**
 * Controller for menu. 
 * Handles the CRUD operation for Menu related operation.
 *
 * Dynamic 
 * Octobor, 2016
 */

package forester.familykitchen;
public class Utils {
    public static boolean validateString(String str) {
        if(str == null || str.length() == 0) return false;

        return str.matches("^\\w[a-zA-Z0-9_@-]*$");
    }
}
