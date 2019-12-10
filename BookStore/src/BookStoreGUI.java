import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class BookStoreGUI extends JFrame {
	
	static final String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore";
	static final String DB_USER = "root";
	static final String DB_PASS = "FinalProject4523";	//	EDIT THIS LINE
	
	private static Connection con;
	private static Statement stmt;

	private static final int WINDOW_WIDTH= 750;
	private static final int WINDOW_LENGTH = 500;

	private JPanel booksPanel;			
	private JPanel buttonsPanel;		
	private JPanel shoppingCartPanel;	
	private JPanel bannerPanel;			
	private JPanel searchButtonsPanel;	
	private JList<String> booksList;			
	private JList<Object> cartList;			

	private JButton addSelected;		
	private JButton removeSelected;		
	private JButton checkOut;			
	private JButton searchButton;		
	private JButton showAllButton;		

	private BookInfo booksInfo; 			
	private ArrayList<String> bookNames;	
	private ArrayList<Double> bookPrices;

	private JScrollPane scrollPane1;	
	private JScrollPane scrollPane2;	

	private JLabel panelTitle;			
	private JLabel cartTitle;			
	private JLabel banner;				

	private JTextField searchField;		

	private int element = -1;			
	private int selectedIndex;			
	private int index;					
	private int i,count;				

	private double totalWithTax;				
	private double totalPrice;			
	private final double TAX=0.06;		

	private ListModel<String> books;			
	private ListModel<Object> shoppingCart;		
	protected DefaultListModel<Object> shoppingCartDLM;

	private DecimalFormat money;		
	private Object selectedBookName; 	

	private String searchResults;		
	private String notFound = " Title not found";	
	private boolean found = false;

	public BookStoreGUI(Connection con2) throws IOException, SQLException {
		
		booksInfo = new BookInfo(con2);
		bookNames = booksInfo.getBookNames();	
		bookPrices = booksInfo.getBookPrices();
		
		con = con2;
		stmt = con.createStatement();
		
		setTitle("Database Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(WINDOW_WIDTH, WINDOW_LENGTH);

		buildBooksPanel();
		buildButtonsPanel();
		buildShoppingCartPanel();
		buildBannerPanel();
		buildSearchButtonsPanel();

		add(bannerPanel,BorderLayout.NORTH);
		add(booksPanel, BorderLayout.WEST);
		add(buttonsPanel, BorderLayout.CENTER);
		add(shoppingCartPanel, BorderLayout.EAST);
		add(searchButtonsPanel, BorderLayout.SOUTH);

		setVisible(true);
		pack();
	}

	public void buildBooksPanel() {
		booksPanel = new JPanel();
		booksPanel.setLayout(new BorderLayout());
		booksList = new JList(bookNames.toArray());
		booksList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		booksList.setVisibleRowCount(5);
		scrollPane1 = new JScrollPane(booksList);
		scrollPane1.setPreferredSize(new Dimension(175,50));
	 	panelTitle = new JLabel("Available Books");

		booksPanel.add(panelTitle, BorderLayout.NORTH);
		booksPanel.add(scrollPane1);
	}

	public void buildButtonsPanel() {
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,3,3,3));
		addSelected = new JButton("Add Selected Item");
		removeSelected = new JButton("Remove Selected Item");
		checkOut = new JButton("Check Out");
		addSelected.addActionListener(new AddButtonListener());
		removeSelected.addActionListener(new RemoveButtonListener());
		checkOut.addActionListener(new CheckOutButtonListener());

	 	buttonsPanel.add(addSelected);
		buttonsPanel.add(removeSelected);
		buttonsPanel.add(checkOut);
	}

	public void buildShoppingCartPanel() {
		shoppingCartPanel = new JPanel();
		shoppingCartPanel.setLayout(new BorderLayout());
		cartList = new JList<Object>();
		cartList.setVisibleRowCount(5);
		scrollPane2 = new JScrollPane(cartList);
		scrollPane2.setPreferredSize(new Dimension(175,175));
		cartTitle = new JLabel("Shopping Cart");

		shoppingCartPanel.add(cartTitle, BorderLayout.NORTH);
		shoppingCartPanel.add(scrollPane2);
	}

	public void buildBannerPanel() {
		bannerPanel = new JPanel();
		setLayout(new BorderLayout());
		String labelText= "Database Bookstore Assignment" ;
		JLabel banner = new JLabel(labelText);
		banner.setFont(new Font("Serif",Font.BOLD,28));

		bannerPanel.add(banner);
	}

	public void buildSearchButtonsPanel() {
		searchButtonsPanel = new JPanel();
		searchButtonsPanel.setLayout(new GridLayout(1, 3 , 5, 5));
		searchButton = new JButton("Search");
		showAllButton = new JButton("Show All");
		searchField = new JTextField(15);
		searchButton.addActionListener(new SearchButtonListener());
		showAllButton.addActionListener(new ShowAllButtonListener());

		searchButtonsPanel.add(searchField);
		searchButtonsPanel.add(searchButton);
		searchButtonsPanel.add(showAllButton);
	}

	public class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			selectedIndex = booksList.getSelectedIndex();
			selectedBookName = booksList.getSelectedValue();

			books = booksList.getModel();
			shoppingCart = cartList.getModel();

			shoppingCartDLM = new DefaultListModel<Object>();

			for(count=0; count<shoppingCart.getSize(); count++){
				shoppingCartDLM.addElement(shoppingCart.getElementAt(count));
			}

			if(element == -1)
				totalPrice += bookPrices.get(selectedIndex);
			else
				totalPrice += bookPrices.get(element);

			shoppingCartDLM.addElement(selectedBookName);
			cartList.setModel(shoppingCartDLM);
		}
	}

	public class RemoveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			index = cartList.getSelectedIndex();
			((DefaultListModel<Object>)cartList.getModel()).remove(index);

			if(element == -1)
				if(bookPrices.get(selectedIndex) <= totalPrice)
					totalPrice -= (bookPrices.get(selectedIndex));
				else
					totalPrice = (bookPrices.get(index)) - totalPrice;
			else
				if(bookPrices.get(element) <= totalPrice)
					totalPrice -= (bookPrices.get(element));
				else
					totalPrice = (bookPrices.get(index)) - totalPrice;
		}
	}

	//	TODO adding to Ordering table
	public class CheckOutButtonListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			money = new DecimalFormat("#,##0.00");
			totalWithTax = (totalPrice + (totalPrice*TAX));
			
			Object[] checkoutBooks = shoppingCartDLM.toArray();
			//	TODO
			try {
				String sql = "SELECT * FROM Books";
				ResultSet results = stmt.executeQuery(sql);
				ArrayList<Integer> isbnList = new ArrayList<Integer>();
				ArrayList<Integer> countList = new ArrayList<Integer>();
	
				while(results.next()) {
					isbnList.add(results.getInt("isbn"));
					countList.add(0);
					for (Object book : checkoutBooks) {
						if (results.getString("title") == book.toString()) {
							countList.get(results.getRow());
						}
					}
				}
				
				booksInfo.placeOrder(totalWithTax, isbnList, countList);
			} catch (Exception sqle) {
				sqle.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null, "Subtotal: $" + (money.format(totalPrice)) + "\n" +
												"Tax: $" + (money.format((totalPrice*TAX))) + "\n" +
												"Total: $" + (money.format(totalWithTax)));
		}
	}

	public class SearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			index = 0;

			while(!found && index < bookNames.size())
			{
				if(bookNames.get(index).equals(searchField.getText())){
					found = true;
					element = index;
				}
				index++;
			}

			if(element == -1){
				booksList.setModel(new DefaultListModel<String>());
				((DefaultListModel<String>)booksList.getModel()).addElement(notFound);
			}
			else{
				searchResults = bookNames.get(element);
				booksList.setModel(new DefaultListModel<String>());

				((DefaultListModel<String>)booksList.getModel()).addElement(searchResults);
			}
		}
	}

	public class ShowAllButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			booksList.setModel(new DefaultListModel<String>());
			
			for(i=0; i < bookNames.size(); i++){
				((DefaultListModel<String>)booksList.getModel()).addElement(bookNames.get(i));
				 
			}
		}
	}

	 public static void main(String[] args) throws IOException, SQLException {
		 
		 Connection con = null;
		 try {
				Class.forName(JDBC_Driver);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		 try {
			 con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			 System.out.println("Connection successful");
		 } catch (SQLException e2) {
			 e2.printStackTrace();
		 }
		 new BookStoreGUI(con);
		 
	 }
 }