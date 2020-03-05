package jdbc_project;

import java.sql.*;

public class Person extends ActiveDomainObject {
    private int pid;
    private String navn;
    private String rolle;
    private String faar;
    private String fland; 
	
	public Person (int pid) {
        this.pid = pid;
    }
	
	public int getPid() {
		return this.pid;
	}
	
	public void initialize (Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select Rolle from SkuespillerIEpisode where pid =" + pid);
			while (rs.next()) {
				pid = rs.getInt("PersonID");
				rolle = rs.getString("Rolle");
			}
		} catch (Exception e) {
			System.out.println("db error during select of person=" + e);
			return;
		}
	}

	public void refresh(Connection conn) {
		initialize (conn);
		
	}

	public void save(Connection conn) {
		try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery("update Person set fødselsår="+faar+", fødselsland="+fland+"where pid=" +pid);
        } catch (Exception e) {
            System.out.println("db error during update of person="+e);
            return;
        }
	}
	
	public static void main(String[] args) {
		Person person = new Person(1);
		System.out.println(person.getPid());
	}
	
}
