package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.ProductDAO;
import model.Product;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;

public class ProductForm extends JFrame {

    private JTextField txtProductID;
    private JTextField txtCategoryID;
    private JTextField txtSupplierID;
    private JTextField txtProductName;
    private JTextField txtSKU;
    private JTextField txtPrice;
    private JTextField txtReorderLevel;
    private JTextField txtDescription;

    private JComboBox<String> cmbStatus;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnSearch;

    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductForm() {

        setTitle("Product Management");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color buttonColor = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Product Management");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtProductID = new JTextField();
        txtCategoryID = new JTextField();
        txtSupplierID = new JTextField();
        txtProductName = new JTextField();
        txtSKU = new JTextField();
        txtPrice = new JTextField();
        txtReorderLevel = new JTextField();
        txtDescription = new JTextField();

        cmbStatus = new JComboBox<>(new String[]{
                "Active",
                "Inactive",
                "Discontinued"
        });

        txtProductID.setEditable(false);

        addField(formPanel, gbc,0,"Product ID",txtProductID);
        addField(formPanel, gbc,1,"Category ID",txtCategoryID);
        addField(formPanel, gbc,2,"Supplier ID",txtSupplierID);
        addField(formPanel, gbc,3,"Product Name",txtProductName);
        addField(formPanel, gbc,4,"SKU",txtSKU);
        addField(formPanel, gbc,5,"Unit Price",txtPrice);
        addField(formPanel, gbc,6,"Reorder Level",txtReorderLevel);
        addField(formPanel, gbc,7,"Description",txtDescription);

        JLabel lblStatus = new JLabel("Status");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Segoe UI",Font.BOLD,15));

        gbc.gridx=0;
        gbc.gridy=8;
        formPanel.add(lblStatus,gbc);

        gbc.gridx=1;
        formPanel.add(cmbStatus,gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.setBackground(panelColor);

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnDelete=new JButton("Delete");
        btnSearch=new JButton("Search");
        btnRefresh=new JButton("Refresh");

        JButton[] buttons={
                btnAdd,
                btnUpdate,
                btnDelete,
                btnSearch,
                btnRefresh
        };

        for(JButton b:buttons){

            b.setBackground(buttonColor);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(110,35));

            buttonPanel.add(b);

        }

        gbc.gridx=0;
        gbc.gridy=9;
        gbc.gridwidth=2;

        formPanel.add(buttonPanel,gbc);

        mainPanel.add(formPanel,BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{
                "ID",
                "Category",
                "Supplier",
                "Product Name",
                "SKU",
                "Price",
                "Reorder",
                "Description",
                "Status"
        });

        productTable = new JTable(tableModel);

        productTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(productTable);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel);

        loadProducts();

        productTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = productTable.getSelectedRow();

                if (row != -1) {

                    txtProductID.setText(tableModel.getValueAt(row,0).toString());
                    txtCategoryID.setText(tableModel.getValueAt(row,1).toString());
                    txtSupplierID.setText(tableModel.getValueAt(row,2).toString());
                    txtProductName.setText(tableModel.getValueAt(row,3).toString());
                    txtSKU.setText(tableModel.getValueAt(row,4).toString());
                    txtPrice.setText(tableModel.getValueAt(row,5).toString());
                    txtReorderLevel.setText(tableModel.getValueAt(row,6).toString());
                    txtDescription.setText(tableModel.getValueAt(row,7).toString());

                    cmbStatus.setSelectedItem(tableModel.getValueAt(row,8).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            Product product = new Product();

            product.setCategoryID(Integer.parseInt(txtCategoryID.getText()));
            product.setSupplierID(Integer.parseInt(txtSupplierID.getText()));
            product.setProductName(txtProductName.getText());
            product.setSku(txtSKU.getText());
            product.setUnitPrice(Double.parseDouble(txtPrice.getText()));
            product.setReorderLevel(Integer.parseInt(txtReorderLevel.getText()));
            product.setDescription(txtDescription.getText());
            product.setStatus(cmbStatus.getSelectedItem().toString());

            ProductDAO dao = new ProductDAO();

            if(dao.addProduct(product)){

                JOptionPane.showMessageDialog(this,"Product Added Successfully");

                loadProducts();

            }else{

                JOptionPane.showMessageDialog(this,"Failed");

            }

        });

        btnUpdate.addActionListener(e -> {

            Product product = new Product();

            product.setProductID(Integer.parseInt(txtProductID.getText()));
            product.setCategoryID(Integer.parseInt(txtCategoryID.getText()));
            product.setSupplierID(Integer.parseInt(txtSupplierID.getText()));
            product.setProductName(txtProductName.getText());
            product.setSku(txtSKU.getText());
            product.setUnitPrice(Double.parseDouble(txtPrice.getText()));
            product.setReorderLevel(Integer.parseInt(txtReorderLevel.getText()));
            product.setDescription(txtDescription.getText());
            product.setStatus(cmbStatus.getSelectedItem().toString());

            ProductDAO dao = new ProductDAO();

            if(dao.updateProduct(product)){

                JOptionPane.showMessageDialog(this,"Updated Successfully");

                loadProducts();

            }

        });

        btnDelete.addActionListener(e -> {

            ProductDAO dao = new ProductDAO();

            int id = Integer.parseInt(txtProductID.getText());

            if(dao.deleteProduct(id)){

                JOptionPane.showMessageDialog(this,"Deleted Successfully");

                loadProducts();

            }

        });

        btnRefresh.addActionListener(e -> {

            loadProducts();

            txtProductID.setText("");
            txtCategoryID.setText("");
            txtSupplierID.setText("");
            txtProductName.setText("");
            txtSKU.setText("");
            txtPrice.setText("");
            txtReorderLevel.setText("");
            txtDescription.setText("");

            cmbStatus.setSelectedIndex(0);

        });

        btnSearch.addActionListener(e -> {

            String keyword = JOptionPane.showInputDialog(this,"Enter Product Name");

            ProductDAO dao = new ProductDAO();

            tableModel.setRowCount(0);

            for(Product p : dao.searchProduct(keyword)){

                tableModel.addRow(new Object[]{

                        p.getProductID(),
                        p.getCategoryID(),
                        p.getSupplierID(),
                        p.getProductName(),
                        p.getSku(),
                        p.getUnitPrice(),
                        p.getReorderLevel(),
                        p.getDescription(),
                        p.getStatus()

                });

            }

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

    private void loadProducts() {

        tableModel.setRowCount(0);

        ProductDAO dao = new ProductDAO();

        ArrayList<Product> products = dao.getAllProducts();

        for (Product p : products) {

            tableModel.addRow(new Object[]{

                    p.getProductID(),
                    p.getCategoryID(),
                    p.getSupplierID(),
                    p.getProductName(),
                    p.getSku(),
                    p.getUnitPrice(),
                    p.getReorderLevel(),
                    p.getDescription(),
                    p.getStatus()

            });

        }

    }

}