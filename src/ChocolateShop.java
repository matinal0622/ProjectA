public class ChocolateShop {
    private Database database;
    private MenuUI menu;

    public ChocolateShop() {
        database = new Database();
        String[] menuOptions = { "Chocolate Shop", "Stock management", "Membership" };
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
        boolean End = false;
        while (!End) {
            menu.printMenuOptions();
            int choice = menu.userInput();
            if (choice == 1) {
                handleUpdateStock();
            } else if (choice == 2) {
                handleMembership();
            } else if (choice == 3) {
                End = true;
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
        if (productName.equals("")) {
            System.out.println("Enter a new, non-empty product name.");
        } else if (database.hasProduct(productName)) {
            System.out.println("Enter a new, non-empty product name.");
        } else {
            System.out.print("Type (D) Dark, (M) Milk: ");
            String type = In.nextLine().trim().toUpperCase();
            boolean validType;
            if (type.equals("D")) {
                validType = true;
            } else if (type.equals("M")) {
                validType = true;
            } else {
                validType = false;
            }
            if (!validType) {
                System.out.println("Invalid product type.");
            } else {
                int weightGrams = readWholeNumber("Weight in grams (1-1000000): ", 1, 1000000);
                if (weightGrams == -1) {
                    System.out.println("Invalid weight. Product not added.");
                } else {
                    int cocoaPercentage = readWholeNumber("Cocoa percentage (0-100): ", 0, 100);
                    if (cocoaPercentage == -1) {
                        System.out.println("Invalid cocoa percentage. Product not added.");
                    } else {
                        int quantity = readWholeNumber("Initial quantity (0-1000000): ", 0, 1000000);
                        if (quantity == -1) {
                            System.out.println("Invalid quantity. Product not added.");
                        } else {
                            System.out.print("Expiry : ");
                            String expiry = In.nextLine().trim();
                            double buyPrice = readPrice("Buy price (0-1000000, up to 2 decimal places): ");
                            if (buyPrice == -1) {
                                System.out.println("Invalid buy price. Product not added.");
                            } else {
                                double sellPrice = readPrice("Sell price (0-1000000, up to 2 decimal places): ");
                                if (sellPrice == -1) {
                                    System.out.println("Invalid sell price. Product not added.");
                                } else {
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
                                        System.out.println(
                                                "Product not added. Check ranges and sell price >= buy price.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleChangeStock(int choice) {
        System.out.print("Product name: ");
        String productName = In.nextLine().trim();
        if (!database.hasProduct(productName)) {
            System.out.println("Product not found.");
        } else {
            System.out.println("Current quantity: " + database.getStockQuantity(productName));
            int quantity = readWholeNumber("Quantity to change (1-1000000): ", 1, 1000000);
            if (quantity == -1) {
                System.out.println("Invalid quantity. Stock unchanged.");
            } else {
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
        }
    }

    private void handleRemoveProduct() {
        System.out.print("Product name: ");
        String productName = In.nextLine().trim();
        if (!database.hasProduct(productName)) {
            System.out.println("Product not found.");
        } else {
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
        if (action.equals("A")) {
            System.out.print("Member ID: ");
            String memberId = In.nextLine().trim();
            System.out.print("Member name: ");
            String name = In.nextLine().trim();
            System.out.print("Contact number: ");
            String contactNumber = In.nextLine().trim();
            if (database.addMember(memberId, name, contactNumber)) {
                System.out.println("Member added.");
            } else {
                System.out.println("Member was not added. Check fields and duplicate ID.");
            }
        } else if (action.equals("V")) {
            System.out.print("Member ID: ");
            String memberId = In.nextLine().trim();
            Member member = database.getMember(memberId);
            if (member == null) {
                System.out.println("Member not found.");
            } else {
                System.out.println(member);
            }
        } else {
            System.out.println("Invalid action.");
        }
    }

    // These checks belong to the stock form, not the classroom In class.
    private int readWholeNumber(String prompt, int minimum, int maximum) {
        System.out.print(prompt);
        String input = In.nextLine().trim();
        if (input.equals("")) {
            return -1;
        } else if (input.length() > 7) {
            return -1;
        } else {
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) < '0') {
                    return -1;
                } else if (input.charAt(i) > '9') {
                    return -1;
                }
            }
            int value = Integer.valueOf(input);
            if (value < minimum) {
                return -1;
            } else if (value > maximum) {
                return -1;
            } else {
                return value;
            }
        }
    }

    private double readPrice(String prompt) {
        System.out.print(prompt);
        String input = In.nextLine().trim();
        if (input.equals("")) {
            return -1;
        } else if (input.length() > 10) {
            return -1;
        } else {
            int point = input.indexOf('.');
            if (point == 0) {
                return -1;
            } else if (point == input.length() - 1) {
                return -1;
            } else {
                if (point >= 0) {
                    if (input.length() - point - 1 > 2) {
                        return -1;
                    }
                }
                for (int i = 0; i < input.length(); i++) {
                    if (i != point) {
                        if (input.charAt(i) < '0') {
                            return -1;
                        } else if (input.charAt(i) > '9') {
                            return -1;
                        }
                    }
                }
                double value = Double.valueOf(input);
                if (value > 1000000) {
                    return -1;
                } else {
                    return value;
                }
            }
        }
    }

    public static void main(String[] args) {
        new ChocolateShop().run();
    }
}
