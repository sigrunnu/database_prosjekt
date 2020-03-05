package jdbc_project;

import java.sql.*;
import java.util.Properties;

public class DBconn {
	
    protected Connection conn;
    public DBconn () {
    }
    public void connect() {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");   
            // Properties for user and password
            Properties p = new Properties();
            p.put("user", "root");
            p.put("password", "root");           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", p);
        } catch (Exception e)
    	{
            throw new RuntimeException("Unable to connect", e);
    	}
    }

}
