package gui;

import dao.SalesOrderItemDAO;
import model.SalesOrderItem;
import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SalesOrderItemForm extends JFrame {

    private JTextField txtItemID;
    private JTextField txtSalesOrderID;

    private JComboBox<String> cmbProduct;

    private JTextField txtQuantity;
    private JTextField txtUnitPrice;

    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnRefresh;

    private JTable itemTable;
    private DefaultTableModel tableModel;

    public SalesOrderItemForm() {

        setTitle("Sales Order Items");
        setSize(1300,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Sales Order Items");
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

        txtItemID = new JTextField();
        txtSalesOrderID = new JTextField();

        txtItemID.setEditable(false);

        cmbProduct = new JComboBox<>();

        txtQuantity = new JTextField();
        txtUnitPrice = new JTextField();

        addField(formPanel,gbc,0,"Item ID",txtItemID);
        addField(formPanel,gbc,1,"Sales Order ID",txtSalesOrderID);
        addCombo(formPanel,gbc,2,"Product",cmbProduct);
        addField(formPanel,gbc,3,"Quantity",txtQuantity);
        addField(formPanel,gbc,4,"Unit Price",txtUnitPrice);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        JButton[] buttons = {
                btnAdd,
                btnDelete,
                btnRefresh
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

                "Item ID",
                "Sales Order ID",
                "Product ID",
                "Quantity",
                "Unit Price"

        });

        itemTable = new JTable(tableModel);
        itemTable.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(itemTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel);

        loadProducts();

        itemTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = itemTable.getSelectedRow();

                if (row != -1) {

                    txtItemID.setText(tableModel.getValueAt(row, 0).toString());
                    txtSalesOrderID.setText(tableModel.getValueAt(row, 1).toString());

                    int productID = Integer.parseInt(
                            tableModel.getValueAt(row,2).toString()
                    );

                    for(int i=0; i<cmbProduct.getItemCount(); i++){

                        if(cmbProduct.getItemAt(i).startsWith(productID + " -")){

                            cmbProduct.setSelectedIndex(i);
                            break;

                        }

                    }

                    txtQuantity.setText(tableModel.getValueAt(row, 3).toString());
                    txtUnitPrice.setText(tableModel.getValueAt(row, 4).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            try {

                SalesOrderItem item = new SalesOrderItem();

                item.setSalesOrderID(
                        Integer.parseInt(txtSalesOrderID.getText())
                );

                String selected = cmbProduct.getSelectedItem().toString();

                int productID = Integer.parseInt(
                        selected.split(" - ")[0]
                );

                item.setProductID(productID);

                item.setQuantity(
                        Integer.parseInt(txtQuantity.getText())
                );

                item.setUnitPrice(
                        Double.parseDouble(txtUnitPrice.getText())
                );

                SalesOrderItemDAO dao = new SalesOrderItemDAO();

                if (dao.addItem(item)) {

                    JOptionPane.showMessageDialog(this,
                            "Sales Order Item Added Successfully");

                    loadItems(item.getSalesOrderID());

                    btnRefresh.doClick();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Input");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtItemID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select an Item First");

                return;

            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Item?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                SalesOrderItemDAO dao = new SalesOrderItemDAO();

                if (dao.deleteItem(
                        Integer.parseInt(txtItemID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Deleted Successfully");

                    btnRefresh.doClick();

                }

            }

        });

        btnRefresh.addActionListener(e -> {

            txtItemID.setText("");
            txtSalesOrderID.setText("");
            txtQuantity.setText("");
            txtUnitPrice.setText("");

            loadProducts();

            if(cmbProduct.getItemCount() > 0){
                cmbProduct.setSelectedIndex(0);
            }

            tableModel.setRowCount(0);

        });

    }

    public SalesOrderItemForm(int salesOrderID) {

        this();

        txtSalesOrderID.setText(String.valueOf(salesOrderID));
        txtSalesOrderID.setEditable(false);

        loadItems(salesOrderID);

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

    private void loadItems(int salesOrderID){

        tableModel.setRowCount(0);

        SalesOrderItemDAO dao = new SalesOrderItemDAO();

        ArrayList<SalesOrderItem> list = dao.getItems(salesOrderID);

        for(SalesOrderItem item : list){

            tableModel.addRow(new Object[]{

                    item.getSalesOrderItemID(),
                    item.getSalesOrderID(),
                    item.getProductID(),
                    item.getQuantity(),
                    item.getUnitPrice()

            });

        }

    }

    private void loadProducts() {

        cmbProduct.removeAllItems();

        ProductDAO dao = new ProductDAO();

        for (Product product : dao.getAllProducts()) {

            cmbProduct.addItem(
                    product.getProductID() + " - " +
                            product.getProductName()
            );

        }

    }

}