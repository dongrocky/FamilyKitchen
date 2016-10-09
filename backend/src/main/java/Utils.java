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
        return str != null && str.length() > 0;
    }

    /**
     * @brief: Validate Username 
     */
    public static boolean validateUsername(String name) {
        if(!validateString(name)) {
            return false;
        }
        
        return name.matches("^\\w[a-zA-Z0-9_@ ]*$");
    }
}
