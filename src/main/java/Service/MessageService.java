package Service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public boolean userExists(int accountId) throws SQLException{
          Account user = accountDAO.getAccountById(accountId);
          return user != null;
    }



    public Message createMessage(Message message) throws SQLException{



        Message newMessage = messageDAO.insertMessage(message);

        if(message.getMessage_text().isBlank()){
            return null;
        }
        if(message.getMessage_text().length() >= 255){
            return null;
        }

        if(!userExists(message.getPosted_by())){
            return null;
        }

        return newMessage;
    }

    //return all messages
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();
        
        if( messages == null || messages.isEmpty()){
             return new ArrayList<>();
          }
  
        return messages;
      }

   
        
    public Message getMessageById(int message_id) throws SQLException {
        Message message = messageDAO.getMessageByMessageId(message_id);
       
        return message;
    }


    public Message deletedMessageById(int message_id) throws SQLException{
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessageById(int message_id, Message newMessage) throws SQLException{
        Message existingMessage = messageDAO.getMessageByMessageId(message_id);
        String newMessageText = newMessage.getMessage_text();
        if (existingMessage == null) {
            return null; //Message does not exist
        }
         
        //check is the new message text is valid 
        if(newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() >= 255){
            return null; //Invalid message text
        }

        //update the text
        existingMessage.setMessage_text(newMessageText);
        messageDAO.updateMessage(existingMessage);

        return existingMessage;
    }

    public List<Message> getAllMessagesFromUser(int account_id){
        //get all messages from a given user
        return messageDAO.getAllMessagesFromUser(account_id);
    }

    
    
}
