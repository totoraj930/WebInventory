package net.totoraj.webinventory.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import net.totoraj.webinventory.DataManager;

public class IdTable {
	private final DataManager datamanager;
	private final String tableName;
	private final MySQL mysql;

	public IdTable(DataManager datamanager) {
		this.datamanager = datamanager;
		this.tableName = datamanager.getPrefix() + "id";
		this.mysql = datamanager.getMySQL();
	}

	public boolean setUpTable() {
//		String columns[] = { "id int auto_increment", "name char(16)",
//				"uuid varchar(36) unique", "index(id)" };
//		String cls = String.join(",", columns);
//		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + cls
//				+ ")";

		String sql = ""
				+"\n"+"CREATE TABLE IF NOT EXISTS "+tableName+" ("
				+"\n"+"id int AUTO_INCREMENT,"
				+"\n"+"name char(16) NOT NULL,"
				+"\n"+"uuid varchar(36) NOT NULL UNIQUE,"
				+"\n"+"PRIMARY KEY(id),"
				+"\n"+"INDEX(id)"
				+"\n"+");";

		// テーブル作成SQLをデータベースに投げる
		Connection con = null;
		Statement stmt = null;
		try {
			con = mysql.getConnection();
			if (con == null) {
				return false;
			}
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			mysql.closeStatement(stmt);
		}
		return true;
	}

	// 指定されたuuidのプレイヤーのデータベースのidを返す
	// 存在しなければ0を返す
	public int getPlayerIdFromUUID (UUID uuid) {
		int id = 0;
		String sql = "SELECT * FROM "+tableName
				+" WHERE uuid = '"+uuid.toString()+"'";

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			con = mysql.getConnection();
			if (con == null) {
				return 0;
			}
			stmt = mysql.getConnection().createStatement();
			rset = stmt.executeQuery(sql);
			while (rset.next()) {
				id = rset.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.closeResultSet(rset);
			mysql.closeStatement(stmt);
		}

		return id;
	}

	// 指定されたnameのプレイヤーのデータベースのidを返す
	// 存在しなければ0を返す
	public int getPlayerIdFromName (String playerName) {
		int id = 0;
		String sql = "SELECT * FROM "+tableName
				+" WHERE uuid = '"+playerName+"'";
		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;
		// SQL文を投げる
		try {
			con = mysql.getConnection();
			stmt = con.createStatement();
			rset = stmt.executeQuery(sql);
			while (rset.next()) {
				id = rset.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.closeStatement(stmt);
			mysql.closeResultSet(rset);
		}

		return id;
	}

	// 指定されたuuidとnameをデータベースに追加する
	public boolean addPlayer (UUID uuid, String playerName) {
		Connection con = null;
		Statement stmt = null;
		String sql = null;
		int id = getPlayerIdFromUUID(uuid);
		boolean isInsert = false;
		// 同じuuidがなければINSERT
		if (id == 0) {
			sql = "INSERT INTO "+tableName
					+" (name, uuid) VALUES ("
					+"'"+playerName+"','"+uuid.toString()+"')";
			isInsert = true;
		}
		// 同じuuidがあればUPDATE
		else {
			sql = "UPDATE "+tableName+" SET"
					+" name = '"+playerName+"'"
					+" WHERE id = "+id;
		}

		// SQL文を投げる
		try {
			con = mysql.getConnection();
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			mysql.closeStatement(stmt);
		}


		// 新規追加だったらアイテムレコードを用意する
		if (isInsert) {
			id = this.getPlayerIdFromUUID(uuid);
			if (id != 0) {
				datamanager.getItemTable().setUpPlayerItemRecords(id);
			}
		}
		return true;
	}

	public String getTableName () {
		return tableName;
	}
}
