package dao;

import database.DBConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ReportDAO {

    public DefaultTableModel getReport(String tableName){

        DefaultTableModel model = new DefaultTableModel();

        try{

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM " + tableName
            );

            ResultSetMetaData meta = rs.getMetaData();

            int columns = meta.getColumnCount();

            for(int i=1;i<=columns;i++){

                model.addColumn(meta.getColumnName(i));

            }

            while(rs.next()){

                Object[] row = new Object[columns];

                for(int i=1;i<=columns;i++){

                    row[i-1] = rs.getObject(i);

                }

                model.addRow(row);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return model;

    }

}