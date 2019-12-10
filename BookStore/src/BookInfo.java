import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookInfo {
	
	Connection con;
	static Statement stmt;
	
	public BookInfo(Connection con) {
		this.con = con;
		try {
			BookInfo.stmt = this.con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
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
		
//		int count=0;
//		final int SIZE = 7;
//
//		String[] books = new String[SIZE];
//
//		File myFile = new File("BookPrices.txt");
//		Scanner inputFile = new Scanner(myFile);
//
//		while(inputFile.hasNext() && count < books.length)
//		{
//			String str;
//
//			str= inputFile.nextLine();
//			String[] parts = str.split(",");
//			books[count]=parts[0];
//		 	count++;
//		}
//		
//		inputFile.close();
//		return books;
	}

	public ArrayList<Double> getBookPrices() throws IOException, SQLException {
		String sql = "SELECT price FROM Books";
		ResultSet results = stmt.executeQuery(sql);
		ArrayList<Double> strArray = new ArrayList<Double>();
		while(results.next()) {
			strArray.add(results.getDouble("price"));
		}
		return strArray;
		
//		int count=0;
//		final int SIZE = 7;
//		double[] prices = new double[SIZE];
//
//		File myFile = new File("BookPrices.txt");
//		Scanner inputFile = new Scanner(myFile);
//
//		while(inputFile.hasNext() && count < prices.length)
//		{
//			String str;
//
//			str = inputFile.nextLine();
//			String[] parts = str.split(",");
//
//		 	prices[count]= Double.parseDouble(parts[1]) ;
//		 	count++;
//		}
//		inputFile.close();
//
//		return prices;
	}

}
