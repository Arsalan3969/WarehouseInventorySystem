package dao;

import database.DBConnection;
import model.GoodsReceipt;

import java.sql.*;
import java.util.ArrayList;

public class GoodsReceiptDAO {

    public boolean addReceipt(GoodsReceipt receipt){

        try{

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO GoodsReceipt(PurchaseOrderID,ReceivedBy,ReceivedDate,Remarks) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, receipt.getPurchaseOrderID());
            ps.setInt(2, receipt.getReceivedBy());
            ps.setDate(3, receipt.getReceivedDate());
            ps.setString(4, receipt.getRemarks());

            return ps.executeUpdate() > 0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<GoodsReceipt> getAllReceipts(){

        ArrayList<GoodsReceipt> list = new ArrayList<>();

        try{

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM GoodsReceipt");

            while(rs.next()){

                GoodsReceipt receipt = new GoodsReceipt();

                receipt.setReceiptID(rs.getInt("ReceiptID"));
                receipt.setPurchaseOrderID(rs.getInt("PurchaseOrderID"));
                receipt.setReceivedBy(rs.getInt("ReceivedBy"));
                receipt.setReceivedDate(rs.getDate("ReceivedDate"));
                receipt.setRemarks(rs.getString("Remarks"));

                list.add(receipt);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return list;

    }

    public boolean deleteReceipt(int id){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM GoodsReceipt WHERE ReceiptID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}