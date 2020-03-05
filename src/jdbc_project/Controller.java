package jdbc_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Controller {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hent skuespiller: ");
		
		String skuespiller = scanner.nextLine();
		System.out.println(skuespiller);
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		
    		Statement statement = conn.createStatement();
    		
    		ResultSet rs = statement.executeQuery("select * from Person where Navn=" + "'" + skuespiller + "'");
    		
    		while (rs.next()) {
    			System.out.println(rs.getString("Navn"));
    		}
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
   
    	}
	}
}
