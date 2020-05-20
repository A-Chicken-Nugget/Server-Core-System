package nyeblock.Core.ServerCoreTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandling {
	private Connection connection;
	private String username;
	private String password;
	private String url;
	private long lastQuered = 0;
	
	public DatabaseHandling(Main mainInstance, String host, String database, int port, String username, String password) {
		this.username = username;
		this.password = password;
		
		url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
		mainInstance.getTimerInstance().createMethodTimer("db_connectionMontior", 2, 0, "checkConnectionStatus", false, null, this);
		
		//Check database connection
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			mainInstance.setDatabaseCanConnectStatus(false);
		}
		
		//Create tables in database
		query(
			"CREATE TABLE IF NOT EXISTS users ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "name VARCHAR(16) NOT NULL,"
				+ "points INT DEFAULT 0 NOT NULL,"
				+ "time_played INT DEFAULT 0 NOT NULL,"
				+ "ip VARCHAR(45) NOT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_groups ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "groups TEXT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_ips ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "ip VARCHAR(45) NOT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_bans ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "created INT NOT NULL,"
				+ "length INT NOT NULL,"
				+ "reason TEXT NOT NULL,"
				+ "is_expired BOOLEAN DEFAULT 0 NOT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS ip_bans ("
				+ "id INT AUTO_INCREMENT,"
				+ "ip VARCHAR(45) NOT NULL,"
				+ "created INT NOT NULL,"
				+ "length INT NOT NULL,"
				+ "reason TEXT NOT NULL,"
				+ "is_expired BOOLEAN DEFAULT 0 NOT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_shop_items ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "items TEXT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_stats ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "realm_xp TEXT NULL,"
				+ "games_played TEXT NULL,"
				+ "games_won TEXT NULL,"
				+ "game_kills TEXT NULL,"
				+ "game_deaths TEXT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS user_achievements ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "achievements TEXT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
		query(
			"CREATE TABLE IF NOT EXISTS hub_parkour_times ("
				+ "id INT AUTO_INCREMENT,"
				+ "uniqueId VARCHAR(36) NOT NULL,"
				+ "type BOOLEAN NOT NULL,"
				+ "time INT NOT NULL,"
				+ "PRIMARY KEY (id)"
			+ ") "
		,true);
	}
	
	public void checkConnectionStatus() {	
		if ((System.currentTimeMillis() / 1000L) - lastQuered > 15) {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			}
		}
	}
	public ArrayList<HashMap<String,String>> query(String query, boolean changeData) {
		ArrayList<HashMap<String,String>> returnData = new ArrayList<HashMap<String,String>>();
 
		try {
			if (connection == null || connection.isClosed()) {				
				connection = DriverManager.getConnection(url, username, password);
			}
			PreparedStatement statement = connection.prepareStatement(query);
			lastQuered = System.currentTimeMillis() / 1000L;
			
			if (changeData) {
				statement.executeUpdate();
			} else {				
				ResultSet data = statement.executeQuery();
				
				if (data != null) {					
					while (data.next()) {
						ResultSetMetaData dataMetaData = data.getMetaData();
						
						if (data != null && dataMetaData != null && !data.isClosed()) {
							HashMap<String,String> currentData = new HashMap<String,String>();
							
							for (int i = 1; i < dataMetaData.getColumnCount()+1; i++) {							
								currentData.put(dataMetaData.getColumnName(i),data.getString(i));
							}
							returnData.add(currentData);
						}
					}
				}
			}
		} catch(SQLException e) {
			try {
				if (connection == null || connection.isClosed()) {
					System.out.println("beans");
				} else {
					System.out.println("[Core] Database error:");
					e.printStackTrace();
					System.out.println("[Core] Error occurred with query: " + query);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return returnData;
	}
}
