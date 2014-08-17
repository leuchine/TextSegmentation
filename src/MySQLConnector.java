import java.sql.Connection;
import java.sql.Statement;

class MySQLConnector {
	private static Connection conn = null;
	private static int idCounter = 1;
 	public MySQLConnector() {
		
	}
	
	public boolean createTable() {
		DBconnection dbconnection = new DBconnection();
		try {
			conn = dbconnection.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String sqlCreateNodeTable = null;
			
			sqlCreateNodeTable = "CREATE TABLE NodeTable(" +
					  "nodeid INTEGER, " + 
					  "pid INTEGER, " + 
					  "did INTEGER, " +
					  "start INTEGER, " +
					  "end INTEGER, " +
					  "keywords VARCHAR(100), " +
					  "terms VARCHAR(400))";
			stmt.executeUpdate(sqlCreateNodeTable);
			
			conn.commit();
		    conn.setAutoCommit(true);
			
			stmt.close();		
			DBconnection.closeConnection(conn);
		    return true;
		} catch (Exception e) {
			System.out.println("Failed!");
			return false;
		}
	}
	
	public boolean insertTuple(int start, int end, int parentid, int bookid, String keywords, String terms) {
		return insertTuple(idCounter, start, end, parentid, bookid, keywords, terms);
	}
	
	public boolean insertTuple(int nodeid, int start, int end, int parentid, int bookid, String keywords, String terms) {
		DBconnection dbconnection = new DBconnection();
		try {
			conn = dbconnection.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String sqlInsertTuple = null;
			
			sqlInsertTuple = "INSERT INTO NodeTable VALUES('" + 
					nodeid + "', '" + 
					parentid + "', '" +
					bookid + "', '" +
					start + "', '" + 
					end + "', '" + 
					keywords + "', '" + 				
					terms +  "')";
			
			stmt.executeUpdate(sqlInsertTuple);
			idCounter++;
			
			conn.commit();
		    conn.setAutoCommit(true);
			
			stmt.close();		
			DBconnection.closeConnection(conn);
		    return true;
		} catch (Exception e) {
			System.out.println("Failed!");
			return false;
		}
	}
}
