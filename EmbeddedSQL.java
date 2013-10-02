/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 * Parth, Alex
 */
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import java.sql.*;
import java.io.*;
import java.util.Scanner; // used for reading in ints

import java.util.*;

public class EmbeddedSQL {
	public static ArrayList<String> orderslist = new ArrayList<String>();
	static String currentUser = "";
	static boolean superUser = false;
	//static boolean keepon = true;
	//static int video_id = 0;
	static EmbeddedSQL esql = null;

	// reference to physical database connection.
	private static Connection _connection = null;
	//private Statement stmt = this._connection.createStatement ();

	// handling the keyboard inputs through a BufferedReader
	// This variable can be global for convenience.
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

   /**
    * Creates a new instance of EmbeddedSQL
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
	public EmbeddedSQL (String dbname, String dbport, String user, String passwd) throws SQLException {

		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");

			// obtain a physical connection
			this._connection = DriverManager.getConnection(url, user, passwd);
			System.out.println("Done");
		}catch (Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
			System.out.println("Make sure you started postgres on this machine");
			System.exit(-1);
		}//end catch
	}//end EmbeddedSQL


	public void executeUpdate (String sql) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement ();
		// issues the update instruction
		stmt.executeUpdate (sql);
		// close the instruction
		stmt.close ();
	}//end executeUpdate

    	public static ResultSet rs = null;
    	public static ArrayList<String> output_buffer = new ArrayList<String>();
    	
	public Integer executeQuery (String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the query instruction
		rs = stmt.executeQuery (query);


		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		// iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
					output_buffer.add(rsmd.getColumnName(i) + "\t");
				}
				System.out.println();
				output_buffer.add("\n");
				outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i){
				System.out.print (rs.getString (i) + "\t");
				output_buffer.add(rs.getString (i) + "\t");
			}
			System.out.println ();
			output_buffer.add("\n");
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}//end executeQuery
	
	public Integer executeWall(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the query instruction
		rs = stmt.executeQuery (query);


		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		// iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		//String current_id = rs.getString(1);
		String current_id = rs.getString(1);
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
					output_buffer.add(rsmd.getColumnName(i) + "\t");
				}
				System.out.println();
				output_buffer.add("\n");
				outputHeader = false;
			}
			/*
			if( current_id != rs.getString(1) ){
				current_id = rs.getString(1);
				output_buffer.add(current_id);
			}*/
			
				System.out.print (rs.getString (2) + "\t");
				output_buffer.add(rs.getString (2) + "\t");
			
