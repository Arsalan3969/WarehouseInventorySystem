package dao;

import database.DBConnection;
import model.PurchaseOrderItem;

import java.sql.*;
import java.util.ArrayList;

public class PurchaseOrderItemDAO {

    public boolean addItem(PurchaseOrderItem item){

        try{

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO PurchaseOrderItem(PurchaseOrderID,ProductID,Quantity,UnitPrice) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,item.getPurchaseOrderID());
            ps.setInt(2,item.getProductID());
            ps.setInt(3,item.getQuantity());
            ps.setDouble(4,item.getUnitPrice());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<PurchaseOrderItem> getItems(int purchaseOrderID){

        ArrayList<PurchaseOrderItem> list = new ArrayList<>();

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM PurchaseOrderItem WHERE PurchaseOrderID=?"
            );

            ps.setInt(1,purchaseOrderID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                PurchaseOrderItem item = new PurchaseOrderItem();

                item.setPurchaseOrderItemID(rs.getInt("PurchaseOrderItemID"));
                item.setPurchaseOrderID(rs.getInt("PurchaseOrderID"));
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
                    "DELETE FROM PurchaseOrderItem WHERE PurchaseOrderItemID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}