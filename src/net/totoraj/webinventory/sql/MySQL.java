package net.totoraj.webinventory.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
	private final String Address, Username, Password, Db;
	private final int Port;
	private Connection con;

	public MySQL(String Address, int Port, String Username, String Password, String Db) {
		this.Address = Address;
		this.Port = Port;
		this.Username = Username;
		this.Password = Password;
		this.Db = Db;
	}


	// 接続
	public boolean connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + Address+ ":"+Port+"/" + Db+ "?autoReconnect=true",
					Username, Password);
			return true;
		} catch (SQLException e) {
			System.out.println("[Coins MySQL] The connection to MySQL couldn't be made! reason: " + e.getMessage());
		}
		return false;
	}

	// 切断
	public boolean close() {
		try {
			if (con != null) {
				con.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public Connection getConnection () {
		return con;
	}
}
