package dao;

import database.DBConnection;
import model.Warehouse;

import java.sql.*;
import java.util.ArrayList;

public class WarehouseDAO {

    public boolean addWarehouse(Warehouse warehouse) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO Warehouse(WarehouseName,Location,Capacity,ManagerName,Status) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, warehouse.getWarehouseName());
            ps.setString(2, warehouse.getLocation());
            ps.setInt(3, warehouse.getCapacity());
            ps.setString(4, warehouse.getManagerName());
            ps.setString(5, warehouse.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Warehouse> getAllWarehouses() {

        ArrayList<Warehouse> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Warehouse");

            while (rs.next()) {

                Warehouse w = new Warehouse();

                w.setWarehouseID(rs.getInt("WarehouseID"));
                w.setWarehouseName(rs.getString("WarehouseName"));
                w.setLocation(rs.getString("Location"));
                w.setCapacity(rs.getInt("Capacity"));
                w.setManagerName(rs.getString("ManagerName"));
                w.setStatus(rs.getString("Status"));

                list.add(w);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

    public boolean updateWarehouse(Warehouse warehouse) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE Warehouse SET WarehouseName=?, Location=?, Capacity=?, ManagerName=?, Status=? WHERE WarehouseID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, warehouse.getWarehouseName());
            ps.setString(2, warehouse.getLocation());
            ps.setInt(3, warehouse.getCapacity());
            ps.setString(4, warehouse.getManagerName());
            ps.setString(5, warehouse.getStatus());
            ps.setInt(6, warehouse.getWarehouseID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteWarehouse(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM Warehouse WHERE WarehouseID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Warehouse> searchWarehouse(String keyword) {

        ArrayList<Warehouse> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Warehouse WHERE WarehouseName LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Warehouse w = new Warehouse();

                w.setWarehouseID(rs.getInt("WarehouseID"));
                w.setWarehouseName(rs.getString("WarehouseName"));
                w.setLocation(rs.getString("Location"));
                w.setCapacity(rs.getInt("Capacity"));
                w.setManagerName(rs.getString("ManagerName"));
                w.setStatus(rs.getString("Status"));

                list.add(w);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}