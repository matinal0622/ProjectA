public class PurchaseRecord {
    private String customerId;
    private String productName;
    private int quantity;
    private double amount;

    public PurchaseRecord(String customerId, String productName, int quantity, double amount){
        this.customerId = customerId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
    }

    @Override 
    public String toString() {
        return "Customer " + customerId + " bought " + quantity + " " + productName + ".\nThe amount is " + amount + ".";
    }
}
