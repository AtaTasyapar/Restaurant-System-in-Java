import java.util.ArrayList;
import java.util.Iterator;

public class Order {
    final public static int ORDER_CLOSED = 1;
    final public static int ORDER_CANCELED = 2;

    private int orderID;
    private int state;  //0:arrive 1:closed 2:canceled
    private double total;
    private ArrayList<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

    /**
     * Constructor for objects of class Order
     **/
    public Order() {
        this.orderID = -1;
        this.state = 0;
        this.total = 0;
    }

    /**
     * Getter
     **/
    int getOrderID() {
        return this.orderID;
    }

    int getState() {
        return this.state;
    }

    double getTotal() {
        return this.total;
    }

    ArrayList<OrderDetail> getOrderDetail() {
        return this.orderDetailList;
    }

    /**
     * Setter
     **/
    public void setOrderID(int newID) {
        this.orderID = newID;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addItem(MenuItem rNewMenuItem, byte quantity) {
        Iterator<OrderDetail> it = orderDetailList.iterator();
        OrderDetail re;

        boolean found = false;

        while (it.hasNext() && !found) {
            re = it.next();
            if (rNewMenuItem.getID() == re.getItemID()) {
                found = true;
                re.addQuantity(quantity);
            }
        }

        if (!found) {
            OrderDetail detail = new OrderDetail(rNewMenuItem, quantity);
            orderDetailList.add(detail);

        }

        calculateTotal();
    }

    public boolean deleteItem(int index) {
        try {
            orderDetailList.remove(index);
            calculateTotal();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void calculateTotal() {
        total = 0;
        OrderDetail re;
        Iterator<OrderDetail> it = orderDetailList.iterator();
        while (it.hasNext()) {
            re = it.next();
            total += re.getTotalPrice();
        }
    }
}
