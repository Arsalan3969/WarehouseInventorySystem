package gui;

import dao.PurchaseOrderDAO;
import model.PurchaseOrder;
import dao.SupplierDAO;
import model.Supplier;
import model.LoggedInUser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PurchaseOrderForm extends JFrame {

    private JTextField txtPOID;
    private JComboBox<String> cmbSupplier;
    private LoggedInUser loggedInUser;
    private JTextField txtOrderDate;
    private JTextField txtExpectedDate;
    private JComboBox<String> cmbStatus;
    private JTextField txtTotalAmount;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnNext;

    private JTable purchaseTable;
    private DefaultTableModel tableModel;

    public PurchaseOrderForm(LoggedInUser user) {

        this.loggedInUser = user;

        setTitle("Purchase Order Management");
        setSize(1400,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Purchase Order Management");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI",Font.BOLD,28));
        heading.setForeground(Color.WHITE);
        heading.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));

        mainPanel.add(heading,BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPOID = new JTextField();
        txtPOID.setEditable(false);

        cmbSupplier = new JComboBox<>();


        txtOrderDate = new JTextField("2026-06-30");
        txtExpectedDate = new JTextField("2026-07-05");

        cmbStatus = new JComboBox<>(new String[]{
                "Pending",
                "Approved",
                "Received",
                "Cancelled"
        });

        txtTotalAmount = new JTextField();

        addField(formPanel,gbc,0,"Purchase Order ID",txtPOID);
        addCombo(formPanel,gbc,1,"Supplier",cmbSupplier);
        addField(formPanel,gbc,2,"Order Date",txtOrderDate);
        addField(formPanel,gbc,3,"Expected Date",txtExpectedDate);
        addCombo(formPanel,gbc,4,"Status",cmbStatus);
        addField(formPanel,gbc,5,"Total Amount",txtTotalAmount);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnNext = new JButton("Next >>");

        JButton[] buttons={
                btnAdd,
                btnUpdate,
                btnDelete,
                btnRefresh,
                btnNext
        };

        for(JButton b:buttons){

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
                "PO ID",
                "Supplier ID",
                "Created By",
                "Order Date",
                "Expected Date",
                "Status",
                "Total Amount"
        });

        purchaseTable = new JTable(tableModel);
        purchaseTable.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(purchaseTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel);

        loadPurchaseOrders();

        loadSuppliers();


        purchaseTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = purchaseTable.getSelectedRow();

                if (row != -1) {

                    txtPOID.setText(tableModel.getValueAt(row,0).toString());

                    int supplierID = Integer.parseInt(
                            tableModel.getValueAt(row,1).toString()
                    );

                    for(int i = 0; i < cmbSupplier.getItemCount(); i++){

                        if(cmbSupplier.getItemAt(i).startsWith(supplierID + " -")){

                            cmbSupplier.setSelectedIndex(i);
                            break;

                        }

                    }

                    txtOrderDate.setText(tableModel.getValueAt(row,3).toString());
                    txtExpectedDate.setText(tableModel.getValueAt(row,4).toString());

                    cmbStatus.setSelectedItem(
                            tableModel.getValueAt(row,5).toString());

                    txtTotalAmount.setText(
                            tableModel.getValueAt(row,6).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            try {

                PurchaseOrder po = new PurchaseOrder();

                String selectedSupplier = cmbSupplier.getSelectedItem().toString();

                int supplierID = Integer.parseInt(
                        selectedSupplier.split(" - ")[0]
                );

                po.setSupplierID(supplierID);

                po.setCreatedBy(loggedInUser.getUserID());

                po.setOrderDate(java.sql.Date.valueOf(txtOrderDate.getText()));

                po.setExpectedDate(java.sql.Date.valueOf(txtExpectedDate.getText()));

                po.setStatus(cmbStatus.getSelectedItem().toString());

                po.setTotalAmount(Double.parseDouble(txtTotalAmount.getText()));

                PurchaseOrderDAO dao = new PurchaseOrderDAO();

                if (dao.addPurchaseOrder(po)) {

                    JOptionPane.showMessageDialog(this,
                            "Purchase Order Added Successfully");

                    loadPurchaseOrders();

                    btnRefresh.doClick();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Data");

            }

        });

        btnUpdate.addActionListener(e -> {

            try {

                PurchaseOrder po = new PurchaseOrder();

                po.setPurchaseOrderID(Integer.parseInt(txtPOID.getText()));

                String selectedSupplier = cmbSupplier.getSelectedItem().toString();

                int supplierID = Integer.parseInt(
                        selectedSupplier.split(" - ")[0]
                );

                po.setSupplierID(supplierID);

                po.setCreatedBy(loggedInUser.getUserID());

                po.setOrderDate(java.sql.Date.valueOf(txtOrderDate.getText()));

                po.setExpectedDate(java.sql.Date.valueOf(txtExpectedDate.getText()));

                po.setStatus(cmbStatus.getSelectedItem().toString());

                po.setTotalAmount(Double.parseDouble(txtTotalAmount.getText()));

                PurchaseOrderDAO dao = new PurchaseOrderDAO();

                if (dao.updatePurchaseOrder(po)) {

                    JOptionPane.showMessageDialog(this,
                            "Updated Successfully");

                    loadPurchaseOrders();

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Data");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtPOID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select Purchase Order First");

                return;

            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Delete Purchase Order?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                PurchaseOrderDAO dao = new PurchaseOrderDAO();

                if (dao.deletePurchaseOrder(
                        Integer.parseInt(txtPOID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Deleted Successfully");

                    loadPurchaseOrders();

                    btnRefresh.doClick();

                }

            }

        });

        btnRefresh.addActionListener(e -> {

            txtPOID.setText("");

            cmbSupplier.setSelectedIndex(0);

            txtOrderDate.setText("");

            txtExpectedDate.setText("");

            cmbStatus.setSelectedIndex(0);

            txtTotalAmount.setText("");

            loadPurchaseOrders();

        });

        btnNext.addActionListener(e -> {

            if (txtPOID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select or Save a Purchase Order First!");

                return;

            }

            int purchaseOrderID = Integer.parseInt(txtPOID.getText());

            PurchaseOrderItemForm form = new PurchaseOrderItemForm(purchaseOrderID);

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

    private void loadPurchaseOrders(){

        tableModel.setRowCount(0);

        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        ArrayList<PurchaseOrder> list = dao.getAllPurchaseOrders();

        for(PurchaseOrder po:list){

            tableModel.addRow(new Object[]{

                    po.getPurchaseOrderID(),
                    po.getSupplierID(),
                    po.getCreatedBy(),
                    po.getOrderDate(),
                    po.getExpectedDate(),
                    po.getStatus(),
                    po.getTotalAmount()

            });

        }

    }

    private void loadSuppliers() {

        cmbSupplier.removeAllItems();

        SupplierDAO dao = new SupplierDAO();

        for (Supplier supplier : dao.getAllSuppliers()) {

            cmbSupplier.addItem(
                    supplier.getSupplierID()
                            + " - "
                            + supplier.getCompanyName()
            );

        }

    }
}