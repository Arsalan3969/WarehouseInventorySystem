package dao;

import database.DBConnection;
import model.SalesOrder;

import java.sql.*;
import java.util.ArrayList;

public class SalesOrderDAO {

    public boolean addSalesOrder(SalesOrder so){

        try{

            Connection con = DBConnection.getConnection();

            String sql="INSERT INTO SalesOrder(CustomerID,CreatedBy,OrderDate,Status,TotalAmount) VALUES(?,?,?,?,?)";

            PreparedStatement ps=con.prepareStatement(sql);

            ps.setInt(1,so.getCustomerID());
            ps.setInt(2,so.getCreatedBy());
            ps.setDate(3,so.getOrderDate());
            ps.setString(4,so.getStatus());
            ps.setDouble(5,so.getTotalAmount());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<SalesOrder> getAllSalesOrders(){

        ArrayList<SalesOrder> list=new ArrayList<>();

        try{

            Connection con=DBConnection.getConnection();

            Statement st=con.createStatement();

            ResultSet rs=st.executeQuery("SELECT * FROM SalesOrder");

            while(rs.next()){

                SalesOrder so=new SalesOrder();

                so.setSalesOrderID(rs.getInt("SalesOrderID"));
                so.setCustomerID(rs.getInt("CustomerID"));
                so.setCreatedBy(rs.getInt("CreatedBy"));
                so.setOrderDate(rs.getDate("OrderDate"));
                so.setStatus(rs.getString("Status"));
                so.setTotalAmount(rs.getDouble("TotalAmount"));

                list.add(so);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return list;

    }

    public boolean updateSalesOrder(SalesOrder so){

        try{

            Connection con=DBConnection.getConnection();

            String sql="UPDATE SalesOrder SET CustomerID=?,CreatedBy=?,OrderDate=?,Status=?,TotalAmount=? WHERE SalesOrderID=?";

            PreparedStatement ps=con.prepareStatement(sql);

            ps.setInt(1,so.getCustomerID());
            ps.setInt(2,so.getCreatedBy());
            ps.setDate(3,so.getOrderDate());
            ps.setString(4,so.getStatus());
            ps.setDouble(5,so.getTotalAmount());
            ps.setInt(6,so.getSalesOrderID());

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteSalesOrder(int id){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
                    "DELETE FROM SalesOrder WHERE SalesOrderID=?"
            );

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();
            return false;

        }

    }

}