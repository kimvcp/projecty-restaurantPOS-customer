package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Menu;
import util.PropertyManager;

/**
 * DBManager contains method for managing data on database. Also associate with
 * classes that use the database. With help from BCrypt all password are
 * encrypted.
 * 
 * @author Piyawat & Vichaphol
 *
 */
public class DBManager {
	private static PropertyManager pm = PropertyManager.getInstance();
	private static DBManager instance;
	private Connection connection;
	private String DB_URL = pm.getProperty("database.url");
	private String USER = pm.getProperty("database.user");
	private String PASS = pm.getProperty("database.password");
	private String sqlCommand;

	/**
	 * Private constructor for DBManger. Getting the connection from the database.
	 */
	private DBManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting the instance of DBManager with the condition if the
	 * instance is null, create new instance.
	 * 
	 * @return instance of the DBManager
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	// during in test
	public List<String> getFoodUrl(String table) {
		List<String> temp = new ArrayList<>();
		sqlCommand = "SELECT * FROM " + table;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String text = rs.getString("url");
				temp.add(text);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/**
	 * Method for getting data from the database which are food names and prices to
	 * create a Menu object.
	 * 
	 * @param tablename
	 * @return List<Menu> of food names
	 */
	public List<Menu> getFoodname(String foodkind) {
		List<Menu> temp = new ArrayList<>();
		sqlCommand = "SELECT * FROM " + foodkind;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String text = rs.getString("name");
				int price = rs.getInt("price");
				Menu mn = new Menu(text, price);
				temp.add(mn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/**
	 * Method for checking table existence in database.
	 * 
	 * @param table
	 *            number
	 * @return true if table exist, false if not
	 */
	public boolean checkTable(String tableNumber) {
		DatabaseMetaData dbm = null;
		try {
			dbm = connection.getMetaData();
			ResultSet table = dbm.getTables(null, null, tableNumber, null);
			if (table.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Method for inserting current orders into the requested table in database.
	 * 
	 * @param tablenumber
	 * @param Map<Menu,Integer>
	 *            of orders
	 */
	public void orderToDB(String tableNumber, Map<Menu, Integer> map) {
		String tabletmp = "table" + tableNumber;
		sqlCommand = "INSERT INTO `" + tabletmp + "` (`name`, `price`, `quantity`) VALUES (?, ?, ?)";
		PreparedStatement stmt = null;
		try {
			for (Map.Entry<Menu, Integer> order : map.entrySet()) {
				stmt = connection.prepareStatement(sqlCommand);
				String name = order.getKey().getName();
				int price = order.getKey().getPrice();
				int qty = order.getValue();
				stmt.setString(1, name);
				stmt.setInt(2, price);
				stmt.setInt(3, qty);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method for getting all orders and from the wanted table in database and
	 * collect them as Map<Menu,Integer>.
	 * 
	 * @param tableNumber
	 * @return Map<Menu,Integer> of orders
	 */
	public Map<Menu, Integer> getDBOrders(String tableNumber) {
		Map<Menu, Integer> temp = new LinkedHashMap<>();
		String tabletmp = "table" + tableNumber;
		sqlCommand = "SELECT * FROM " + tabletmp;
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sqlCommand);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int qty = rs.getInt("quantity");
				Menu menu = new Menu(name, price);
				if (!temp.containsKey(menu)) {
					temp.put(menu, qty);
				} else {
					temp.put(menu, temp.get(menu) + qty);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

}
