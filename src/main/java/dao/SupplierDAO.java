package dao;

import database.DBConnection;
import model.Supplier;
import java.sql.*;
import java.util.ArrayList;

public class SupplierDAO {

    public boolean addSupplier(Supplier supplier) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO Supplier(CompanyName,ContactPerson,PhoneNumber,Email,Address) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getPhoneNumber());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Supplier> getAllSuppliers() {

        ArrayList<Supplier> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Supplier");

            while (rs.next()) {

                Supplier s = new Supplier();

                s.setSupplierID(rs.getInt("SupplierID"));
                s.setCompanyName(rs.getString("CompanyName"));
                s.setContactPerson(rs.getString("ContactPerson"));
                s.setPhoneNumber(rs.getString("PhoneNumber"));
                s.setEmail(rs.getString("Email"));
                s.setAddress(rs.getString("Address"));

                list.add(s);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    public boolean updateSupplier(Supplier supplier) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE Supplier SET CompanyName=?, ContactPerson=?, PhoneNumber=?, Email=?, Address=? WHERE SupplierID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getPhoneNumber());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());
            ps.setInt(6, supplier.getSupplierID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteSupplier(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM Supplier WHERE SupplierID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Supplier> searchSupplier(String keyword) {

        ArrayList<Supplier> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Supplier WHERE CompanyName LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Supplier s = new Supplier();

                s.setSupplierID(rs.getInt("SupplierID"));
                s.setCompanyName(rs.getString("CompanyName"));
                s.setContactPerson(rs.getString("ContactPerson"));
                s.setPhoneNumber(rs.getString("PhoneNumber"));
                s.setEmail(rs.getString("Email"));
                s.setAddress(rs.getString("Address"));

                list.add(s);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}