package DAO;

import java.sql.*;


import Model.Account;

public class AccountDAO {
    private Connection connection;

    public AccountDAO(){
        //initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:8080/mydatabase", "username", "password");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Create new account

    public Account createNewAccount(Account account) throws SQLException{
        //validate username. It should not be blank
        if(account.getUsername().equals("") ||account.getPassword().length() <4){
            throw new IllegalArgumentException("Username can not be blank");
        }

        //validate password. it miust be more than 4 characters long
        if(account.getPassword().length()< 4){
            throw new IllegalArgumentException("Password can not be less than 4 characters");
        }

        // check if account already exists

        String sql = "Select * FROM account WHERE username = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql);

        statement1.setString(1,account.getUsername());

        ResultSet rs = statement1.executeQuery();
        if (rs.next()){
            throw new IllegalArgumentException("Account already exists");
        }

        //insert account into accounts table
        String sql2 = "INSERT INTO account(username, password) VALUES(?,?)";

        PreparedStatement statement2 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement2.setString(1, account.getUsername());
        statement2.setString(2, account.getPassword());
        statement2.executeUpdate();

        ResultSet generatedKeys = statement2.getGeneratedKeys();

        //if there is a key to return
        if(generatedKeys.next()){
            account.setAccount_id(generatedKeys.getInt(1));
        }

        statement1.close();
        statement2.close();
        return account;


    }
    
}