			System.out.println ();
			output_buffer.add("\n");
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}//end executeQuery


	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
			// ignored.
		}//end try
	}//end cleanup

    
	public static String username = "";
	public static void main (String[] args) {
		
		boolean keepOn=false;
		if (args.length != 4) 
		{
			System.err.println ("Usage: "+"java [-classpath <classpath>] " + EmbeddedSQL.class.getName ()+" <dbname> <port> <user> <passwd>");
			return;
		}

		//Greeting();
		//EmbeddedSQL esql = null;
		//EmbeddedSQL static esql = null;
		try{
			
			// use postgres JDBC driver.
			Class.forName ("org.postgresql.Driver").newInstance ();
			// instantiate the EmbeddedSQL object and creates a physical
			// connection.
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			String passwd = args[3];
			esql = new EmbeddedSQL (dbname, dbport, user, passwd);

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				
					LoginGUI();
					
			
				}//end run()
			});
		}catch(Exception e){
			System.err.println (e.getMessage ());
			System.out.println("Could not open postgresql");
		}
		finally{
			// make sure to cleanup the created table and close the connection.
			try{
				if(esql != null && keepOn) 
				{
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if
			}catch (Exception e){
				// ignored.
			}//end try
		}//end finally
	
	}//end main

	public static int readChoice() {
		int input;
      		// returns only if a correct value is given.
		do {
        		System.out.print("Please make your choice: ");
        		try { // read the integer, parse it and break.
        		input = Integer.parseInt(in.readLine());
            		break;
         		}catch (Exception e) {
         			System.out.println("Your input is invalid!");
         			continue;
         		}//end try
      		}while (true);
      			return input;
   	}//end readChoice
   	
   	public static void LoginGUI(){
	
		final JFrame LoginScreen = new JFrame("MovieNet");
		LoginScreen.setLayout(null);
		LoginScreen.setBounds(0,0,1200,1024);
				
		//MovieNettitle
		JLabel MovieNettitle = new JLabel("MovieNet");
		MovieNettitle.setSize(400,200);
		MovieNettitle.setLocation(450,25);
		MovieNettitle.setFont(new Font("Helvetica",Font.BOLD, 48));
		MovieNettitle.setForeground(Color.red);
		MovieNettitle.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(MovieNettitle);
		
		//copyright
		JLabel copyright = new JLabel("Copyright @ 2013 | Parth Sangani, Alxeander Vo");
		copyright.setSize(400,75);
		copyright.setLocation(900,900);
		copyright.setFont(new Font("Helvetica",Font.BOLD, 8));
		copyright.setForeground(Color.red);
		copyright.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(copyright);

		//UsernameLabel
		JLabel UsernameLabel = new JLabel("Username");
		UsernameLabel.setSize(100,25);
		UsernameLabel.setLocation(880,200);
		UsernameLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		UsernameLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(UsernameLabel);

		//UsernameInput
		final JTextField UsernameInput = new JTextField();
		UsernameInput.setSize(125,25);
		UsernameInput.setLocation(980,200);
		LoginScreen.add(UsernameInput);

		//PasswordLabel
		JLabel PasswordLabel = new JLabel("Password");
		PasswordLabel.setSize(100,25);
		PasswordLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		PasswordLabel.setLocation(880,225);
		PasswordLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(PasswordLabel);
	
		//PasswordInput
		final JTextField PasswordInput = new JPasswordField();
		PasswordInput.setSize(125,25);
		PasswordInput.setLocation(980,225);
		LoginScreen.add(PasswordInput);

		//btnLogin
		JButton btnLoginEnter = new JButton("Login");
		btnLoginEnter.setSize(100,25);
		btnLoginEnter.setLocation(980,250);
		LoginScreen.add(btnLoginEnter);
		
		//RegFirstNameLabel
		JLabel RegFirstName = new JLabel("First Name");
		RegFirstName.setSize(100,25);
		RegFirstName.setLocation(75,200);
		RegFirstName.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegFirstName.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegFirstName);

		//RegFirstNameInput
		final JTextField RegFirstNameInput = new JTextField();
		RegFirstNameInput.setSize(125,25);
		RegFirstNameInput.setLocation(200,200);
		LoginScreen.add(RegFirstNameInput);
		
		//RegMiddleNameLabel
		JLabel RegMiddleName = new JLabel("Middle");
		RegMiddleName.setSize(100,25);
		RegMiddleName.setLocation(75,225);
		RegMiddleName.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegMiddleName.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegMiddleName);

		//RegMiddleNameInput
		final JTextField RegMiddleInput = new JTextField();
		RegMiddleInput.setSize(125,25);
		RegMiddleInput.setLocation(200,225);
		LoginScreen.add(RegMiddleInput);
		
		//RegLastNameLabel
		JLabel RegLastName = new JLabel("Last Name");
		RegLastName.setSize(100,25);
		RegLastName.setLocation(75,250);
		RegLastName.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegLastName.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegLastName);

		//RegLastNameInput
		final JTextField RegLastInput = new JTextField();
		RegLastInput.setSize(125,25);
		RegLastInput.setLocation(200,250);
		LoginScreen.add(RegLastInput);

		//RegUserIDLabel
		JLabel RegUserID = new JLabel("UserID");
		RegUserID.setSize(100,25);
		RegUserID.setLocation(75,275);
		RegUserID.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegUserID.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegUserID);

		//RegUserIDInput
		final JTextField RegUserIDInput = new JTextField();
		RegUserIDInput.setSize(125,25);
		RegUserIDInput.setLocation(200,275);
		LoginScreen.add(RegUserIDInput);
		
		//RegPasswordLabel
		JLabel RegPasswordLabel = new JLabel("Password");
		RegPasswordLabel.setSize(100,25);
		RegPasswordLabel.setLocation(75,300);
		RegPasswordLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegPasswordLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegPasswordLabel);
	
		//RegPasswordInput
		final JTextField RegPasswordInput = new JPasswordField();
		RegPasswordInput.setSize(125,25);
		RegPasswordInput.setLocation(200,300);
		LoginScreen.add(RegPasswordInput);
		
		//RegEmailLabel
		JLabel RegEmailLabel = new JLabel("E-mail");
		RegEmailLabel.setSize(100,25);
		RegEmailLabel.setLocation(75,325);
		RegEmailLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegEmailLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegEmailLabel);

		//RegEmailInput
		final JTextField RegEmailInput = new JTextField();
		RegEmailInput.setSize(125,25);
		RegEmailInput.setLocation(200,325);
		LoginScreen.add(RegEmailInput);
		
		//RegStreet1Label
		JLabel RegStreet1Label = new JLabel("Street1");
		RegStreet1Label.setSize(100,25);
		RegStreet1Label.setLocation(75,350);
		RegStreet1Label.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegStreet1Label.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegStreet1Label);

		//RegStreet1Input
		final JTextField RegStreet1Input = new JTextField();
		RegStreet1Input.setSize(125,25);
		RegStreet1Input.setLocation(200,350);
		LoginScreen.add(RegStreet1Input);
		
		//RegStreet2Label
		JLabel RegStreet2Label = new JLabel("Street2");
		RegStreet2Label.setSize(100,25);
		RegStreet2Label.setLocation(75,375);
		RegStreet2Label.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegStreet2Label.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegStreet2Label);

		//RegStreet2Input
		final JTextField RegStreet2Input = new JTextField();
		RegStreet2Input.setSize(125,25);
		RegStreet2Input.setLocation(200,375);
		LoginScreen.add(RegStreet2Input);
		
		//RegStateLabel
		JLabel RegStateLabel = new JLabel("State");
		RegStateLabel.setSize(100,25);
		RegStateLabel.setLocation(75,400);
		RegStateLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegStateLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegStateLabel);

		//RegStateInput
		final JTextField RegStateInput = new JTextField();
		RegStateInput.setSize(125,25);
		RegStateInput.setLocation(200,400);
		LoginScreen.add(RegStateInput);
		
		//RegCountryLabel
		JLabel RegCountryLabel = new JLabel("Country");
		RegCountryLabel.setSize(100,25);
		RegCountryLabel.setLocation(75,425);
		RegCountryLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegCountryLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegCountryLabel);

		//RegCountryInput
		final JTextField RegCountryInput = new JTextField();
		RegCountryInput.setSize(125,25);
		RegCountryInput.setLocation(200,425);
		LoginScreen.add(RegCountryInput);

		//RegZipLabel
		JLabel RegZipLabel = new JLabel("Zip Code");
		RegZipLabel.setSize(100,25);
		RegZipLabel.setLocation(75,450);
		RegZipLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegZipLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegZipLabel);

		//RegZipInput
		final JTextField RegZipInput = new JTextField();
		RegZipInput.setSize(125,25);
		RegZipInput.setLocation(200,450);
		LoginScreen.add(RegZipInput);
		
		//RegBalanceLabel
		JLabel RegBalanceLabel = new JLabel("Balance");
		RegBalanceLabel.setSize(100,25);
		RegBalanceLabel.setLocation(75,475);
		RegBalanceLabel.setFont(new Font("Helvetica",Font.BOLD, 18));
		RegBalanceLabel.setVerticalAlignment(SwingConstants.CENTER);
		LoginScreen.add(RegBalanceLabel);

		//RegBalanceInput
		final JTextField RegBalanceInput = new JTextField();
		RegBalanceInput.setSize(125,25);
		RegBalanceInput.setLocation(200,475);
		LoginScreen.add(RegBalanceInput);
		
		//btnReginsterEnter
		JButton btnRegisterEnter = new JButton("Register");
		btnRegisterEnter.setSize(100,25);
		btnRegisterEnter.setLocation(200,500);
		LoginScreen.add(btnRegisterEnter);
		
		JLabel loginBackground = new JLabel( new ImageIcon("/tmp/sanganip/images.jpeg"));
		loginBackground.setSize(1200,1024);
		loginBackground.setLocation(0,0);
		LoginScreen.add(loginBackground);
		
		LoginScreen.setVisible(true);
	
		btnLoginEnter.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String Username = UsernameInput.getText();
			String Password = PasswordInput.getText();

			if( Login(esql,Username,Password) ){
				System.out.println("Logged in");
				EmbeddedSQL.currentUser = Username;
				username = Username;
				LoginScreen.setVisible(false);
				WallGUI(LoginScreen);
			}
			else{
				JOptionPane.showMessageDialog(LoginScreen,"User and/or Password Invalid","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		});
		
		btnRegisterEnter.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String FirstName = RegFirstNameInput.getText();
			String MiddleName = RegMiddleInput.getText();
			String LastName = RegLastInput.getText();
			String UserID = RegUserIDInput.getText();
			String Password = RegPasswordInput.getText();
			String Email = RegEmailInput.getText();
			String Street1 = RegStreet1Input.getText();
			String Street2 = RegStreet2Input.getText();
			String State = RegStateInput.getText();
			String Country = RegCountryInput.getText();
			String Zip = RegZipInput.getText();
			String Balance = RegBalanceInput.getText();
			
			Register(esql,FirstName,MiddleName,LastName,UserID,Password,Email,Street1,Street2,State,Country,Zip,Balance);
		}
		});
		
	}
	
	
	public static void WallGUI(final JFrame LoginScreen){
			
		//Wall Frame
		final JFrame WallScreen = new JFrame("MovieNet");
		WallScreen.setLayout(null);
		WallScreen.setBounds(0,0,1200,1024);
					
		//MovieNettitle
		JLabel MovieNettitle = new JLabel("MovieNet");
		MovieNettitle.setSize(400,75);
		MovieNettitle.setLocation(450,0);
		MovieNettitle.setFont(new Font("Helvetica",Font.BOLD, 48));
		MovieNettitle.setForeground(Color.red);
		MovieNettitle.setVerticalAlignment(SwingConstants.CENTER);
		WallScreen.add(MovieNettitle);

		//copyright
		JLabel copyright = new JLabel("Copyright @ 2013 | Parth Sangani, Alxeander Vo");
		copyright.setSize(400,75);
		copyright.setLocation(900,900);
		copyright.setFont(new Font("Helvetica",Font.BOLD, 8));
		copyright.setForeground(Color.red);
		copyright.setVerticalAlignment(SwingConstants.CENTER);
		WallScreen.add(copyright);

		//btnAddBalance
		JButton btnAddBalance = new JButton("Add Balance");
		btnAddBalance.setSize(125,25);
		btnAddBalance.setLocation(100,100);
		btnAddBalance.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnAddBalance);
			
		//btnOrder
		JButton btnOrder = new JButton("Order");
		btnOrder.setSize(125,25);
		btnOrder.setLocation(250,100);
		btnOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnOrder);
			
		//btnSuperUser
		JButton btnSuperUser = new JButton("Super User");
		btnSuperUser.setSize(125,25);
		btnSuperUser.setLocation(400,100);
		btnSuperUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnSuperUser);
			
		//btnComment
		JButton btnCommentMovie = new JButton("Comments");
		btnCommentMovie.setSize(125,25);
		btnCommentMovie.setLocation(550,100);
		btnCommentMovie.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnCommentMovie);
		
		//btnFollow
		JButton btnFollow = new JButton("Follow");
		btnFollow.setSize(125,25);
		btnFollow.setLocation(700,100);
		btnFollow.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnFollow);
		//btncart
       	 JButton btncart = new JButton("Cart/Checkout");
       	 btncart.setSize(150,25);
        btncart.setLocation(1000,250);
        btncart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WallScreen.add(btncart);
           
        //btnListMoviebyRating
        JButton btnListMoviebyRating = new JButton("Movie by Ratings");
        btnListMoviebyRating.setSize(150,25);
        btnListMoviebyRating.setLocation(850,250);
        btnListMoviebyRating.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WallScreen.add(btnListMoviebyRating);
        
		//btnUpdatePermission
		JButton btnUpdatePermission = new JButton("Permissions");
        	btnUpdatePermission.setSize(125,25);
        	btnUpdatePermission.setLocation(850,300);
        	btnUpdatePermission.setCursor(new Cursor(Cursor.HAND_CURSOR));
        	WallScreen.add(btnUpdatePermission);

		//btnLike
		JButton btnLike = new JButton("Like");
		btnLike.setSize(125,25);
		btnLike.setLocation(850,100);
		btnLike.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnLike);
		
		//btnGenre?
		JButton btnGenre = new JButton("Genre?");
		btnGenre.setSize(125,25);
		btnGenre.setLocation(850,150);
		btnGenre.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnGenre);
		
		//btnRate
		JButton btnRate = new JButton("Rate");
		btnRate.setSize(125,25);
		btnRate.setLocation(850,200);
		btnRate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnRate);
		
		//btnWatch
		JButton btnWatch = new JButton("Watch");
		btnWatch.setSize(125,25);
		btnWatch.setLocation(1000,150);
		btnWatch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnWatch);
		
		//btnSeeWall
		JButton btnSeeWall = new JButton("Wall");
		btnSeeWall.setSize(125,25);
		btnSeeWall.setLocation(1000,200);
		btnSeeWall.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnSeeWall);
			
		//btnLogOut
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setSize(125,25);
		btnLogOut.setLocation(1000,100);
		btnLogOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnLogOut);
			
		//btnSearch
		JButton btnSearch = new JButton("Search");
		btnSearch.setSize(125,25);
		btnSearch.setLocation(100,150);
		btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		WallScreen.add(btnSearch);
			
		final JTextField SearchInput = new JTextField();
		SearchInput.setSize(575,25);
		SearchInput.setLocation(250,150);
		WallScreen.add(SearchInput);
			
		final JTextArea WallTxt = new JTextArea();
		WallTxt.setEditable(false);
		WallTxt.setBackground(Color.black);
		WallTxt.setForeground(Color.green);
		WallScreen.add(WallTxt);
			
		JScrollPane ScrollPane = new JScrollPane(WallTxt);
		ScrollPane.setBounds(100,200,725,700);
		//ScrollPane.setForeground(Color.cyan);
		WallScreen.add(ScrollPane);							
		
		
		
		//RegBalanceLabel
		//JLabel BackGroundLabel = new JLabel(new ImageIcon("/tmp/sanganip/abstract-wallpaper.jpg"));
		JLabel BackGroundLabel = new JLabel(new ImageIcon("/tmp/sanganip/images.jpeg"));
		BackGroundLabel.setSize(1200,1024);
		BackGroundLabel.setLocation(0,0);
		WallScreen.add(BackGroundLabel);
	
		ListWall(WallTxt);
		WallScreen.setVisible(true);
			
		btnAddBalance.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			AddBalance(WallTxt);
		}
		});
		
		btnListMoviebyRating.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            Listbyrating(WallTxt);
        }
        });
        
		btnSeeWall.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			WallTxt.setText("");
			ListWall(WallTxt);
		}
		});


		btnUpdatePermission.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){

			UpdatePermissionwindow();
		}
		});


		btnWatch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			String blank = "";
			if( !movieTitle.equals(blank) ){
			
			System.out.println(movieTitle + " is playing");
			final JFrame WatchScreen = new JFrame("Video");
			WatchScreen.setLayout(null);
			WatchScreen.setBounds(0,0,320,240);
			
			//OrderChoiceLabel
			JLabel WatchLabel = new JLabel(movieTitle + " is playing");
			WatchLabel.setSize(200,25);
			WatchLabel.setLocation(100,15);
			WatchLabel.setVerticalAlignment(SwingConstants.CENTER);
			WatchScreen.add(WatchLabel);
			
			//btnWatchReturn
			JButton WatchReturn = new JButton("Return");
			WatchReturn.setSize(125,25);
			WatchReturn.setLocation(100,100);
			WatchReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			WatchScreen.add(WatchReturn);
			
			UpdateWatch(esql); // Inserting recently watched into watch table
				
			WatchScreen.setVisible(true);
			
			WatchReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
					
				WatchScreen.setVisible(false);
			}
			});
			}
			else{
				System.out.println("No video selected");
				final JFrame WatchScreen = new JFrame("Video");
				WatchScreen.setLayout(null);
				WatchScreen.setBounds(0,0,320,240);
			
				//OrderChoiceLabel
				JLabel WatchLabel = new JLabel("No video is selected!");
				WatchLabel.setSize(200,25);
				WatchLabel.setLocation(100,15);
				WatchLabel.setVerticalAlignment(SwingConstants.CENTER);
				WatchScreen.add(WatchLabel);
			
				//btnWatchReturn
				JButton WatchReturn = new JButton("Return");
				WatchReturn.setSize(125,25);
				WatchReturn.setLocation(100,100);
				WatchReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				WatchScreen.add(WatchReturn);
				
				WatchScreen.setVisible(true);
			
				WatchReturn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					
					WatchScreen.setVisible(false);
				}
				});
			}
			
		}
		});
		
		btnOrder.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			Order();
		}
		});
		
		btnSuperUser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			try{
				String query = "select super_user_id from super_user where super_user_id = '" + username + "';";
				Integer rowCount = esql.executeQuery(query);
				if( rowCount == 0 ){
					System.out.println("Not superuser");
					final JFrame NotSuperUserScreen = new JFrame("Denied");
					NotSuperUserScreen.setLayout(null);
					NotSuperUserScreen.setBounds(0,0,320,240);
				
					//btnAddBalance
					JButton DeniedOk = new JButton("Return");
					DeniedOk.setSize(125,25);
					DeniedOk.setLocation(100,100);
					DeniedOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
					NotSuperUserScreen.add(DeniedOk);
				
					NotSuperUserScreen.setVisible(true);
					DeniedOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
					
						NotSuperUserScreen.setVisible(false);
					}
					});
				}
				else{
					System.out.println("Is superuser");
					SuperUser();
				}
			}
			catch(Exception er){
				
				
			}
			
		}
		});
		
		btnCommentMovie.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			Comment(WallTxt);
		}
		});
		
		btnGenre.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			Genre(WallTxt);
			
		}
		});
		
		btnRate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			Rate(WallTxt);
		}
		});
		
		btncart.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			Cart();
		}
		});
		
		btnLogOut.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			int reply = JOptionPane.showConfirmDialog(null, "Logout?", "Logout", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				WallScreen.setVisible(false);
				LoginScreen.setVisible(true);
			}
		}
		});
		
					
		btnSearch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			String input = SearchInput.getText();
			output_buffer.clear();
			WallTxt.setText("");
			Search(esql,input);
			Output(WallTxt);
		}
		});
		
		btnFollow.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			Follow(WallTxt);
		}
		});
		
		btnLike.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			UpdateLike(esql);
			Output(WallTxt);
		}
		});
	
	}
	
   public static void UpdatePermissionwindow(){
   	final JFrame AddPermissionScreen = new JFrame("Permissions");
	AddPermissionScreen.setLayout(null);
	AddPermissionScreen.setBounds(0,0,320,240);
	//CurrentBalanceLabel
	JLabel CurrentBalanceLabel = new JLabel("Block user_id: ");
	CurrentBalanceLabel.setSize(125,25);
	CurrentBalanceLabel.setLocation(25,60);
	CurrentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
	AddPermissionScreen.add(CurrentBalanceLabel);
	//CurrentBalanceField
	final JTextField CurrentBalanceField = new JTextField();
	CurrentBalanceField.setSize(125,25);
	CurrentBalanceField.setLocation(160,60);
	AddPermissionScreen.add(CurrentBalanceField);

	AddPermissionScreen.setVisible(true);
	
	JButton btnblock = new JButton("Block");
	btnblock.setSize(125,25);
	btnblock.setLocation(20,120);
	btnblock.setCursor(new Cursor(Cursor.HAND_CURSOR));
	AddPermissionScreen.add(btnblock);

	btnblock.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String block = CurrentBalanceField.getText();
			UpdatePermission(esql,block);
			AddPermissionScreen.setVisible(false);
		}
		});

   }


	//List by rating
    public static void Listbyrating(JTextArea WallTxt){
    try{
    output_buffer.clear();
    String query = "Select title, year, rating from video  order by rating desc;";
    Integer rowCount = esql.executeQuery(query);
    WallTxt.setText("");
    Output(WallTxt);       
    }
    catch(Exception e){System.err.println(e.getMessage());}
    }
    
	// AddBalance - Finished?
	public static void AddBalance(final JTextArea WallTxt){
		try{
		final JFrame AddBalanceScreen = new JFrame("Balance");
		AddBalanceScreen.setLayout(null);
		AddBalanceScreen.setBounds(0,0,320,240);
		
		//CurrentBalanceLabel
		JLabel CurrentBalanceLabel = new JLabel("Current Balance");
		CurrentBalanceLabel.setSize(125,25);
		CurrentBalanceLabel.setLocation(25,60);
		CurrentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		AddBalanceScreen.add(CurrentBalanceLabel);

		//CurrentBalanceField
		final JTextField CurrentBalanceField = new JTextField();
		CurrentBalanceField.setSize(125,25);
		CurrentBalanceField.setLocation(160,60);
		String query = "select balance from users where user_id = '" + username + "';";
		Integer rowCount = esql.executeQuery(query);
		CurrentBalanceField.setText(rs.getString(1));
		CurrentBalanceField.setHorizontalAlignment(SwingConstants.CENTER);
		CurrentBalanceField.setEditable(false);
		AddBalanceScreen.add(CurrentBalanceField);
		
		//btnAddBalance
		JButton btnAddBalance = new JButton("Add Balance");
		btnAddBalance.setSize(125,25);
		btnAddBalance.setLocation(20,120);
		btnAddBalance.setCursor(new Cursor(Cursor.HAND_CURSOR));
		AddBalanceScreen.add(btnAddBalance);
		
		//AddBalanceInput
		final JTextField AddBalanceInput = new JTextField();
		AddBalanceInput.setSize(125,25);
		AddBalanceInput.setLocation(160,120);
		AddBalanceScreen.add(AddBalanceInput);
		
		AddBalanceScreen.setVisible(true);
		
		
		
		btnAddBalance.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = AddBalanceInput.getText();
			UpdateBalance(esql,input);
			AddBalanceScreen.setVisible(false);
			//output_buffer.clear();
			Output(WallTxt);
		}
		});
		}
		catch(Exception error){}
	}
	
	// Order - Finished?
	public static void Order(){
		final JFrame OrderScreen = new JFrame("Order");
		OrderScreen.setLayout(null);
		OrderScreen.setBounds(0,0,500,300);
		
		//OrderChoiceLabel
		JLabel OrderChoiceLabel = new JLabel("Select an option");
		OrderChoiceLabel.setSize(100,30);
		OrderChoiceLabel.setLocation(125,15);
		OrderChoiceLabel.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(OrderChoiceLabel);
		
		//NameLabel
		JLabel NameLabel = new JLabel("Name: ");
		NameLabel.setSize(100,25);
		NameLabel.setLocation(50,60);
		NameLabel.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(NameLabel);

		//AddName
		final JTextField AddName = new JTextField();
		AddName.setSize(200,25);
		AddName.setLocation(150,60);
		OrderScreen.add(AddName);

		//AddressLabel
		JLabel AddressLabel = new JLabel("Address: ");
		AddressLabel.setSize(200,25);
		AddressLabel.setLocation(50,90);
		AddressLabel.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(AddressLabel);

		//AddAddress
		final JTextField AddAddress = new JTextField();
		AddAddress.setSize(200,25);
		AddAddress.setLocation(150,90);
		OrderScreen.add(AddAddress);

		//EmailLabel
		JLabel EmailLabel = new JLabel("Email: ");
		EmailLabel.setSize(200,25);
		EmailLabel.setLocation(50,120);
		EmailLabel.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(EmailLabel);

		//AddEmail
		final JTextField AddEmail = new JTextField();
		AddEmail.setSize(200,25);
		AddEmail.setLocation(150,120);
		OrderScreen.add(AddEmail);

		//Ordermovietitle
		JLabel Ordermovietitle = new JLabel("Movie: " + movieTitle);
		Ordermovietitle.setSize(300,25);
		Ordermovietitle.setLocation(50,150);
		Ordermovietitle.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(Ordermovietitle);

		//btnOnline
		JButton btnOnline = new JButton("Online");
		btnOnline.setSize(125,25);
		btnOnline.setLocation(300,180);
		btnOnline.setCursor(new Cursor(Cursor.HAND_CURSOR));
		OrderScreen.add(btnOnline);

		//Orderquantity
		JLabel Orderquantity = new JLabel("If DVD - How many?");
		Orderquantity.setSize(200,25);
		Orderquantity.setLocation(50,210);
		Orderquantity.setVerticalAlignment(SwingConstants.CENTER);
		OrderScreen.add(Orderquantity);

		//AddQuantity
		final JTextField AddQuantity = new JTextField();
		AddQuantity.setSize(25,25);
		AddQuantity.setLocation(250,210);
		OrderScreen.add(AddQuantity);

		//btnDVD
		JButton btnDVD = new JButton("DVD");
		btnDVD.setSize(125,25);
		btnDVD.setLocation(300,210);
		btnDVD.setCursor(new Cursor(Cursor.HAND_CURSOR));
		OrderScreen.add(btnDVD);
		
		//btnReturn
		JButton btnReturn = new JButton("Return");
		btnReturn.setSize(125,25);
		btnReturn.setLocation(300,240);
		btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		OrderScreen.add(btnReturn);	
		
		OrderScreen.setVisible(true);

		btnOnline.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){

			Order(esql,"Online","");
			OrderScreen.setVisible(false);

		}
		});
		
		btnDVD.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){

			String dvdquantity = AddQuantity.getText();
			Order(esql,"DVD",dvdquantity);
			OrderScreen.setVisible(false);

			
		}
		});
		
		btnReturn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){

			OrderScreen.setVisible(false);
		}
		});

	}

	public static void Cart(){
		final JFrame CartScreen = new JFrame("Cart");
		CartScreen.setLayout(null);
		CartScreen.setBounds(0,0,440,240);
		
		final JTextArea cartTxt = new JTextArea();
		cartTxt.setEditable(true);
		CartScreen.add(cartTxt);
			
		JScrollPane ScrollPane = new JScrollPane(cartTxt);
		ScrollPane.setBounds(25,25,380,140);
		ScrollPane.setForeground(Color.cyan);
		CartScreen.add(ScrollPane);
		try{
		String query0 = "select video_id from video v where title = '" + movieTitle + "';";
		Integer rowCount = esql.executeQuery(query0);
		String videoid = rs.getString(1);

		String get_order = "select order_id from orders;";
		if(esql.executeQuery(get_order) != 0){
			get_order = "select MAX(order_id) from orders;";
			String max_order = rs.getString(1);
   			order_number = Integer.parseInt(max_order);
		}
		else {order_number = 0;}

		output_buffer.clear();
		String query = "select * from cart where user_id = '"+username+"';";
		rowCount = esql.executeQuery(query);
		query = "select sum(price) from  cart;";
		Integer rowCount1 = esql.executeQuery(query);
		int cost = Integer.parseInt(rs.getString(1));
		Output(cartTxt);
		
		query = "select balance from users where user_id = '" + username + "';";
		rowCount1 = esql.executeQuery(query);
	 	
		int balance = Integer.parseInt(rs.getString(1));

		if( cost > balance ){
			System.out.println("You do not have enough in your balance to order this movie");
			query = "delete from cart;";
			esql.executeUpdate(query);
			output_buffer.add("You do not have enough in your balance to order this movie");
			Output(cartTxt);
			CartScreen.setVisible(true);
			return;
		}
		balance = balance - cost;
		System.out.println("Inserting into orders");
		order_number++;
		String query1 = "INSERT INTO orders VALUES('" + order_number + "','" + videoid;
		query1 += "','" + username + "');";
		esql.executeUpdate(query1);
		System.out.println("updating user balance");
		String query2 = "UPDATE users set balance = " + balance + " where user_id = '" + username + "';";
		esql.executeUpdate(query2);
						
		query2 = "delete from cart;";
		esql.executeUpdate(query2);
		
		query = "insert into watch values('" + username + "','" + videoid + "');";
		esql.executeUpdate(query);
		}
		catch(Exception e){System.out.println(e.getMessage());}	
		CartScreen.setVisible(true);	
	}
	
	public static String totalordercost = "";
	// Comment - Finished
	public static void Comment(final JTextArea WallTxt){
		final JFrame CommentScreen = new JFrame("Comment");
		CommentScreen.setLayout(null);
		CommentScreen.setBounds(0,0,440,240);
				
		final JTextArea CommentTxt = new JTextArea();
		CommentTxt.setEditable(true);
		CommentScreen.add(CommentTxt);
			
		JScrollPane ScrollPane = new JScrollPane(CommentTxt);
		ScrollPane.setBounds(25,25,380,140);
		ScrollPane.setForeground(Color.cyan);
		CommentScreen.add(ScrollPane);
		
		//AddComment
		JButton AddComment = new JButton("Comment");
		AddComment.setSize(125,25);
		AddComment.setLocation(25,170);
		AddComment.setCursor(new Cursor(Cursor.HAND_CURSOR));
		CommentScreen.add(AddComment);
		
		CommentScreen.setVisible(true);
		
		AddComment.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			String input = CommentTxt.getText();
			Comment(esql,input);
			CommentScreen.setVisible(false);
			output_buffer.add( username + " : " + input + "\n");
			Output(WallTxt);
		}
		});
	
	}
	
	public static void ListWall(JTextArea WallTxt)
	{

		try
		{
			output_buffer.clear();
			
			output_buffer.add("Favorite List of" + username + ":\n");
			String query = "select title,year from likes, video where user_id = '" + username + "' and video.video_id = likes.video_id";
			Integer rowCount = esql.executeQuery(query);
			
			output_buffer.add("\n\n" + username + " follows: \n" );
			query = "select user_id_to from follow where user_id_from = '" + username + "';";
			rowCount = esql.executeQuery(query);

			output_buffer.add("\n\n Users who follow you: \n" );
			query = "select user_id_from from follow where user_id_to = '" + username + "';";
			rowCount = esql.executeQuery(query);
			
			output_buffer.add("\n\n Movies Liked by followees \n");
			//query = "select title from video v, follow f, likes l where v.video_id = l.video_id and f.user_id_to = l.user_id and f.user_id_from =  '" + username+ "'";
			query = "select distinct user_id_to, title,year from video v, follow f, likes l where v.video_id = l.video_id and f.user_id_to = l.user_id and f.user_id_from = '" + username + "';";
			rowCount = esql.executeQuery(query);

			
			output_buffer.add("\n\n Movies Watched by followees \n");
			query = "select f.user_id_to, v.title, v.year from watch w, video v, follow f where w.user_id = f.user_id_to and f.user_id_from = '" + username + "' and w.video_id = v.video_id;";
			rowCount = esql.executeQuery(query);
			

			Output(WallTxt);
			
			

		}catch(Exception ev){
			System.err.println (ev.getMessage ());
		}
	}
	
	public static void Follow(final JTextArea WallTxt){
		try{
			final JFrame FollowScreen = new JFrame("Follow");
			FollowScreen.setLayout(null);
			FollowScreen.setBounds(0,0,320,240);
		
			//FollowChoiceLabel
			JLabel FollowChoiceLabel = new JLabel("Select an option");
			FollowChoiceLabel.setSize(100,25);
			FollowChoiceLabel.setLocation(125,15);
			FollowChoiceLabel.setVerticalAlignment(SwingConstants.CENTER);
			FollowScreen.add(FollowChoiceLabel);
		
			//btnFollow
			JButton btnFollow = new JButton("Follow");
			btnFollow.setSize(125,25);
			btnFollow.setLocation(25,50);
			btnFollow.setCursor(new Cursor(Cursor.HAND_CURSOR));
			FollowScreen.add(btnFollow);	
			
			//FollowInput
			final JTextField FollowInput = new JTextField();
			FollowInput.setSize(125,25);
			FollowInput.setLocation(175,50);
			FollowScreen.add(FollowInput);
		
			//btnUnfollow
			JButton btnUnfollow = new JButton("Unfollow");
			btnUnfollow.setSize(125,25);
			btnUnfollow.setLocation(25,100);
			btnUnfollow.setCursor(new Cursor(Cursor.HAND_CURSOR));
			FollowScreen.add(btnUnfollow);
			
			//UnfollowInput
			final JTextField UnfollowInput = new JTextField();
			UnfollowInput.setSize(125,25);
			UnfollowInput.setLocation(175,100);
			FollowScreen.add(UnfollowInput);
			
			//btnReturn
			JButton btnReturn = new JButton("Return");
			btnReturn.setSize(125,25);
			btnReturn.setLocation(100,150);
			btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			FollowScreen.add(btnReturn);	
				
			FollowScreen.setVisible(true);
			
		btnFollow.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			String input = FollowInput.getText();
			UpdateFollow(esql,input);
			FollowScreen.setVisible(false);
			Output(WallTxt);
		}
		});
		
		btnUnfollow.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = UnfollowInput.getText();
			UpdateUnfollow(esql,input);
			FollowScreen.setVisible(false);
			Output(WallTxt);
		}
		});
		
		btnReturn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			
			FollowScreen.setVisible(false);
		}
		});
		}
		catch(Exception e){
		
		}
	}
	
	public static void Genre(final JTextArea WallTxt){
		final JFrame GenreScreen = new JFrame("Genre");
		GenreScreen.setLayout(null);
		GenreScreen.setBounds(0,0,320,240);
		
		//btnAddBalance
		JButton btnAddGenre = new JButton("Add Genre");
		btnAddGenre.setSize(125,25);
		btnAddGenre.setLocation(20,80);
		btnAddGenre.setCursor(new Cursor(Cursor.HAND_CURSOR));
		GenreScreen.add(btnAddGenre);
		
		//AddBalanceInput
		final JTextField AddGenreInput = new JTextField();
		AddGenreInput.setSize(125,25);
		AddGenreInput.setLocation(160,80);
		GenreScreen.add(AddGenreInput);
		
		GenreScreen.setVisible(true);
		
		btnAddGenre.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = AddGenreInput.getText();
			UpdateGenre(esql,input);
			GenreScreen.setVisible(false);
			Output(WallTxt);
		}
		});
	}

	public static void Rate(final JTextArea WallTxt){
		final JFrame RateScreen = new JFrame("Rate");
		RateScreen.setLayout(null);
		RateScreen.setBounds(0,0,320,240);
		
		//btnAddBalance
		JButton btnRate = new JButton("Rate");
		btnRate.setSize(125,25);
		btnRate.setLocation(20,80);
		btnRate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		RateScreen.add(btnRate);
		
		//AddBalanceInput
		final JTextField RateInput = new JTextField();
		RateInput.setSize(125,25);
		RateInput.setLocation(160,80);
		RateScreen.add(RateInput);
		
		RateScreen.setVisible(true);
		
		btnRate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = RateInput.getText();
			UpdateRate(esql,input);
			RateScreen.setVisible(false);
			Output(WallTxt);
		}
		});
	}

	public static void Register(EmbeddedSQL esql,String FirstName,String MiddleName, String LastName, String UserID, String Password, String Email, String Street1, String Street2, String State, String Country, String Zip, String Balance){
   		try{
   		
   			String query = "select user_id from users where user_id = '" + UserID + "';";
   			Integer rowCount = esql.executeQuery(query);
   			System.out.println("Checking if user_id already taken");
   			if( rowCount == 1 ){
   				System.out.println("Username already taken!");
   				final JFrame InvalidUserScreen = new JFrame("Invalid username!");
				InvalidUserScreen.setLayout(null);
				InvalidUserScreen.setBounds(0,0,320,240);
				
				//btnReturn
				JButton btnReturn = new JButton("Return");
				btnReturn.setSize(150,25);
				btnReturn.setLocation(85,100);
				btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				InvalidUserScreen.add(btnReturn);
				
				JLabel InvalidUsername = new JLabel("Username already taken!");
				InvalidUsername.setSize(200,25);
				InvalidUsername.setLocation(80,50);
				InvalidUsername.setVerticalAlignment(SwingConstants.CENTER);
				InvalidUserScreen.add(InvalidUsername);
				
				btnReturn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					InvalidUserScreen.setVisible(false);
					return;
				}
				});
				
				InvalidUserScreen.setVisible(true);
   				//return;
   			}
   			else{
   			query = "insert into users values('" + UserID + "','" + Password + "','" + FirstName + "','" + MiddleName+ "','" + LastName + "','" + Email + "','" + Street1 + "','" + Street2 + "','" + State + "','" + Country + "','" + Zip + "','" + Integer.parseInt(Balance) + "');";
   			esql.executeUpdate(query);
   			System.out.println("User successfully inserted");
   			}
   		}
   		catch(Exception er){
   			System.err.println(er.getMessage());
   		}
	}
   
	public static void SuperUser(){
   		final JFrame AdminScreen = new JFrame("Admin Control Panel");
		AdminScreen.setLayout(null);
		AdminScreen.setBounds(0,0,320,240);
	
		//btnRegisterMovie
		JButton btnRegisterMovie = new JButton("Register Movie");
		btnRegisterMovie.setSize(150,25);
		btnRegisterMovie.setLocation(50,50);
		btnRegisterMovie.setCursor(new Cursor(Cursor.HAND_CURSOR));
		AdminScreen.add(btnRegisterMovie);
		
		JButton btnDeleteUser = new JButton("Delete User");
		btnDeleteUser.setSize(150,25);
		btnDeleteUser.setLocation(50,100);
		btnDeleteUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		AdminScreen.add(btnDeleteUser);
		
		JButton btnDeleteComment = new JButton("Delete Comment");
		btnDeleteComment.setSize(150,25);
		btnDeleteComment.setLocation(50,150);
		btnDeleteComment.setCursor(new Cursor(Cursor.HAND_CURSOR));
		AdminScreen.add(btnDeleteComment);
		
		AdminScreen.setVisible(true);
		
		btnRegisterMovie.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			SuperRegisterNewMovie();
		}
		});
		
		btnDeleteUser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			SuperDeleteUser();
		}
		});
		
		btnDeleteComment.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			SuperDeleteComment();
		}
		});
   	}
   	
   	public static void SuperDeleteUser(){
   		final JFrame SuperDeleteUserScreen = new JFrame("Admin Control Panel - Delete User");
		SuperDeleteUserScreen.setLayout(null);
		SuperDeleteUserScreen.setBounds(0,0,720,480);
		
		final JTextArea CommentTxt = new JTextArea();
		CommentTxt.setEditable(false);
		SuperDeleteUserScreen.add(CommentTxt);
		
		JScrollPane ScrollPane = new JScrollPane(CommentTxt);
		ScrollPane.setBounds(25,25,400,400);
		ScrollPane.setForeground(Color.cyan);
		SuperDeleteUserScreen.add(ScrollPane);
		
		final JTextArea UserID = new JTextArea();
		UserID.setSize(50,25);
		UserID.setLocation(625,75);
		//CommentID.setVerticalAlignment(SwingConstants.CENTER);
		SuperDeleteUserScreen.add(UserID);
		
		//btnDeleteComment
		JButton btnDeleteUser = new JButton("Delete user");
		btnDeleteUser.setSize(150,25);
		btnDeleteUser.setLocation(450,75);
		btnDeleteUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		SuperDeleteUserScreen.add(btnDeleteUser);	
		
		try{
			output_buffer.clear();
			String query = "select user_id from users;";
			Integer rowCount = esql.executeQuery(query);
			Output(CommentTxt);
		}
		catch(Exception er){
			System.err.println(er.getMessage());
		}
		
		
		btnDeleteUser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = UserID.getText();
			deleteUser(esql,input,CommentTxt);
		}
		});
		
		SuperDeleteUserScreen.setVisible(true);
   	}
   	
   	public static void SuperDeleteComment(){
   		final JFrame SuperDeleteCommentScreen = new JFrame("Admin Control Panel - Delete Comment");
		SuperDeleteCommentScreen.setLayout(null);
		SuperDeleteCommentScreen.setBounds(0,0,720,480);
		
		final JTextArea CommentTxt = new JTextArea();
		CommentTxt.setEditable(false);
		SuperDeleteCommentScreen.add(CommentTxt);
		
		JScrollPane ScrollPane = new JScrollPane(CommentTxt);
		ScrollPane.setBounds(25,25,400,400);
		ScrollPane.setForeground(Color.cyan);
		SuperDeleteCommentScreen.add(ScrollPane);
		
		final JTextArea CommentID = new JTextArea();
		CommentID.setSize(50,25);
		CommentID.setLocation(625,25);
		SuperDeleteCommentScreen.add(CommentID);
		
		//btnDeleteComment
		JButton btnDeleteComment = new JButton("Delete comment");
		btnDeleteComment.setSize(150,25);
		btnDeleteComment.setLocation(450,25);
		btnDeleteComment.setCursor(new Cursor(Cursor.HAND_CURSOR));
		SuperDeleteCommentScreen.add(btnDeleteComment);	
		
		final JTextArea UserID = new JTextArea();
		UserID.setSize(50,25);
		UserID.setLocation(625,75);
		//CommentID.setVerticalAlignment(SwingConstants.CENTER);
		SuperDeleteCommentScreen.add(UserID);
		
		//btnDeleteComment
		JButton btnDeleteUser = new JButton("Delete user");
		btnDeleteUser.setSize(150,25);
		btnDeleteUser.setLocation(450,75);
		btnDeleteUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		SuperDeleteCommentScreen.add(btnDeleteUser);	
		
		try{
			output_buffer.clear();
			String query = "select user_id, comment_id, video_id, content from comment;";
			Integer rowCount = esql.executeQuery(query);
			Output(CommentTxt);
		}
		catch(Exception er){
			System.err.println(er.getMessage());
		}
		
		
		btnDeleteComment.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = CommentID.getText();
			deleteComment(esql,input,CommentTxt);
		}
		});
		
		
		btnDeleteUser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String input = UserID.getText();
			deleteUser(esql,input,CommentTxt);
		}
		});
		
		SuperDeleteCommentScreen.setVisible(true);
   	}
   	
   	public static void SuperRegisterNewMovie(){
   		
   		      final JFrame RegisterMovieScreen = new JFrame("Admin Control Panel - Register new movie");
        RegisterMovieScreen.setLayout(null);
        RegisterMovieScreen.setBounds(0,0,640,480);
   
        //btnRegisterMovie
        JButton btnRegisterMovie = new JButton("Register Movie");
        btnRegisterMovie.setSize(150,25);
        btnRegisterMovie.setLocation(25,25);
        btnRegisterMovie.setCursor(new Cursor(Cursor.HAND_CURSOR));
        RegisterMovieScreen.add(btnRegisterMovie);
       
        //RegMovieTitle
        JLabel RegMovieTitle = new JLabel("Movie Title");
        RegMovieTitle.setSize(125,25);
        RegMovieTitle.setLocation(50,100);
        RegMovieTitle.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegMovieTitle);
       
        //RegMovieTitleInput
        final JTextField RegMovieTitleInput = new JTextField();
        RegMovieTitleInput.setSize(125,25);
        RegMovieTitleInput.setLocation(150,100);
        RegisterMovieScreen.add(RegMovieTitleInput);
       
        //RegMovieOnlinePrice
        JLabel RegMovieOnlinePrice = new JLabel("Online Price");
        RegMovieOnlinePrice.setSize(100,25);
        RegMovieOnlinePrice.setLocation(50,125);
        RegMovieOnlinePrice.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegMovieOnlinePrice);
       
        //RegMovieOnlinePriceInput
        final JTextField RegMovieOnlinePriceInput = new JTextField();
        RegMovieOnlinePriceInput.setSize(125,25);
        RegMovieOnlinePriceInput.setLocation(150,125);
        RegisterMovieScreen.add(RegMovieOnlinePriceInput);
       
        //RegMovieDVDPrice
        JLabel RegMovieDVDPrice = new JLabel("DVD Price");
        RegMovieDVDPrice.setSize(100,25);
        RegMovieDVDPrice.setLocation(50,150);
        RegMovieDVDPrice.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegMovieDVDPrice);
       
        //RegMovieDVDPriceInput
        final JTextField RegMovieDVDPriceInput = new JTextField();
        RegMovieDVDPriceInput.setSize(125,25);
        RegMovieDVDPriceInput.setLocation(150,150);
        RegisterMovieScreen.add(RegMovieDVDPriceInput);
       
        //RegMovieGenre
        JLabel RegMovieGenre = new JLabel("Genre");
        RegMovieGenre.setSize(100,25);
        RegMovieGenre.setLocation(50,175);
        RegMovieGenre.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegMovieGenre);
       
        //RegMovieGenreInput
        final JTextField RegMovieGenreInput = new JTextField();
        RegMovieGenreInput.setSize(125,25);
        RegMovieGenreInput.setLocation(150,175);
        RegisterMovieScreen.add(RegMovieGenreInput);
       
        //RegSeriesMovie
        JLabel RegSeriesMovie = new JLabel("Series/Movie");
        RegSeriesMovie.setSize(100,25);
        RegSeriesMovie.setLocation(50,200);
        RegSeriesMovie.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegSeriesMovie);
       
        //RegSeriesMovieInput
        final JTextField RegSeriesMovieInput = new JTextField();
        RegSeriesMovieInput.setSize(125,25);
        RegSeriesMovieInput.setLocation(150,200);
        RegisterMovieScreen.add(RegSeriesMovieInput);
       
        //RegYear
        JLabel RegYear = new JLabel("Year");
        RegYear.setSize(100,25);
        RegYear.setLocation(50,225);
        RegYear.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegYear);
       
        //RegYearInput
        final JTextField RegYearInput = new JTextField();
        RegYearInput.setSize(125,25);
        RegYearInput.setLocation(150,225);
        RegisterMovieScreen.add(RegYearInput);
       
        //RegRate
        JLabel RegRate = new JLabel("Rate");
        RegRate.setSize(100,25);
        RegRate.setLocation(50,250);
        RegRate.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegRate);
       
        //RegRateInput
        final JTextField RegRateInput = new JTextField();
        RegRateInput.setSize(125,25);
        RegRateInput.setLocation(150,250);
        RegisterMovieScreen.add(RegRateInput);

        //RegEpisode
        JLabel RegEpisode = new JLabel("Episode");
        RegEpisode.setSize(100,25);
        RegEpisode.setLocation(50,275);
        RegEpisode.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegEpisode);
       
        //RegEpisodeInput
        final JTextField RegEpisodeInput = new JTextField();
        RegEpisodeInput.setSize(125,25);
        RegEpisodeInput.setLocation(150,275);
        RegisterMovieScreen.add(RegEpisodeInput);
       
        //RegSeason
        JLabel RegSeason = new JLabel("Season");
        RegSeason.setSize(100,25);
        RegSeason.setLocation(50,300);
        RegSeason.setVerticalAlignment(SwingConstants.CENTER);
        RegisterMovieScreen.add(RegSeason);
       
        //RegSeasonInput
        final JTextField RegSeasonInput = new JTextField();
        RegSeasonInput.setSize(125,25);
        RegSeasonInput.setLocation(150,300);
        RegisterMovieScreen.add(RegSeasonInput);
       
        RegisterMovieScreen.setVisible(true);
       
        btnRegisterMovie.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            String newMovieTitle = RegMovieTitleInput.getText();
            String OnlinePrice = RegMovieOnlinePriceInput.getText();
            String DVDPrice = RegMovieDVDPriceInput.getText();
            String Genre = RegMovieGenreInput.getText();
            String SeriesMovie = RegSeriesMovieInput.getText();
            String newYear = RegYearInput.getText();
            String Season = RegSeasonInput.getText();
            String Episode = RegEpisodeInput.getText();
            String Rate = RegRateInput.getText();
            
            RegisterNewMovie(esql,newMovieTitle,newYear,OnlinePrice,DVDPrice,Rate,Genre,SeriesMovie,Season,Episode );
        }
        });
   	}
   		
	public static void Output(JTextArea WallTxt)
	{
		try
		{
			//WallTxt.setText("");
			for(int i = 0; i < output_buffer.size();i++){
				WallTxt.append(output_buffer.get(i));
			}
			
		}catch(Exception ev){
			System.err.println (ev.getMessage ());
		}
	}
						
	public static boolean Login(EmbeddedSQL esql, String UsernameInput, String PasswordInput){
   		try{
			String query = "Select password from users where user_id = '";
			query+= UsernameInput + "' AND password = '" + PasswordInput + "';";
			Integer rowCount = esql.executeQuery(query);
			String password = rs.getString(1);
		
			if(PasswordInput.equals( password)) {return true;}
			else {return false;}
    
   		}catch(Exception e){
      			System.err.println (e.getMessage ());
      			return false;
    		}
	}
	
	
	// General Seach query
	public static String onlineprice = "";
	public static String dvdprice = "";
	public static String movieTitle = "";
	public static void Search(EmbeddedSQL esql,String input){
	//String input = "";
     try{
	System.out.println("Enter movie/author/actor/director/user:");
	//input = in.readLine();

	String query = "Select video_id from video where title = '" + input + "';";

	String[] token = input.split(" ");

	String query1 = "Select author_id from author where first_name = '" +token[0]+ "' and last_name = '" +token[1]+ "';";

	String query2 = "Select star_id from star where first_name = '" +token[0]+ "' and last_name = '" +token[1]+ "';";

	String query3 = "Select director_id from director where first_name = '" +token[0]+ "' and last_name = '" +token[1]+ "';";
	
	//String query4 = "Select user_id from users where user_id = '" + input + "';";

	if(esql.executeQuery(query) == 1){
		output_buffer.clear();
		System.out.println(input + "is a Movie");
		output_buffer.add(input + " is a Movie\n");
		query = "Select * from video where title = '";
		query+= input + "';";
		movieTitle = input;
		Integer rowCount = esql.executeQuery(query);

		System.out.println("\n Genre");
		output_buffer.add("\n--Genre--\n");
		query = "select genre_name from genre g, video v, categorize c where title = '" + input + "' and v.video_id = c.video_id and c.genre_id = g.genre_id;"; 
		Integer rowCount4 = esql.executeQuery(query);

		System.out.println ("\n Director");
		output_buffer.add("\n--Director--\n");
		query = "select first_name, last_name from director d, video v, directed p where title = '" + input + "' and v.video_id = p.video_id and p.director_id = d.director_id;";
		Integer rowCount1 = esql.executeQuery(query);

		System.out.println ("\n Authors");
		output_buffer.add("\n--Authors--\n");
 		query = "select first_name, last_name from  author a, video v, written w where title = '" + input + "' and v.video_id = w.author_id and w.author_id = a.author_id;";
		Integer rowCount2 = esql.executeQuery(query);

		System.out.println ("\n Actors");
		output_buffer.add("\n--Actors--\n");
		query = "select first_name, last_name from star s, video v, played p where title = '" + input + "' and v.video_id = p.video_id and p.star_id = s.star_id;";
		Integer rowCount3 = esql.executeQuery(query);
	}
	if(esql.executeQuery(query1) == 1){
	output_buffer.clear();
		System.out.println(input + "is an Author");
		output_buffer.add(input + " is a Author\n");
		String authorid = rs.getString(1);
        	query1 = "select video_id from written where author_id = '" + authorid + "';";
       		esql.executeQuery(query1);
        	String videoid = rs.getString(1);
        	System.out.println("Grabbing video info");
        	output_buffer.add("\n--Movie-- \n");
        	query1 = "select title,year,online_price,dvd_price,votes,rating from video where video_id = '" + videoid + "';";
        	esql.executeQuery(query1);
	}

	if(esql.executeQuery(query2) == 1){
	output_buffer.clear();
		System.out.println(input + " is an Actor");
		output_buffer.add(input + " is a Actor\n");
		String starid = rs.getString(1);
		query2 = "Select v.title from star s, video v, played p where first_name = '" +token[0]+ "' and last_name = '" +token[1]+ "' and s.star_id = p.star_id AND p.video_id = v.video_id;";
		System.out.println( query2);
		Integer rowCount = esql.executeQuery(query2);

		String movieTitle = rs.getString(1);

		System.out.println ("\n--Movies");
		output_buffer.add("\n--Movies-- \n");
		query2 = "Select * from video where title = '";
		query2 += movieTitle + "';";
		Integer rowCount3 = esql.executeQuery(query2);

		System.out.println ("\n Director");
		output_buffer.add("\n--Director--\n");
		query2 = "select first_name, last_name from director d, video v, directed p where title = '" + movieTitle + "' and v.video_id = p.video_id and p.director_id = d.director_id;";
		Integer rowCount1 = esql.executeQuery(query2);

		System.out.println ("\n Authors");
		output_buffer.add("\n--Authors--\n");
 		query2 = "select first_name, last_name from  author a, video v, written w where title = '" + movieTitle + "' and v.video_id = w.video_id and w.author_id = a.author_id;";
		Integer rowCount2 = esql.executeQuery(query2);
	}

	if(esql.executeQuery(query3) == 1){
	output_buffer.clear();
		System.out.println(input + " is a Director");
		output_buffer.add(input + " is a Director\n");
		query3 = "Select title from video, director d,directed dd where first_name = '";
		query3 += token[0] + "' AND last_name = '" + token[1] + "' and d.director_id = dd.director_id and dd.video_id = video.video_id;";
		
		Integer rowCount = esql.executeQuery(query3);
		
		String movieTitle = rs.getString(1);
		
		System.out.println ("\n Movies");
		output_buffer.add("\n--Movies--\n");
		System.out.println (movieTitle);
		query3 = "Select * from video where title = '" + movieTitle + "';";
		Integer rowCount3 = esql.executeQuery(query3);

		System.out.println ("\n Actors");
		output_buffer.add("\n--Actors--\n");
		query3 = "select first_name, last_name from star s, video v, played p where title = '" + movieTitle + "' and v.video_id = p.video_id and p.star_id = s.star_id;";
		Integer rowCount1 = esql.executeQuery(query3);

		System.out.println ("\n Authors");
		output_buffer.add("\n--Authors--\n");
 		query3 = "select first_name, last_name from  author a, video v, written w where title = '" + movieTitle + "' and v.video_id = w.video_id and w.author_id = a.author_id;";
		Integer rowCount2 = esql.executeQuery(query3);

	}

	/*if(esql.executeQuery(query4) == 1){
		System.out.println(input + " is a User");
		query4 = "Select user_id,first_name,middle_name,last_name,e_mail from users where user_id = '" + input + "';";
		Integer rowCount = esql.executeQuery(query4);
	}
*/
     }
     catch(Exception e){
 		System.err.println (e.getMessage());// + input + " Not found");
     }
   }
   
   public static int order_number;

