package gui;

import dao.InventoryTransactionDAO;
import model.InventoryTransaction;
import dao.ProductDAO;
import dao.WarehouseDAO;
import model.Product;
import model.Warehouse;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryTransactionForm extends JFrame {

    private JTextField txtTransactionID;
    private JComboBox<String> cmbProduct;
    private JComboBox<String> cmbWarehouse;
    private JComboBox<String> cmbTransactionType;
    private JTextField txtQuantity;
    private JTextField txtTransactionDate;

    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnRefresh;

    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public InventoryTransactionForm() {

        setTitle("Inventory Transactions");
        setSize(1400,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Inventory Transaction Management");
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

        txtTransactionID = new JTextField();
        txtTransactionID.setEditable(false);

        cmbProduct = new JComboBox<>();
        cmbWarehouse = new JComboBox<>();

        cmbTransactionType = new JComboBox<>(new String[]{
                "Stock In",
                "Stock Out",
                "Transfer"
        });

        txtQuantity = new JTextField();
        txtTransactionDate = new JTextField("2026-06-30");

        addField(formPanel,gbc,0,"Transaction ID",txtTransactionID);
        addCombo(formPanel,gbc,1,"Product",cmbProduct);
        addCombo(formPanel,gbc,2,"Warehouse",cmbWarehouse);
        addCombo(formPanel,gbc,3,"Transaction Type",cmbTransactionType);
        addField(formPanel,gbc,4,"Quantity",txtQuantity);
        addField(formPanel,gbc,5,"Transaction Date",txtTransactionDate);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        JButton[] buttons = {btnAdd,btnDelete,btnRefresh};

        for(JButton b : buttons){

            b.setBackground(blue);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(120,35));

            buttonPanel.add(b);

        }

        gbc.gridx=0;
        gbc.gridy=6;
        gbc.gridwidth=2;

        formPanel.add(buttonPanel,gbc);

        mainPanel.add(formPanel,BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{
                "ID",
                "Product",
                "Warehouse",
                "Type",
                "Quantity",
                "Date"
        });

        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(transactionTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel);

        loadProducts();
        loadWarehouses();
        loadTransactions();

        transactionTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = transactionTable.getSelectedRow();

                if (row != -1) {

                    txtTransactionID.setText(tableModel.getValueAt(row,0).toString());

                    int productID = Integer.parseInt(
                            tableModel.getValueAt(row,1).toString()
                    );

                    for(int i=0;i<cmbProduct.getItemCount();i++){

                        if(cmbProduct.getItemAt(i).startsWith(productID + " -")){

                            cmbProduct.setSelectedIndex(i);
                            break;

                        }

                    }

                    int warehouseID = Integer.parseInt(
                            tableModel.getValueAt(row,2).toString()
                    );

                    for(int i=0;i<cmbWarehouse.getItemCount();i++){

                        if(cmbWarehouse.getItemAt(i).startsWith(warehouseID + " -")){

                            cmbWarehouse.setSelectedIndex(i);
                            break;

                        }

                    }

                    cmbTransactionType.setSelectedItem(
                            tableModel.getValueAt(row,3).toString());

                    txtQuantity.setText(
                            tableModel.getValueAt(row,4).toString());

                    txtTransactionDate.setText(
                            tableModel.getValueAt(row,5).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            try {

                InventoryTransaction transaction = new InventoryTransaction();

                String selectedProduct = cmbProduct.getSelectedItem().toString();

                int productID = Integer.parseInt(
                        selectedProduct.split(" - ")[0]
                );

                transaction.setProductID(productID);

                String selectedWarehouse = cmbWarehouse.getSelectedItem().toString();

                int warehouseID = Integer.parseInt(
                        selectedWarehouse.split(" - ")[0]
                );

                transaction.setWarehouseID(warehouseID);

                String type = cmbTransactionType.getSelectedItem().toString();

                if(type.equals("Stock In"))
                    type = "IN";
                else if(type.equals("Stock Out"))
                    type = "OUT";
                else
                    type = "TRANSFER";

                transaction.setTransactionType(type);

                transaction.setQuantity(
                        Integer.parseInt(txtQuantity.getText()));

                transaction.setTransactionDate(
                        java.sql.Date.valueOf(txtTransactionDate.getText()));

                InventoryTransactionDAO dao = new InventoryTransactionDAO();

                if (dao.addTransaction(transaction)) {

                    JOptionPane.showMessageDialog(this,
                            "Transaction Added Successfully");

                    loadTransactions();

                    btnRefresh.doClick();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed to Add Transaction");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Input");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtTransactionID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select a Transaction First");

                return;

            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Transaction?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                InventoryTransactionDAO dao = new InventoryTransactionDAO();

                if (dao.deleteTransaction(
                        Integer.parseInt(txtTransactionID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Transaction Deleted");

                    loadTransactions();

                    btnRefresh.doClick();

                }

            }

        });

        btnRefresh.addActionListener(e -> {

            txtTransactionID.setText("");
            txtQuantity.setText("");
            txtTransactionDate.setText("");

            loadProducts();
            loadWarehouses();

            if(cmbProduct.getItemCount() > 0)
                cmbProduct.setSelectedIndex(0);

            if(cmbWarehouse.getItemCount() > 0)
                cmbWarehouse.setSelectedIndex(0);

            cmbTransactionType.setSelectedIndex(0);

            loadTransactions();

        });

    }

    private void addField(JPanel panel, GridBagConstraints gbc,
                          int row, String label, JTextField field){

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

    private void addCombo(JPanel panel, GridBagConstraints gbc,
                          int row, String label, JComboBox<String> combo){

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,15));

        gbc.gridx=0;
        gbc.gridy=row;
        panel.add(lbl,gbc);

        gbc.gridx=1;
        panel.add(combo,gbc);

    }

    private void loadTransactions(){

        tableModel.setRowCount(0);

        InventoryTransactionDAO dao = new InventoryTransactionDAO();

        ArrayList<InventoryTransaction> list =
                dao.getAllTransactions();

        for(InventoryTransaction t : list){

            tableModel.addRow(new Object[]{

                    t.getTransactionID(),
                    t.getProductID(),
                    t.getWarehouseID(),
                    t.getTransactionType(),
                    t.getQuantity(),
                    t.getTransactionDate()

            });

        }

    }

    private void loadProducts() {

        cmbProduct.removeAllItems();

        ProductDAO dao = new ProductDAO();

        for(Product product : dao.getAllProducts()){

            cmbProduct.addItem(
                    product.getProductID() +
                            " - " +
                            product.getProductName()
            );

        }

    }

    private void loadWarehouses() {

        cmbWarehouse.removeAllItems();

        WarehouseDAO dao = new WarehouseDAO();

        for(Warehouse warehouse : dao.getAllWarehouses()){

            cmbWarehouse.addItem(
                    warehouse.getWarehouseID() +
                            " - " +
                            warehouse.getWarehouseName()
            );

        }

    }

}