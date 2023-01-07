
card stories
1. register a new user, auto login
2. card payment, empty input 
3. login again, card payment, valid card
3. save card
4. login again, recent items
5. card again, auto payment

seller stories
1. availble items
2. update qty
3. summary, sold how many
4. buy with cash
5. login again, check stock, summary
6. buy again, card, invalid

cashier stories
9. availabel cash, updated
10. transactions, details, show items
11. buy stuff

owner
1. failed transaction
2. transaction
3. sold record
4. cash available
5. remove seller2
6. check again
7. add seller3
8. check

logout





















================================
## AS a customer,
- I Want Snacks are divided into four different categories: beverages (Mineral Water, Sprite, Coca cola, Pepsi, Juice.),
  chocolate (Mars, M&Ms, Bounty, Snickers.), chips (Smiths, Pringles, Kettle, Thins)
  and lollipops ( Mentos, Sour Patch, Skittles.). The vending machine can maintain a maximum of 15 items per product, for example,
  a maximum of 15 bottles of Coca-Cola for beverages and a maximum of 7 products to choose from for the first purchase, for example, 7 Pepsi. So that i can know what can i to choose.
- I want to display the list product options, enter the category and select one or more items from the list of available products and select the number of items.
  I can also cancel a transaction at any time, any idle activity over 2 minutes (timeout) will automatically cancel the transaction. Canceled transactions will return the application to the start page.
- I want to have a button from the default page to log in/create an account by providing a username and password.
  The username must be unique and the password input is hidden by showing an asterisk (*). When I create an account for the first time,
  I will be automatically logged in. When I log in, the default page will show a list of my 5 most recently purchased products/items. When I am logged in as an anonymous user without an account,
  the default page will show a list of my 5 most recently purchased products.
- I need to be able to use the amount of coins and/or bills that are valid in Australia (e.g. $5, $10, $20, 5c, 10c, etc.). If the funds I provide are not sufficient to complete the transaction, the customer should be prompted to enter the remaining amount or cancel the transaction.
  After a successful transaction, I will receive the selected product and the correct amount of change.
  The change must be optimal in the case of higher priority notes or coins. For example, if a customer uses a $50 bill for a $5 item,
  the change will be two $20 bills and a $5 bill (instead of four $10 bills and five $1 coins). However, when calculating change, you must consider the number of coins or bills available in the vending machine.
  For example, if there is only one $20 bill, the change in the previous example would be one $20 bill, two $10 bills and one $5 bill.
  If there is no change available for the given cash provided by the customer, you need to inform the customer that there is no change available for the money inserted.
  You can ask the customer to insert/use a different note/coin to complete the payment or cancel the transaction. On the first run, you can set the availability to 5 per bill and per coin.
- I want to be able to provide "cardholder name" and "credit card number".
  You just need to check if the credit card details provided match any of the ones in this credit card list: credit_cards.json, and notify me if they are invalid (no match in the list).
  When the transaction is completed and successful, I will be prompted to choose to save this credit card details.
  Once saved I will automatically fill in the credit card details the next time I make a transaction. Please note that the entered card number will also be hidden with an asterisk (*).
- I want that when a transaction is completed, the product inventory will be updated accordingly and any logged in customers will be automatically logged out.
  Any cancelled transactions (either due to a timeout or by pressing the cancel button in any page state) will also log the customer out
## As an seller
- I want to be able to fill/modify item details. To fill and modify items, this role can select and modify item details such as item name, item code, item category, item quantity and item price.
  If the number of items added exceeds the limit (15 per item/product), "Number of items exceeds 15" is displayed.
  When there is a conflicting item code/name/category, it displays "Change unavailable!" .
- I want to get two reports (csv or text files) after login.
  A list of currently available items, including item details (with item code, item name, number of available items).
  A summary containing the item code, the item name and the total number of items sold per item (e.g. "1001; Mineral Water; 12", "1002; Mars; 1", etc.).


## As a cashier
- I want to be able to fill/modify the change or notes/coins available in the vending machine say 20 as an okay bill and 1c as an unavailable coin.
  The character will be able to modify the number of bills and coins per bill in the vending machine, for example 20 as available for 5 AUD.
- I want a transaction summary in csv or txt format that includes the date and time of the transaction, the item sold, the amount paid, the change returned, and the payment method.
## AS owner
- I want to be able to add/remove a seller or cashier or owner user. In addition, this role can modify item details and changes available in the vending machine.
  Please note that a user cannot have more than one role, for example, a customer user cannot have the seller role
- I want a list of usernames in the vending machine that have an associated role (customer, seller, cashier, or administrator).
  A summary of cancelled transactions. This summary includes only the date and time of the cancellation, the user (if available, otherwise "Anonymous") and the reason (e.g. "Timeout", "User Cancellation", "Change Unavailable", etc.). "Change unavailable", etc.).

