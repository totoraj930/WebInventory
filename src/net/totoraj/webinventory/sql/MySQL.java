package net.totoraj.webinventory.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	public Connection getConnection () {
		try {
			if (con == null ||con.isClosed() || !con.isValid(10)) {
				con = DriverManager.getConnection("jdbc:mysql://"+Address +":"+Port+"/"+Db, Username, Password);
				con.setAutoCommit(false);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		return con;
	}
	public void close () {
		if (con != null){
			try {
				con.close();
			} catch (SQLException e) {
//				e.printStackTrace();
			}
		}
	}
	public void closeStatement (Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
//				e.printStackTrace();
			}
		}
	}
	public void closeResultSet (ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
//				e.printStackTrace();
			}
		}
	}
}
