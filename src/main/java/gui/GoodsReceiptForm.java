package gui;

import dao.GoodsReceiptDAO;
import model.GoodsReceipt;
import dao.PurchaseOrderDAO;
import model.PurchaseOrder;
import model.LoggedInUser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GoodsReceiptForm extends JFrame {

    private JTextField txtReceiptID;
    private JComboBox<String> cmbPurchaseOrder;
    private JTextField txtReceivedDate;
    private JTextField txtRemarks;
    private LoggedInUser loggedInUser;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnRefresh;

    private JTable receiptTable;
    private DefaultTableModel tableModel;

    public GoodsReceiptForm(LoggedInUser user) {

        this.loggedInUser = user;

        setTitle("Goods Receipt");
        setSize(1300,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Goods Receipt Management");
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

        txtReceiptID = new JTextField();
        txtReceiptID.setEditable(false);

        cmbPurchaseOrder = new JComboBox<>();
        txtReceivedDate = new JTextField("2026-06-30");
        txtRemarks = new JTextField();

        addField(formPanel, gbc, 0, "Receipt ID", txtReceiptID);
        addCombo(formPanel, gbc, 1, "Purchase Order", cmbPurchaseOrder);
        addField(formPanel, gbc, 2, "Received Date", txtReceivedDate);
        addField(formPanel, gbc, 3, "Remarks", txtRemarks);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        JButton[] buttons = {btnAdd, btnDelete, btnRefresh};

        for (JButton b : buttons) {

            b.setBackground(blue);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(120,35));

            buttonPanel.add(b);

        }

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{

                "Receipt ID",
                "Purchase Order",
                "Received By",
                "Received Date",
                "Remarks"

        });

        receiptTable = new JTable(tableModel);
        receiptTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(receiptTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        loadPurchaseOrders();
        loadReceipts();

        receiptTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = receiptTable.getSelectedRow();

                if (row != -1) {

                    txtReceiptID.setText(tableModel.getValueAt(row,0).toString());

                    int purchaseOrderID = Integer.parseInt(
                            tableModel.getValueAt(row,1).toString()
                    );

                    for(int i=0;i<cmbPurchaseOrder.getItemCount();i++){

                        if(cmbPurchaseOrder.getItemAt(i).startsWith(purchaseOrderID + " -")){

                            cmbPurchaseOrder.setSelectedIndex(i);
                            break;

                        }

                    }

                    txtReceivedDate.setText(
                            tableModel.getValueAt(row,3).toString());

                    txtRemarks.setText(
                            tableModel.getValueAt(row,4).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            try {

                GoodsReceipt receipt = new GoodsReceipt();

                String selectedPO = cmbPurchaseOrder.getSelectedItem().toString();

                int purchaseOrderID = Integer.parseInt(
                        selectedPO.split(" - ")[0]
                );

                receipt.setPurchaseOrderID(purchaseOrderID);

                receipt.setReceivedBy(loggedInUser.getUserID());

                receipt.setReceivedDate(
                        java.sql.Date.valueOf(txtReceivedDate.getText())
                );

                receipt.setRemarks(txtRemarks.getText());

                GoodsReceiptDAO dao = new GoodsReceiptDAO();

                if (dao.addReceipt(receipt)) {

                    JOptionPane.showMessageDialog(this,
                            "Goods Receipt Added Successfully");

                    loadReceipts();

                    btnRefresh.doClick();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed to Add Goods Receipt");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Data");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtReceiptID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select a Receipt First");

                return;

            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Goods Receipt?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                GoodsReceiptDAO dao = new GoodsReceiptDAO();

                if (dao.deleteReceipt(
                        Integer.parseInt(txtReceiptID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Goods Receipt Deleted");

                    loadReceipts();

                    btnRefresh.doClick();

                }

            }

        });

        btnRefresh.addActionListener(e -> {

            txtReceiptID.setText("");
            txtReceivedDate.setText("");
            txtRemarks.setText("");

            loadPurchaseOrders();

            if(cmbPurchaseOrder.getItemCount() > 0){

                cmbPurchaseOrder.setSelectedIndex(0);

            }
            loadReceipts();

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

    private void loadReceipts(){

        tableModel.setRowCount(0);

        GoodsReceiptDAO dao = new GoodsReceiptDAO();

        ArrayList<GoodsReceipt> list = dao.getAllReceipts();

        for(GoodsReceipt receipt : list){

            tableModel.addRow(new Object[]{

                    receipt.getReceiptID(),
                    receipt.getPurchaseOrderID(),
                    receipt.getReceivedBy(),
                    receipt.getReceivedDate(),
                    receipt.getRemarks()

            });

        }

    }

    private void loadPurchaseOrders() {

        cmbPurchaseOrder.removeAllItems();

        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        for (PurchaseOrder po : dao.getAllPurchaseOrders()) {

            cmbPurchaseOrder.addItem(
                    po.getPurchaseOrderID()
                            + " - PO"
            );

        }

    }

}