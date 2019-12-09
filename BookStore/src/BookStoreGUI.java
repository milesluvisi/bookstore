import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.*;
import java.text.DecimalFormat;

public class BookStoreGUI extends JFrame {

	private static final int WINDOW_WIDTH= 750;
	private static final int WINDOW_LENGTH = 250;

	private JPanel booksPanel;			
	private JPanel buttonsPanel;		
	private JPanel shoppingCartPanel;	
	private JPanel bannerPanel;			
	private JPanel searchButtonsPanel;	
	private JList booksList;			
	private JList selectedList;			

	private JButton addSelected;		
	private JButton removeSelected;		
	private JButton checkOut;			
	private JButton searchButton;		
	private JButton showAllButton;		

	private BookInfo booksInfo = new BookInfo(); 			
	private String[] bookNames = booksInfo.getBookNames();	
	private double[] bookPrices = booksInfo.getBookPrices();

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

	private double total;				
	private double bookPrice;			
	private final double TAX=0.06;		

	private ListModel books;			
	private ListModel shoppingCart;		
	private DefaultListModel shoppingCartDFM;

	private DecimalFormat money;		
	private Object selectedBookName; 	

	private String searchResults;		
	private String notFound = " Title not found";	
	private boolean found = false;

	public BookStoreGUI() throws IOException {
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
		booksList = new JList(bookNames);
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
		selectedList = new JList();
		selectedList.setVisibleRowCount(5);
		scrollPane2 = new JScrollPane(selectedList);
		scrollPane2.setPreferredSize(new Dimension(175,50));
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

	//TODO: Change all the listeners to interact based on a database rather than a txt file
	public class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			selectedIndex = booksList.getSelectedIndex();
			selectedBookName = booksList.getSelectedValue();

			books = booksList.getModel();
			shoppingCart = selectedList.getModel();

			shoppingCartDFM = new DefaultListModel();

			for(count=0; count<shoppingCart.getSize(); count++){
				shoppingCartDFM.addElement(shoppingCart.getElementAt(count));
			}

			if(element == -1)
				bookPrice += bookPrices[selectedIndex];
			else
				bookPrice += bookPrices[element];

			shoppingCartDFM.addElement(selectedBookName);
			selectedList.setModel(shoppingCartDFM);
		}
	}

	public class RemoveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			index = selectedList.getSelectedIndex();
			((DefaultListModel)selectedList.getModel()).remove(index);

			if(element == -1)
				if(bookPrices[selectedIndex] <= bookPrice)
					bookPrice -= (bookPrices[selectedIndex]);
				else
					bookPrice = (bookPrices[index]) - bookPrice;
			else
				if(bookPrices[element] <= bookPrice)
					bookPrice -= (bookPrices[element]);
				else
					bookPrice = (bookPrices[index]) - bookPrice;
		}
	}

	public class CheckOutButtonListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {

			money = new DecimalFormat("#,##0.00");
			total = (bookPrice + (bookPrice*TAX));

			JOptionPane.showMessageDialog(null, "Subtotal: $" + (money.format(bookPrice)) + "\n" +
												"Tax: $" + (money.format((bookPrice*TAX))) + "\n" +
												"Total: $" + (money.format(total)));
		}
	}

	public class SearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			index = 0;

			while(!found && index < bookNames.length)
			{
				if(bookNames[index].equals(searchField.getText())){
					found = true;
					element = index;
				}
				index++;
			}

			if(element == -1){
				booksList.setModel(new DefaultListModel());
				((DefaultListModel)booksList.getModel()).addElement(notFound);
			}
			else{
				searchResults = bookNames[element];
				booksList.setModel(new DefaultListModel());

				((DefaultListModel)booksList.getModel()).addElement(searchResults);
			}
		}
	}

	public class ShowAllButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			booksList.setModel(new DefaultListModel());
			
			for(i=0; i < bookNames.length; i++){
				((DefaultListModel)booksList.getModel()).addElement(bookNames[i]);
				 
			}
		}
	}

	 public static void main(String[] args) throws IOException {

		 new BookStoreGUI();
	 }
 }