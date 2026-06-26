package gui;

import dao.WarehouseDAO;
import model.Warehouse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class WarehouseForm extends JFrame {

    private JTextField txtWarehouseID;
    private JTextField txtWarehouseName;
    private JTextField txtLocation;
    private JTextField txtCapacity;
    private JTextField txtManager;

    private JComboBox<String> cmbStatus;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnRefresh;

    private JTable warehouseTable;
    private DefaultTableModel tableModel;

    public WarehouseForm() {

        setTitle("Warehouse Management");
        setSize(1300,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Warehouse Management");
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

        txtWarehouseID = new JTextField();
        txtWarehouseName = new JTextField();
        txtLocation = new JTextField();
        txtCapacity = new JTextField();
        txtManager = new JTextField();

        txtWarehouseID.setEditable(false);

        cmbStatus = new JComboBox<>(new String[]{
                "Active",
                "Inactive"
        });

        addField(formPanel,gbc,0,"Warehouse ID",txtWarehouseID);
        addField(formPanel,gbc,1,"Warehouse Name",txtWarehouseName);
        addField(formPanel,gbc,2,"Location",txtLocation);
        addField(formPanel,gbc,3,"Capacity",txtCapacity);
        addField(formPanel,gbc,4,"Manager Name",txtManager);

        JLabel lblStatus = new JLabel("Status");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Segoe UI",Font.BOLD,15));

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(lblStatus,gbc);

        gbc.gridx = 1;
        formPanel.add(cmbStatus,gbc);

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
                "Warehouse",
                "Location",
                "Capacity",
                "Manager",
                "Status"
        });

        warehouseTable = new JTable(tableModel);
        warehouseTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(warehouseTable);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel);

        loadWarehouses();

        warehouseTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = warehouseTable.getSelectedRow();

                if (row != -1) {

                    txtWarehouseID.setText(tableModel.getValueAt(row,0).toString());
                    txtWarehouseName.setText(tableModel.getValueAt(row,1).toString());
                    txtLocation.setText(tableModel.getValueAt(row,2).toString());
                    txtCapacity.setText(tableModel.getValueAt(row,3).toString());
                    txtManager.setText(tableModel.getValueAt(row,4).toString());
                    cmbStatus.setSelectedItem(tableModel.getValueAt(row,5).toString());

                }

            }

        });

        btnAdd.addActionListener(e -> {

            Warehouse warehouse = new Warehouse();

            warehouse.setWarehouseName(txtWarehouseName.getText());
            warehouse.setLocation(txtLocation.getText());
            warehouse.setCapacity(Integer.parseInt(txtCapacity.getText()));
            warehouse.setManagerName(txtManager.getText());
            warehouse.setStatus(cmbStatus.getSelectedItem().toString());

            WarehouseDAO dao = new WarehouseDAO();

            if (dao.addWarehouse(warehouse)) {

                JOptionPane.showMessageDialog(this,
                        "Warehouse Added Successfully");

                loadWarehouses();

                btnRefresh.doClick();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Failed to Add Warehouse");

            }

        });

        btnUpdate.addActionListener(e -> {

            Warehouse warehouse = new Warehouse();

            warehouse.setWarehouseID(Integer.parseInt(txtWarehouseID.getText()));
            warehouse.setWarehouseName(txtWarehouseName.getText());
            warehouse.setLocation(txtLocation.getText());
            warehouse.setCapacity(Integer.parseInt(txtCapacity.getText()));
            warehouse.setManagerName(txtManager.getText());
            warehouse.setStatus(cmbStatus.getSelectedItem().toString());

            WarehouseDAO dao = new WarehouseDAO();

            if (dao.updateWarehouse(warehouse)) {

                JOptionPane.showMessageDialog(this,
                        "Warehouse Updated Successfully");

                loadWarehouses();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Update Failed");

            }

        });

        btnDelete.addActionListener(e -> {

            if (txtWarehouseID.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Select a Warehouse First");

                return;

            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this Warehouse?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                WarehouseDAO dao = new WarehouseDAO();

                if (dao.deleteWarehouse(Integer.parseInt(txtWarehouseID.getText()))) {

                    JOptionPane.showMessageDialog(this,
                            "Warehouse Deleted Successfully");

                    loadWarehouses();

                    btnRefresh.doClick();

                }

            }

        });

        btnSearch.addActionListener(e -> {

            String keyword = JOptionPane.showInputDialog(
                    this,
                    "Enter Warehouse Name"
            );

            if (keyword == null || keyword.isBlank())
                return;

            WarehouseDAO dao = new WarehouseDAO();

            tableModel.setRowCount(0);

            for (Warehouse warehouse : dao.searchWarehouse(keyword)) {

                tableModel.addRow(new Object[]{

                        warehouse.getWarehouseID(),
                        warehouse.getWarehouseName(),
                        warehouse.getLocation(),
                        warehouse.getCapacity(),
                        warehouse.getManagerName(),
                        warehouse.getStatus()

                });

            }

        });

        btnRefresh.addActionListener(e -> {

            txtWarehouseID.setText("");
            txtWarehouseName.setText("");
            txtLocation.setText("");
            txtCapacity.setText("");
            txtManager.setText("");

            cmbStatus.setSelectedIndex(0);

            loadWarehouses();

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

    private void loadWarehouses(){

        tableModel.setRowCount(0);

        WarehouseDAO dao = new WarehouseDAO();

        ArrayList<Warehouse> list = dao.getAllWarehouses();

        for(Warehouse w : list){

            tableModel.addRow(new Object[]{

                    w.getWarehouseID(),
                    w.getWarehouseName(),
                    w.getLocation(),
                    w.getCapacity(),
                    w.getManagerName(),
                    w.getStatus()

            });

        }

    }

}