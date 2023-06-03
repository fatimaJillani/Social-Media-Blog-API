package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO{
    private Connection connection;
    public int affectedRows;

    

    //addMessage
    public Message insertMessage(Message message) throws SQLException{

        connection = ConnectionUtil.getConnection();

        ResultSet generatedkeys;
    
        //insert message into database
        String sql = "INSERT INTO message (message_text, time_posted_epoch) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        
        statement.setString(1, message.getMessage_text());
        statement.setLong(2, message.getTime_posted_epoch());
        statement.executeUpdate();

        generatedkeys = statement.getGeneratedKeys();

            //if there is a key to return
            if(generatedkeys.next()) {
                int generated_message_id = generatedkeys.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        return null;
    }
    
      
    

    public Message getUserByPostedBy(int postedBy) throws SQLException{
        connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM  message WHERE posted_by = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, postedBy);

        ResultSet rs = statement.executeQuery();

         while(rs.next()){
            Message message = new Message(rs.getInt("message_id"),
                             rs.getInt("posted_by"),
                             rs.getString(" message_text"),
                             rs.getLong("time_posted_epoch"));
                return message;                  
            }

        return null;
    
    }


    //get all messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            //Sql logic
            String sql = "SELECT * FROM message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                   rs.getInt("posted_by"),
                                 rs.getString("message_text"),
                                 rs.getLong("time_posted_epoch"));
                messages.add(message);
    
            }    
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return messages;
    }

   
    //get message by messageID
    public Message getMessageByMessageId(int message_id) throws SQLException{
        

        connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, message_id);

        ResultSet rs = statement.executeQuery();

        if(rs.next()){
            Message message;
            message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        
            return message;
        }
    
        return null;
    }

    //Delete a Message Given Message Id
    public Message deleteMessageById(int message_id) throws SQLException {
    connection = ConnectionUtil.getConnection();
    String sql = "SELECT * FROM message WHERE message_id = ?";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, message_id);
    ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            // Retrieve the message before deleting it
            Message deletedMessage = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
    
            // Delete the message
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, message_id);
            deleteStatement.executeUpdate();
    
            return deletedMessage;
        }
    
        return null;

    }


    //Update message
    public void updateMessage (Message message){
        connection =ConnectionUtil.getConnection();

        String sql = "UPDATE message SET message_text = ? WHERE message_id =?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    //get all messages from a given user
    public List<Message> getAllMessagesFromUser(int account_id){
        connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account_id);

            ResultSet rs = statement.executeQuery();
            

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                  rs.getInt("posted_by"),
                  rs.getString("message_text"),
                  rs.getLong("time_posted_epoch"));
                  
                //add retrived user messages to the list  
                messages.add(message);
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return messages;

    }

}


