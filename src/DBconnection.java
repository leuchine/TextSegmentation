import java.sql.*;

class DBconnection {
	
	private static Connection conn = null;
	private static String DatabaseDriver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost/annotation";
	private static String user = "root";
	private static String password = "";
	
    	
	public static synchronized Connection getConnection() throws SQLException {
		try {			
			if(conn == null||conn.isClosed()){
				Class.forName(DatabaseDriver).newInstance();			
				conn = DriverManager.getConnection(url, user, password);
				System.out.println("Database connect successfully: " + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
			if (conn != null) {
		        conn.rollback();
		        conn.setAutoCommit(true);
		    }
		}
		
		return conn;		
	}
	
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connection close successfully: " + url);
	}
	
	public static void closeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connection close successfully: " + url);
	}
	
}
