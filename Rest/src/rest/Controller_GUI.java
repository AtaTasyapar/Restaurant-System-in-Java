import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Controller_GUI {
    private UserInterface_GUI cView;
    private Database cDatabase;
    private String todaysDate;

    private int todaysOrderCnt;     //Today's order count
    private double totalSales;         //Today's total sales
    private int todaysCancelCnt;    //Today's cancel count
    private double cancelTotal;        //Total cost of today's canceled orders


    private String errorMessage;


    public Controller_GUI() {
        this.cDatabase = new Database();
        try {
            cDatabase.loadFiles();
        } catch (DatabaseException de) {
            System.out.println(de.getErrMessage());
            System.exit(0);
        }

        cView = new UserInterface_GUI(this);

        Date date = new Date();
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        todaysDate = stf.format(date);
        cView.setVisible(true);

        todaysOrderCnt = 0;
        totalSales = 0;
        todaysCancelCnt = 0;
        cancelTotal = 0;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        String result = this.errorMessage;
        this.errorMessage = "";
        return result;
    }

    public int getTodaysOrderCnt() {
        return this.todaysOrderCnt;
    }

    public int getTodaysCancelCnt() {
        return this.todaysCancelCnt;
    }

    public double getTotalSales() {
        return this.totalSales;
    }

    public double getCancelTotal() {
        return this.cancelTotal;
    }

    public double getOrderTotalCharge(int orderID) {
        return cDatabase.getOrderTotalCharge(orderID);
    }

    public int getOrderState(int orderID) {
        return cDatabase.getOrderState(orderID);
    }

    /**
     * Order management
     **/
    public int createOrder() {
        return cDatabase.addOrder();
    }

    public boolean addNewOrderItem(int orderID, int addItemID, byte addItemQuantity) {
        MenuItem rNewItem = null;

        rNewItem = cDatabase.findMenuItemByID(addItemID);
        if (rNewItem == null) {
            setErrorMessage("MenuID[" + addItemID + "]is not found.");
            addItemID = 0;
            return false;
        }

        cDatabase.addOrderItem(orderID, rNewItem, addItemQuantity);
        return true;
    }

    public boolean deleteOrderItem(int orderID, int deleteNo) {
        deleteNo -= 1;  //index actually starts from zero
        if (!cDatabase.deleteOrderItem(orderID, deleteNo)) {
            setErrorMessage("Not found.");
            return false;
        }
        return true;
    }

    public boolean closeOrder(int closeOrderID) {
        Order rOrder = cDatabase.findOrderByID(closeOrderID);
        if (rOrder.getState() != 0) {
            setErrorMessage("The order is already closed or canceled.");
            return false;
        }
        cDatabase.closeOrder(closeOrderID);
        todaysOrderCnt++;
        totalSales += rOrder.getTotal();
        return true;
    }

    public boolean cancelOrder(int cancelOrderID) {
        Order rOrder = cDatabase.findOrderByID(cancelOrderID);

        if (rOrder.getState() != 0) {
            setErrorMessage("The order is already closed or canceled.");
            return false;
        }

        cDatabase.cancelOrder(cancelOrderID);
        todaysCancelCnt++;
        cancelTotal += rOrder.getTotal();
        return true;
    }

    /**
     * Create string lists
     **/
    public ArrayList<String> createOrderList() {
        Iterator<Order> it = cDatabase.getOrderList().iterator();
        String state;
        ArrayList<String> initData = new ArrayList<String>();
        String output;

        while (it.hasNext()) {
            Order re = it.next();
            switch (re.getState()) {
                case Order.ORDER_CLOSED:
                    state = "Closed";
                    break;
                case Order.ORDER_CANCELED:
                    state = "Canceled";
                    break;
                default:
                    state = "-";
                    break;
            }

            output = String.format("Order ID:%4d Total:$%5.2f State:%-8s\n",
                    re.getOrderID(), re.getTotal(), state);
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }

    public ArrayList<String> createOrderItemlList(int orderID) {
        Order rOrder = cDatabase.findOrderByID(orderID);
        ArrayList<String> initData = new ArrayList<String>();

        if (rOrder == null) {
            initData.add("No order information");
            //list.setListData(initData);
            return initData;
        }

        String output;

        Iterator<OrderDetail> it = rOrder.getOrderDetail().iterator();
        OrderDetail re;

        int count = 0;

        while (it.hasNext()) {
            re = it.next();
            output = String.format("%-4d|%-24s|%5d|%5.2f",
                    ++count, re.getItemName(), re.getQuantity(), re.getTotalPrice());
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No item");
        //list.setListData(initData);
        return initData;
    }

    public ArrayList<String> createMenuList(int disuplayMenuType) {
        Iterator<MenuItem> it = cDatabase.getMenuList().iterator();
        ArrayList<String> initData = new ArrayList<String>();

        while (it.hasNext()) {
            MenuItem re = (MenuItem) it.next();
            byte menuType = re.getType();
            //displayMenuType != ALL && ...
            if (disuplayMenuType != 0 && disuplayMenuType != menuType)
                continue;
            String strMenuType;
            switch (menuType) {
                case MenuItem.MAIN:
                    strMenuType = "Main";
                    break;
                case MenuItem.DRINK:
                    strMenuType = "Drink";
                    break;
                case MenuItem.STARTERS:
                    strMenuType = "Starters";
                    break;
                case MenuItem.DESSERT:
                    strMenuType = "Dessert";
                    break;
                default:
                    strMenuType = "Undefined";
                    break;
            }
            String output = String.format("Menu ID:%4d  Name:%-20s  Price:%5.2f Type:%s",
                    re.getID(), re.getName(), re.getPrice(), strMenuType);

            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }
}
