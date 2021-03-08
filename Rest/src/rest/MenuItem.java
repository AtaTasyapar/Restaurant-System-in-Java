public class MenuItem {

    //definition of menu item type
    public final static int MAIN = 1;
    public final static int DRINK = 2;
    public final static int STARTERS = 3;
    public final static int DESSERT = 4;

    private int ID;
    private String name;
    private byte type;
    private double price;


    public MenuItem(int newID, String newName, double newPrice, byte newType) {
        this.ID = newID;
        this.name = newName;
        this.price = newPrice;
        this.type = newType;
    }


    //getter
    int getID() {
        return this.ID;
    }

    String getName() {
        return this.name;
    }

    double getPrice() {
        return this.price;
    }

    byte getType() {
        return this.type;
    }

}
