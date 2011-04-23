import java.sql.*;
import java.util.*;

public class Database {
	
	public Connection connect;
	
	public Database(String s){
		try{
			String url = "jdbc:odbc:"+s;
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connect = DriverManager.getConnection(url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Database(Connection c){
		connect = c;
	}
	/*
	public void clear(){
		System.out.println("Clear all CDs");
		try{
			Statement clearCDs = connect.createStatement();
			String sql = "DELETE FROM CDs;";
			//System.out.println("Executing statement: " + sql);
			clearCDs.executeUpdate(sql);
			clearCDs.close();
			//System.out.println("CDs cleared successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public void insertCD(CD cd){
		System.out.println("Adding CD: " + cd.toString());
		try{
			Statement addCD = connect.createStatement();
			String sql = "INSERT INTO CDs (CD_id,title,artist) VALUES("
					+ cd.getNumber() + ", "
					+ "'" + cd.getTitle() + "', "
					+ "'" + cd.getArtist() + "')";

			//System.out.println("Executing statement: " + sql);
			addCD.executeUpdate(sql);
			addCD.close();
			//System.out.println("CD added successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public void removeCD(CD cd){
		System.out.println("Removing CD: " + cd.toString());
		try{
			Statement deleteCD = connect.createStatement();
			String sql = "DELETE FROM CDs WHERE CD_id = "+cd.getNumber()+";";

			//System.out.println("Executing statement: " + sql);
			deleteCD.executeUpdate(sql);
			deleteCD.close();
			//System.out.println("CD removed successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	public Vector<CD> findAll(){
		Vector<CD> cds = new Vector<CD>();
		try{
			Statement selectAll = connect.createStatement();
			String sql = "SELECT * FROM CDs ";
			ResultSet results = selectAll.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				cds.add(cd);
			}
			results.close();
			selectAll.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}
	
	public Vector<CD> findByTitle(String s){
		System.out.println("Searching for title: '" + s+"'");
		Vector<CD> cds = new Vector<CD>();
		try{
			Statement selectByTitle = connect.createStatement();
			String sql = "SELECT * FROM CDs WHERE title LIKE '%" +s+ "%' " 
						+ "OR title LIKE '" +s+ "%' "
						+ "OR title LIKE '%" +s+ "' ";
			ResultSet results = selectByTitle.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				System.out.println("CD found: "+cd.toString());
				cds.add(cd);
			}
			results.close();
			selectByTitle.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}
	
	public Vector<CD> findByArtist(String s){
		System.out.println("Searching for artist: '" + s+"'");
		Vector<CD> cds = new Vector<CD>();
		try{
			Statement selectByArtist = connect.createStatement();
			String sql = "SELECT * FROM CDs WHERE artist LIKE '%" +s+ "%' " 
						+ "OR artist LIKE '" +s+ "%' "
						+ "OR artist LIKE '%" +s+ "' ";
			ResultSet results = selectByArtist.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				System.out.println("CD found: "+cd.toString());
				cds.add(cd);
			}
			results.close();
			selectByArtist.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}	
	*/
}