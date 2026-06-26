package gui;

import dao.SalesOrderDAO;
import model.SalesOrder;
import dao.CustomerDAO;
import model.Customer;
import model.LoggedInUser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SalesOrderForm extends JFrame {

    private JTextField txtSalesOrderID;
    private JComboBox<String> cmbCustomer;
    private LoggedInUser loggedInUser;
    private JTextField txtOrderDate;
    private JComboBox<String> cmbStatus;
    private JTextField txtTotalAmount;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnNext;

    private JTable salesTable;
    private DefaultTableModel tableModel;

    public SalesOrderForm(LoggedInUser user) {

        this.loggedInUser = user;

        setTitle("Sales Order Management");
        setSize(1400,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Sales Order Management");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,28));
        heading.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));

        mainPanel.add(heading,BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSalesOrderID = new JTextField();
        txtSalesOrderID.setEditable(false);

        cmbCustomer = new JComboBox<>();

        txtOrderDate = new JTextField("2026-06-30");

        cmbStatus = new JComboBox<>(new String[]{
                "Pending",
                "Completed",
                "Cancelled"
        });

        txtTotalAmount = new JTextField();

        addField(formPanel,gbc,0,"Sales Order ID",txtSalesOrderID);
        addCombo(formPanel,gbc,1,"Customer",cmbCustomer);
        addField(formPanel,gbc,2,"Order Date",txtOrderDate);
        addCombo(formPanel,gbc,3,"Status",cmbStatus);
        addField(formPanel,gbc,4,"Total Amount",txtTotalAmount);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnNext = new JButton("Next >>");

        JButton[] buttons = {
                btnAdd,
                btnUpdate,
                btnDelete,
                btnRefresh,
                btnNext
        };

        for(JButton b : buttons){

            b.setBackground(blue);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(120,35));

            buttonPanel.add(b);

        }

        gbc.gridx=0;
        gbc.gridy=5;
        gbc.gridwidth=2;

        formPanel.add(buttonPanel,gbc);

        mainPanel.add(formPanel,BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{

                "Sales Order ID",
                "Customer ID",
                "Created By",
                "Order Date",
                "Status",
                "Total Amount"

        });

        salesTable = new JTable(tableModel);
        salesTable.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(salesTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel);

        loadCustomers();
        loadSalesOrders();

        salesTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = salesTable.getSelectedRow();

                if (row != -1) {

                    txtSalesOrderID.setText(tableModel.getValueAt(row,0).toString());

                    int customerID = Integer.parseInt(
                            tableModel.getValueAt(row,1).toString()
                    );

                    for(int i = 0; i < cmbCustomer.getItemCount(); i++){

                        if(cmbCustomer.getItemAt(i).startsWith(customerID + " -")){

                            cmbCustomer.setSelectedIndex(i);
                            break;

                        }

                    }

                    txtOrderDate.setText(tableModel.getValueAt(row,3).toString());

                    cmbStatus.setSelectedItem(
                            tableModel.getValueAt(row,4).toString()
                    );

                    txtTotalAmount.setText(
                            tableModel.getValueAt(row,5).toString()
                    );

                }

            }

        });


        btnAdd.addActionListener(e -> {

            try {

                SalesOrder so = new SalesOrder();

                String selectedCustomer = cmbCustomer.getSelectedItem().toString();

                int customerID = Integer.parseInt(
                        selectedCustomer.split(" - ")[0]
                );

                so.setCustomerID(customerID);

                so.setCreatedBy(loggedInUser.getUserID());

                so.setOrderDate(java.sql.Date.valueOf(txtOrderDate.getText()));

                so.setStatus(cmbStatus.getSelectedItem().toString());

                so.setTotalAmount(Double.parseDouble(txtTotalAmount.getText()));

                SalesOrderDAO dao = new SalesOrderDAO();

                if (dao.addSalesOrder(so)) {

                    JOptionPane.showMessageDialog(this,
                            "Sales Order Added Successfully");

                    loadSalesOrders();

                    btnRefresh.doClick();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed to Add Sales Order");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Data");

            }

        });

        btnUpdate.addActionListener(e -> {

            try {

                SalesOrder so = new SalesOrder();

                so.setSalesOrderID(Integer.parseInt(txtSalesOrderID.getText()));

                String selectedCustomer = cmbCustomer.getSelectedItem().toString();

                int customerID = Integer.parseInt(
                        selectedCustomer.split(" - ")[0]
                );

                so.setCustomerID(customerID);

                so.setCreatedBy(loggedInUser.getUserID());

                so.setOrderDate(java.sql.Date.valueOf(txtOrderDate.getText()));

                so.setStatus(cmbStatus.getSelectedItem().toString());

                so.setTotalAmount(Double.parseDouble(txtTotalAmount.getText()));

                SalesOrderDAO dao = new SalesOrderDAO();

                if (dao.updateSalesOrder(so)) {

                    JOptionPane.showMessageDialog(this,
                            "Sales Order Updated Successfully");

                    loadSalesOrders();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Update Failed");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Data");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtSalesOrderID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select a Sales Order First");

                return;

            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Sales Order?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                SalesOrderDAO dao = new SalesOrderDAO();

                if (dao.deleteSalesOrder(
                        Integer.parseInt(txtSalesOrderID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Sales Order Deleted");

                    loadSalesOrders();

                    btnRefresh.doClick();

                }

            }

        });

        btnRefresh.addActionListener(e -> {

            txtSalesOrderID.setText("");
            txtOrderDate.setText("");
            txtTotalAmount.setText("");

            loadCustomers();

            if(cmbCustomer.getItemCount() > 0)
                cmbCustomer.setSelectedIndex(0);

            cmbStatus.setSelectedIndex(0);

            loadSalesOrders();

        });

        btnNext.addActionListener(e -> {

            if (txtSalesOrderID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select or Save a Sales Order First!");

                return;

            }

            int salesOrderID = Integer.parseInt(txtSalesOrderID.getText());

            SalesOrderItemForm form = new SalesOrderItemForm(salesOrderID);

            form.setVisible(true);

        });


    }

    private void addField(JPanel panel,
                          GridBagConstraints gbc,
                          int row,
                          String label,
                          JTextField field){

        JLabel lbl = new JLabel(label);

        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,15));

        field.setPreferredSize(new Dimension(220,32));

        gbc.gridx=0;
        gbc.gridy=row;

        panel.add(lbl,gbc);

        gbc.gridx=1;

        panel.add(field,gbc);

    }

    private void addCombo(JPanel panel,
                          GridBagConstraints gbc,
                          int row,
                          String label,
                          JComboBox<String> combo){

        JLabel lbl = new JLabel(label);

        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,15));

        gbc.gridx=0;
        gbc.gridy=row;

        panel.add(lbl,gbc);

        gbc.gridx=1;

        panel.add(combo,gbc);

    }

    private void loadSalesOrders(){

        tableModel.setRowCount(0);

        SalesOrderDAO dao = new SalesOrderDAO();

        ArrayList<SalesOrder> list = dao.getAllSalesOrders();

        for(SalesOrder so : list){

            tableModel.addRow(new Object[]{

                    so.getSalesOrderID(),
                    so.getCustomerID(),
                    so.getCreatedBy(),
                    so.getOrderDate(),
                    so.getStatus(),
                    so.getTotalAmount()

            });

        }

    }

    private void loadCustomers() {

        cmbCustomer.removeAllItems();

        CustomerDAO dao = new CustomerDAO();

        for (Customer customer : dao.getAllCustomers()) {

            cmbCustomer.addItem(
                    customer.getCustomerID()
                            + " - "
                            + customer.getCustomerName()
            );

        }

    }

}