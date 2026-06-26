package dao;

import database.DBConnection;
import model.PurchaseOrder;

import java.sql.*;
import java.util.ArrayList;

public class PurchaseOrderDAO {

    public boolean addPurchaseOrder(PurchaseOrder po) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO PurchaseOrder(SupplierID,CreatedBy,OrderDate,ExpectedDate,Status,TotalAmount) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, po.getSupplierID());
            ps.setInt(2, po.getCreatedBy());
            ps.setDate(3, po.getOrderDate());
            ps.setDate(4, po.getExpectedDate());
            ps.setString(5, po.getStatus());
            ps.setDouble(6, po.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<PurchaseOrder> getAllPurchaseOrders(){

        ArrayList<PurchaseOrder> list = new ArrayList<>();

        try{

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM PurchaseOrder");

            while(rs.next()){

                PurchaseOrder po = new PurchaseOrder();

                po.setPurchaseOrderID(rs.getInt("PurchaseOrderID"));
                po.setSupplierID(rs.getInt("SupplierID"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setOrderDate(rs.getDate("OrderDate"));
                po.setExpectedDate(rs.getDate("ExpectedDate"));
                po.setStatus(rs.getString("Status"));
                po.setTotalAmount(rs.getDouble("TotalAmount"));

                list.add(po);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return list;

    }

    public boolean updatePurchaseOrder(PurchaseOrder po){

        try{

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE PurchaseOrder SET SupplierID=?,CreatedBy=?,OrderDate=?,ExpectedDate=?,Status=?,TotalAmount=? WHERE PurchaseOrderID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,po.getSupplierID());
            ps.setInt(2,po.getCreatedBy());
            ps.setDate(3,po.getOrderDate());
            ps.setDate(4,po.getExpectedDate());
            ps.setString(5,po.getStatus());
            ps.setDouble(6,po.getTotalAmount());
            ps.setInt(7,po.getPurchaseOrderID());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public boolean deletePurchaseOrder(int id){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM PurchaseOrder WHERE PurchaseOrderID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}