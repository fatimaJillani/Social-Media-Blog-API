import java.sql.Connection;
import java.sql.DriverManager;

public class MessageDAO{
    private Connection connection;

    public MessageDAO(){
        //initialize database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:8080/mydatabase", "username", "password");
    }

    //addMessage

    //updateMessage

    //deletedMessage

    //getMessage

    //getAllMessage


}