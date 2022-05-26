package ATMproject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class sqliteDB {
	Connection connection = null;
	Statement statement = null;
	String jdbcUrl = "jdbc:sqlite://Users/mibo/eclipse-workspace/Projects/bin/ATMproject/account.db";
	sqliteDB(){
		try {
			connection = DriverManager.getConnection(jdbcUrl);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());;
		}
	}
	public void listOfAccounts() {
		try {
			connection = DriverManager.getConnection(jdbcUrl);
			this.statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM users");
			while (result.next()) {
				int id = result.getInt("id");
				String username = result.getString("username");
				String password = result.getString("password");
				BigDecimal cash = result.getBigDecimal("cash");
				
				System.out.println(id + " " + username + " : " + password + " | " + cash);
			}
		} catch (SQLException e) {
			e.getStackTrace();
		}
	}
	public HashMap<String, String> users(){
		try {
			connection = DriverManager.getConnection(jdbcUrl);
			this.statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT username, password FROM users");
			HashMap<String, String> users = new HashMap<>();
			while (result.next()) {
				String name = result.getString("username");
				String pass = result.getString("password");
				users.put(name, pass);
			}
			return users;
		} catch (SQLException e) {
			e.getStackTrace();
		}
		return null;
	}
	public boolean checkUsername(String username) {
		HashMap<String, String> users = users();
		for (String name:users.keySet()) {
			if (name.compareTo(username)==0) {
				return true;
			}
		}
		System.out.println("Username does not exist!");
		return false;
	}
	public boolean checkPassword(String username, String password) {
		HashMap<String, String> users = users();
		if (users.get(username).compareTo(password)==0) {
			return true;
		}
		System.out.println("Incorrect Password!");
		return false;
	}
	public boolean checkDuplicate(String username) {
		HashMap<String, String> users = users();
		for (String name:users.keySet()) {
			if (name.compareTo(username)==0) {
				System.out.println("Username already exists!");
				return false;
			}
		}
		return true;
	}
	public void storing(String username, String password) {
		String sql = "INSERT INTO users(username, password) VALUES(?,?)";

	    try (Connection connection = DriverManager.getConnection(jdbcUrl);
	    	PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, username);
	        pstmt.setString(2, password);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}
	public BigDecimal cash(String username) {
		String sql = "SELECT cash FROM users WHERE username=?";

	    try (Connection connection = DriverManager.getConnection(jdbcUrl);
	    	PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, username);
	        ResultSet result  = pstmt.executeQuery();
	        while (result.next()) {
	        	return result.getBigDecimal("cash");
	        }
	        
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
		return null;
	}
	
	public void updateCash(String username, BigDecimal cash) {
		String sql = "UPDATE users SET cash = ? WHERE username = ?";

	    try (Connection connection = DriverManager.getConnection(jdbcUrl);
	    	PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setBigDecimal(1, cash);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	public void clear() {
		try (Connection connection = DriverManager.getConnection(jdbcUrl);) {
			this.statement = connection.createStatement();
			statement.executeQuery("DELETE FROM users");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void close() throws SQLException {
		connection.close();
	}
}
