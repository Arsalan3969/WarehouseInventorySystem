package dao;

import database.DBConnection;
import model.SalesOrderItem;

import java.sql.*;
import java.util.ArrayList;

public class SalesOrderItemDAO {

    public boolean addItem(SalesOrderItem item){

        try{

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO SalesOrderItem(SalesOrderID,ProductID,Quantity,UnitPrice) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,item.getSalesOrderID());
            ps.setInt(2,item.getProductID());
            ps.setInt(3,item.getQuantity());
            ps.setDouble(4,item.getUnitPrice());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<SalesOrderItem> getItems(int salesOrderID){

        ArrayList<SalesOrderItem> list = new ArrayList<>();

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM SalesOrderItem WHERE SalesOrderID=?"
            );

            ps.setInt(1,salesOrderID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                SalesOrderItem item = new SalesOrderItem();

                item.setSalesOrderItemID(rs.getInt("SalesOrderItemID"));
                item.setSalesOrderID(rs.getInt("SalesOrderID"));
                item.setProductID(rs.getInt("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                item.setUnitPrice(rs.getDouble("UnitPrice"));

                list.add(item);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return list;

    }

    public boolean deleteItem(int id){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM SalesOrderItem WHERE SalesOrderItemID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}