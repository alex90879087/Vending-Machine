import App.VendingMachine;
import Payment.*;
import Payment.Transaction.Result;
import Roles.*;
import Snacks.Chips;
import Snacks.Chocolates;
import Snacks.Drinks;
import Snacks.Snacks;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class App extends Application implements Initializable {

    @FXML
    public PasswordField CardNumber;
    @FXML
    public TextField CardName;

    @FXML
    public TextField UserName;
    @FXML
    public PasswordField Password;

    private String dbPathSnacks = "src/main/resources/snacks.json";
    private String dbPathUsers = "src/main/resources/users.json";
    private String dbPathCash = "src/main/resources/cash.json";
    private String dbPathCard = "src/main/resources/credit_cards.json";
    public static VendingMachine machine = new VendingMachine();

    private AnchorPane pane = new AnchorPane();
    private Stage stage;
    private Scene scene;
    private Parent root;


    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage)  throws IOException {

        System.out.println("> Loading json file");
        loadSnacks(dbPathSnacks); // load snacks database
        loadUsers(dbPathUsers); // load users database
        loadCash(dbPathCash); // load cash details : notes / coins
        loadCards(dbPathCard);

//        this.save(machine); save testing
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Login.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        primaryStage.setTitle("Vending Machine");
        this.scene = new Scene(root, 550,850);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void loadCards(String path) {
        JSONParser parser = new JSONParser();
        try{
            LinkedHashMap<String, Integer> cardsData = new LinkedHashMap<>();
            JSONArray array = (JSONArray) parser.parse(new FileReader(path));
            for (Object o: array) {
                JSONObject person = (JSONObject) o;
                String name = (String) person.get("name");
                Integer number = Integer.parseInt((String) person.get("number"));

                if (!cardsData.containsKey(name)) {
                    cardsData.put(name, number);
                }
            }
            if (cardsData != null) machine.setCardsDataBase(cardsData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadCash(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File fileObj = new File(path);
//            Map<Double, Integer> cashData = new TreeMap<>(Collections.reverseOrder());
            LinkedHashMap<Double, Integer> cashData = mapper.readValue(fileObj, LinkedHashMap.class);
            machine.setCashInMachine(cashData);
            // print for testing
            System.out.println("printing cash data");
            System.out.println(cashData);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSnacks(String path) {

        JSONParser parser = new JSONParser();

        try {
            Object object = parser.parse(new FileReader(path));
            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;
            // reading the "[snacks]" section:
            JSONObject jsonSnacks = (JSONObject) jsonObject.get("snacks");
            // reading the "drinks", "chips", "chocolates", "lollies" array:
            JSONArray jsonDrinks = (JSONArray) jsonSnacks.get("drinks");
            JSONArray jsonChips = (JSONArray) jsonSnacks.get("chips");
            JSONArray jsonChocolates = (JSONArray) jsonSnacks.get("chocolates");
            JSONArray jsonLollies = (JSONArray) jsonSnacks.get("lollies");

            // reading from the drinks array:
            parseSnacksArray("Drinks", jsonDrinks);
            parseSnacksArray("Chips", jsonChips);
            parseSnacksArray("Chocolates", jsonChocolates);
            parseSnacksArray("Candies", jsonLollies);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers(String path) {
        JSONParser parser = new JSONParser();

        try {
            Object object = parser.parse(new FileReader(path));
            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;
            // reading the "[users]" section:
            JSONObject jsonUsers = (JSONObject) jsonObject.get("users");
            // reading the "customer", "owner", "seller", "cashier" array:
            JSONArray jsonCustomer = (JSONArray) jsonUsers.get("customer");
            JSONArray jsonSeller = (JSONArray) jsonUsers.get("seller");
            JSONArray jsonOwner = (JSONArray) jsonUsers.get("owner");
            JSONArray jsonCashier = (JSONArray) jsonUsers.get("cashier");

            parseUserArray("customer", jsonCustomer);
            parseUserArray("seller", jsonSeller);
            parseUserArray("owner", jsonOwner);
            parseUserArray("cashier", jsonCashier);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseUserArray(String Role, JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            JSONObject jsonUser = (JSONObject) obj;
            String username = (String) jsonUser.get("name");
            String password = (String) jsonUser.get("password");
            User user = null;
            switch(Role) {
                case "customer":
                    user = new Customer(username, password);
                    break;
                case "seller":
                    user = new Seller(username, password);
                    break;
                case "owner":
                    user = new Owner(username, password);
                    break;
                case "cashier":
                    user = new Cashier(username, password);
                    break;
            }
            // check database, load history
            if (user != null) machine.addUser(user);
        }
    }

    private void parseSnacksArray(String category, JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            JSONObject jsonProduct = (JSONObject) obj;
            String name = (String) jsonProduct.get("name");
//            System.out.println(name);
            Double price = (Double) jsonProduct.get("price");
            int quantity = (int) (long) jsonProduct.get("quantity");
            String code = (String) jsonProduct.get("code");
            System.out.printf("> Loaded %s, %s, %f, %d, %s\n", category, name, price, quantity, code);
            machine.addSnacks(machine.getFactoryRegistry().create(category, name, code, quantity, price));
        }
    }

    public void save(VendingMachine machine) {
        // save cash data
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(dbPathCash), machine.getCashInMachine());
            System.out.printf("> Cash Map data successfully written to the %s.json file.\n", dbPathCash);

            this.writeCardToJson();
            System.out.printf("> Card Map data successfully written to the %s.json file.\n", dbPathCard);

            this.saveSnacks();
            System.out.printf("> Snack data successfully written to the %s.json file.\n", dbPathSnacks);

            this.saveUsers();
            System.out.printf("> User data successfully written to the %s.json file.\n", dbPathUsers);


        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }
        // TODO: save user data, write to json file
        System.out.println(machine.getUsers());
        // TODO: update any users database changes
        // TODO: snacks stock update
    }

    public void saveCards() {
        JSONArray arr = new JSONArray();

        LinkedHashMap<String, Integer> cardList = machine.getCardsData();

        for (String key: cardList.keySet()) {
            JSONObject temp = new JSONObject();
            temp.put("name", key);
            temp.put("number", cardList.get(key));
            arr.add(temp);
        }
        //"src/main/resources/credit_cards.json"

        try {
            FileWriter f = new FileWriter(dbPathCard);
            f.write(arr.toJSONString());
            f.close();
            System.out.println("it works");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        JSONArray customer = new JSONArray();
        JSONArray seller = new JSONArray();
        JSONArray cashier = new JSONArray();
        JSONArray owner = new JSONArray();

        for (User user: machine.getUsers()) {

            JSONObject temp = new JSONObject();

            if (user instanceof Customer) {
                temp.put("name", ((Customer) user).getUsername());
                temp.put("password", ((Customer) user).getPassword());
                customer.add(temp);
            }
            else if (user instanceof Seller) {
                temp.put("name", ((Seller) user).getUsername());
                temp.put("password", ((Seller) user).getPassword());
                seller.add(temp);
            }
            else if (user instanceof Cashier) {
                temp.put("name", ((Cashier) user).getUsername());
                temp.put("password", ((Cashier) user).getPassword());
                cashier.add(temp);
            }
            else if (user instanceof  Anonymous) {

            }
            else{
                temp.put("name", ((Owner) user).getUsername());
                temp.put("password", ((Owner) user).getPassword());
                owner.add(temp);
            }
            JSONObject users = new JSONObject();
            users.put("customer", customer);
            users.put("seller", seller);
            users.put("cashier", cashier);
            users.put("owner", owner);

            JSONObject whole = new JSONObject();
            whole.put("users", users);

            try {
                FileWriter f = new FileWriter("src/main/resources/users.json");
                f.write(whole.toJSONString());
                f.close();
                System.out.println("it works");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveSnacks() {
        JSONArray drinks = new JSONArray();
        JSONArray chips = new JSONArray();
        JSONArray chocolates = new JSONArray();
        JSONArray candies = new JSONArray();

        for (Snacks snack: machine.getSnacks()) {

            JSONObject temp = new JSONObject();
            temp.put("name", snack.getName());
            temp.put("price", snack.getPrice());
            temp.put("quantity", snack.getQuantity());
            temp.put("code", snack.getCode());

            if (snack instanceof Drinks) {
                drinks.add(temp);
            }
            else if (snack instanceof Chips) {
                chips.add(temp);
            }
            else if (snack instanceof Chocolates) {
                chocolates.add(temp);
            }
            else candies.add(temp);
        }

        JSONObject snack = new JSONObject();
        snack.put("drinks", drinks);
        snack.put("chips", chips);
        snack.put("chocolates", chocolates);
        snack.put("lollies", candies);

        JSONObject whole = new JSONObject();
        whole.put("snacks", snack);


        try {
            FileWriter f = new FileWriter(dbPathSnacks);
            f.write(whole.toJSONString());
            f.close();
            System.out.println("it works");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCardToJson() {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String key: machine.getCardsData().keySet()) {
            jsonObject = new JSONObject();
            jsonObject.put("name", key);
            jsonObject.put("number", String.valueOf(machine.getCardsData().get(key)));

            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);

        try{
            FileWriter file = new FileWriter(dbPathCard);
            file.write(jsonArray.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//========================================================================
    // fxml Controller


    public void ktest(MouseEvent mouseEvent) {
//        System.out.println("UI Testing");
        this.save(machine);

    }

    public void ktestTwo(MouseEvent mouseEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(App.class.getResource("fxml2/root.fxml"));

        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        scene = new Scene(pane);
        stage.setScene(scene);
//        stage.sizeToScene();
        stage.show();

    }

    public void clickToVersionOne(MouseEvent mouseEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void clickToLoginUser(MouseEvent mouseEvent) throws IOException {
        Label msgToUser = labelCreate("", 100, 430);

        String userName = UserName.getText();
        String password = Password.getText();

        User user = machine.login(userName, password);
        if (user != null) {
            clickToMainPage(mouseEvent);
        }

    }

    public void register(MouseEvent mouseEvent) throws IOException {
        String userName = UserName.getText();
        String password = Password.getText();
        User user = new Customer(userName, password);

        if (machine.checkUser(userName, password)) {
            machine.addUser(user);
            machine.login(userName, password);
            clickToMainPage(mouseEvent);
        }
    }

    public abstract class AddMinusButtonHandler implements EventHandler<ActionEvent> {
        private Snacks snack;
        private Label label;
        private Label labelTwo;

        public AddMinusButtonHandler(Snacks snack, Label label) {
            this.snack = snack;
            this.label = label;
        }
        public AddMinusButtonHandler(Snacks snack, Label label, Label labelTwo) {
            this.snack = snack;
            this.label = label;
            this.labelTwo = labelTwo;
        }

        public Snacks getSnack() {
            return this.snack;
        }

        public Label getLabelTwo() {
            return this.labelTwo;
        }
        public Label getLabel() {
            return this.label;
        }
    }

    public abstract class cashAddMinusButtonHandler implements EventHandler<ActionEvent> {
        private Cash cash;
        private String type;
        private Label label;

        public cashAddMinusButtonHandler(Cash cash, String type, Label label) {
            this.cash = cash;
            this.type = type;
            this.label = label;
        }

        public Cash getCash() {
            return this.cash;
        }

        public String getType(){
            return this.type;
        }

        public Label getLabel() {
            return this.label;
        }
    }

    public abstract class getChangeButtonHandler implements EventHandler<ActionEvent> {
        private Transaction t;
        private AnchorPane p;
        private Label l;

        public getChangeButtonHandler(Transaction t, AnchorPane p, Label l) {
            this.t = t;
            this.p = p;
            this.l = l;
        }

        public Label getLabel() {
            return this.l;
        }
        public Transaction getTransaction() {
            return this.t;
        }
        public AnchorPane getAnchorPane() {
            return this.p;
        }
    }
    public abstract class updateSnackButtonHandler implements EventHandler<ActionEvent> {

        private Snacks snack;

        public updateSnackButtonHandler(Snacks snack) {
            this.snack = snack;
        }

        public Snacks getSnack() {
            return this.snack;
        }
    }

    public abstract class updateCashButtonHandler implements EventHandler<ActionEvent> {

        private String type;
        private int qty;

        public updateCashButtonHandler(String type, int qty) {
            this.type = type;
            this.qty = qty;
        }

        public String getType() {
            return this.type;
        }

        public int getQty() {
            return this.qty;
        }

    }

//    public abstract class payByCardHandler implements EventHandler<ActionEvent> {
//        private Transaction t;
//        private AnchorPane p;
//        private Label l;
//        private String name;
//        private int number;
//
//
//        public payByCardHandler(Transaction t, AnchorPane p, Label l, String name, int number) {
//            this.t = t;
//            this.p = p;
//            this.l = l;
//            this.name = name;
//            this.number = number;
//        }
//
//        public String getCardName() {
//            return this.name;
//        }
//
//        public int getCardNumber() {
//            return this.number;
//        }
//        public Label getLabel() {
//            return this.l;
//        }
//        public Transaction getTransaction() {
//            return this.t;
//        }
//        public AnchorPane getAnchorPane() {
//            return this.p;
//        }
//    }


    public Button buttonCreate(String label, double x, double y) {
        Button button = new Button(label);
        button.setLayoutX(x);
        button.setLayoutY(y);
        return button;
    }

    public Label labelCreate(String label, double x, double y) {
        Label lb = new Label(label);
        lb.setLayoutX(x);
        lb.setLayoutY(y);
        return lb;
    }



    public void clickToMainPage(MouseEvent mouseEvent) throws IOException {
//        machine.newCart();
        if (machine.getUser() == null) machine.loginAnonymous();

        BorderPane borderPane = new BorderPane();

        AnchorPane pane = FXMLLoader.load(App.class.getResource("fxml/Main.fxml"));

        GridPane recentItemsPane = new GridPane();
        List<Snacks> recentSnacks = machine.getUser().getRecentItems();
        int row = 1;
//        int column = 1;

        if (recentSnacks.size() != 0) {
            Label recentItemLabel = new Label("Recent Items Purchased");
            recentItemsPane.add(recentItemLabel, 0, 0);
            for (Snacks snack : recentSnacks) {
                String itemCode = String.format("Item Code: %s", snack.getCode());
                String itemName = String.format("Item Name: %s", snack.getName());
                String itemPrice = String.format("Price: $ %.2f", snack.getPrice());

                Label codeLabel = new Label(itemCode);
                Label nameLabel = new Label(itemName);
                Label priceLabel = new Label(itemPrice);
                recentItemsPane.add(codeLabel, 0, row);
                recentItemsPane.add(nameLabel, 1, row);
                recentItemsPane.add(priceLabel, 2, row);
                row++;

            }
        }
        System.out.println("> trying to get the list of recent purchase items");

        recentItemsPane.setHgap(20);
        recentItemsPane.setVgap(20);

//        AnchorPane bottomPane = new AnchorPane();
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    autoLogout();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        HBox hbox = new HBox(30, logout);
        hbox.setAlignment(Pos.CENTER_RIGHT);
//        bottomPane.getChildren().add(hbox);

        borderPane.setTop(pane);

        borderPane.setCenter(recentItemsPane);
        borderPane.setAlignment(recentItemsPane, Pos.CENTER);
        borderPane.setMargin(recentItemsPane, new Insets(20,20,20,50));

        borderPane.setBottom(hbox);
        borderPane.setAlignment(hbox, Pos.CENTER_RIGHT);
        borderPane.setMargin(hbox, new Insets(20,50,50,20));

        // Seller Specific :
        if (machine.getUser() instanceof Seller) {
            GridPane controlPane = new GridPane();
            Label commands = new Label("Available Commands:");
            Button showAllCurrentItems = new Button("Available Items");
            showAllCurrentItems.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createAvailableItemsPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button summaryButton = new Button("Summary");
            summaryButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createSoldRecordPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            controlPane.add(commands, 0,0);
            controlPane.add(showAllCurrentItems, 0, 1);
            controlPane.add(summaryButton, 0, 2);
            controlPane.setHgap(20);
            controlPane.setVgap(20);

            borderPane.setLeft(controlPane);
            borderPane.setAlignment(controlPane, Pos.CENTER_RIGHT);
            borderPane.setMargin(controlPane, new Insets(20,20,20,50));

            // cashier specific
        } else if (machine.getUser() instanceof Cashier) {
            GridPane controlPane = new GridPane();
            Label commands = new Label("Available Commands:");
            Button showAllCash = new Button("Available Cash");
            showAllCash.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createAvailableCashPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button showTransaction = new Button("Transactions");
            showTransaction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createTransactionsSummaryPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            controlPane.add(commands, 0,0);
            controlPane.add(showAllCash, 0, 1);
            controlPane.add(showTransaction, 0, 2);
            controlPane.setHgap(20);
            controlPane.setVgap(20);

            borderPane.setLeft(controlPane);
            borderPane.setAlignment(controlPane, Pos.CENTER_RIGHT);
            borderPane.setMargin(controlPane, new Insets(20,20,20,50));

            // owner specific
        } else if (machine.getUser() instanceof Owner) {
            /*This role is able to add/remove Seller or Cashier or Owner user(s).*/
            GridPane controlPane = new GridPane();
            Label commands = new Label("Available Commands:");

            Button userManagementButton = new Button("User Management");
            userManagementButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createUserManagementPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button showFailedTransaction = new Button("Failed Transaction");
            showFailedTransaction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createFailedTransactionSummary(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button showAllCurrentItems = new Button("Available Items");
            showAllCurrentItems.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createAvailableItemsPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button summaryButton = new Button("Sold Record Summary");
            summaryButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createSoldRecordPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button showAllCash = new Button("Available Cash");
            showAllCash.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createAvailableCashPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            Button showTransaction = new Button("Transactions");
            showTransaction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createTransactionsSummaryPane(borderPane));
                    stage.setMaximized(true);
                }
            });

            controlPane.add(commands, 0,0);
            controlPane.add(userManagementButton, 0, 1);
            controlPane.add(showTransaction,0,2);
            controlPane.add(showFailedTransaction, 0, 3);
            controlPane.add(showAllCurrentItems, 0, 4);
            controlPane.add(summaryButton,0,5);
            controlPane.add(showAllCash,0,6);

            controlPane.setHgap(20);
            controlPane.setVgap(20);

            borderPane.setLeft(controlPane);
            borderPane.setAlignment(controlPane, Pos.CENTER_RIGHT);
            borderPane.setMargin(controlPane, new Insets(20,20,20,50));
        }

        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        scene = new Scene(borderPane);
        stage.setScene(scene);
//        stage.sizeToScene();
        stage.show();
    }

    public GridPane createFailedTransactionSummary(BorderPane borderPane) {
        GridPane summaryPane = new GridPane();

        int row = 1;
        Label paneLabel = new Label("Failed Transactions Summary: ");
        summaryPane.add(paneLabel, 0,0);

        for (Transaction t : machine.getCancelTransactions()) {
            /*transaction date and time, item sold, amount of money paid, returned change and payment method*/
            String tID = String.format("Transaction ID: %d", t.getTransactionId());
            String tDate = String.format("Transaction Date : %s", t.getDate().toString());
//            expand
            String tMethod = String.format("Payment Method: %s", t.getPaymentMethod());
            String tReason = String.format("Reason : %s", t.getReason());

            Label idLabel = new Label(tID);
            Label dateLabel = new Label(tDate);
            Label methodLabel = new Label(tMethod);
            Label reasonLabel = new Label(tReason);

            summaryPane.add(idLabel, 0, row);
            summaryPane.add(dateLabel, 1, row);
            summaryPane.add(methodLabel, 2, row);
            summaryPane.add(reasonLabel, 3, row);

            row++;
        }
        summaryPane.setVgap(20);
        summaryPane.setHgap(20);
        return summaryPane;
    }

    public GridPane createUserManagementPane(BorderPane borderPane){
        GridPane gp = new GridPane();
        // remove
        int row = 1;
        Label gpLabel = new Label("User Management: ");
        gp.add(gpLabel,0,0);

        Button addUser = new Button("Add Admin Users");
        addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane inputPane = new GridPane();

                Label usernameLabel = new Label("UserName");
                Label passwordLabel = new Label("Password");
                TextField usernameInput = new TextField();
                TextField passwordInput = new TextField();
                inputPane.add(usernameLabel, 0, 1);
                inputPane.add(passwordLabel, 0, 2);
                inputPane.add(usernameInput, 1, 1);
                inputPane.add(passwordInput, 1, 2);

                Button ownerButton = new Button("OWNER");
                ownerButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        User newUser = new Owner(usernameInput.getText(), passwordInput.getText());
                        machine.addUser(newUser);

                        GridPane confirmPane = new GridPane();
                        Text confirmText = new Text("> DONE !!");
                        confirmPane.add(confirmText, 1, 1);
                        borderPane.setCenter(confirmPane);
                    }
                });
                Button cashierButton = new Button("CASHIER");
                cashierButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        User newUser = new Cashier(usernameInput.getText(), passwordInput.getText());
                        machine.addUser(newUser);
                        GridPane confirmPane = new GridPane();
                        Text confirmText = new Text("> DONE !!");
                        confirmPane.add(confirmText, 1, 1);
                        borderPane.setCenter(confirmPane);
                    }
                });
                Button sellerButton = new Button("SELLER");
                sellerButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        User newUser = new Seller(usernameInput.getText(), passwordInput.getText());
                        machine.addUser(newUser);
                        GridPane confirmPane = new GridPane();
                        Text confirmText = new Text("> DONE !!");
                        confirmPane.add(confirmText, 1, 1);
                        borderPane.setCenter(confirmPane);
                    }
                });

                inputPane.add(ownerButton, 3, 1);
                inputPane.add(cashierButton, 3, 2);
                inputPane.add(sellerButton, 3, 3);

                inputPane.setHgap(20);
                inputPane.setVgap(20);

                borderPane.setCenter(inputPane);

            }
        });
        gp.add(addUser, 1, 0);

        for(User user : machine.getUsers()) {
            if(user instanceof Owner || user instanceof Seller || user instanceof Cashier) {
                Label userType = new Label(String.format("User Type: '%s'", ((Register) user).getClass().getSimpleName()));
                gp.add(userType, 0, row);

                Label userName = new Label(String.format("Username : %s", ((Register) user).getUsername()));
                gp.add(userName, 1, row);

                Label passwordLabel = new Label(String.format("Password : [%s]", ((Register) user).getPassword()));
                gp.add(passwordLabel, 2, row);

                Button removeButton = new Button("remove");
                removeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        machine.removeUser(user);

                        GridPane confirmPane = new GridPane();
                        Text confirmText = new Text("> DONE !!");
                        confirmPane.add(confirmText, 1, 1);
                        borderPane.setCenter(confirmPane);
                    }
                });
                gp.add(removeButton, 3, row);
                row++;
            }
        }

        gp.setVgap(20);
        gp.setHgap(20);
        return gp;
    }

    public GridPane createTransactionsSummaryPane(BorderPane borderPane) {
        GridPane summaryPane = new GridPane();

        int row = 1;
        Label paneLabel = new Label("Transactions Summary: ");
        summaryPane.add(paneLabel, 0,0);

        for (Transaction t : machine.getTransactions()) {
            /*transaction date and time, item sold, amount of money paid, returned change and payment method*/
            String tID = String.format("Transaction ID: %d", t.getTransactionId());
            String tDate = String.format("Transaction Date : %s", t.getDate().toString());
//            expand
            String tMethod = String.format("Payment Method: %s", t.getPaymentMethod());
            String tPaidAmount;
            String tReturnChange;

            if (t.getPayment() instanceof Cash) {
                tPaidAmount = String.format("Paid Amount : $ %.2f", (((Cash) t.getPayment()).getInsertedValue()));
                tReturnChange = String.format("Return Change : $ %.2f", (((Cash) t.getPayment()).getBillAmount()));
            } else {
                tPaidAmount = String.format("Paid Amount : $ %.2f", (t.getCart().getCartBillAmount()));
                tReturnChange = String.format("Return Change : $ 0.00");
            }

            Label idLabel = new Label(tID);
            Label dateLabel = new Label(tDate);
            Label methodLabel = new Label(tMethod);
            Label paidLabel = new Label(tPaidAmount);
            Label changeLabel = new Label(tReturnChange);
            Button expand = new Button("Details");
            expand.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Iterator<Entry<Snacks, Integer> > new_i = t.getCart().getList().entrySet().iterator();
                    GridPane gp = new GridPane();
                    int row = 1;
                    while (new_i.hasNext()) {
                        Map.Entry<Snacks, Integer> map = new_i.next();
                        Label nameLabel = new Label(String.format("ItemName: %s", map.getKey().getName()));
                        Label codeLabel = new Label(String.format("ItemCode: %s", map.getKey().getCode()));
                        Label qtyLabel = new Label(String.format("Qty: %d", map.getValue()));

                        gp.add(nameLabel, 0, row);
                        gp.add(codeLabel, 1, row);
                        gp.add(qtyLabel, 2, row);
                        row++;
                    }
                    gp.setVgap(20);
                    gp.setHgap(20);
                    borderPane.setCenter(gp);

                }
            });

            summaryPane.add(idLabel, 0, row);
            summaryPane.add(dateLabel, 1, row);
            summaryPane.add(methodLabel, 2, row);
            summaryPane.add(paidLabel, 3, row);
            summaryPane.add(changeLabel, 4, row);
            summaryPane.add(expand, 5, row);

            row++;
        }
        summaryPane.setVgap(20);
        summaryPane.setHgap(20);
        return summaryPane;
    }

    public GridPane createAvailableCashPane(BorderPane borderPane) {
        GridPane summaryPane = new GridPane();


        Label paneLabel = new Label("List: Current Cash in the Machine.");
        summaryPane.add(paneLabel, 0,0);

        Iterator<Entry<String, Integer> > new_i = machine.getCashInMachine().entrySet().iterator();
        int row = 1;
        while (new_i.hasNext()) {
            Map.Entry<String, Integer> map = new_i.next();
            Label typeLabel = new Label(String.format("Type: $ %s", map.getKey()));
            Label qtyLabel = new Label(String.format("Qty: %d",map.getValue()));

            Button updateButton = new Button("update");
            updateButton.setOnAction(new updateCashButtonHandler(map.getKey(), map.getValue()) {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createUpdateCashDetailsPane(borderPane, getType(), getQty()));
                    stage.setMaximized(true);
                }
            });;

            summaryPane.add(typeLabel, 0, row);
            summaryPane.add(qtyLabel, 1, row);
            summaryPane.add(updateButton, 2, row);

            row++;
        }
        summaryPane.setVgap(20);
        summaryPane.setHgap(20);
        return summaryPane;
    }

    public GridPane createUpdateCashDetailsPane(BorderPane pane, String type, int qty) {
        GridPane gridPane = new GridPane();

        Label typeLabel = new Label(String.format("Type $ %s: ", type));
        Label qtyLabel = new Label(String.format("Current Quantity : %d -> ", qty));

//        VBox labelBox = new VBox(10, nameLabel, codeLabel, categoryLabel, quantityLabel, priceLabel);
        gridPane.add(typeLabel, 0, 1);
        gridPane.add(qtyLabel, 0, 2);

        TextField typeField = new TextField(type);
        typeField.setEditable(false);
        TextField qtyField = new TextField(String.valueOf(qty));

        gridPane.add(typeField, 1, 1);
        gridPane.add(qtyField, 1, 2);

        Button submit = new Button("submit");
        gridPane.add(submit, 1, 3);
        submit.setOnAction(new updateCashButtonHandler(type, qty) {
            @Override
            public void handle(ActionEvent event) {

                String qtyInputStr = qtyField.getText();
                int qtyInput;
                try {
                    qtyInput = Integer.parseInt(qtyInputStr);
                    machine.getCashInMachine().put(getType(), qtyInput);
                    pane.setCenter(new AnchorPane());
                } catch (NumberFormatException e) {
                    System.out.println("> input error : integer only");
                }
            }
        });

        gridPane.setHgap(30);
        gridPane.setVgap(10);
        return gridPane;
    }

    public GridPane createSoldRecordPane(BorderPane borderPane) {
        GridPane summaryPane = new GridPane();

        int row = 1;
        Label paneLabel = new Label("Items Sold Summary: ");
        summaryPane.add(paneLabel, 0,0);
        for (Snacks snack : machine.getSnacks()) {

            String itemCode = String.format("Item Code: %s", snack.getCode());
            String itemName = String.format("Item Name: %s", snack.getName());
            String itemQuantity = String.format("Sold : %d", snack.getSold());

            Label codeLabel = new Label(itemCode);
            Label nameLabel = new Label(itemName);
            Label quantityLabel = new Label(itemQuantity);

            summaryPane.add(codeLabel, 0, row);
            summaryPane.add(nameLabel, 1, row);
            summaryPane.add(quantityLabel, 2, row);

            row++;
        }
        summaryPane.setVgap(20);
        summaryPane.setHgap(20);
        return summaryPane;
    }
    public GridPane createUpdatePane(BorderPane pane, Snacks snack) {
        GridPane gridPane = new GridPane();

        Label nameLabel = new Label("Name");
        Label codeLabel = new Label("Code");
        Label categoryLabel = new Label("Category");
        Label quantityLabel = new Label("Quantity");
        Label priceLabel = new Label("Price");

//        VBox labelBox = new VBox(10, nameLabel, codeLabel, categoryLabel, quantityLabel, priceLabel);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(codeLabel, 0, 2);
        gridPane.add(categoryLabel, 0, 3);
        gridPane.add(quantityLabel, 0, 4);
        gridPane.add(priceLabel, 0, 5);

        TextField name = new TextField(snack.getName());
        TextField code = new TextField(snack.getCode());
        TextField category = new TextField(snack.getClass().getSimpleName());
        TextField quantity = new TextField(String.valueOf(snack.getQuantity()));
        TextField price = new TextField(String.valueOf(snack.getPrice()));

        gridPane.add(name, 1, 1);
        gridPane.add(code, 1, 2);
        gridPane.add(category, 1, 3);
        gridPane.add(quantity, 1, 4);
        gridPane.add(price, 1, 5);

        Button submit = new Button("submit");
        gridPane.add(submit, 1, 6);
        submit.setOnAction(new updateSnackButtonHandler(snack) {
            @Override
            public void handle(ActionEvent event) {
                String nameInput = name.getText();
                String codeInput = code.getText();
                String categoryInput = category.getText();
                String quantityInput = quantity.getText();
                String priceInput = price.getText();

                // TODO: change category, switch

                if (Integer.parseInt(quantityInput) >= 15) {
                    System.out.println("> Incorrect Quantity Number");
                    quantity.setText("Incorrect Input");
                } else {
                    snack.setQuantity(Integer.parseInt(quantityInput));
                    snack.setName(nameInput);
                    snack.setCode(codeInput);
                    snack.setPrice(Double.parseDouble(priceInput));
                    pane.setCenter(new AnchorPane());
                }
            }
        });

        gridPane.setHgap(30);
        gridPane.setVgap(10);
        return gridPane;
    }
    public GridPane createAvailableItemsPane(BorderPane borderPane) {
        GridPane summaryPane = new GridPane();

        int row = 1;
        Label paneLabel = new Label("List: Current Available Items in the Machine.");
        summaryPane.add(paneLabel, 0,0);
        for (Snacks snack : machine.getSnacks()) {
            String itemCode = String.format("Item Code: %s", snack.getCode());
            String itemName = String.format("Item Name: %s", snack.getName());
            String itemPrice = String.format("Price: $ %.2f", snack.getPrice());
            String itemQuantity = String.format("Quantity : %d", snack.getQuantity());

            Label codeLabel = new Label(itemCode);
            Label nameLabel = new Label(itemName);
            Label priceLabel = new Label(itemPrice);
            Label quantityLabel = new Label(itemQuantity);
            Button updateButton = new Button("update");
            updateButton.setOnAction(new updateSnackButtonHandler(snack) {
                @Override
                public void handle(ActionEvent event) {
                    borderPane.setCenter(createUpdatePane(borderPane, getSnack()));
                    stage.setMaximized(true);
                }
            });

            summaryPane.add(codeLabel, 0, row);
            summaryPane.add(nameLabel, 1, row);
            summaryPane.add(priceLabel, 2, row);
            summaryPane.add(quantityLabel, 3, row);
            summaryPane.add(updateButton, 4, row);

            row++;
        }
        summaryPane.setVgap(20);
        summaryPane.setHgap(20);
        return summaryPane;


    }

    public void ExitFromTransaction(MouseEvent mouseEvent) throws IOException {
        if (!machine.isProcessing()) {
            machine.newCart();
            root = FXMLLoader.load(App.class.getResource("fxml/Main.fxml"));
            stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public AnchorPane productsMenu(String type, String xml) throws ClassNotFoundException, IOException {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource(xml)));

        int n = 0;
        for (Snacks snack : machine.getSnacks()) {
            if (Class.forName(type).isInstance(snack)) {
//            if (snack instanceof categories) {
                Label productName = labelCreate(snack.getName(), 100, 100+n);

                String priceDisplay = String.format("Price: $ %.2f\n", snack.getPrice());
                Label price = labelCreate(priceDisplay, 100, 120+n);

                String quantityDisplay = String.format("Stock: %d\n", snack.getQuantity());
                Label quantity = labelCreate(quantityDisplay, 100, 140 + n);

                // selected number
                String selectedQuantity = "0";
                if (machine.getCart().getList().containsKey(snack)) {
                    selectedQuantity = String.valueOf(machine.getCart().getList().get(snack));
                }
                Label selected = labelCreate(selectedQuantity, 280, 120 + n);

                Button addToCart = buttonCreate("+", 300, 115+n);
                addToCart.setOnAction(new AddMinusButtonHandler(snack, selected) {
                    @Override
                    public void handle(ActionEvent event) {
//                        System.out.println("clicked");
                        // TODO: limit -> max number to add, based on quantity
                        machine.getCart().addSnacks(getSnack());
                        getLabel().setText(String.valueOf(machine.getCart().getList().get(getSnack())));
//                        System.out.println(machine.getCart().getList().get(getSnack()));
                    }
                });

                Button remove = buttonCreate("-", 250, 115 + n);
                remove.setOnAction(new AddMinusButtonHandler(snack, selected) {
                    @Override
                    public void handle(ActionEvent event) {
//                        System.out.println("clicked");
                        if (machine.getCart().getList().containsKey(getSnack()) && machine.getCart().getList().get(getSnack()) > 1) {
                            machine.getCart().getList().put(getSnack(), machine.getCart().getList().get(getSnack()) - 1);
                            getLabel().setText(String.valueOf(machine.getCart().getList().get(getSnack())));
                        } else {
                            machine.getCart().getList().remove(getSnack());
                            getLabel().setText("0");
                        }
//                        System.out.println(machine.getCart().getList().get(getSnack()));
                    }
                });

                n += 100; // position shift
                pane.getChildren().add(productName);
                pane.getChildren().add(price);
                pane.getChildren().add(quantity);
                pane.getChildren().add(addToCart);
                pane.getChildren().add(remove);
                pane.getChildren().add(selected);

            }
        }
        return pane;
    }

    public void toDrinks(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        AnchorPane pane = productsMenu("Snacks.Drinks", "fxml/DrinksV2.fxml");
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void toChocolates(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        AnchorPane pane = productsMenu("Snacks.Chocolates", "fxml/Chocolates.fxml");
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void toChips(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        AnchorPane pane = productsMenu("Snacks.Chips", "fxml/Chips.fxml");
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void toCandies(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        AnchorPane pane = productsMenu("Snacks.Candies", "fxml/Candies.fxml");
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void clickToPayTab(MouseEvent mouseEvent) throws IOException {

        AnchorPane pane = new AnchorPane();
        pane.getChildren().clear();
        pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource("fxml/Payment.fxml")));

        // label to show billAmount
        String billAmountDisplayString = String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount());
        Label billAmountDisplay = labelCreate(billAmountDisplayString, 120, 200);
        pane.getChildren().add(billAmountDisplay);

        // print summary
        int shift = 30;
        Iterator<Entry<Snacks, Integer> > new_Iterator = machine.getCart().getList().entrySet().iterator();
        while (new_Iterator.hasNext()) {
            Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
            // Displaying HashMap
            String itemSummary = String.format("Item name:  %s  x  %d\n", new_Map.getKey().getName(), new_Map.getValue());
            Label itemSummaryLabel = labelCreate(itemSummary, 120, 230 + shift);

            Button addQtyButton = buttonCreate("+", 70, 230 + shift);
            addQtyButton.setOnAction(new AddMinusButtonHandler(new_Map.getKey(), itemSummaryLabel, billAmountDisplay) {
                @Override
                public void handle(ActionEvent event) {
                    Snacks s = getSnack();
                    LinkedHashMap<Snacks, Integer> cartMap = machine.getCart().getList();
                    if (cartMap.containsKey(s)) {
                        machine.getCart().addSnacks(s);
                        getLabel().setText(String.format("Item name:  %s  x  %d\n", s.getName(), cartMap.get(s)));
                        getLabelTwo().setText(String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount()));
                    }
                }
            });


            Button rmQtyButton = buttonCreate("-", 35, 230 + shift);
            rmQtyButton.setOnAction(new AddMinusButtonHandler(new_Map.getKey(), itemSummaryLabel, billAmountDisplay) {
                @Override
                public void handle(ActionEvent event) {
                    Snacks s = getSnack();
                    LinkedHashMap<Snacks, Integer> cartMap = machine.getCart().getList();
                    if (cartMap.containsKey(s) && cartMap.get(s) > 0) {
                        machine.getCart().removeSnacks(s);
                        getLabel().setText(String.format("Item name:  %s  x  %d\n", s.getName(), cartMap.get(s)));
                        getLabelTwo().setText(String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount()));
                    }
                }
            });

            shift += 50;
            pane.getChildren().add(addQtyButton);
            pane.getChildren().add(rmQtyButton);
            pane.getChildren().add(itemSummaryLabel);
        }


        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void cash(MouseEvent mouseEvent) throws IOException {
        AnchorPane pane = new AnchorPane();

        machine.getCart().clearZero();
        Transaction t = new Transaction(machine, machine.getUser(), machine.getCart());
        t.payAsCash();
        machine.processing();

        // display
        pane.getChildren().clear();
        pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource("fxml/Cash.fxml")));

        // label to show billAmount
        String billAmountDisplayString = String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount());
        Label billAmountDisplay = labelCreate(billAmountDisplayString, 180, 200);
        pane.getChildren().add(billAmountDisplay);

        Cash cash = (Cash) t.getPayment();
        String insertedAmountDisplayStr = String.format("Inserted Value : $ %.2f\n", cash.getInsertedValue());
        Label insertedAmountDisplay = labelCreate(insertedAmountDisplayStr, 200, 240);

        Iterator<Entry<String, Integer> > new_i = machine.getCashInMachine().entrySet().iterator();
        int shift_cash = 0;
        while (new_i.hasNext()) {
            Map.Entry<String, Integer> map = new_i.next();
//            System.out.println(map.getKey());
            Label coinsLabel = labelCreate(String.format("$ %s\n", map.getKey()), 240, 270 + shift_cash);

            Button addCashButton = buttonCreate("+", 290, 270+shift_cash);
            addCashButton.setOnAction(new cashAddMinusButtonHandler((Cash) t.getPayment(), map.getKey(), insertedAmountDisplay) {
                @Override
                public void handle(ActionEvent event) {
                    getCash().insertCash(getType());
                    String insertedAmountDisplayStr = String.format("Inserted Value : $ %.2f\n", getCash().getInsertedValue());
                    getLabel().setText(insertedAmountDisplayStr);
                    getLabel().setLayoutX(200);
                    getLabel().setLayoutY(240);
                }
            });

            Button removeCash = buttonCreate("-", 200, 270+shift_cash);
            removeCash.setOnAction(new cashAddMinusButtonHandler((Cash) t.getPayment(), map.getKey(), insertedAmountDisplay) {
                @Override
                public void handle(ActionEvent event) {
                    getCash().ejectCash(getType());
                    String insertedAmountDisplayStr = String.format("Inserted Value : $ %.2f\n", getCash().getInsertedValue());
                    getLabel().setText(insertedAmountDisplayStr);
                    getLabel().setLayoutX(200);
                    getLabel().setLayoutY(240);
                }
            });

            pane.getChildren().add(coinsLabel);
            pane.getChildren().add(addCashButton);
            pane.getChildren().add(removeCash);
            shift_cash += 30;
        }

        Button getChangeButton = buttonCreate("PAY", 240, 580);
        getChangeButton.setOnAction(new getChangeButtonHandler(t, pane, insertedAmountDisplay) {
            @Override
            public void handle(ActionEvent event) {
                // payment execution
                if (getTransaction().isCompleted()) return;
                if (getTransaction().isCancel()) return;
                Result result = getTransaction().execute();
                getTransaction().getPayment().pause();

                // Testing: Transaction status return after change calculation
//                System.out.println(getTransaction().isCompleted());
//                System.out.println(result.toString());

                switch(result) {
                    case NOCHANGE:
                        System.out.println("no change needed");
                        Label noChange = labelCreate("Successful: Collect your item(s)", 100, 650);
                        getAnchorPane().getChildren().add(noChange);

                        // invoice
                        System.out.println("> successful transaction");

                        // label to show invoice
                        String invoiceTitle = String.format("TransactionID: %d \n " +
                                "Paid: $ %.2f", t.getTransactionId(), machine.getCart().getCartBillAmount());
                        Label invoiceLabel = labelCreate(invoiceTitle, 380, 300);
                        getAnchorPane().getChildren().add(invoiceLabel);

                        // print summary
                        int shift = 20;
                        Iterator<Entry<Snacks, Integer> > new_Iterator = machine.getCart().getList().entrySet().iterator();
                        while (new_Iterator.hasNext()) {
                            Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
                            // Displaying HashMap
                            String itemSummary = String.format("Item name:  %s  x  %d\n", new_Map.getKey().getName(), new_Map.getValue());
                            Label itemSummaryLabel = labelCreate(itemSummary, 380, 330 + shift);
                            shift += 20;
                            getAnchorPane().getChildren().add(itemSummaryLabel);
                        }

                        machine.idle();
                        Timeline timer = new Timeline(
                                new KeyFrame(Duration.seconds(10),
                                        event1 -> {
                                            try {
                                                autoLogout();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }));
                        timer.play();

                        break;

                    case ENOUGH:
                        Cash c = (Cash) getTransaction().getPayment();
                        System.out.printf("TESTING: change map : %s\n",c.getChangeMap());

                        System.out.println("> successful transaction");

                        // label to show invoice
                        invoiceTitle = String.format("TransactionID: %d \n " +
                                "Paid: $ %.2f", t.getTransactionId(), machine.getCart().getCartBillAmount());
                        invoiceLabel = labelCreate(invoiceTitle, 380, 300);
                        getAnchorPane().getChildren().add(invoiceLabel);

                        // print summary
                        shift = 20;
                        new_Iterator = machine.getCart().getList().entrySet().iterator();
                        while (new_Iterator.hasNext()) {
                            Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
                            // Displaying HashMap
                            String itemSummary = String.format("Item name:  %s  x  %d\n", new_Map.getKey().getName(), new_Map.getValue());
                            Label itemSummaryLabel = labelCreate(itemSummary, 380, 330 + shift);
                            shift += 20;
                            pane.getChildren().add(itemSummaryLabel);
                        }

                        // change result
                        Iterator<Entry<String, Integer> > changeResult_i = c.getChangeMap().entrySet().iterator();
                        Label lb = labelCreate("Change Result: ", 100, 650);
                        getAnchorPane().getChildren().add(lb);

                        int n = 0;
                        while (changeResult_i.hasNext()) {
                            Map.Entry<String, Integer> changeMap = changeResult_i.next();
                            String resultStr = String.format("Type: $ %s x %d\n", changeMap.getKey(), changeMap.getValue());
                            Label resultLabel = labelCreate(resultStr, 100, 670 + n);
                            getAnchorPane().getChildren().add(resultLabel);
                            n += 18;
                        }

                        getLabel().setText("Collect Your Change Below.");
                        machine.idle();

                        timer = new Timeline(
                                new KeyFrame(Duration.seconds(10),
                                        event1 -> {
                                            try {
                                                autoLogout();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }));
                        timer.play();

                        break;

                    case NOTENOUGH:
                        System.out.println("CANCEL or TRY again");

                        Label failedOutcome = labelCreate("No available change for you, please press Refund. \n" +
                                "you can Cancel Transaction or Try different coins / notes.", 100, 680);
                        getAnchorPane().getChildren().add(failedOutcome);

                        Button cancel = buttonCreate("CANCEL Transaction", 400, 400);
                        getAnchorPane().getChildren().add(cancel);

                        // CANCEL TRANSACTION
                        cancel.setOnAction(new getChangeButtonHandler(getTransaction(), getAnchorPane(), getLabel()) {
                            @Override
                            public void handle(ActionEvent event) {
                                Cash c = (Cash) getTransaction().getPayment();
                                c.ejectCash("all");
                                getTransaction().cancel();
                                getTransaction().recordReason("change not available");

                                String str = String.format("[ Transaction ID: %d ] Cancelled by user.", getTransaction().getTransactionId());
                                getLabel().setText(str);
                                getAnchorPane().getChildren().subList(getAnchorPane().getChildren().size()-2, getAnchorPane().getChildren().size()).clear();
                                machine.idle();

                                Timeline timer = new Timeline(
                                        new KeyFrame(Duration.seconds(10),
                                                event1 -> {
                                                    try {
                                                        autoLogout();
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }));
                                timer.play();
                            }
                        });

                        // TRY AGAIN
                        Button tryAgain = buttonCreate("TRY AGAIN", 400, 450);
                        getAnchorPane().getChildren().add(tryAgain);
                        tryAgain.setOnAction(new getChangeButtonHandler(getTransaction(), getAnchorPane(), getLabel()) {
                            @Override
                            public void handle(ActionEvent event) {
                                Cash c = (Cash) getTransaction().getPayment();
                                c.ejectCash("all");
                                getTransaction().getPayment().unpause();
                                getLabel().setText("Refunded. Try again!");
//                                System.out.println(getTransaction().getPayment().getInsertedValue());
                                getAnchorPane().getChildren().subList(getAnchorPane().getChildren().size()-3, getAnchorPane().getChildren().size()).clear();
                            }
                        });
                        break;

                    case PAY:
                        System.out.println("please insert more coins/ notes to proceed payment");
                        Label insufficientDisplay = labelCreate("Insufficient Amount!!\n" +
                                "Cancel or enter the remaining amount", 100, 680);
                        getAnchorPane().getChildren().add(insufficientDisplay);

                        Button quit = buttonCreate("CANCEL Transaction", 400, 400);
                        getAnchorPane().getChildren().add(quit);

                        // CANCEL TRANSACTION
                        quit.setOnAction(new getChangeButtonHandler(getTransaction(), getAnchorPane(), getLabel()) {
                            @Override
                            public void handle(ActionEvent event) {
                                Cash c = (Cash) getTransaction().getPayment();
                                c.ejectCash("all");
                                getTransaction().cancel();
                                getTransaction().recordReason("user cancelled");
                                String str = String.format("[ Transaction ID: %d ] Cancelled by user.", getTransaction().getTransactionId());
                                getLabel().setText(str);
                                getAnchorPane().getChildren().subList(getAnchorPane().getChildren().size()-2, getAnchorPane().getChildren().size()).clear();
                                machine.idle();
                                Timeline timer = new Timeline(
                                        new KeyFrame(Duration.seconds(10),
                                                event1 -> {
                                                    try {
                                                        autoLogout();
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }));
                                timer.play();
                            }
                        });

                        // TRY AGAIN
                        Button insertMore = buttonCreate("Insert More", 400, 450);
                        getAnchorPane().getChildren().add(insertMore);
                        insertMore.setOnAction(new getChangeButtonHandler(getTransaction(), getAnchorPane(), getLabel()) {
                            @Override
                            public void handle(ActionEvent event) {
                                Cash c = (Cash) getTransaction().getPayment();
//                                c.ejectCash("all");
                                getTransaction().getPayment().unpause();
//                                getLabel().setText("Refunded. Try again!");
//                                System.out.println(getTransaction().getPayment().getInsertedValue());
                                getAnchorPane().getChildren().subList(getAnchorPane().getChildren().size()-3, getAnchorPane().getChildren().size()).clear();
                            }
                        });
                        break;
                }
            }
        });

        pane.getChildren().add(insertedAmountDisplay);
        pane.getChildren().add(getChangeButton);
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    public void card(MouseEvent mouseEvent) throws IOException {

        // display
        pane.getChildren().clear();
        pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource("fxml/Card.fxml")));
        // label to show billAmount
        String billAmountDisplayString = String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount());
        Label billAmountDisplay = labelCreate(billAmountDisplayString, 200, 180);
        pane.getChildren().add(billAmountDisplay);
        System.out.println("> loaded card.fxml");


        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        stage.setScene(scene);
        stage.show();


    }
    @FXML
    public void confirmPayButton(MouseEvent mouseEvent) throws IOException, InterruptedException {
        Label msgToUser = labelCreate("", 100, 430);

        machine.getCart().clearZero();
        Transaction t = new Transaction(machine, machine.getUser(), machine.getCart());
        t.payAsCard();

        Card card = (Card) t.getPayment();

        String cardName;
        int cardNumber;
        if (machine.getUser() instanceof Register) {
            SavedCard savedCard = ((Register) machine.getUser()).getSavedCard();
            if (savedCard != null) {
                cardName = savedCard.getCardName();
                cardNumber = savedCard.getCardNumber();
            } else {
                cardName = CardName.getText();
                cardNumber = Payment.tryParse(CardNumber.getText());
            }
        } else {
            cardName = CardName.getText();
            cardNumber = Payment.tryParse(CardNumber.getText());
        }

//        Card.cardValidation(machine.getCardsData(), cardName, cardNumber);
        boolean result = Card.cardValidation(machine.getCardsData(), cardName, cardNumber);

        if (result) {
            t.getPayment().setResult(Result.CARDVALID);
            System.out.println("> System Message: Valid Card Details... loading invoice");
            pane.getChildren().clear();
            pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource("fxml/ValidCard.fxml")));
            // invoice

            // save card button, yes  / no
            if (!(machine.getUser() instanceof Anonymous)) {
                CheckBox saveOption = new CheckBox("Save for future payments !");
                saveOption.setLayoutX(138);
                saveOption.setLayoutY(470);
                saveOption.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (saveOption.isSelected()) {
                            System.out.println("selected");
                            // save card
                            card.saveCard(machine.getUser(), cardName, cardNumber);
                        }
                    }
                });
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6),
                        event1 -> {
                            try {
                                autoLogout();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
                timeline.play();
                pane.getChildren().add(saveOption);

            } else { // Anonymous User
                System.out.println("> successful transaction");

                // label to show invoice
                String invoiceTitle = String.format("TransactionID: %d \n " +
                        "Paid: $ %.2f", t.getTransactionId(), machine.getCart().getCartBillAmount());
                Label invoiceLabel = labelCreate(invoiceTitle, 100, 200);
                pane.getChildren().add(invoiceLabel);

                // print summary
                int shift = 20;
                Iterator<Entry<Snacks, Integer> > new_Iterator = machine.getCart().getList().entrySet().iterator();
                while (new_Iterator.hasNext()) {
                    Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
                    // Displaying HashMap
                    String itemSummary = String.format("Item name:  %s  x  %d\n", new_Map.getKey().getName(), new_Map.getValue());
                    Label itemSummaryLabel = labelCreate(itemSummary, 100, 230 + shift);
                    shift += 20;
                    pane.getChildren().add(itemSummaryLabel);
                }

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10),
                        event -> {
                            try {
                                autoLogout();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
                timeline.play();
            }
            t.complete();

        } else { // failed
            t.getPayment().setResult(Result.CARDINVALID);
            System.out.println("> Invalid");

            t.recordReason("InvalidCard");
            t.cancel();

            pane.getChildren().clear();
            pane.getChildren().setAll((Node) FXMLLoader.load(App.class.getResource("fxml/Card.fxml")));

            // label to show invoice
            String error = String.format("TransactionID: %d \n " +
                    "> System Message: Invalid Card!!", t.getTransactionId(), machine.getCart().getCartBillAmount());
            Label errorLabel = labelCreate(error, 108, 420);
            pane.getChildren().add(errorLabel);

            String billAmountDisplayString = String.format("Total Amount to Pay: $ %.2f", machine.getCart().getCartBillAmount());
            Label billAmountDisplay = labelCreate(billAmountDisplayString, 200, 180);

            pane.getChildren().add(billAmountDisplay);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5),
                    event -> {
                        try {
                            autoLogout();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
            timeline.play();


        }

        this.pane.getChildren().add(msgToUser);
        scene = new Scene(pane);
        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        stage.setScene(scene);
        stage.show();

    }

    public void autoLogout() throws IOException {
        machine.logout();
        root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}




/* Archive */
//    public void loginAnonymous(MouseEvent mouseEvent) throws IOException {
////        System.out.println("> logged in as Anonymous User");
//        machine.loginAnonymous();
//        root = FXMLLoader.load(App.class.getResource("Main.fxml"));
//        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

//    public void HomePage(MouseEvent mouseEvent) throws IOException {
//        root = FXMLLoader.load(App.class.getResource("fxml/Login.fxml"));
//        stage = (Stage) (((Node) mouseEvent.getSource()).getScene().getWindow());
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }