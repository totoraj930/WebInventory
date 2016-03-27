package net.totoraj.webinventory.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import net.totoraj.webinventory.DataManager;
import net.totoraj.webinventory.WebInventory;

public class ItemTable {
	private WebInventory plugin;
	private final DataManager datamanager;
	private final String tableName;
	private final MySQL mysql;

	public ItemTable(WebInventory plugin, DataManager datamanager) {
		this.plugin = plugin;
		this.datamanager = datamanager;
		this.tableName = datamanager.getPrefix() + "item";
		this.mysql = datamanager.getMySQL();
	}

	public boolean setUpTable() {
		String columns[] = { "id int auto_increment", "player_id int",
				"slot_number int",
				"item_stack text", "index(id)",
				"FOREIGN KEY (player_id) REFERENCES " + datamanager.getIdTable().getTableName() + "(id)" };
		String cls = String.join(",", columns);
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + cls
				+ ")";

		// テーブル作成SQLをデータベースに投げる
		Statement stmt = null;
		try {
			stmt = mysql.getConnection().createStatement();
			stmt.execute(sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
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

	public boolean setItems (int player_id, HashMap<Integer, String> item_map) {
		String delete_sql = "DELETE FROM "+tableName+" WHERE player_id = "+player_id;
		String insert_sql = "INSERT INTO "+tableName+" (player_id, slot_number, item_stack) VALUES ";
		ArrayList<String> insert_values = new ArrayList<String>();


		try{
			// 削除リクエスト
			Statement delete_stmt = mysql.getConnection().createStatement();
			delete_stmt.execute(delete_sql);

			// 追加がなければ終了
			if (item_map.isEmpty()) {
				return true;
			}
			// 追加リクエスト
			for (int slot_number : item_map.keySet()) {
				insert_values.add("("+player_id+","+slot_number+", ?)");
			}
			PreparedStatement insert_stmt = mysql.getConnection().prepareStatement(insert_sql+String.join(",", insert_values));
			int count = 0;
			for (int key : item_map.keySet()) {
				insert_stmt.setString(++count, item_map.get(key));
			}
			insert_stmt.execute();
			insert_stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public HashMap<Integer, String> getItems2 (int player_id) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		String select_sql = "SELECT * FROM "+tableName+" WHERE player_id = ?;";
		PreparedStatement select_stmt = null;

		// SQL文を投げる
		try {
			select_stmt = mysql.getConnection().prepareStatement(select_sql);
			select_stmt.setInt(1, player_id);
			ResultSet rs = select_stmt.executeQuery();
			while(rs.next()) {
				map.put(rs.getInt("slot_number"), rs.getString("item_stack"));
			}
			select_stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}


}
