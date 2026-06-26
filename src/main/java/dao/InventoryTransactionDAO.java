package dao;

import database.DBConnection;
import model.InventoryTransaction;

import java.sql.*;
import java.util.ArrayList;

public class InventoryTransactionDAO {

    public boolean addTransaction(InventoryTransaction transaction){

        try{

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO InventoryTransaction(ProductID,WarehouseID,TransactionType,Quantity,TransactionDate) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,transaction.getProductID());
            ps.setInt(2,transaction.getWarehouseID());
            ps.setString(3,transaction.getTransactionType());
            ps.setInt(4,transaction.getQuantity());
            ps.setDate(5,transaction.getTransactionDate());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<InventoryTransaction> getAllTransactions(){

        ArrayList<InventoryTransaction> list=new ArrayList<>();

        try{

            Connection con=DBConnection.getConnection();

            Statement st=con.createStatement();

            ResultSet rs=st.executeQuery(
                    "SELECT * FROM InventoryTransaction");

            while(rs.next()){

                InventoryTransaction t=new InventoryTransaction();

                t.setTransactionID(rs.getInt("TransactionID"));
                t.setProductID(rs.getInt("ProductID"));
                t.setWarehouseID(rs.getInt("WarehouseID"));
                t.setTransactionType(rs.getString("TransactionType"));
                t.setQuantity(rs.getInt("Quantity"));
                t.setTransactionDate(rs.getDate("TransactionDate"));

                list.add(t);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return list;

    }

    public boolean deleteTransaction(int id){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
                    "DELETE FROM InventoryTransaction WHERE TransactionID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}