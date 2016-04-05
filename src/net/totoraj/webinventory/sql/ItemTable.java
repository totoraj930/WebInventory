package net.totoraj.webinventory.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.totoraj.webinventory.DataManager;

public class ItemTable {
	private final DataManager datamanager;
	private final String tableName;
	private final MySQL mysql;

	public ItemTable(DataManager datamanager) {
		this.datamanager = datamanager;
		this.tableName = datamanager.getPrefix() + "item";
		this.mysql = datamanager.getMySQL();
	}

	public boolean setUpTable() {
//		String columns[] = { "id int auto_increment", "player_id int",
//				"slot_number int",
//				"item_stack text", "index(id)",
//				"FOREIGN KEY (player_id) REFERENCES " + datamanager.getIdTable().getTableName() + "(id)" };
//		String cls = String.join(",", columns);
//		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + cls
//				+ ")";

		String sql = ""
				+"\n"+"CREATE TABLE IF NOT EXISTS "+tableName+" ("
				+"\n"+"id int AUTO_INCREMENT,"
				+"\n"+"player_id int,"
				+"\n"+"slot_number int,"
				+"\n"+"item_stack text,"
				+"\n"+"FOREIGN KEY (player_id)"
				+"\n"+"REFERENCES "+datamanager.getIdTable().getTableName()+"(id),"
				+"\n"+"PRIMARY KEY (player_id, slot_number),"
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
			stmt = mysql.getConnection().createStatement();
			stmt.executeUpdate(sql);
			con.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		} finally {
			mysql.closeStatement(stmt);
		}
		return true;
	}


	// プレイヤー用のレコードを用意する
	public boolean setUpPlayerItemRecords (int player_id) {
		String select_sql = "SELECT * FROM "+tableName+" WHERE player_id = "+player_id;

		Connection con = null;
		Statement select_stmt = null;
		Statement insert_stmt = null;
		ResultSet rs = null;
		try {
			con = mysql.getConnection();
			if (con == null) {
				return false;
			}
			select_stmt = con.createStatement();
			rs = select_stmt.executeQuery(select_sql);
			// 1つでもレコードが存在したら何もせず終了
			while (rs.next()) {
				rs.close();
				select_stmt.close();
				return true;
			}

			// INSERTする内容を作成
			String insert_sql = "INSERT INTO "+tableName+" (player_id, slot_number, item_stack) VALUES ";
			ArrayList<String> insert_values = new ArrayList<String>();
			for (int i=0; i < 54; i++) {
				String temp = "";
				temp += "("+player_id+","+i+",NULL)";
				insert_values.add(temp);
			}
			insert_sql += String.join(",", insert_values);

			// データベースにINSERTを投げる
			insert_stmt = con.createStatement();
			insert_stmt.executeUpdate(insert_sql);
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			mysql.closeResultSet(rs);
			mysql.closeStatement(select_stmt);
			mysql.closeStatement(insert_stmt);
		}
		return true;

	}

	// データベースのアイテムレコードを更新する
	public boolean updateItemRecords (int player_id, String[] item_stacks) {
		if (item_stacks.length != 54) {
			return false;
		}
		String update_sql = "";
		String update_sql_before = "UPDATE "+tableName+" SET item_stack = CASE";
		String update_sql_after = " ELSE item_stack END WHERE player_id = "+player_id;
		String update_values = "";

		for (int i=0; i < 54; i++) {
			String temp = "\n";
			temp += "WHEN slot_number = "+i+" THEN ?";
			update_values += temp;
		}
		update_sql = update_sql_before + update_values + update_sql_after;


		Connection con = null;
		PreparedStatement update_stmt = null;
		try {
			con = mysql.getConnection();
			if (con == null) {
				return false;
			}
			update_stmt = con.prepareStatement(update_sql);

			// SQL文のitem_stackをセット
			for (int i=0; i < 54; i++) {
				if (item_stacks[i] == null) {
					update_stmt.setNull(i+1, java.sql.Types.NULL);
				}
				else {
					update_stmt.setString(i+1, item_stacks[i]);
				}
			}

			update_stmt.execute();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			mysql.closeStatement(update_stmt);
		}

		return true;
	}

	// データベースのアイテムレコードを取得する
	public String[] getItemRecords (int player_id) {
		String item_stacks[] = new String[54];
		String select_sql = "SELECT * FROM "+tableName
				+" WHERE player_id = "+player_id
				+" ORDER BY slot_number"
				+" LIMIT 54";
		Statement select_stmt = null;
		ResultSet rs = null;
		try {
			select_stmt = mysql.getConnection().createStatement();
			rs = select_stmt.executeQuery(select_sql);

			// 取得したアイテムを配列にセット
			int count = 0;
			while (rs.next()) {
				item_stacks[count] = rs.getString("item_stack");
				count++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			mysql.closeResultSet(rs);
			mysql.closeStatement(select_stmt);
		}

		return item_stacks;
	}

}
