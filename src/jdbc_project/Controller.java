package jdbc_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Controller {
	
	public static void getRoleForActor() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Skriv inn skuespilleren som du vil hente alle rollene til:");
		
		String skuespiller = scanner.nextLine();
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select Rolle from SkuespillerIEpisode\r\n" + 
    				"inner join Person on SkuespillerIEpisode.PersonID = Person.PersonID\r\n" + 
    				"where Navn=" + "'" + skuespiller + "';");
    		
    		System.out.println("Skuespiller: " + skuespiller);
    		System.out.println("Rolle(r): ");
    		while (rs.next()) {
    			System.out.println(rs.getString("Rolle"));
    		}
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
    	}
	}
	
	public static void getMovies() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Skriv inn skuespiller: ");
		
		String skuespiller = scanner.nextLine();
		
		try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select Episode.Tittel, SkuespillerIEpisode.PersonID"
    				+ " from SkuespillerIEpisode"
    				+ " inner join Episode on Episode.EpisodeID = SkuespillerIEpisode.EpisodeID"
    				+ " inner join Person on Person.PersonID = SkuespillerIEpisode.PersonID" 
    				+ " where Person.Navn = " + "'" + skuespiller +"'" );
    		
    		System.out.println("Skuespiller "+skuespiller+" spiller i f�lgende filmer: ");
    		while (rs.next()) {
    			System.out.println(rs.getString("Tittel"));
    		}	
    	} 
		catch (Exception exc) {
    		exc.printStackTrace();
    	}
	}
	
	public static void SelskapSjanger() {
		// Henter ut alle utgivelseselskap og hvor mange episoder de har produsert i hver sjanger
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT Utgivelsesselskap.Navn, Sjanger.Navn, COUNT(Sjanger.SjangerID) AS AntallSjanger FROM Sjanger" 
					+ " inner join SjangerIEpisode on SjangerIEpisode.SjangerID = Sjanger.SjangerID"
					+ " inner join Episode on Episode.EpisodeID = SjangerIEpisode.EpisodeID"
					+ " inner join Utgivelsesselskap on Episode.SelskapID = Utgivelsesselskap.SelskapID"
					+ " group by Utgivelsesselskap.Navn, Sjanger.SjangerID"
					+ " order by AntallSjanger DESC;");				
		
			while (rs.next()) {
				System.out.println(rs.getString("Navn") + " har gitt ut " + rs.getInt("AntallSjanger") +" i " + rs.getString("Sjanger.Navn"));				
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	
	}
	
	public static void addActor(int episodeID) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Navn p� skuespillere du vil legge til episode: ");
		String skuespiller = scanner.nextLine();
		System.out.println("Rolle p� skuespilleren: ");
		String rolle = scanner.nextLine();
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select PersonID from Person where Navn="+"'"+skuespiller+"'");
    		
    		int personID = 0;
			if (rs.next()) {
    			personID = rs.getInt("PersonID");
    		}
    		String s =  "insert INTO SkuespillerIEpisode (PersonID, EpisodeID, Rolle) "
    				+ "values (?, ?, ?);";
    		PreparedStatement ps = conn.prepareStatement(s);
    		ps.setInt(1, personID);
    		ps.setInt(2, episodeID);
    		ps.setString(3, rolle);
    		ps.executeUpdate();
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
   
    	}

		
	}
	
	public static void addWriter(int episodeID) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Navn p� manusforfatter du vil legge til episode: ");
		String manusforfatter = scanner.nextLine();
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select PersonID from Person where Navn="+"'"+manusforfatter+"'");
    		
    		int personID = 0;
    		if (rs.next()) {
    			personID = rs.getInt("PersonID");
    		}
    		String s =  "insert INTO ManusforfatterIEpisode (PersonID, EpisodeID) "
    				+ "values (?, ?);";
    		PreparedStatement ps = conn.prepareStatement(s);
    		ps.setInt(1, personID);
    		ps.setInt(2, episodeID);
    		ps.executeUpdate();
    		
    		} catch (Exception exc) {
        		exc.printStackTrace();
        	}
    	} 
		
	
	public static void addProducer(int episodeID) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Vi antar at cast m� ligge inne i databasen fra f�r av");
		System.out.println("Navn p� regiss�r du vil legge til episode: ");
		String regiss�r = scanner.nextLine();
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select PersonID from Person where Navn="+"'"+regiss�r+"'");
    		
    		int personID = 0;
			if (rs.next()) {
    			personID = rs.getInt("PersonID");
    		}
    		String s =  "insert INTO Regiss�rIEpisode (PersonID, EpisodeID) "
    				+ "values (?, ?);";
    		PreparedStatement ps = conn.prepareStatement(s);
    		ps.setInt(1, personID);
    		ps.setInt(2, episodeID);
    		ps.executeUpdate();	
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
   
    	}
	}
	
	public static void createSjangerIEpisodeRelation(int sjangerID, int episodeID) {
		try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		
    		String rs = "insert into SjangerIEpisode values (?,?)";
    		
    		PreparedStatement ps = conn.prepareStatement(rs);
    		ps.setInt(1, episodeID);
    		ps.setInt(2, sjangerID);
    		ps.executeUpdate();	
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
    	}
	}
	
	public static void addEpisode() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Sett inn en ny film! \n"
				+ "EpisodeID (nummer): ");
		int eid = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Tittel: ");
		String tittel = scanner.nextLine();
		System.out.println("Lengde: ");
		int lengde = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Utgivelses�r (yyyy-mm-dd): ");
		String utgivelsesaar = scanner.nextLine();
		System.out.println("Lanseringsdato (yyyy-mm-dd): ");
		String lanseringsdato = scanner.nextLine();
		System.out.println("Storyline: ");
		String storyline = scanner.nextLine();
		System.out.println("Laget for (DVD, streaming ..): ");
		String lagetFor = scanner.nextLine();
		System.out.println("Er det p� video? (1 - JA, 0 - NEI): ");
		int video = scanner.nextInt();
		scanner.nextLine();
		System.out.println("SelskapsID (nummer): ");
		int selskapsID = scanner.nextInt();
		scanner.nextLine();
		System.out.println("SjangerID (nummer): ");
		int sjangerID = scanner.nextInt();
		scanner.nextLine();
		
				
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		
    		String rs =  "insert INTO Episode (EpisodeID, Tittel, Lengde, Utgivelses�r, Lanseringsdato, Storyline, LagetFor, P�Video, SelskapID, SjangerID) "
    				+ "values (?,?,?,?,?,?,?,?,?,?);";
    		
    		PreparedStatement ps = conn.prepareStatement(rs);
    		
    		ps.setInt(1, eid);
    		ps.setString(2, tittel);
    		ps.setInt(3, lengde);
    		ps.setString(4, utgivelsesaar);
    		ps.setString(5, lanseringsdato);
    		ps.setString(6, storyline);
    		ps.setString(7, lagetFor);
    		ps.setInt(8, video);
    		ps.setInt(9, selskapsID);
    		ps.setInt(10, sjangerID);
    		    		
    		ps.executeUpdate();
    		
    		addProducer(eid);
    		addWriter(eid);
    		addActor(eid);
    		createSjangerIEpisodeRelation(sjangerID, eid);
    		
    		System.out.println("Du har oppretttet en ny film som heter "+tittel+"!");

    	} catch (Exception exc) {
    		exc.printStackTrace();
    	}
    	
	}
	
	public static void addReviewEpisodeInSeries() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hva er brukerID din? ");
		int brukerid = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Hvilken episode i serie vil du skrive anmeldelse (navn p� episode)?");
		String episode = scanner.nextLine();
		
		System.out.println("Skriv din anmeldelse her: ");
		String anmeldelse = scanner.nextLine();
		
		System.out.println("Rating (1-10):");
		int rating = scanner.nextInt();
		
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_prosjekt", "root", "root");
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select EpisodeISerie.EpisodeID from EpisodeISerie\r\n" + 
    				"inner join Episode on Episode.EpisodeID = EpisodeISerie.EpisodeID where Tittel="+ "'" + episode+"'");
    		
    		int episodeID = 0;
    		if (rs.next()) {
    			episodeID = rs.getInt("EpisodeID");
    		}    		
    		String s = "insert into AnmeldelseEpisode (BrukerID, EpisodeID, Tekst, Rating) "
    				+ "values (?,?,?,?)";
    		
    		PreparedStatement ps = conn.prepareStatement(s);
    		ps.setInt(1, brukerid);
    		ps.setInt(2, episodeID);
    		ps.setString(3, anmeldelse);
    		ps.setInt(4, rating);
    		ps.executeUpdate();
    		
    		System.out.println("\n Du har satt inn anmeldelse for f�lgende film: "+episode);
    		
    	} catch (Exception exc) {
    		exc.printStackTrace();
   
    	}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Tast inn 1, 2, 3, 4 eller 5 ut i fra hva du vil gj�re.\n"
				+ "1. Finne navnet p� alle rollene en gitt skuespiller har \n"
				+ "2. Finne hvilke filmer som en gitt skuespiller opptrer i \n"
				+ "3. Finne hvilke filmselskap som lager flest filmer inne hver sjanger (gr�ssere, familie, o.l.) \n"
				+ "4. Sette inn en ny film med regiss�r, manusforfattere, skuespillere og det som h�rer med \n"
				+ "5. Sette inn ny anmeldelse av en episode av en serie");
		
		int nr = scanner.nextInt();
		
		if (nr == 1) {
			getRoleForActor();
		} else if (nr == 2) {
			getMovies();
		} else if (nr == 3) {
			SelskapSjanger();
		} else if (nr == 4) {
			addEpisode();
		} else if (nr == 5) {
			addReviewEpisodeInSeries();
		} else {
			System.out.println("Nummeret er ikke gyldig.");
		}
		
	}

}
