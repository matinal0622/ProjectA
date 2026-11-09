public class Customer {
    private String id;
    private String name;
    private ChocolateShop shop; 

    public Customer(String id, String name, ChocolateShop shop){
        this.id = id;
        this.name = name;
        this.shop = shop;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public boolean purchase(String productName, int quantity){
        return shop.purchase(id, productName, quantity);
    }

    @Override 
    public String toString(){
        return "Customer's name is " + name + ".\nCustomer id is " + id + ".";
    }
}
