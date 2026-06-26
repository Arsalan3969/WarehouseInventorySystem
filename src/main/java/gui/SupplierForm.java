package gui;

import dao.SupplierDAO;
import model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SupplierForm extends JFrame {

    private JTextField txtSupplierID;
    private JTextField txtCompanyName;
    private JTextField txtContactPerson;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnRefresh;

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public SupplierForm() {

        setTitle("Supplier Management");
        setSize(1300,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Supplier Management");
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

        txtSupplierID = new JTextField();
        txtCompanyName = new JTextField();
        txtContactPerson = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        txtSupplierID.setEditable(false);

        addField(formPanel,gbc,0,"Supplier ID",txtSupplierID);
        addField(formPanel,gbc,1,"Company Name",txtCompanyName);
        addField(formPanel,gbc,2,"Contact Person",txtContactPerson);
        addField(formPanel,gbc,3,"Phone Number",txtPhone);
        addField(formPanel,gbc,4,"Email",txtEmail);
        addField(formPanel,gbc,5,"Address",txtAddress);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panelColor);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");

        JButton[] buttons = {
                btnAdd,
                btnUpdate,
                btnDelete,
                btnSearch,
                btnRefresh
        };

        for(JButton b : buttons){

            b.setBackground(blue);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(110,35));

            buttonPanel.add(b);

        }

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        formPanel.add(buttonPanel,gbc);

        mainPanel.add(formPanel,BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{
                "ID",
                "Company",
                "Contact Person",
                "Phone",
                "Email",
                "Address"
        });

        supplierTable = new JTable(tableModel);
        supplierTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(supplierTable);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel);

        loadSuppliers();

        supplierTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = supplierTable.getSelectedRow();

                if (row != -1) {

                    txtSupplierID.setText(tableModel.getValueAt(row,0).toString());
                    txtCompanyName.setText(tableModel.getValueAt(row,1).toString());
                    txtContactPerson.setText(tableModel.getValueAt(row,2).toString());
                    txtPhone.setText(tableModel.getValueAt(row,3).toString());
                    txtEmail.setText(tableModel.getValueAt(row,4).toString());
                    txtAddress.setText(tableModel.getValueAt(row,5).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            Supplier supplier = new Supplier();

            supplier.setCompanyName(txtCompanyName.getText());
            supplier.setContactPerson(txtContactPerson.getText());
            supplier.setPhoneNumber(txtPhone.getText());
            supplier.setEmail(txtEmail.getText());
            supplier.setAddress(txtAddress.getText());

            SupplierDAO dao = new SupplierDAO();

            if (dao.addSupplier(supplier)) {

                JOptionPane.showMessageDialog(this, "Supplier Added Successfully");

                loadSuppliers();

                btnRefresh.doClick();

            } else {

                JOptionPane.showMessageDialog(this, "Failed to Add Supplier");

            }

        });

        btnUpdate.addActionListener(e -> {

            Supplier supplier = new Supplier();

            supplier.setSupplierID(Integer.parseInt(txtSupplierID.getText()));
            supplier.setCompanyName(txtCompanyName.getText());
            supplier.setContactPerson(txtContactPerson.getText());
            supplier.setPhoneNumber(txtPhone.getText());
            supplier.setEmail(txtEmail.getText());
            supplier.setAddress(txtAddress.getText());

            SupplierDAO dao = new SupplierDAO();

            if (dao.updateSupplier(supplier)) {

                JOptionPane.showMessageDialog(this, "Supplier Updated Successfully");

                loadSuppliers();

            } else {

                JOptionPane.showMessageDialog(this, "Update Failed");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtSupplierID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Select a Supplier First");

                return;

            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Supplier?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                SupplierDAO dao = new SupplierDAO();

                if (dao.deleteSupplier(Integer.parseInt(txtSupplierID.getText()))) {

                    JOptionPane.showMessageDialog(this, "Supplier Deleted");

                    loadSuppliers();

                    btnRefresh.doClick();

                }

            }

        });

        btnSearch.addActionListener(e -> {

            String keyword = JOptionPane.showInputDialog(
                    this,
                    "Enter Company Name"
            );

            if (keyword == null || keyword.isBlank()) {
                return;
            }

            SupplierDAO dao = new SupplierDAO();

            tableModel.setRowCount(0);

            for (Supplier s : dao.searchSupplier(keyword)) {

                tableModel.addRow(new Object[]{

                        s.getSupplierID(),
                        s.getCompanyName(),
                        s.getContactPerson(),
                        s.getPhoneNumber(),
                        s.getEmail(),
                        s.getAddress()

                });

            }

        });

        btnRefresh.addActionListener(e -> {

            txtSupplierID.setText("");
            txtCompanyName.setText("");
            txtContactPerson.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");

            loadSuppliers();

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

        gbc.gridx = 0;
        gbc.gridy = row;

        panel.add(lbl,gbc);

        gbc.gridx = 1;

        panel.add(field,gbc);

    }

    private void loadSuppliers(){

        tableModel.setRowCount(0);

        SupplierDAO dao = new SupplierDAO();

        ArrayList<Supplier> list = dao.getAllSuppliers();

        for(Supplier s : list){

            tableModel.addRow(new Object[]{

                    s.getSupplierID(),
                    s.getCompanyName(),
                    s.getContactPerson(),
                    s.getPhoneNumber(),
                    s.getEmail(),
                    s.getAddress()

            });

        }

    }

}