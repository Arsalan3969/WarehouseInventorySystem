package dao;

import database.DBConnection;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    public boolean addCustomer(Customer customer) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO Customer(CustomerName, PhoneNumber, Email, Address) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Customer> getAllCustomers() {

        ArrayList<Customer> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Customer");

            while (rs.next()) {

                Customer c = new Customer();

                c.setCustomerID(rs.getInt("CustomerID"));
                c.setCustomerName(rs.getString("CustomerName"));
                c.setPhoneNumber(rs.getString("PhoneNumber"));
                c.setEmail(rs.getString("Email"));
                c.setAddress(rs.getString("Address"));

                list.add(c);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    public boolean updateCustomer(Customer customer) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE Customer SET CustomerName=?, PhoneNumber=?, Email=?, Address=? WHERE CustomerID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setInt(5, customer.getCustomerID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteCustomer(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM Customer WHERE CustomerID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Customer> searchCustomer(String keyword) {

        ArrayList<Customer> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Customer WHERE CustomerName LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Customer c = new Customer();

                c.setCustomerID(rs.getInt("CustomerID"));
                c.setCustomerName(rs.getString("CustomerName"));
                c.setPhoneNumber(rs.getString("PhoneNumber"));
                c.setEmail(rs.getString("Email"));
                c.setAddress(rs.getString("Address"));

                list.add(c);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}