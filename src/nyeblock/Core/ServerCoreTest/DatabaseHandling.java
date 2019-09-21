package nyeblock.Core.ServerCoreTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandling {
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	
	public DatabaseHandling(String host, String database, int port, String username, String password) {
		this.host = host;
		this.database = database;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public ArrayList<HashMap<String,String>> selectQuery(String query, int columns) {
		Connection conn;
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
		ArrayList<HashMap<String,String>> returnData = new ArrayList<HashMap<String,String>>();
 
		//Attempt to connect
		try {
			//Connection succeeded
			conn = DriverManager.getConnection(url, username, password);
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet data = statement.executeQuery();
			ResultSetMetaData dataMetaData = data.getMetaData();
			while (data.next()) {
				HashMap<String,String> currentData = new HashMap<String,String>();
				
				for (int i = 1; i == columns; i++) {
					currentData.put(dataMetaData.getColumnName(i),data.getString(i));
				}
				returnData.add(currentData);
			}
			
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			//Couldn't connect to the database
		}
		return returnData;
	}
	public void updateQuery(String query) {
		Connection conn;
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
 
		//Attempt to connect
		try {
			//Connection succeeded
			conn = DriverManager.getConnection(url, username, password);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.execute(query);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			//Couldn't connect to the database
		}
	}
}
