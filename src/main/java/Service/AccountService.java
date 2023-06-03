package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
 
   //state
   private AccountDAO accountDAO;
 
 
   public AccountService(){
         this.accountDAO = new AccountDAO();
   }
 
   public Account createNewAccount(Account account) throws SQLException{
 
     Account existingAccount = accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account. getPassword());
    
     //if account already exists
      if(existingAccount != null){
         return null;
      }
 
      //validate username and password
      if(account.getUsername().isEmpty() || account.getPassword().length() < 4){
         return null;
      }
     
     return accountDAO.createNewAccount(account);

   }
 
   public Account authenticateUser(String username, String password) throws SQLException{
      //return  account back to user after successful registration
      Account user = accountDAO.getAccountByUsernameAndPassword(username, password);
      return user;
   }

   
 
}
