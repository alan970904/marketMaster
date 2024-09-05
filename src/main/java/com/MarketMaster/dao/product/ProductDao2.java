package com.MarketMaster.dao.product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.MarketMaster.bean.product.ProductBean;

public class ProductDao2 {
	static Connection connection;
	static PreparedStatement stmt;
	static ResultSet rs;
	// 連線
	static final String url = "jdbc:sqlserver://localhost:1433;DatabaseName=ispan;encrypt=false";
	static final String user = "Java05";
	static final String password = "0000";
	// 查
	static final String getOneSql = "SELECT * FROM products WHERE product_id = ?";
	static final String getLikeSql = "SELECT * FROM products WHERE product_name LIKE";
	static final String getAllSql = "SELECT * FROM products";
	static final String getPageSql = "SELECT * FROM products ORDER BY product_id OFFSET ? ROWS FETCH NEXT ? ROWS only";
	// 刪
	static final String deleteSql = "DELETE FROM products WHERE product_id=?";
	// 增
	static final String insertSql = "INSERT INTO products (product_id, product_category, product_name, product_price, product_safeinventory, Number_of_shelve, Number_of_inventory, Number_of_sale, Number_of_exchange, Number_of_destruction, Number_of_remove)\r\n"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	// 改
	static final String updateSql = "UPDATE products SET product_id=?, product_category=?, product_name=?, product_price=?, product_safeinventory=?, Number_of_shelve=?, Number_of_inventory=?, Number_of_sale=?, Number_of_exchange=?, Number_of_destruction=?, Number_of_remove=? WHERE product_id=?";
	static final String shelveSql = "UPDATE products SET  Number_of_inventory=Number_of_inventory -?,Number_of_shelve=Number_of_shelve +?WHERE product_id=?";
	static final String removeSql = "UPDATE products SET Number_of_remove = Number_of_inventory + Number_of_shelve + Number_of_remove, Number_of_inventory = 0, Number_of_shelve = 0 WHERE product_id = ?";

	static final String updateSaleByCheckOut = "IF EXISTS (\r\n" + "    SELECT 1\r\n" + "    FROM products p\r\n"
			+ "    LEFT JOIN (\r\n" + "        SELECT product_id, SUM(number_of_checkout) as total_checkout\r\n"
			+ "        FROM checkout_details\r\n" + "        GROUP BY product_id\r\n"
			+ "    ) cd ON p.product_id = cd.product_id\r\n"
			+ "    WHERE COALESCE(cd.total_checkout, 0) != p.Number_of_sale\r\n" + ")\r\n" + "BEGIN\r\n"
			+ "   UPDATE p\r\n" + "SET p.Number_of_shelve=p.Number_of_shelve-(newcd.newsale-p.Number_of_sale),\r\n"
			+ "	p.Number_of_sale=newcd.newsale\r\n" + "FROM products p INNER JOIN\r\n"
			+ "(SELECT cd.product_id ,SUM(cd.number_of_checkout) newsale\r\n" + "FROM checkout_details cd\r\n"
			+ "GROUP BY cd.product_id) newcd\r\n" + "ON p.product_id=newcd.product_id\r\n" + "END";

