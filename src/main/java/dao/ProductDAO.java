package dao;

import database.DBConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    public boolean addProduct(Product product) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO Product(CategoryID,SupplierID,ProductName,SKU,UnitPrice,ReorderLevel,Description,Status) VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, product.getCategoryID());
            ps.setInt(2, product.getSupplierID());
            ps.setString(3, product.getProductName());
            ps.setString(4, product.getSku());
            ps.setDouble(5, product.getUnitPrice());
            ps.setInt(6, product.getReorderLevel());
            ps.setString(7, product.getDescription());
            ps.setString(8, product.getStatus());

            return ps.executeUpdate() > 0;

        }

        catch (Exception e){

            e.printStackTrace();

            return false;

        }

    }

    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Product";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product product = new Product();

                product.setProductID(rs.getInt("ProductID"));
                product.setCategoryID(rs.getInt("CategoryID"));
                product.setSupplierID(rs.getInt("SupplierID"));
                product.setProductName(rs.getString("ProductName"));
                product.setSku(rs.getString("SKU"));
                product.setUnitPrice(rs.getDouble("UnitPrice"));
                product.setReorderLevel(rs.getInt("ReorderLevel"));
                product.setDescription(rs.getString("Description"));
                product.setStatus(rs.getString("Status"));

                list.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateProduct(Product product) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE Product SET CategoryID=?, SupplierID=?, ProductName=?, SKU=?, UnitPrice=?, ReorderLevel=?, Description=?, Status=? WHERE ProductID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, product.getCategoryID());
            ps.setInt(2, product.getSupplierID());
            ps.setString(3, product.getProductName());
            ps.setString(4, product.getSku());
            ps.setDouble(5, product.getUnitPrice());
            ps.setInt(6, product.getReorderLevel());
            ps.setString(7, product.getDescription());
            ps.setString(8, product.getStatus());
            ps.setInt(9, product.getProductID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteProduct(int productID) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM Product WHERE ProductID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, productID);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public ArrayList<Product> searchProduct(String keyword) {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Product WHERE ProductName LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();

                p.setProductID(rs.getInt("ProductID"));
                p.setCategoryID(rs.getInt("CategoryID"));
                p.setSupplierID(rs.getInt("SupplierID"));
                p.setProductName(rs.getString("ProductName"));
                p.setSku(rs.getString("SKU"));
                p.setUnitPrice(rs.getDouble("UnitPrice"));
                p.setReorderLevel(rs.getInt("ReorderLevel"));
                p.setDescription(rs.getString("Description"));
                p.setStatus(rs.getString("Status"));

                list.add(p);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }

}