package Controller;


import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    private Javalin app;


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        
        //create Javalin instance
        app = Javalin.create();
        
       // create endpoints
       app.post("/register", this::postRegisterAccountHandler);
       app.post("/login", this::postUserLoginHandler);
       app.post("/messages", this::postMessageHandler);
       app.get("/messages", this::getAllMessagesHandler);
       app.get("/messages/{message_id}", this::getMessageByIdHandler);
       app.delete("/messages/{message_id}", this::deletedMessageByIdHandler);
       app.patch("/messages/{message_id}", this::updateMessageById);
       app.get("accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */


    
    private void postRegisterAccountHandler(Context context) throws JsonMappingException, JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account newAccount = accountService.createNewAccount(account);
        // String response = mapper.writeValueAsString(newAccount);

        //if the registration was not successfull, newAccount will be null
        if(newAccount!= null){
            context.json(newAccount);
        }else{
            context.status(400);
        }
    }
    
    private void postUserLoginHandler(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Account userLogin = mapper.readValue(ctx.body(), Account.class);
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();
        
        Account authenticatedUser = accountService.authenticateUser(username, password);
        // String response  = mapper.writeValueAsString(authenticatedUser);
        
        
        if(authenticatedUser != null){
            ctx.json(authenticatedUser);
        } else {
            ctx.status(401);
        }
    }
    
    private void postMessageHandler(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        Message messageAdded = messageService.createMessage(message);
        
        if(messageAdded == null) {
            ctx.status(400);
        } else {
            ctx.json(messageAdded);
        }
    }
    
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException, SQLException {
         List<Message> messages = messageService.getAllMessages();
         ObjectMapper mapper = new ObjectMapper();
         String response = mapper.writeValueAsString(messages);
         ctx.result(response);
    }


    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        // ObjectMapper mapper = new ObjectMapper();
        //get message id
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        //get message
        Message messageReturned = messageService.getMessageById(message_id);
        // String response = mapper.writeValueAsString(messageReturned);
        //retrun message back to user
        if(messageReturned == null){
            ctx.result("");
        } else {
            ctx.json(messageReturned);
        }   
    }

    private void deletedMessageByIdHandler(Context ctx) throws SQLException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message deletedMessage = messageService.deletedMessageById(message_id);

        if (deletedMessage != null) {
          ctx.json(deletedMessage);
        } else {
        // Return an empty response body with a status of 200
        ctx.result(""); 
        }
    }

    private void updateMessageById(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        //Get the new message text from the request body
        Message message = mapper.readValue(ctx.body(), Message.class);

        //update the message and retreive the updated message
        Message updatedMessage = messageService.updateMessageById(message_id, message);

        if(updatedMessage == null){
            
            ctx.status(400);
            ctx.result("");
        } else{
            ctx.json(updatedMessage);
        }
    }

    private void getAllMessagesFromUserHandler(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromUser(account_id);

        ctx.json(messages);
    }
    
}
     