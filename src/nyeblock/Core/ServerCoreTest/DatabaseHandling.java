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
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	private Connection connection;
	private long lastConnected = System.currentTimeMillis() / 1000L;
	
	public DatabaseHandling(Main mainInstance, String host, String database, int port, String username, String password) {
		this.host = host;
		this.database = database;
		this.port = port;
		this.username = username;
		this.password = password;
		
		mainInstance.getTimerInstance().createTimer("db_connection_montior", 2, 0, "checkConnectionStatus", false, null, this);
	}
	
	public void checkConnectionStatus() {
		if ((System.currentTimeMillis() / 1000L) - lastConnected > 15) {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			}
		}
	}
	public synchronized ArrayList<HashMap<String,String>> query(String query, int columns, boolean changeData) {
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
		ArrayList<HashMap<String,String>> returnData = new ArrayList<HashMap<String,String>>();
 
		//Attempt to connect
		try {
			//Connection succeeded
			if (connection == null || connection.isClosed()) {				
				connection = DriverManager.getConnection(url, username, password);
				lastConnected = System.currentTimeMillis() / 1000L;
			}
			PreparedStatement statement = connection.prepareStatement(query);
			
			if (changeData) {
				statement.executeUpdate();
			} else {				
				ResultSet data = statement.executeQuery();
				while (data.next()) {
					ResultSetMetaData dataMetaData = data.getMetaData();
					HashMap<String,String> currentData = new HashMap<String,String>();
					
					for (int i = 1; i < columns+1; i++) {
						currentData.put(dataMetaData.getColumnName(i),data.getString(i));
					}
					returnData.add(currentData);
				}
			}
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			//Couldn't connect to the database
		}
		return returnData;
	}
//	public void updateQuery(String query) {
//		Connection conn;
//		String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
// 
//		//Attempt to connect
//		try {
//			//Connection succeeded
//			conn = DriverManager.getConnection(url, username, password);
//			PreparedStatement statement = conn.prepareStatement(query);
//			statement.execute(query);
//		} catch(Exception e) {
//			System.out.println("Error: " + e.getMessage());
//			//Couldn't connect to the database
//		}
//	}
}
