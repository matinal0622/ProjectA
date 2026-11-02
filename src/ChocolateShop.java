public class ChocolateShop {
    private Database database;
    private MenuUI menu;

    public ChocolateShop() {
        database = new Database();
        String[] menuOptions = {"Chocolate Shop", "Stock management", "Membership"};
        menu = new Menu(menuOptions);
        addStartingData();
    }

    private void addStartingData() {
        Chocolate dark = new DarkChocolate("Dark Chocolate", 100, 70);
        Chocolate milk = new MilkChocolate("Milk Chocolate", 100, 35);
        database.addProduct(dark, 3, "", 2.0, 5.0);
        database.addProduct(milk, 10, "", 1.0, 4.0);
    }

    public Database getDatabase() {
        return database;
    }

    public void run() {
        boolean finished = false;
        while (!finished) {
            menu.printMenuOptions();
            int choice = menu.userInput();
            if (choice == 1) {
                handleUpdateStock();
            } else if (choice == 2) {
                handleMembership();
            } else if (choice == 3) {
                finished = true;
            }
        }
        System.out.println("Goodbye. Session data is not saved.");
    }

    private void handleUpdateStock() {
        boolean finished = false;
        while (!finished) {
            System.out.println("\n========== Stock Management ==========");
            System.out.println("1. Add product");
            System.out.println("2. Increase stock");
            System.out.println("3. Decrease stock");
            System.out.println("4. Remove product");
            System.out.println("5. View stock");
            System.out.println("0. Back");
            System.out.print("--> ");
            String choice = In.nextLine().trim();
            if (choice.equals("0")) {
                finished = true;
            } else if (choice.equals("1")) {
                handleAddProduct();
            } else if (choice.equals("2")) {
                handleChangeStock(2);
            } else if (choice.equals("3")) {
                handleChangeStock(3);
            } else if (choice.equals("4")) {
                handleRemoveProduct();
            } else if (choice.equals("5")) {
                handleViewStock();
            } else {
                System.out.println("Enter a number from 0 to 5.");
            }
        }
    }

    private void handleAddProduct() {
        System.out.print("Product name: ");
        String productName = In.nextLine().trim();
        if (productName.equals("") || database.hasProduct(productName)) {
            System.out.println("Enter a new, non-empty product name.");
            return;
        }
        System.out.print("Type (D) Dark, (M) Milk: ");
        String type = In.nextLine().trim().toUpperCase();
        if (!type.equals("D") && !type.equals("M")) {
            System.out.println("Invalid product type.");
            return;
        }
        int weightGrams = readWholeNumber("Weight in grams (1-1000000): ", 1, 1000000);
        if (weightGrams == -1) {
            System.out.println("Invalid weight. Product not added.");
            return;
        }
        int cocoaPercentage = readWholeNumber("Cocoa percentage (0-100): ", 0, 100);
        if (cocoaPercentage == -1) {
            System.out.println("Invalid cocoa percentage. Product not added.");
            return;
        }
        int quantity = readWholeNumber("Initial quantity (0-1000000): ", 0, 1000000);
        if (quantity == -1) {
            System.out.println("Invalid quantity. Product not added.");
            return;
        }
        System.out.print("Expiry (optional text): ");
        String expiry = In.nextLine().trim();
        double buyPrice = readPrice("Buy price (0-1000000, up to 2 decimal places): ");
        if (buyPrice == -1) {
            System.out.println("Invalid buy price. Product not added.");
            return;
        }
        double sellPrice = readPrice("Sell price (0-1000000, up to 2 decimal places): ");
        if (sellPrice == -1) {
            System.out.println("Invalid sell price. Product not added.");
            return;
        }
        Chocolate product;
        if (type.equals("D")) {
            product = new DarkChocolate(productName, weightGrams, cocoaPercentage);
        } else {
            product = new MilkChocolate(productName, weightGrams, cocoaPercentage);
        }
        if (database.addProduct(product, quantity, expiry, buyPrice, sellPrice)) {
            System.out.println("Product added.");
            database.printProduct(productName);
        } else {
            System.out.println("Product not added. Check ranges and sell price >= buy price.");
        }
    }

    private void handleChangeStock(int choice) {
        System.out.print("Product name: ");
        String productName = In.nextLine().trim();
        if (!database.hasProduct(productName)) {
            System.out.println("Product not found.");
            return;
        }
        System.out.println("Current quantity: " + database.getStockQuantity(productName));
        int quantity = readWholeNumber("Quantity to change (1-1000000): ", 1, 1000000);
        if (quantity == -1) {
            System.out.println("Invalid quantity. Stock unchanged.");
            return;
        }
        boolean updated;
        if (choice == 2) {
            updated = database.increaseStock(productName, quantity);
        } else {
            updated = database.decreaseStock(productName, quantity);
        }
        if (updated) {
            System.out.println("Stock updated. Current quantity: "
                    + database.getStockQuantity(productName));
        } else {
            System.out.println("Stock unchanged. Check quantity, available stock and the 1000000 limit.");
        }
    }

    private void handleRemoveProduct() {
        System.out.print("Product name: ");
        String productName = In.nextLine().trim();
        if (!database.hasProduct(productName)) {
            System.out.println("Product not found.");
            return;
        }
        database.printProduct(productName);
        System.out.print("Remove this product and its remaining stock? (Y/N): ");
        String answer = In.nextLine().trim().toUpperCase();
        if (answer.equals("Y")) {
            if (database.removeProduct(productName)) {
                System.out.println("Product removed.");
            }
        } else {
            System.out.println("Removal cancelled.");
        }
    }

    private void handleViewStock() {
        System.out.print("Product name (blank for all products): ");
        String productName = In.nextLine().trim();
        if (productName.equals("")) {
            database.printStock();
        } else {
            database.printProduct(productName);
        }
    }

    private void handleMembership() {
        System.out.print("(A) Add member, (V) View member: ");
        String action = In.nextLine().trim().toUpperCase();
        if (!action.equals("A") && !action.equals("V")) {
            System.out.println("Invalid action.");
            return;
        }
        System.out.print("Member ID: ");
        String memberId = In.nextLine().trim();
        if (action.equals("A")) {
            System.out.print("Member name: ");
            String name = In.nextLine().trim();
            System.out.print("Contact number: ");
            String contactNumber = In.nextLine().trim();
            if (database.addMember(memberId, name, contactNumber)) {
                System.out.println("Member added.");
            } else {
                System.out.println("Member was not added. Check fields and duplicate ID.");
            }
        } else {
            Member member = database.getMember(memberId);
            if (member == null) {
                System.out.println("Member not found.");
            } else {
                System.out.println(member);
            }
        }
    }

    // These checks belong to the stock form, not the classroom In class.
    private int readWholeNumber(String prompt, int minimum, int maximum) {
        System.out.print(prompt);
        String input = In.nextLine().trim();
        if (input.equals("") || input.length() > 7) {
            return -1;
        }
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) < '0' || input.charAt(i) > '9') {
                return -1;
            }
        }
        int value = Integer.valueOf(input);
        if (value < minimum || value > maximum) {
            return -1;
        }
        return value;
    }

    private double readPrice(String prompt) {
        System.out.print(prompt);
        String input = In.nextLine().trim();
        if (input.equals("") || input.length() > 10) {
            return -1;
        }
        int point = input.indexOf('.');
        if (point == 0 || point == input.length() - 1) {
            return -1;
        }
        if (point >= 0 && input.length() - point - 1 > 2) {
            return -1;
        }
        for (int i = 0; i < input.length(); i++) {
            if (i != point && (input.charAt(i) < '0' || input.charAt(i) > '9')) {
                return -1;
            }
        }
        double value = Double.valueOf(input);
        if (value > 1000000) {
            return -1;
        }
        return value;
    }

    public static void main(String[] args) {
        new ChocolateShop().run();
    }
}
