package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    private Connection connection;

     // check if account already exists
     // return account when needed
     // get account by userName and password
    public Account getAccountByUsernameAndPassword(String username, String password) throws SQLException{
        connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql);

        statement1.setString(1,username);
        statement1.setString(2, password);

        ResultSet rs = statement1.executeQuery();
        if (rs.next()){
            Account account = new Account(rs.getInt("account_id"),
                             rs.getString("username"),
                             rs.getString("password"));
                    
            return account;
        }
        return null;

    }

    //Create new account

    public Account createNewAccount(Account account) throws SQLException{

        //connect to the database
         connection = ConnectionUtil.getConnection(); 

        //insert account into accounts table
        String sql2 = "INSERT INTO account(username, password) VALUES(?, ?)";

        PreparedStatement statement2 = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
        statement2.setString(1, account.getUsername());
        statement2.setString(2, account.getPassword());
        statement2.executeUpdate();

        ResultSet generatedKeys = statement2.getGeneratedKeys();

        //if there is a key to return
        if(generatedKeys.next()){
            account.setAccount_id(generatedKeys.getInt(1));
        }
        return account;


    }

    //get acount by id
    public Account getAccountById(int accountId) throws SQLException {
    connection = ConnectionUtil.getConnection();

     String sql = "SELECT * FROM account WHERE account_id = ?";
     PreparedStatement statement = connection.prepareStatement(sql);
 
     statement.setInt(1, accountId);
 
     ResultSet rs = statement.executeQuery();
        if (rs.next()) {
           Account account = new Account(rs.getInt("account_id"),
                   rs.getString("username"),
                   rs.getString("password"));
   
                   return account;
        }

        
        return null;
    }


 
}
