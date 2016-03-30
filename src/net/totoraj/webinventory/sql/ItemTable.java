package net.totoraj.webinventory.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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

		String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
				+"id int AUTO_INCREMENT,\n"
				+"player_id int,\n"
				+"slot_number int,\n"
				+"item_stack text,\n"
				+"FOREIGN KEY (player_id)"
				+" REFERENCES "+datamanager.getIdTable().getTableName()+"(id),\n"
				+"PRIMARY KEY (player_id, slot_number),\n"
				+"INDEX(id)\n"
				+")";

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


	// プレイヤー用のレコードを用意する
	public void setUpPlayerItemRecords (int player_id) {
		String select_sql = "SELECT * FROM "+tableName+" WHERE player_id = "+player_id;
		try {
			Statement select_stmt = mysql.getConnection().createStatement();
			ResultSet rs = select_stmt.executeQuery(select_sql);
			// 1つでもレコードが存在したら何もせず終了
			while (rs.next()) {
				rs.close();
				select_stmt.close();
				return;
			}
			rs.close();
			select_stmt.close();

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
			Statement insert_stmt = mysql.getConnection().createStatement();
			insert_stmt.execute(insert_sql);

			insert_stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

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
		try {
			PreparedStatement update_stmt = mysql.getConnection().prepareStatement(update_sql);

			// SQL文のitem_stackをセット
			for (int i=0; i < 54; i++) {
				if (item_stacks[i] == null) {
					update_stmt.setNull(i+1, java.sql.Types.NULL);
				}
				else {
					update_stmt.setString(i+1, item_stacks[i]);
				}
			}
			// 送信
			update_stmt.execute();
			// 閉じる
			update_stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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

		try {
			Statement select_stmt = mysql.getConnection().createStatement();
			ResultSet rs = select_stmt.executeQuery(select_sql);

			// 取得したアイテムを配列にセット
			int count = 0;
			while (rs.next()) {
				item_stacks[count] = rs.getString("item_stack");
				count++;
			}

			// 閉じる
			rs.close();
			select_stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return item_stacks;
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
