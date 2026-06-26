package gui;

import model.LoggedInUser;
import dao.DashboardDAO;
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private LoggedInUser user;

    private JButton btnProducts;
    private JButton btnSuppliers;
    private JButton btnCustomers;
    private JButton btnWarehouses;
    private JButton btnPurchase;
    private JButton btnSales;
    private JButton btnInventory;
    private JButton btnReports;
    private JButton btnLogout;
    private JLabel lblProducts;
    private JLabel lblSuppliers;
    private JLabel lblCustomers;
    private JLabel lblWarehouses;
    private JLabel lblPurchaseOrders;
    private JLabel lblSalesOrders;
    private JButton btnGoodsReceipt;

    public Dashboard(LoggedInUser user) {

        this.user = user;

        setTitle("Warehouse Inventory System");
        setSize(1400,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color card = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(background);

        //---------------- TOP ----------------

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(card);
        top.setPreferredSize(new Dimension(100,80));

        JLabel title = new JLabel("Warehouse Inventory System");
        title.setFont(new Font("Segoe UI",Font.BOLD,28));
        title.setForeground(Color.WHITE);

        String role;

        switch(user.getRoleID()){

            case 1:
                role = "Administrator";
                break;

            case 2:
                role = "Inventory Manager";
                break;

            case 3:
                role = "Warehouse Staff";
                break;

            case 4:
                role = "Sales Manager";
                break;

            default:
                role = "User";

        }

        JLabel welcome = new JLabel(
                "<html>Welcome, <b>" + user.getFullName() +
                        "</b><br>Role: " + role + "</html>");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI",Font.PLAIN,18));

        top.add(title,BorderLayout.WEST);
        top.add(welcome,BorderLayout.EAST);

        main.add(top,BorderLayout.NORTH);

        //---------------- CENTER ----------------

        JPanel center = new JPanel(new GridLayout(3,4,25,25));

        center.setBackground(background);

        center.setBorder(BorderFactory.createEmptyBorder(
                40,40,40,40));

        btnProducts=createButton("Products",blue);
        btnSuppliers=createButton("Suppliers",blue);
        btnCustomers=createButton("Customers",blue);
        btnWarehouses=createButton("Warehouses",blue);
        btnPurchase=createButton("Purchase Orders",blue);
        btnGoodsReceipt=createButton("Goods Receipt",blue);
        btnSales=createButton("Sales Orders",blue);
        btnInventory=createButton("Inventory",blue);
        btnReports=createButton("Reports",blue);
        btnLogout=createButton("Logout",new Color(190,50,50));

        center.add(btnProducts);
        center.add(btnSuppliers);
        center.add(btnCustomers);
        center.add(btnWarehouses);

        center.add(btnPurchase);
        center.add(btnGoodsReceipt);
        center.add(btnSales);
        center.add(btnInventory);

        center.add(btnReports);
        center.add(btnLogout);
        center.add(new JLabel());
        center.add(new JLabel());

        main.add(center);

        DashboardDAO dao = new DashboardDAO();

        lblProducts = new JLabel("Products : " + dao.getCount("Product"));
        lblSuppliers = new JLabel("Suppliers : " + dao.getCount("Supplier"));
        lblCustomers = new JLabel("Customers : " + dao.getCount("Customer"));
        lblWarehouses = new JLabel("Warehouses : " + dao.getCount("Warehouse"));
        lblPurchaseOrders = new JLabel("Purchase Orders : " + dao.getCount("PurchaseOrder"));
        lblSalesOrders = new JLabel("Sales Orders : " + dao.getCount("SalesOrder"));

        JLabel[] labels = {
                lblProducts,
                lblSuppliers,
                lblCustomers,
                lblWarehouses,
                lblPurchaseOrders,
                lblSalesOrders
        };

        for (JLabel lbl : labels) {

            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        }

        JPanel bottom = new JPanel(new GridLayout(2,3,20,20));

        bottom.setBackground(background);

        bottom.setBorder(BorderFactory.createEmptyBorder(0,40,30,40));

        bottom.add(lblProducts);
        bottom.add(lblSuppliers);
        bottom.add(lblCustomers);
        bottom.add(lblWarehouses);
        bottom.add(lblPurchaseOrders);
        bottom.add(lblSalesOrders);

        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        applyPermissions();

        // Navigation

        btnProducts.addActionListener(e ->
                new ProductForm().setVisible(true));

        btnSuppliers.addActionListener(e ->
                new SupplierForm().setVisible(true));

        btnCustomers.addActionListener(e ->
                new CustomerForm().setVisible(true));

        btnWarehouses.addActionListener(e ->
                new WarehouseForm().setVisible(true));

        btnPurchase.addActionListener(e ->
                new PurchaseOrderForm(user).setVisible(true));

        btnGoodsReceipt.addActionListener(e ->
                new GoodsReceiptForm(user).setVisible(true));

        btnSales.addActionListener(e ->
                new SalesOrderForm(user).setVisible(true));

        btnInventory.addActionListener(e ->
                new InventoryTransactionForm().setVisible(true));

        btnReports.addActionListener(e ->
                new ReportForm().setVisible(true));

        btnLogout.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if(choice == JOptionPane.YES_OPTION){

                dispose();
                new LoginForm().setVisible(true);

            }

        });
    }

    private JButton createButton(String text, Color color){

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI",Font.BOLD,20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;

    }

    private void applyPermissions(){

        switch(user.getRoleID()){

            case 1:
                // Admin
                break;

            case 2:

                btnCustomers.setEnabled(false);
                btnSales.setEnabled(false);

                break;

            case 3:

                btnCustomers.setEnabled(false);
                btnSuppliers.setEnabled(false);
                btnPurchase.setEnabled(false);
                btnSales.setEnabled(false);
                btnReports.setEnabled(false);

                break;

            case 4:

                btnSuppliers.setEnabled(false);
                btnWarehouses.setEnabled(false);
                btnPurchase.setEnabled(false);
                btnInventory.setEnabled(false);

                break;

        }

    }

}