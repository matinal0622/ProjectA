import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Database {
    private static final double MEMBER_DISCOUNT_RATE = 0.10;
    private static final int MAX_QUANTITY = 1000000;
    private static final double MAX_PRICE = 1000000.0;

    private DataTable stock;
    private DataTable prices;
    private HashMap<String, Chocolate> products;
    private HashMap<String, Member> members;

    public Database() {
        stock = new DataTable(new ArrayList<>(Arrays.asList("Quantity", "Expiry")));
        prices = new DataTable(new ArrayList<>(Arrays.asList("Buy Price", "Sell Price")));
        products = new HashMap<>();
        members = new HashMap<>();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().equals("");
    }

    private boolean validPrice(double value) {
        return value >= 0 && value <= MAX_PRICE;
    }

    private double roundMoney(double value) {
        int cents = (int) (value * 100 + 0.5);
        return cents / 100.0;
    }

    public boolean addProduct(Chocolate product, int quantity, String expiry,
            double buyPrice, double sellPrice) {
        if (product == null || isBlank(product.getName())
                || product.getWeightGrams() <= 0
                || product.getWeightGrams() > MAX_QUANTITY
                || product.getCocoaPercentage() < 0 || product.getCocoaPercentage() > 100
                || quantity < 0 || quantity > MAX_QUANTITY || expiry == null
                || !validPrice(buyPrice) || !validPrice(sellPrice) || sellPrice < buyPrice) {
            return false;
        }
        String productName = product.getName();
        if (products.containsKey(productName)) {
            return false;
        }
        products.put(productName, product);
        stock.updateRow(productName, new ArrayList<>(Arrays.asList(
                String.valueOf(quantity), expiry.trim())));
        prices.updateRow(productName, new ArrayList<>(Arrays.asList(
                String.valueOf(roundMoney(buyPrice)), String.valueOf(roundMoney(sellPrice)))));
        return true;
    }

    public boolean hasProduct(String productName) {
        return !isBlank(productName) && products.containsKey(productName.trim());
    }

    public Chocolate getProduct(String productName) {
        if (!hasProduct(productName)) {
            return null;
        }
        return products.get(productName.trim());
    }

    public int getStockQuantity(String productName) {
        if (!hasProduct(productName)) {
            return -1;
        }
        return Integer.valueOf(stock.getValue(productName.trim(), 0));
    }

    public boolean increaseStock(String productName, int quantity) {
        if (!hasProduct(productName) || quantity <= 0 || quantity > MAX_QUANTITY) {
            return false;
        }
        int currentQuantity = getStockQuantity(productName);
        if (currentQuantity + quantity > MAX_QUANTITY) {
            return false;
        }
        stock.updateValue(productName.trim(), 0, String.valueOf(currentQuantity + quantity));
        return true;
    }

    public boolean decreaseStock(String productName, int quantity) {
        if (!hasProduct(productName) || quantity <= 0) {
            return false;
        }
        int currentQuantity = getStockQuantity(productName);
        if (quantity > currentQuantity) {
            return false;
        }
        stock.updateValue(productName.trim(), 0, String.valueOf(currentQuantity - quantity));
        return true;
    }

    public boolean removeProduct(String productName) {
        if (!hasProduct(productName)) {
            return false;
        }
        productName = productName.trim();
        products.remove(productName);
        stock.removeRow(productName);
        prices.removeRow(productName);
        return true;
    }

    public void printProduct(String productName) {
        if (!hasProduct(productName)) {
            System.out.println("Product not found.");
            return;
        }
        productName = productName.trim();
        System.out.println(products.get(productName));
        System.out.println("Quantity: " + getStockQuantity(productName)
                + " | Expiry: " + stock.getValue(productName, 1));
        System.out.printf("Buy price: $%.2f | Sell price: $%.2f%n",
                getBuyPrice(productName), getSellPrice(productName));
    }

    public void printStock() {
        if (products.size() == 0) {
            System.out.println("No products listed.");
            return;
        }
        for (String productName : stock.getKeys()) {
            printProduct(productName);
        }
    }

    public double getBuyPrice(String productName) {
        if (!hasProduct(productName)) {
            return -1;
        }
        return Double.valueOf(prices.getValue(productName.trim(), 0));
    }

    public double getSellPrice(String productName) {
        if (!hasProduct(productName)) {
            return -1;
        }
        return Double.valueOf(prices.getValue(productName.trim(), 1));
    }

    public boolean addMember(String memberId, String name, String contactNumber) {
        if (isBlank(memberId) || isBlank(name) || isBlank(contactNumber)) {
            return false;
        }
        memberId = memberId.trim();
        if (members.containsKey(memberId)) {
            return false;
        }
        members.put(memberId, new Member(memberId, name.trim(), contactNumber.trim()));
        return true;
    }

    public Member getMember(String memberId) {
        if (isBlank(memberId)) {
            return null;
        }
        return members.get(memberId.trim());
    }

    public double getCheckoutPrice(String productName, String memberId) {
        return getCheckoutPrice(productName, memberId, 0);
    }

    // The discount use case supplies the rate, for example 0.20 for 20% off.
    public double getCheckoutPrice(String productName, String memberId, double discountRate) {
        if (!hasProduct(productName) || memberId == null
                || !(discountRate >= 0 && discountRate <= 1)) {
            return -1;
        }
        double price = roundMoney(getSellPrice(productName) * (1 - discountRate));
        if (!memberId.trim().equals("")) {
            if (getMember(memberId) == null) {
                return -1;
            }
            price = roundMoney(price * (1 - MEMBER_DISCOUNT_RATE));
        }
        if (price < getBuyPrice(productName)) {
            price = getBuyPrice(productName);
        }
        return price;
    }

    // Call once after a successful payment, never for a stock adjustment or a price query.
    public boolean addMemberPoints(String memberId, double paidAmount) {
        Member member = getMember(memberId);
        if (member == null || !validPrice(paidAmount)) {
            return false;
        }
        return member.addPoints((int) paidAmount);
    }
}
