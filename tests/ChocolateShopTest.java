public class ChocolateShopTest {
    private static int passed = 0;
    private static int failed = 0;
    private static Database database;

    private static void check(boolean condition, String message) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAILED: " + message);
        }
    }

    private static void money(double actual, double expected, String message) {
        check(actual - expected < 0.000001 && expected - actual < 0.000001, message);
    }

    private static boolean add(String name, int quantity, double buyPrice, double sellPrice) {
        return database.addProduct(new DarkChocolate(name, 100, 70),
                quantity, "", buyPrice, sellPrice);
    }

    private static void reset() {
        database = new Database();
        add("Dark Chocolate", 10, 2, 5);
        database.addMember("M1", "Alex", "0400000000");
    }

    private static void testProducts() {
        reset();
        check(database.getProduct("Dark Chocolate") instanceof DarkChocolate, "Dark subtype");
        check(database.addProduct(new MilkChocolate("Milk", 100, 35),
                0, "2027-01-01", 1, 4), "Add milk with zero initial stock");
        check(database.getProduct("Milk") instanceof MilkChocolate, "Milk subtype");
        check(database.getStockQuantity("Milk") == 0, "Zero stock product still exists");
        check(!add("Dark Chocolate", 99, 1, 8), "Reject duplicate product");
        check(database.getStockQuantity("Dark Chocolate") == 10, "Duplicate leaves stock unchanged");
        money(database.getBuyPrice("Dark Chocolate"), 2, "Duplicate leaves prices unchanged");
        check(add(" Trimmed ", 3, 1, 2), "Trim product name");
        check(database.hasProduct(" Trimmed "), "Trim lookup");
        check(database.getProduct("missing") == null, "Unknown product");
        check(database.getStockQuantity("missing") == -1, "Unknown stock");
        check(database.getBuyPrice("missing") == -1, "Unknown buy price");
        check(database.getSellPrice("missing") == -1, "Unknown sell price");
        check(!add("", 1, 1, 2), "Reject empty name");
        check(!add(null, 1, 1, 2), "Reject null name");
        check(!database.addProduct(null, 1, "", 1, 2), "Reject null object");
        check(!add("Bad", -1, 1, 2), "Reject negative initial stock");
        check(!add("Bad", 1000001, 1, 2), "Reject excessive initial stock");
        check(!add("Bad", 1, -1, 2), "Reject negative price");
        check(!add("Bad", 1, 3, 2), "Reject sell price below cost");
        check(!add("Bad", 1, 1, 1000001), "Reject excessive price");
        check(!add("Bad", 1, Double.valueOf("NaN"), 2), "Reject NaN");
        check(!add("Bad", 1, 1, Double.valueOf("Infinity")), "Reject infinity");
        check(!database.addProduct(new DarkChocolate("Bad", 0, 70),
                1, "", 1, 2), "Reject zero weight");
        check(!database.addProduct(new DarkChocolate("Bad", 100, 101),
                1, "", 1, 2), "Reject invalid cocoa");
        check(!database.addProduct(new DarkChocolate("Bad", 100, -1),
                1, "", 1, 2), "Reject negative cocoa");
        check(!database.addProduct(new DarkChocolate("Bad", 100, 70),
                1, null, 1, 2), "Reject null expiry");
        check(!database.hasProduct("Bad"), "Invalid products never saved");
        ChocolateShop shop = new ChocolateShop();
        check(shop.getDatabase().getStockQuantity("Dark Chocolate") == 3, "Initial dark stock");
        check(shop.getDatabase().getStockQuantity("Milk Chocolate") == 10, "Initial milk stock");
    }

    private static void testStockChanges() {
        reset();
        check(database.increaseStock("Dark Chocolate", 5), "Increase stock");
        check(database.getStockQuantity("Dark Chocolate") == 15, "Increased quantity");
        check(database.decreaseStock(" Dark Chocolate ", 3), "Decrease stock");
        check(database.getStockQuantity("Dark Chocolate") == 12, "Decreased quantity");
        check(!database.decreaseStock("Dark Chocolate", 13), "Reject excess reduction");
        check(!database.decreaseStock("Dark Chocolate", 0), "Reject zero reduction");
        check(!database.decreaseStock("Dark Chocolate", -1), "Reject negative reduction");
        check(!database.increaseStock("Dark Chocolate", 0), "Reject zero addition");
        check(!database.increaseStock("Dark Chocolate", -1), "Reject negative addition");
        check(!database.increaseStock("Dark Chocolate", 1000000), "Reject stock above limit");
        check(database.getStockQuantity("Dark Chocolate") == 12, "Failures leave quantity unchanged");
        check(!database.increaseStock("missing", 1), "Cannot add stock to unlisted product");
        check(!database.decreaseStock(null, 1), "Cannot reduce unknown stock");
        check(database.decreaseStock("Dark Chocolate", 12), "Reduce to zero");
        check(database.hasProduct("Dark Chocolate"), "Zero stock is not removal");
        check(database.getStockQuantity("Dark Chocolate") == 0, "Zero remaining stock");
        check(database.increaseStock("Dark Chocolate", 1000000), "Reach quantity limit");
        check(!database.increaseStock("Dark Chocolate", 1), "Cannot exceed quantity limit");
        money(database.getSellPrice("Dark Chocolate"), 5, "Adjustments leave price unchanged");
        check(database.getMember("M1").getPoints() == 0, "Adjustments never award points");
    }

    private static void testRemoval() {
        reset();
        check(database.addMemberPoints("M1", 9), "Set points before removal");
        check(database.removeProduct(" Dark Chocolate "), "Remove with remaining quantity");
        check(!database.hasProduct("Dark Chocolate"), "Removed from product list");
        check(database.getProduct("Dark Chocolate") == null, "Removed object lookup");
        check(database.getStockQuantity("Dark Chocolate") == -1, "Removed stock lookup");
        check(database.getBuyPrice("Dark Chocolate") == -1, "Removed prices lookup");
        check(!database.increaseStock("Dark Chocolate", 1), "Cannot restock removed product");
        check(database.getCheckoutPrice("Dark Chocolate", "M1") == -1, "Cannot price removed product");
        check(!database.removeProduct("Dark Chocolate"), "Cannot remove twice");
        check(!database.removeProduct(null), "Reject null removal");
        check(database.getMember("M1").getPoints() == 9, "Removal preserves member points");
        check(add("Dark Chocolate", 4, 3, 8), "Can relist product");
        check(database.getStockQuantity("Dark Chocolate") == 4, "Relisting uses new quantity");
        money(database.getSellPrice("Dark Chocolate"), 8, "Relisting uses new price");
    }

    private static void testMembers() {
        reset();
        check(database.getMember("M1").getPoints() == 0, "Initial points");
        check(database.getMember("M1").getContactNumber().equals("0400000000"), "Phone keeps zero");
        check(!database.addMember(" M1 ", "Other", "123"), "Duplicate member rejected");
        check(database.getMember("M1").getName().equals("Alex"), "Member not overwritten");
        check(!database.addMember("", "Alex", "123"), "Empty member ID");
        check(!database.addMember("M2", "", "123"), "Empty member name");
        check(!database.addMember("M2", "Bob", null), "Null contact");
        check(database.addMember(" M2 ", " Bob ", " 0401 "), "Trim registration");
        check(database.getMember(" M2 ").getName().equals("Bob"), "Trim member lookup");
        check(database.getMember("missing") == null, "Unknown member");
        check(database.getMember(null) == null, "Null member lookup");
        check(database.addMemberPoints("M1", 9), "Award whole dollar points");
        check(database.addMemberPoints("M1", 4.99), "Award fractional amount");
        check(database.getMember("M1").getPoints() == 13, "Whole dollars per payment");
        check(database.addMemberPoints("M1", 0.99), "Sub-dollar payment");
        check(database.addMemberPoints("M1", 0.99), "Second sub-dollar payment");
        check(database.getMember("M1").getPoints() == 13, "Fractions never carry");
        check(!database.addMemberPoints("M1", -1), "Reject negative payment");
        check(!database.addMemberPoints("missing", 9), "Reject missing member points");
        check(!database.addMemberPoints("M1", Double.valueOf("NaN")), "Reject NaN payment");
        check(!database.addMemberPoints("M1", Double.valueOf("Infinity")), "Reject infinite payment");
        check(!database.addMemberPoints("M1", 1000001), "Reject excessive payment");
        check(database.getMember("M1").getPoints() == 13, "Invalid awards unchanged");
        check(database.addMemberPoints("M2", 1000000), "Reach point limit");
        check(!database.addMemberPoints("M2", 1), "Reject excess points");
        check(!database.getMember("M2").addPoints(-1), "Reject negative points");
        check(database.getMember("M2").getPoints() == 1000000, "Point limit unchanged");
    }

    private static void testDiscounts() {
        reset();
        money(database.getCheckoutPrice("Dark Chocolate", ""), 5, "Guest price");
        money(database.getCheckoutPrice("Dark Chocolate", "M1"), 4.5, "Member 10 percent off");
        money(database.getCheckoutPrice("Dark Chocolate", "", 0.2), 4, "Product discount");
        money(database.getCheckoutPrice("Dark Chocolate", "M1", 0.2), 3.6, "Stacked discount");
        money(database.getCheckoutPrice("Dark Chocolate", "M1", 1), 2, "Cost floor");
        check(add("Cost", 1, 4.8, 5), "Add cost floor example");
        money(database.getCheckoutPrice("Cost", "M1"), 4.8, "Member cannot go below cost");
        check(add("Small", 1, 0.1, 0.99), "Add rounding example");
        money(database.getCheckoutPrice("Small", "M1"), 0.89, "Round to cents");
        check(database.getCheckoutPrice("Dark Chocolate", "missing") == -1, "Invalid member");
        check(database.getCheckoutPrice("Dark Chocolate", null) == -1, "Null member");
        check(database.getCheckoutPrice("Dark Chocolate", "M1", -0.1) == -1, "Negative rate");
        check(database.getCheckoutPrice("Dark Chocolate", "M1", 1.1) == -1, "Excess rate");
        check(database.getCheckoutPrice("Dark Chocolate", "M1", Double.valueOf("NaN")) == -1,
                "Invalid rate");
        check(database.getStockQuantity("Dark Chocolate") == 10, "Price query leaves stock");
        check(database.getMember("M1").getPoints() == 0, "Price query never awards points");
        money(database.getSellPrice("Dark Chocolate"), 5, "Price query leaves original price");
    }

    public static void main(String[] args) {
        testProducts();
        testStockChanges();
        testRemoval();
        testMembers();
        testDiscounts();
        System.out.println("Passed " + passed + " checks. Failed " + failed + " checks.");
    }
}
