package net.totoraj.webinventory.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import net.totoraj.webinventory.DataManager;
import net.totoraj.webinventory.WebInventory;

public class IdTable {
	private WebInventory plugin;
	private final DataManager datamanager;
	private final String tableName;
	private final MySQL mysql;

	public IdTable(WebInventory plugin, DataManager datamanager) {
		this.plugin = plugin;
		this.datamanager = datamanager;
		this.tableName = datamanager.getPrefix() + "id";
		this.mysql = datamanager.getMySQL();
	}

	public boolean setUpTable() {
		String columns[] = { "id int auto_increment", "name char(16)",
				"uuid varchar(36) unique", "index(id)" };
		String cls = String.join(",", columns);
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + cls
				+ ")";

		// テーブル作成SQLをデータベースに投げる
		Statement stmt = null;
		try {
			stmt = mysql.getConnection().createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}

	// 指定されたuuidのプレイヤーのデータベースのidを返す
	// 存在しなければ0を返す
	public int getPlayerIdFromUUID (UUID uuid) {
		Statement stmt = null;
		ResultSet rset = null;
		int id = 0;
		String sql = "SELECT * FROM "+tableName
				+" WHERE uuid = '"+uuid.toString()+"'";
		try {
			stmt = mysql.getConnection().createStatement();
			rset = stmt.executeQuery(sql);
			while (rset.next()) {
				id = rset.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}

		return id;
	}

	// 指定されたnameのプレイヤーのデータベースのidを返す
	// 存在しなければ0を返す
	public int getPlayerIdFromName (String playerName) {
		Statement stmt = null;
		ResultSet rset = null;
		int id = 0;
		String sql = "SELECT * FROM "+tableName
				+" WHERE uuid = '"+playerName+"'";

		// SQL文を投げる
		try {
			stmt = mysql.getConnection().createStatement();
			rset = stmt.executeQuery(sql);
			while (rset.next()) {
				id = rset.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}

		return id;
	}

	// 指定されたuuidとnameをデータベースに追加する
	public boolean addPlayer (UUID uuid, String playerName) {
		Statement stmt = null;
		String sql = null;
		int id = getPlayerIdFromUUID(uuid);
		// 同じuuidがなければINSERT
		if (id == 0) {
			sql = "INSERT INTO "+tableName
					+" (name, uuid) VALUES ("
					+"'"+playerName+"','"+uuid.toString()+"')";
		}
		// 同じuuidがあればUPDATE
		else {
			sql = "UPDATE "+tableName+" SET"
					+" name = '"+playerName+"'"
					+" WHERE id = "+id;
		}

		// SQL文を投げる
		try {
			stmt = mysql.getConnection().createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		return true;
	}

	public String getTableName () {
		return tableName;
	}
}
