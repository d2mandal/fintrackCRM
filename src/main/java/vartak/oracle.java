package vartak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Statement;


public class oracle {

    public static void main(String[] args) 
    {
        String url="jdbc:oracle:thin:@localhost:1521";
        String user="system";
        String pass="1234";
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver is loaded successfully");
            Connection con=DriverManager.getConnection(url,user,pass);
            System.out.println("Connection is successfully with databse"+" "+con);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from stud");


            while(rs.next()){
                System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3));
            }
        }
        catch(ClassNotFoundException e){
            System.out.println("Driver is not loaded successfully");
        }
        catch(SQLException e){
            System.out.println("Connection is not successfully with database");

        }

       
    }
    
}