public static void Order(EmbeddedSQL esql,String ordertype, String dvdquantity){
	try{

		String query0 = "select video_id from video v where title = '" + movieTitle + "';";
		Integer rowCount = esql.executeQuery(query0);
		String videoid = rs.getString(1);

		int cost = 0;
		
		if( ordertype.equals("Online")){
			query0 = "select online_price from video where video_id = '" + videoid + "';";
			Integer rowCount2 = esql.executeQuery(query0);
			cost = Integer.parseInt(rs.getString(1));
			//onlineprice = Integer.toString(cost);
			query0 = "select video_id from cart;";
			Integer rowCountt = esql.executeQuery(query0);
			/*for(int i = 0; i < rowCountt; i++){
				if(rs.getString(i).equals(videoid)){
				System.out.println("already ordered");
				return;
				}
			}*/
			query0 = "insert into cart values('"+videoid+"','"+username+"',"+cost+") ;";
			esql.executeUpdate(query0);
		}
		else {
			query0 = "select dvd_price from video where video_id = '" + videoid + "';";
			Integer rowCount3 = esql.executeQuery(query0);
			cost = Integer.parseInt(rs.getString(1));
			dvdprice = Integer.toString(cost);
			cost *= Integer.parseInt(dvdquantity);
			query0 = "select video_id from cart;";
			Integer rowCountt = esql.executeQuery(query0);
			/*for(int i = 0; i < rowCountt; i++){
				if(rs.getString(i).equals(videoid)){
				System.out.println("already ordered");
				return;
				}
			}*/
			query0 = "insert into cart values('"+videoid+"','"+username+"',"+cost+") ;";
			esql.executeUpdate(query0);
		}
	}
	catch(Exception e){
 		System.err.println ("error: " + e.getMessage());
	}
   }
   
   public static int comment_number = 0;		
   public static void Comment(EmbeddedSQL esql,String comment){
	try{
	
		 //System.out.println("1");
   		String get_comment = "select comment_id from comment;";
        	if(esql.executeQuery(get_comment) != 0){
            //System.out.println("2");
            	get_comment = "select max(comment_id) from comment;";
            	Integer rowCount = esql.executeQuery(get_comment);
              	String max_comment = rs.getString(1);
               	comment_number = Integer.parseInt(max_comment);
           	}
       	 	else {
            //System.out.println("3");
            	comment_number = 0; }
        	System.out.println("Enter movie title to comment on:");
        	//String movieTitle = in.readLine();
   
        	System.out.println("Enter comment:");
        	//String comment = in.readLine();
       
        	String query0 = "select video_id from video v where title = '" + movieTitle + "';";
        	Integer rowCount = esql.executeQuery(query0);
        	String videoid = rs.getString(1);
       
        	System.out.println("inserting into comment");
        	comment_number++;
        	String query1 = "INSERT INTO comment VALUES('" + comment_number + "','" + username;
        	query1 += "','" + videoid + "',CURRENT_TIMESTAMP,'" + comment + "');";
        	esql.executeUpdate(query1);
        
		
		output_buffer.clear();
		output_buffer.add("Comment successful.\n");
	}
	catch(Exception e){
 		System.err.println ("error: " + e.getMessage());
 		output_buffer.clear();
		output_buffer.add("Adding of comment has failed.\n");
	}
   }
   
   public static void UpdateBalance(EmbeddedSQL esql,String input){
	try{
		System.out.println("grabbing current balance)");
		String query = "select balance from users where user_id = '" + username + "';";
		Integer row = esql.executeQuery(query);
		Integer currentBalance = Integer.parseInt(rs.getString(1));
		
		System.out.println("updating balance)");
		Integer newBalance = currentBalance + Integer.parseInt(input);
		query = "update users set balance = '" + newBalance + "' where user_id = '" + username + "';";
		esql.executeUpdate(query);
		
		output_buffer.clear();
		output_buffer.add(input + " has been added to your balance.\n");
				
	}
	catch(Exception e){
 		System.out.println ("Username not found!");
 		output_buffer.clear();
		output_buffer.add("Updating of balance has failed.\n");
	}
   }
   
   public static void UpdateFollow(EmbeddedSQL esql, String userfollow){
	try{
	System.out.println("Enter Username to follow: ");
	//String userfollow = in.readLine();
	String query = "Insert into follow values('"+ userfollow + "', '" + username + "', current_timestamp);";
	esql.executeUpdate(query);
	
	output_buffer.clear();
	output_buffer.add("You are now following " + userfollow + "\n");
	}
 	catch(Exception e){
	System.err.println(e.getMessage());
	output_buffer.clear();
	output_buffer.add("You are already following " + userfollow + "!\n");
	}
   }
   
   public static void UpdatePermission(EmbeddedSQL esql, String block){
	try{
		String query = "delete from follow where user_id_to = '"+ username + "' and user_id_from = '" + block + "';";
		esql.executeUpdate(query);
	}
	catch(Exception e){System.err.println(e.getMessage());}
   }

   public static void UpdateUnfollow(EmbeddedSQL esql, String userfollow){
	try{
	
	System.out.println("Enter Username to unfollow: ");
	//userfollow = in.readLine();
	String query = "delete from follow where user_id_to = '"+ userfollow + "' and user_id_from = '" + username + "';";
	esql.executeUpdate(query);

	output_buffer.clear();
	output_buffer.add("You have unfollowed " + userfollow + "\n");
	}
 	catch(Exception e){
		System.err.println(e.getMessage());
		output_buffer.clear();
		output_buffer.add("You are not following " + userfollow + "!\n");
	}
   }
   
   // Like - Column out of index
   public static void UpdateLike(EmbeddedSQL esql){
      try{
        System.out.println("Enter movie title: ");
       // String movieTitle = in.readLine();
        System.out.println("grabbing video_id");
        String query1 = "select video_id from video where title = '" + movieTitle + "';";
        esql.executeQuery(query1);
        String videoid = rs.getString(1);
      
        query1 = "select video_id from likes;";
        Integer rowCount_likes = esql.executeQuery(query1);
      
        System.out.println("checking to see if movie already liked");

        System.out.println("inserting into video");
        query1 = "INSERT INTO likes VALUES('" + username + "','" + Integer.parseInt(videoid) + "');";
        esql.executeUpdate(query1);
        
        output_buffer.clear();
        output_buffer.add(movieTitle + " successfuly liked.\n");
    }
    catch(Exception e){
         	
         System.out.println( e.getMessage());
         output_buffer.clear();
         output_buffer.add(movieTitle + " already liked.\n");
  	 }
   }
   
    public static void UpdateGenre(EmbeddedSQL esql,String genre){
     try{
        System.out.println("Enter genre: ");
           
        System.out.println("grabbing genre_id");
        String query1 = "select genre_id from genre where genre_name = '" + genre + "';";
        esql.executeQuery(query1);
        String genreid = rs.getString(1);
       
        query1 = "select genre_id from prefers;";
        Integer rowCount_genre = esql.executeQuery(query1);
       
  
            
        System.out.println("inserting into prefers");
        query1 = "INSERT INTO prefers VALUES('" + username + "','" + Integer.parseInt(genreid) + "');";
        esql.executeUpdate(query1);
        
         output_buffer.clear();
        output_buffer.add(genre + " successfuly preferred.\n");
    }
    catch(Exception e){
         System.out.println("Update genre failed!");
         output_buffer.clear();
         output_buffer.add(genre + " already liked.\n");
         System.out.println( e.getMessage());
    }
   }
   
   public static void UpdateRate(EmbeddedSQL esql,String rating){
	try{
	
		System.out.println("Enter movie title to rate:");
		//String movieTitle = in.readLine();

		String query0 = "select video_id from video v where title = '" + movieTitle + "';";
		Integer rowCount = esql.executeQuery(query0);
		String videoid = rs.getString(1);
		
		String query = "select video_id from rate;";
		//Integer rowCount_video = esql.executeQuery(query);
		System.out.println("Enter rating 1-10:");
		
		query = "delete from rate where user_id = '" + username + "' and video_id = '" + videoid + "';";
		esql.executeUpdate(query);
				
		System.out.println("Inserting into table" );
		String query1 = "INSERT INTO rate VALUES('" + username;
		query1 += "','" + videoid + "',CURRENT_TIMESTAMP,'" + rating + "');";
		esql.executeUpdate(query1);
		
		System.out.println("calculating average");
		query = "select cast(avg as int) from (SELECT AVG(rating) FROM rate where video_id = '" + videoid + "') as a;";
		rowCount = esql.executeQuery(query);
		
		System.out.println("Updating average rating: ");
		Integer avg = Integer.parseInt(rs.getString(1));
		query = "update video set rating = '" + avg + "' where video_id = '" + videoid + "';";
		esql.executeUpdate(query);
		
		output_buffer.clear();
		output_buffer.add("You have rated " + movieTitle + " a " + rating + "\n");
	}
	catch(Exception e){
 		System.err.println ("error: " + e.getMessage());
 		output_buffer.clear();
		output_buffer.add("Rate Failed\n");
	}
   }   
   
   //RegisterNewMovie(esql,newMovieTitle,OnlinePrice,DVDPrice,Genre,SeriesMovie );
   public static void RegisterNewMovie(EmbeddedSQL esql ,String newMovieTitle, String newYear, String OnlinePrice, String DVDPrice, String Rate,String Genre, String SeriesMovie,String Season,String Episode){
   	try{
   		String query = "select max(video_id) from video;";
   		Integer rowCount = esql.executeQuery(query);
   		Integer videoid = Integer.parseInt(rs.getString(1));
   		videoid++;
   		
   		if( SeriesMovie.equals("Movie")){
   			query = "insert into video values('" + videoid + "','" + newMovieTitle + "','" + Integer.parseInt(newYear) + "','" + Integer.parseInt(OnlinePrice) + "','" + Integer.parseInt(DVDPrice) + "','0','" + Integer.parseInt(Rate) + "',null,null);";
   		esql.executeUpdate(query);
   		}
   		else if( SeriesMovie.equals("Series") ){
	
			
   		}
   		
   		query = "select genre_id from genre where genre_name = '" + Genre + "';";
   		rowCount = esql.executeQuery(query);
   		Integer genreid = Integer.parseInt(rs.getString(1));
   		
   		query = "select video_id from video where title = '" + newMovieTitle + "';";
   		rowCount = esql.executeQuery(query);
   		videoid = Integer.parseInt(rs.getString(1));
   		
   		query = "insert into categorize values('" + videoid + "','" + genreid + "');";
   		esql.executeUpdate(query);
   		
   	}
   	catch(Exception er){
   		System.err.println(er.getMessage());
   	}
   
   
   }
   
   public static void UpdateWatch(EmbeddedSQL esql){
   	try{
   		String query = "select video_id from video where title = '" + movieTitle + "';";
   		Integer rowCount = esql.executeQuery(query);
   		Integer videoid = Integer.parseInt(rs.getString(1));
   		
   		query = "insert into watch values('" + username + "','" + videoid + "');";
   		esql.executeUpdate(query);
   		
   	}	
   	catch(Exception er){
   		System.err.println(er.getMessage());
   	}
   }
   
   public static void deleteComment(EmbeddedSQL esql, String input,JTextArea CommentTxt){
   	try{
   		String query = "delete from comment where comment_id = '" + input + "';";
   		esql.executeUpdate(query);
   		
   		try{
			output_buffer.clear();
			String query1 = "select user_id, comment_id, video_id, content from comment;";
			Integer rowCount = esql.executeQuery(query1);
		}
		catch(Exception err){
			System.err.println(err.getMessage());
		}
   		
   		output_buffer.add("Comment " + input + " has been successfully deleted.\n");
   		CommentTxt.setText("");
   		Output(CommentTxt);
   	}
   	catch(Exception er){
   		output_buffer.add("Comment deletion failed.\n");
   	}
   }
   
    public static void deleteUser(EmbeddedSQL esql, String input,JTextArea CommentTxt){
   	try{
   		if( input.equals(username)){
   			output_buffer.clear();
   			output_buffer.add("Cannot delete yourself!\n");
   			Output(CommentTxt);
   			return;
   		}
   		else{
   		String query = "delete from users where user_id = '" + input + "';";
   		esql.executeUpdate(query);
   		
   		try{
			output_buffer.clear();
			String query1 = "select user_id from users;";
			Integer rowCount = esql.executeQuery(query1);
		}
		catch(Exception err){
			System.err.println(err.getMessage());
		}
   		
   		output_buffer.add("User " + input + " has been successfully deleted.\n");
   		CommentTxt.setText("");
   		Output(CommentTxt);
   		}
   	}
   	catch(Exception er){
   		output_buffer.add("User deletion failed.\n");
   	}
   }
}//end EmbeddedSQL
