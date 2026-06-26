package gui;

import dao.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CustomerForm extends JFrame {

    private JTextField txtCustomerID;
    private JTextField txtCustomerName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnRefresh;

    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerForm() {

        setTitle("Customer Management");
        setSize(1300,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Customer Management");
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

        txtCustomerID = new JTextField();
        txtCustomerName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        txtCustomerID.setEditable(false);

        addField(formPanel,gbc,0,"Customer ID",txtCustomerID);
        addField(formPanel,gbc,1,"Customer Name",txtCustomerName);
        addField(formPanel,gbc,2,"Phone Number",txtPhone);
        addField(formPanel,gbc,3,"Email",txtEmail);
        addField(formPanel,gbc,4,"Address",txtAddress);

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
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        formPanel.add(buttonPanel,gbc);

        mainPanel.add(formPanel,BorderLayout.WEST);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{
                "ID",
                "Customer Name",
                "Phone",
                "Email",
                "Address"
        });

        customerTable = new JTable(tableModel);

        customerTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(customerTable);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel);

        loadCustomers();

        customerTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = customerTable.getSelectedRow();

                if (row != -1) {

                    txtCustomerID.setText(tableModel.getValueAt(row, 0).toString());
                    txtCustomerName.setText(tableModel.getValueAt(row, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 3).toString());
                    txtAddress.setText(tableModel.getValueAt(row, 4).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            Customer customer = new Customer();

            customer.setCustomerName(txtCustomerName.getText());
            customer.setPhoneNumber(txtPhone.getText());
            customer.setEmail(txtEmail.getText());
            customer.setAddress(txtAddress.getText());

            CustomerDAO dao = new CustomerDAO();

            if (dao.addCustomer(customer)) {

                JOptionPane.showMessageDialog(this,
                        "Customer Added Successfully");

                loadCustomers();

                btnRefresh.doClick();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Failed to Add Customer");

            }

        });

        btnUpdate.addActionListener(e -> {

            Customer customer = new Customer();

            customer.setCustomerID(Integer.parseInt(txtCustomerID.getText()));
            customer.setCustomerName(txtCustomerName.getText());
            customer.setPhoneNumber(txtPhone.getText());
            customer.setEmail(txtEmail.getText());
            customer.setAddress(txtAddress.getText());

            CustomerDAO dao = new CustomerDAO();

            if (dao.updateCustomer(customer)) {

                JOptionPane.showMessageDialog(this,
                        "Customer Updated Successfully");

                loadCustomers();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Update Failed");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtCustomerID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select a Customer First");

                return;

            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Customer?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                CustomerDAO dao = new CustomerDAO();

                if (dao.deleteCustomer(Integer.parseInt(txtCustomerID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Customer Deleted");

                    loadCustomers();

                    btnRefresh.doClick();

                }

            }

        });

        btnSearch.addActionListener(e -> {

            String keyword = JOptionPane.showInputDialog(
                    this,
                    "Enter Customer Name"
            );

            if (keyword == null || keyword.isBlank())
                return;

            CustomerDAO dao = new CustomerDAO();

            tableModel.setRowCount(0);

            for (Customer c : dao.searchCustomer(keyword)) {

                tableModel.addRow(new Object[]{

                        c.getCustomerID(),
                        c.getCustomerName(),
                        c.getPhoneNumber(),
                        c.getEmail(),
                        c.getAddress()

                });

            }

        });

        btnRefresh.addActionListener(e -> {

            txtCustomerID.setText("");
            txtCustomerName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");

            loadCustomers();

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

    private void loadCustomers(){

        tableModel.setRowCount(0);

        CustomerDAO dao = new CustomerDAO();

        ArrayList<Customer> list = dao.getAllCustomers();

        for(Customer c : list){

            tableModel.addRow(new Object[]{

                    c.getCustomerID(),
                    c.getCustomerName(),
                    c.getPhoneNumber(),
                    c.getEmail(),
                    c.getAddress()

            });

        }

    }

}