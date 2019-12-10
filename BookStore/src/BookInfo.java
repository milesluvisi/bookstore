import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookInfo {
	
	private Connection con;
	private static Statement stmt;
	
	public BookInfo(Connection con) {
		this.con = con;
		try {
			BookInfo.stmt = this.con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//	TODO
	public void placeOrder(double total, ArrayList<Integer> bookISBNs, 
			ArrayList<Integer> amounts) throws Exception {
		String sql = String.format("INSERT INTO Orders(status, price) "
				+ "values('Placed', %.2f);", total);
		stmt.executeUpdate(sql);
		
		sql = "SELECT oid FROM Orders";
		ResultSet oidList = stmt.executeQuery(sql);
		oidList.last();
		int oid = oidList.getInt("oid");
		
		if (bookISBNs.size() != amounts.size()) {throw new Exception("List length mismatch");}
		for (int i=0; i<bookISBNs.size(); i++) {
			sql = String.format("INSERT INTO Ordering(isbn, oid, amount) "
					+ "values(%d, %d, %d);", bookISBNs.get(i), oid, amounts.get(i));
			stmt.executeUpdate(sql);
		}

	}
	
	public ArrayList<String> getBookNames() throws IOException, SQLException {
		String sql = "SELECT title FROM Books";
		ResultSet results = stmt.executeQuery(sql);
		ArrayList<String> strArray = new ArrayList<String>();
		while(results.next()) {
			strArray.add(results.getString("title"));
		}
		return strArray;
	}

	public ArrayList<Double> getBookPrices() throws IOException, SQLException {
		String sql = "SELECT price FROM Books";
		ResultSet results = stmt.executeQuery(sql);
		ArrayList<Double> strArray = new ArrayList<Double>();
		while(results.next()) {
			strArray.add(results.getDouble("price"));
		}
		return strArray;
	}

}
