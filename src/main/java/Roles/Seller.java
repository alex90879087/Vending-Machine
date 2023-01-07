package Roles;

public class Seller extends Register{
//    This role is able to **fill/modify** the item details.

    /*-
    This role is able to **fill/modify** the item details.
    - To fill and modify the items,
    - this role is able to select and modify item details such as
    - item name
    - item code
    - item category
    - item quantity
    - item price.

    - Appropriate error message must be shown if the quantity added will be over than the limit (15 of each item/product) or there is conflicting item code/name/category.

    - This role will also able to obtain two reports (either csv or text file) upon logged in:

    - A list of the current available items that include the item details.

    - A summary that includes items codes, item names and the total number of quantity sold for each item (e.g. "1001; Mineral Water; 12", "1002; Mars; 1", etc.).

    */
    public Seller(String username, String password) {
        super(username, password);
    }





}
