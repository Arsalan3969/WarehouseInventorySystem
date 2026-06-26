package gui;

import dao.ReportDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportForm extends JFrame {

    private JComboBox<String> cmbReport;
    private JButton btnLoad;

    private JTable reportTable;

    public ReportForm() {

        setTitle("Warehouse Reports");
        setSize(1400,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color background = new Color(22,24,42);
        Color panelColor = new Color(40,43,61);
        Color blue = new Color(45,108,223);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);

        JLabel heading = new JLabel("Warehouse Reports");

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,28));

        heading.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));

        mainPanel.add(heading,BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(panelColor);

        cmbReport = new JComboBox<>(new String[]{

                "Product",
                "Supplier",
                "Customer",
                "Warehouse",
                "PurchaseOrder",
                "PurchaseOrderItem",
                "GoodsReceipt",
                "SalesOrder",
                "SalesOrderItem",
                "InventoryTransaction"

        });

        cmbReport.setPreferredSize(new Dimension(250,35));

        btnLoad = new JButton("Load Report");

        btnLoad.setBackground(blue);
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);

        topPanel.add(new JLabel("Report :"));
        topPanel.add(cmbReport);
        topPanel.add(btnLoad);

        mainPanel.add(topPanel,BorderLayout.NORTH);

        reportTable = new JTable();

        JScrollPane scroll = new JScrollPane(reportTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel);

        btnLoad.addActionListener(e -> {

            String table = cmbReport.getSelectedItem().toString();

            ReportDAO dao = new ReportDAO();

            DefaultTableModel model = dao.getReport(table);

            reportTable.setModel(model);

        });

    }

}