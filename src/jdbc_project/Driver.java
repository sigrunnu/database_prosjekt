package jdbc_project;

import java.sql.*;

public class Driver {
	
	public static void main(String[] args) {
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		
    		Statement statement = conn.createStatement();
    		
    		ResultSet rs = statement.executeQuery("select * from Episode");
    		
    		while (rs.next()) {
    			System.out.println(rs.getString("Tittel"));
    		}
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
   
    	}
	}
}
