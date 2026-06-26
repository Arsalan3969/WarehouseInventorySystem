package dao;

import database.DBConnection;
import model.LoggedInUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public LoggedInUser login(String username, String password) {

        try {

            Connection connection = DBConnection.getConnection();

            String sql = "SELECT * FROM SystemUser WHERE Username=? AND PasswordHash=?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                LoggedInUser user = new LoggedInUser();

                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setRoleID(rs.getInt("RoleID"));

                return user;

            }

        }

        catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

}