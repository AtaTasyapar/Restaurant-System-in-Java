import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class UserInterface_GUI extends JFrame implements ActionListener {
    private Container con;
    private Controller_GUI rcController;

    // components for menu
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntm1;


    //-------- Master panel -------------- 
    //Main content panel(CENTER)
    private JPanel mainPanel;

    //Head panel (North)
    private JPanel headPanel;
    private JLabel headTitle;
    private JButton headBtnLogin;
    private JButton headBtnLogout;

    //Main button panel(WEST)
    private JPanel mainBtnsPanel;

    // Main buttons
    private JButton mainBtnShowMenu;
    private JButton mainBtnManageOrder;


    //Information panel(SOUTH)
    private JPanel infoPanel;
    private JLabel labelLoginUserName;
    private JTextArea taMessage;

    //-------- Contents panel --------------
    // components for home panel
    private JPanel homePanel;
    private JLabel homeImage;

    //    private LoginPanel cLoginPanel;
    private MenuListPanel cMenuListPanel;
    private OrderListPanel cOrderListPanel;
    private OrderDetailPanel cOrderDetailPanel;


    private final static int WINDOW_X = 100;
    private final static int WINDOW_Y = 100;
    private final static int WINDOW_WIDTH = 900;
    private final static int WINDOW_HEIGHT = 600;

    /**
     * Constructor for objects of class UserInterface_GUI
     */
    public UserInterface_GUI(Controller_GUI rController) {
        this.rcController = rController;
        this.con = getContentPane();

        // Set frame
        setTitle("Restaurant");
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMasterPanelComponents();

        //------- Create main content panels
        //Home panel
        homePanel = new JPanel();
        homeImage = new JLabel();

        int i = new Random().nextInt(4) + 1;
        homeImage.setHorizontalAlignment(SwingConstants.CENTER);
        homeImage.setVerticalAlignment(SwingConstants.CENTER);
        homeImage.setIcon(new ImageIcon("images/home" + i + ".jpg"));
        homePanel.add(homeImage);
        homePanel.setBackground(Color.WHITE);

        mainPanel.add("Home", homePanel);
        cMenuListPanel = new MenuListPanel();
        mainPanel.add("MenuList", cMenuListPanel);
        cOrderListPanel = new OrderListPanel();
        mainPanel.add("OrderList", cOrderListPanel);
        cOrderDetailPanel = new OrderDetailPanel();
        mainPanel.add("OrderDetail", cOrderDetailPanel);
    }

    private void createMasterPanelComponents() {
        // Add menu to frame

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        menuBar.add(mnFile);

        mntm1 = new JMenuItem("Exit");
        mnFile.add(mntm1);
        mntm1.addActionListener(this);


        //----------- Create main panels ------------
        con.setLayout(new BorderLayout());

        //head panel
        headPanel = new JPanel();
        headPanel.setBackground(Color.BLACK);
        headPanel.setLayout(new FlowLayout());
        headTitle = new JLabel("Restaurant");
        headTitle.setForeground(Color.WHITE);
        headTitle.setPreferredSize(new Dimension(500, 30));
        headTitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        headPanel.add(headTitle);
        con.add(headPanel, BorderLayout.NORTH);

        //main content panel
        mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new CardLayout());
        con.add(mainPanel, BorderLayout.CENTER);

        //main operate buttons panel
        mainBtnsPanel = new JPanel();
        mainBtnsPanel.setLayout(new GridLayout(0, 1));

        mainBtnShowMenu = new JButton("Show menu");
        mainBtnShowMenu.addActionListener(this);
        mainBtnsPanel.add(mainBtnShowMenu);

        mainBtnManageOrder = new JButton("Order management");
        mainBtnManageOrder.addActionListener(this);
        mainBtnsPanel.add(mainBtnManageOrder);

        con.add(mainBtnsPanel, BorderLayout.WEST);

        //Information panel
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        labelLoginUserName = new JLabel();
        labelLoginUserName.setPreferredSize(new Dimension(150, 50));
        taMessage = new JTextArea(3, 50);
        taMessage.setOpaque(true);
        LineBorder border = new LineBorder(Color.BLACK, 3, true);
        taMessage.setBorder(border);
        taMessage.setBackground(Color.WHITE);
        con.add(infoPanel, BorderLayout.SOUTH);
    }


    //--------------------------------------------------------
    // Display message on an information panel
    //--------------------------------------------------------
    public void displayMessage(String message) {
        taMessage.setForeground(Color.BLACK);
        taMessage.setText(message);
    }

    public void displayErrorMessage(String message) {
        taMessage.setForeground(Color.RED);
        taMessage.setText(message);
    }

    //========================================================
    // Show dialog message
    //========================================================
    final static int DIALOG_YES = JOptionPane.YES_OPTION;

    public int showYesNoDialog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return option;
    }

    public void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private int getIDFromString(String stringLine, int length) {
        int index = stringLine.indexOf("ID:");
        if (index == -1) {
            showErrorDialog("Error", "String 'ID:' is not found!!");
            return -1;
        }

        try {
            String strID = stringLine.substring(index + 3, index + 3 + length);
            int id = Integer.parseInt(strID.trim());
            return id;
        } catch (Exception e) {
            showErrorDialog("Error", "Parse error");
            return -1;
        }
    }

    //========================================================
    // Master panel action
    //========================================================
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == mntm1) {
            System.exit(0);
        } else if (ae.getSource() == mainBtnShowMenu) {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "MenuList");
            changeMainPanel("MenuList");
            cMenuListPanel.init();
        } else if (ae.getSource() == mainBtnManageOrder) {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "OrderList");
            changeMainPanel("OrderList");
            cOrderListPanel.init();
        }
    }

    private void changeMainPanel(String panelName) {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, panelName);
        displayMessage("Main panel change :" + panelName);
    }

    /**
     * Menu list panel
     **/
    private class MenuListPanel extends JPanel implements ActionListener {
        private JScrollPane scrollPanel;
        private JTextArea displayArea;
        private JPanel btnPanel;
        private JButton btnAll;
        private JButton btnMain;
        private JButton btnDrink;
        private JButton btnStarters;
        private JButton btnDessert;

        public MenuListPanel() {
            this.setLayout(new BorderLayout());
            displayArea = new JTextArea();
            displayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            displayArea.setEditable(false);
            displayArea.setMargin(new Insets(5, 5, 5, 5));
            scrollPanel = new JScrollPane(displayArea);
            scrollPanel.setPreferredSize(new Dimension(200, 400));
            add(scrollPanel, BorderLayout.CENTER);

            btnPanel = new JPanel();
            btnPanel.setLayout(new FlowLayout());
            btnAll = new JButton("All");
            btnAll.addActionListener(this);
            btnMain = new JButton("Main");
            btnMain.addActionListener(this);
            btnDrink = new JButton("Drink");
            btnDrink.addActionListener(this);
            btnStarters = new JButton("Starters");
            btnStarters.addActionListener(this);
            btnDessert = new JButton("Dessert");
            btnDessert.addActionListener(this);

            btnPanel.add(btnAll);
            btnPanel.add(btnMain);
            btnPanel.add(btnDrink);
            btnPanel.add(btnStarters);
            btnPanel.add(btnDessert);

            add(btnPanel, BorderLayout.SOUTH);
        }

        public void init() {
            showMenuList(0);
        }

        private void showMenuList(int menuType) {
            displayArea.setText("");
            ArrayList<String> menuList = rcController.createMenuList(menuType);
            for (int i = 0; i < menuList.size(); i++)
                displayArea.append(menuList.get(i) + "\n");
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnAll) {
                showMenuList(0);
            } else if (ae.getSource() == btnMain) {
                showMenuList(MenuItem.MAIN);
            } else if (ae.getSource() == btnStarters) {
                showMenuList(MenuItem.STARTERS);
            } else if (ae.getSource() == btnDrink) {
                showMenuList(MenuItem.DRINK);
            } else if (ae.getSource() == btnDessert) {
                showMenuList(MenuItem.DESSERT);
            }
        }
    }

    /**
     * Order list panel
     **/
    private class OrderListPanel extends JPanel implements ActionListener {
        private JScrollPane scrollPanel;
        private JButton btnNewOrder;
        private JButton btnEditOrder;
        private JButton btnCloseOrder;
        private JButton btnCancelOrder;
        private JLabel lblTotalSales;
        private JLabel lblTotalCount;
        private JLabel lblCancelTotal;
        private JLabel lblCancelCount;
        private JList displayList;


        public OrderListPanel() {
            GridBagLayout gbLayout = new GridBagLayout();
            this.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();
            scrollPanel = new JScrollPane();
            scrollPanel.setPreferredSize(new Dimension(500, 300));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(scrollPanel, gbc);
            this.add(scrollPanel);

            lblTotalCount = new JLabel();
            lblTotalCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbLayout.setConstraints(lblTotalCount, gbc);
            this.add(lblTotalCount);

            lblTotalSales = new JLabel();
            lblTotalSales.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblTotalSales, gbc);
            this.add(lblTotalSales);

            lblCancelCount = new JLabel();
            lblCancelCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblCancelCount, gbc);
            this.add(lblCancelCount);

            lblCancelTotal = new JLabel();
            lblCancelTotal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblCancelTotal, gbc);
            this.add(lblCancelTotal);

            btnNewOrder = new JButton("New");
            btnNewOrder.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.weightx = 0.25;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbLayout.setConstraints(btnNewOrder, gbc);
            this.add(btnNewOrder);

            btnEditOrder = new JButton("Edit");
            btnEditOrder.addActionListener(this);
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnEditOrder, gbc);
            this.add(btnEditOrder);

            btnCloseOrder = new JButton("Close");
            btnCloseOrder.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnCloseOrder, gbc);
            this.add(btnCloseOrder);

            btnCancelOrder = new JButton("Cancel");
            btnCancelOrder.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnCancelOrder, gbc);
            this.add(btnCancelOrder);

            displayList = new JList();
        }

        private void setTotalCount(int count) {
            lblTotalCount.setText("Today's order: " + count);
        }

        private void setTotalSales(double sales) {
            lblTotalSales.setText("Total:$ " + sales);
        }

        private void setCancelCount(int count) {
            lblCancelCount.setText("Canceled orders: " + count);
        }

        private void setCancelTotal(double sales) {
            lblCancelTotal.setText("Cancel total:$ " + sales);
        }

        private void showOrderList() {
            displayList.setListData(rcController.createOrderList().toArray());
            scrollPanel.getViewport().setView(displayList);

            setTotalCount(rcController.getTodaysOrderCnt());
            setTotalSales(rcController.getTotalSales());
            setCancelCount(rcController.getTodaysCancelCnt());
            setCancelTotal(rcController.getCancelTotal());

        }

        public void init() {
            showOrderList();
        }

        private int getSelectedOrderID() {
            String orderLine = (String) displayList.getSelectedValue();
            if (orderLine == null)
                return -1;

            return getIDFromString(orderLine, 4);
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnNewOrder) {
                //((CardLayout) mainPanel.getLayout()).show( mainPanel, "OrderDetail");
                changeMainPanel("OrderDetail");
                int orderID = rcController.createOrder();
                cOrderDetailPanel.init(orderID);
                cOrderListPanel.init();
            } else if (ae.getSource() == btnEditOrder) {
                int orderID = getSelectedOrderID();
                if (orderID == -1) return;

                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "OrderDetail");
                cOrderDetailPanel.init(orderID);
            } else if (ae.getSource() == btnCloseOrder) {
                int orderID = getSelectedOrderID();
                if (orderID == -1) return;

                if (showYesNoDialog("Close order", "Are you sure to close the order?") == DIALOG_YES) {
                    if (!rcController.closeOrder(orderID))
                        displayErrorMessage(rcController.getErrorMessage());
                    showOrderList();
                }
            } else if (ae.getSource() == btnCancelOrder) {
                int orderID = getSelectedOrderID();
                if (orderID == -1) return;

                if (showYesNoDialog("Close order", "Are you sure to close the order?") == DIALOG_YES) {
                    if (!rcController.cancelOrder(orderID))
                        displayErrorMessage(rcController.getErrorMessage());
                    showOrderList();
                }
            }
        }
    }

    /**
     * Order detail panel
     **/
    private class OrderDetailPanel extends JPanel implements ActionListener, ListSelectionListener {
        //Right
        private JLabel lblRightTitle;

        private JScrollPane menuScrollPanel;
        private JButton btnAll;
        private JButton btnMain;
        private JButton btnDrink;
        private JButton btnAlcohol;
        private JButton btnDessert;

        //Left
        private JLabel lblLeftTitle;
        private JLabel lblLeftInfo;
        private JScrollPane orderScrollPanel;
        private JButton btnAddItem;
        private JButton btnDeleteItem;
        private JLabel lblQuantity;
        private JTextField tfQuantity;

        private JLabel lblTotalSales;
        private JLabel lblOrderState;
        private JList orderItemList;
        private JList menuList;

        private int currentOrderID;
        private int orderItemCnt;
        private int currentOrderState;

        private JPanel orderDetailPanel;
        private JPanel menuListPanel;

        public OrderDetailPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            orderDetailPanel = new JPanel();

            GridBagLayout gbLayout = new GridBagLayout();
            orderDetailPanel.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();

            lblLeftTitle = new JLabel("Order detail");

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbLayout.setConstraints(lblLeftTitle, gbc);
            orderDetailPanel.add(lblLeftTitle);

            lblLeftInfo = new JLabel("No  Item name                 quantity    price");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(lblLeftInfo, gbc);
            orderDetailPanel.add(lblLeftInfo);

            orderScrollPanel = new JScrollPane();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.weighty = 1.0;
            gbLayout.setConstraints(orderScrollPanel, gbc);
            orderDetailPanel.add(orderScrollPanel);

            lblTotalSales = new JLabel();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weighty = 0;
            gbc.gridwidth = 4;
            //gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(lblTotalSales, gbc);
            orderDetailPanel.add(lblTotalSales);

            lblOrderState = new JLabel();
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbLayout.setConstraints(lblOrderState, gbc);
            orderDetailPanel.add(lblOrderState);

            lblQuantity = new JLabel("Quantity");
            gbc.ipadx = 20;
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblQuantity, gbc);
            orderDetailPanel.add(lblQuantity);

            tfQuantity = new JTextField();
            tfQuantity.setInputVerifier(new IntegerInputVerifier(1, 100));
            tfQuantity.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbLayout.setConstraints(tfQuantity, gbc);
            orderDetailPanel.add(tfQuantity);

            btnAddItem = new JButton("Add");
            btnAddItem.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            gbc.gridheight = 2;
            gbLayout.setConstraints(btnAddItem, gbc);
            orderDetailPanel.add(btnAddItem);

            btnDeleteItem = new JButton("Delete");
            btnDeleteItem.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 6;
            gbLayout.setConstraints(btnDeleteItem, gbc);
            orderDetailPanel.add(btnDeleteItem);


            //Right panel
            menuListPanel = new JPanel();

            menuListPanel.setLayout(gbLayout);

            lblRightTitle = new JLabel("Menu list");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 0;
            gbc.gridwidth = 5;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(lblRightTitle, gbc);
            menuListPanel.add(lblRightTitle);

            menuScrollPanel = new JScrollPane();
            gbc.gridy = 1;
            gbc.weighty = 1.0;

            gbLayout.setConstraints(menuScrollPanel, gbc);
            menuListPanel.add(menuScrollPanel);

            btnAll = new JButton("All");
            btnAll.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(btnAll, gbc);
            menuListPanel.add(btnAll);

            btnMain = new JButton("Main");
            btnMain.addActionListener(this);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnMain, gbc);
            menuListPanel.add(btnMain);

            btnDrink = new JButton("Drink");
            btnDrink.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnDrink, gbc);
            menuListPanel.add(btnDrink);

            btnAlcohol = new JButton("Alcohol");
            btnAlcohol.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnAlcohol, gbc);
            menuListPanel.add(btnAlcohol);

            btnDessert = new JButton("Dessert");
            btnDessert.addActionListener(this);
            gbc.gridx = 4;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnDessert, gbc);
            menuListPanel.add(btnDessert);

            LineBorder border = new LineBorder(Color.BLACK, 1, false);
            menuListPanel.setBorder(border);
            orderDetailPanel.setBorder(border);
            this.add(orderDetailPanel);
            this.add(menuListPanel);

            orderItemList = new JList();
            orderItemList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            orderItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            menuList = new JList();
            menuList.addListSelectionListener(this);
            menuList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        }

        public void init(int orderID) {
            currentOrderID = orderID;
            currentOrderState = rcController.getOrderState(orderID);
            switch (currentOrderState) {
                case Order.ORDER_CLOSED:
                    setOrderState("Closed");
                    break;
                case Order.ORDER_CANCELED:
                    setOrderState("Canceled");
                    break;
                default:
                    break;
            }

            if (currentOrderState != 0) {
                btnAddItem.setEnabled(false);
                btnDeleteItem.setEnabled(false);
            } else {
                btnAddItem.setEnabled(true);
                btnDeleteItem.setEnabled(true);
            }

            refreshOrderDetailList();
            menuList.setListData(rcController.createMenuList(0).toArray());
            menuScrollPanel.getViewport().setView(menuList);
            tfQuantity.setText("");
            tfQuantity.setBackground(UIManager.getColor("TextField.background"));
        }

        private void setTotal(double total) {
            lblTotalSales.setText("Total charge: $" + total);
        }

        private void setOrderState(String state) {
            lblOrderState.setText("Order state: " + state);
        }

        private void refreshOrderDetailList() {
            ArrayList<String> list = rcController.createOrderItemlList(currentOrderID);
            setTotal(rcController.getOrderTotalCharge(currentOrderID));
            orderItemCnt = list.size();
            orderItemList.setListData(list.toArray());
            orderScrollPanel.getViewport().setView(orderItemList);
        }

        private int getOrderDetailIndexFromString(String orderLine) {
            try {
                String strIndex = orderLine.substring(0, 4);
                int index = Integer.parseInt(strIndex.trim());
                return index;
            } catch (Exception e) {
                return -1;
            }
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnAddItem) {
                if (btnAddItem.getVerifyInputWhenFocusTarget()) {
                    btnAddItem.requestFocusInWindow();
                    if (!btnAddItem.hasFocus()) {
                        return;
                    }
                }

                String menuLine = (String) menuList.getSelectedValue();
                if (menuLine == null)
                    return;

                int id = getIDFromString(menuLine, 4);
                if (id == -1)
                    return;
                if (tfQuantity.getText().equals("")) {
                    showErrorDialog("Error", "Enter quantity!!");
                    return;
                }
                byte quantity = Byte.parseByte(tfQuantity.getText().trim());
                displayMessage("Menu ID = " + id + " Quantity = " + quantity);
                if (rcController.addNewOrderItem(currentOrderID, id, quantity) == false) {
                    displayErrorMessage("addNewOrderItem Error!!\n" + rcController.getErrorMessage());
                }
                refreshOrderDetailList();
                orderItemList.ensureIndexIsVisible(orderItemCnt - 1);

            } else if (ae.getSource() == btnDeleteItem) {
                String orderLine = (String) orderItemList.getSelectedValue();
                if (orderLine == null)
                    return;

                int index = getOrderDetailIndexFromString(orderLine);
                if (index == -1)
                    return;
                if (rcController.deleteOrderItem(currentOrderID, index) == false) {
                    displayErrorMessage("deleteOrderItem Error!!\n" + rcController.getErrorMessage());
                }
                refreshOrderDetailList();
            } else if (ae.getSource() == btnAll) {
                menuList.setListData(rcController.createMenuList(0).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnMain) {
                menuList.setListData(rcController.createMenuList(MenuItem.MAIN).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnDrink) {
                menuList.setListData(rcController.createMenuList(MenuItem.DRINK).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnAlcohol) {
                menuList.setListData(rcController.createMenuList(MenuItem.STARTERS).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnDessert) {
                menuList.setListData(rcController.createMenuList(MenuItem.DESSERT).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == true) {
                if (e.getSource() == menuList) {
                    tfQuantity.setText("1");
                }
            }
        }
    }

    /**
     * Input validation
     **/

    private class IntegerInputVerifier extends InputVerifier {
        private int state = 0;  //0:no range check 1:min check 2:min and max check
        private int MAX = 0;
        private int MIN = 0;


        public IntegerInputVerifier(int min, int max) {
            super();
            MIN = min;
            MAX = max;
            state = 2;
        }

        @Override
        public boolean verify(JComponent c) {
            JTextField textField = (JTextField) c;
            boolean result = false;

            try {
                int number = Integer.parseInt(textField.getText());

                switch (state) {
                    case 0:
                        result = true;
                    case 1:
                        if (number < MIN) {
                            displayErrorMessage("Minimum input is " + MIN);
                            textField.setBackground(Color.red);
                            result = false;
                        } else {
                            textField.setBackground(UIManager.getColor("TextField.background"));
                            result = true;
                        }
                        break;
                    case 2:
                        if (number < MIN) {
                            displayErrorMessage("Minimum input is " + MIN);
                            textField.setBackground(Color.red);
                            result = false;
                        } else {
                            if (number > MAX) {
                                displayErrorMessage("Maximum input is " + MAX);
                                textField.setBackground(Color.red);
                                result = false;
                            } else {
                                textField.setBackground(UIManager.getColor("TextField.background"));
                                result = true;
                            }
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                displayErrorMessage("Only number is allowed.");
                textField.setBackground(Color.red);
                result = false;
            }
            return result;
        }
    }
}
