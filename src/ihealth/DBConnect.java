package ihealth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class DBConnect
{
    public static Connection conn = null;
    String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    String serverPath = "jdbc:mysql://localhost/java_project";
    
    public void Connection()
    {
        
    }
    
    
    public static Connection getConnection()throws ClassNotFoundException,SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/java_project","root", "");
            
       
        return conn;
    }
    
}