	public static ProductBean getOne(String productId) {
		ProductBean product = new ProductBean();
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(getOneSql);
			stmt.setString(1, productId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				product.setProductId(rs.getString("product_id"));
				product.setProductCategory(rs.getString("product_category"));
				product.setProductName(rs.getString("product_name"));
				product.setProductPrice(rs.getInt("product_price"));
				product.setproductSafeInventory(rs.getInt("product_safeinventory"));
				product.setNumberOfShelve(rs.getInt("Number_of_shelve"));
				product.setNumberOfInventory(rs.getInt("Number_of_inventory"));
				product.setNumberOfSale(rs.getInt("Number_of_sale"));
				product.setNumberOfExchange(rs.getInt("Number_of_exchange"));
				product.setNumberOfDestruction(rs.getInt("Number_of_destruction"));
				product.setNumberOfRemove(rs.getInt("Number_of_remove"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return product;
	}

	public static List<ProductBean> getLike(String productName) {
		List<ProductBean> products = new ArrayList<ProductBean>();
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			StringBuilder getLikeSqlStr = new StringBuilder(getLikeSql);
			stmt = connection.prepareStatement(getLikeSqlStr.append(" '%" + productName + "%'").toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProductBean product = new ProductBean();
				product.setProductId(rs.getString("product_id"));
				product.setProductCategory(rs.getString("product_category"));
				product.setProductName(rs.getString("product_name"));
				product.setProductPrice(rs.getInt("product_price"));
				product.setproductSafeInventory(rs.getInt("product_safeinventory"));
				product.setNumberOfShelve(rs.getInt("Number_of_shelve"));
				product.setNumberOfInventory(rs.getInt("Number_of_inventory"));
				product.setNumberOfSale(rs.getInt("Number_of_sale"));
				product.setNumberOfExchange(rs.getInt("Number_of_exchange"));
				product.setNumberOfDestruction(rs.getInt("Number_of_destruction"));
				product.setNumberOfRemove(rs.getInt("Number_of_remove"));

				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return products;
	}

	public static List<ProductBean> getLikeProducts(String productName, int pageIndex, int pageSize) {
		List<ProductBean> products = new ArrayList<>();
		int offset = (pageIndex - 1) * pageSize;

		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			String sql = "SELECT * FROM products WHERE product_name LIKE ? ORDER BY product_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, " %" + productName + "%");
			stmt.setInt(2, offset);
			stmt.setInt(3, pageSize);
			rs = stmt.executeQuery();

			while (rs.next()) {
				ProductBean product = new ProductBean();
				product.setProductId(rs.getString("product_id"));
				product.setProductCategory(rs.getString("product_category"));
				product.setProductName(rs.getString("product_name"));
				product.setProductPrice(rs.getInt("product_price"));
				product.setproductSafeInventory(rs.getInt("product_safeinventory"));
				product.setNumberOfShelve(rs.getInt("Number_of_shelve"));
				product.setNumberOfInventory(rs.getInt("Number_of_inventory"));
				product.setNumberOfSale(rs.getInt("Number_of_sale"));
				product.setNumberOfExchange(rs.getInt("Number_of_exchange"));
				product.setNumberOfDestruction(rs.getInt("Number_of_destruction"));
				product.setNumberOfRemove(rs.getInt("Number_of_remove"));

				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return products;
	}

	public static int getTotalLikeProducts(String productName) {
		int total = 0;
		try {
			total = 0;
			String sql = "SELECT COUNT(*) FROM products WHERE product_name LIKE ?";
			
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + productName + "%");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return total;
	}

	public static List<ProductBean> getAll() {
		List<ProductBean> products = new ArrayList<ProductBean>();

		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(getAllSql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProductBean product = new ProductBean();
				product.setProductId(rs.getString("product_id"));
				product.setProductCategory(rs.getString("product_category"));
				product.setProductName(rs.getString("product_name"));
				product.setProductPrice(rs.getInt("product_price"));
				product.setproductSafeInventory(rs.getInt("product_safeinventory"));
				product.setNumberOfShelve(rs.getInt("Number_of_shelve"));
				product.setNumberOfInventory(rs.getInt("Number_of_inventory"));
				product.setNumberOfSale(rs.getInt("Number_of_sale"));
				product.setNumberOfExchange(rs.getInt("Number_of_exchange"));
				product.setNumberOfDestruction(rs.getInt("Number_of_destruction"));
				product.setNumberOfRemove(rs.getInt("Number_of_remove"));

				products.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return products;
	}

	public static List<ProductBean> getPagesProducts(int pageIndex, int pageSize) {
		List<ProductBean> products = new ArrayList<ProductBean>();

		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(getPageSql);
			int pageIndexCalc = (pageIndex - 1) * pageSize;
			stmt.setInt(1, pageIndexCalc);
			stmt.setInt(2, pageSize);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProductBean product = new ProductBean();
				product.setProductId(rs.getString("product_id"));
				product.setProductCategory(rs.getString("product_category"));
				product.setProductName(rs.getString("product_name"));
				product.setProductPrice(rs.getInt("product_price"));
				product.setproductSafeInventory(rs.getInt("product_safeinventory"));
				product.setNumberOfShelve(rs.getInt("Number_of_shelve"));
				product.setNumberOfInventory(rs.getInt("Number_of_inventory"));
				product.setNumberOfSale(rs.getInt("Number_of_sale"));
				product.setNumberOfExchange(rs.getInt("Number_of_exchange"));
				product.setNumberOfDestruction(rs.getInt("Number_of_destruction"));
				product.setNumberOfRemove(rs.getInt("Number_of_remove"));

				products.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return products;
	}

	public static int getTotalProducts() {
		int total = 0;
		try {
			total = 0;
			String sql = "SELECT COUNT(*) FROM products";

			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return total;
	}

	public static boolean insert(ProductBean product) throws SQLException {

		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
		
			stmt = connection.prepareStatement(insertSql);

			stmt.setString(1, product.getProductId());
			stmt.setString(2, product.getProductCategory());
			stmt.setString(3, product.getProductName());
			stmt.setInt(4, product.getProductPrice());
			stmt.setInt(5, product.getproductSafeInventory());
			stmt.setInt(6, product.getNumberOfShelve());
			stmt.setInt(7, product.getNumberOfInventory());
			stmt.setInt(8, product.getNumberOfSale());
			stmt.setInt(9, product.getNumberOfExchange());
			stmt.setInt(10, product.getNumberOfDestruction());
			stmt.setInt(11, product.getNumberOfRemove());

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// Check if the exception is due to duplicate key
			if (e.getSQLState().equals("23000") || e.getErrorCode() == 1062) {
				// Duplicate entry
				return false;
			}
			throw e; // Re-throw other SQL exceptions
		} catch (ClassNotFoundException e) {
			throw new SQLException("Database driver not found", e);
		} finally {
			if (stmt != null)
				stmt.close();
			if (connection != null)
				connection.close();
		}
	}

	public static void update(ProductBean product) {
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);

			stmt = connection.prepareStatement(updateSql);

			String productId = product.getProductId();
			String productCategory = product.getProductCategory();
			String productName = product.getProductName();
			int productPrice = product.getProductPrice();
			int productSafeInventory = product.getproductSafeInventory();
			int numberOfShelve = product.getNumberOfShelve();
			int numberOfInventory = product.getNumberOfInventory();
			int numberOfSale = product.getNumberOfSale();
			int numberOfExchange = product.getNumberOfExchange();
			int numberOfDestruction = product.getNumberOfDestruction();
			int numberOfRemove = product.getNumberOfRemove();

			stmt.setString(1, productId);
			stmt.setString(2, productCategory);
			stmt.setString(3, productName);
			stmt.setInt(4, productPrice);
			stmt.setInt(5, productSafeInventory);
			stmt.setInt(6, numberOfShelve);
			stmt.setInt(7, numberOfInventory);
			stmt.setInt(8, numberOfSale);
			stmt.setInt(9, numberOfExchange);
			stmt.setInt(10, numberOfDestruction);
			stmt.setInt(11, numberOfRemove);
			stmt.setString(12, productId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static void shelve(ProductBean product) {
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(shelveSql);

			String productId = product.getProductId();
			int numberOfShelve = product.getNumberOfShelve();
			int numberOfInventory = product.getNumberOfInventory();

			stmt.setInt(1, numberOfInventory);
			stmt.setInt(2, numberOfShelve);
			stmt.setString(3, productId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static void remove(ProductBean product) {
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(removeSql);

			String productId = product.getProductId();
			stmt.setString(1, productId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static void updateSaleByCheckOut() {
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(updateSaleByCheckOut);
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static boolean checkProductExists(String productId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM products WHERE productId = ?";
		try {
			// javax.naming.Context context = new InitialContext();
			// DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/servdb");
			// connection = ds.getConnection();
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        connection = DriverManager.getConnection(url, user, password);
			
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, productId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void closeConnection() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}