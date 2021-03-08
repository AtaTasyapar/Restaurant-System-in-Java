import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Database {
    private final static String MENU_FILE = "dataFiles/menu_item.txt";

    private ArrayList<MenuItem> menuList = new ArrayList<MenuItem>();
    private ArrayList<Order> orderList = new ArrayList<Order>();

    private Date date;
    int todaysOrderCounts;

    /**
     * Constructor
     **/
    public Database() {
        date = new Date();
        todaysOrderCounts = 0;
    }

    /**
     * Getter
     **/
    public ArrayList<MenuItem> getMenuList() {
        return menuList;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }


    public MenuItem findMenuItemByID(int id) {
        Iterator<MenuItem> it = menuList.iterator();
        MenuItem re = null;
        boolean found = false;

        if (id < 0) {
            return null;
        }

        while (it.hasNext() && !found) {
            re = (MenuItem) it.next();
            if (re.getID() == id) {
                found = true;
            }
        }

        if (found)
            return re;
        else
            return null;
    }

    //----------------------------------------------------------
    // Find order from ID
    //----------------------------------------------------------
    public Order findOrderByID(int id) {
        Iterator<Order> it = orderList.iterator();
        Order re = null;
        boolean found = false;

        if (id < 0) {
            return null;
        }

        while (it.hasNext() && !found) {
            re = it.next();
            if (re.getOrderID() == id) {
                found = true;
            }
        }

        if (found)
            return re;
        else
            return null;
    }

    //---------------------------------------------------------------
    // Order
    //---------------------------------------------------------------
    public int addOrder() {
        int newOrderID = ++todaysOrderCounts;
        Order newOrder = new Order();
        newOrder.setOrderID(newOrderID);
        orderList.add(newOrder);
        return newOrderID;
    }

    public void addOrderItem(int orderID, MenuItem rItem, byte quantity) {
        Order rOrder = findOrderByID(orderID);
        rOrder.addItem(rItem, quantity);
    }

    public boolean deleteOrderItem(int orderID, int index) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return false;
        return rOrder.deleteItem(index);
    }


    public boolean cancelOrder(int orderID) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return false;
        rOrder.setState(Order.ORDER_CANCELED);
        return true;
    }


    public boolean closeOrder(int orderID) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return false;
        rOrder.setState(Order.ORDER_CLOSED);
        return true;
    }

    public int getOrderState(int orderID) {
        Order re = findOrderByID(orderID);
        if (re == null)
            return -1;
        return re.getState();
    }

    public double getOrderTotalCharge(int orderID) {
        Order re = findOrderByID(orderID);
        if (re == null)
            return -1;
        return re.getTotal();
    }

    /**
    * File load
    **/
    public void loadFiles() throws DatabaseException {
        loadMenuFile();
    }

    private void loadMenuFile() throws DatabaseException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE));
            String line = reader.readLine();

            while (line != null) {
                String[] record = line.split(",");

                String id = record[0].trim();
                String name = record[1].trim();
                String price = record[2].trim();
                String type = record[3].trim();

                // Add the data from file to the registerCourses array list
                MenuItem rMenuItem = new MenuItem(Integer.parseInt(id), name, Double.parseDouble(price), Byte.parseByte(type));
                menuList.add(rMenuItem);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            String message = ioe.getMessage() + ioe.getStackTrace();
            throw new DatabaseException(message);
        }
    }
}


