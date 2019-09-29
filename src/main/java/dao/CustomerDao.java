package dao;

import java.sql.*;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.*;

import model.Customer;
import model.Location;

public class CustomerDao {
	/*
	 * This class handles all the database operations related to the customer table
	 */

	private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
	private String user= "andliu";
	private String pw = "111473795";

    public Customer getDummyCustomer() {
        Location location = new Location();
        location.setZipCode(11790);
        location.setCity("Stony Brook");
        location.setState("NY");

        Customer customer = new Customer();
        customer.setId("111-11-1111");
        customer.setAddress("123 Success Street");
        customer.setLastName("Lu");
        customer.setFirstName("Shiyong");
        customer.setEmail("shiyong@cs.sunysb.edu");
        customer.setLocation(location);
        customer.setTelephone("5166328959");
        customer.setCreditCard("1234567812345678");
        customer.setRating(1);

        return customer;
    }
    public List<Customer> getDummyCustomerList() {
        /*Sample data begins*/
        List<Customer> customers = new ArrayList<Customer>();

        for (int i = 0; i < 10; i++) {
            customers.add(getDummyCustomer());
        }
		/*Sample data ends*/

        return customers;
    }

    /**
	 * @param String searchKeyword
	 * @return ArrayList<Customer> object
	 */
	public List<Customer> getCustomers(String searchKeyword) {
		/*
		 * This method fetches one or more customers based on the searchKeyword and returns it as an ArrayList
		 */


		/*
		 * The students code to fetch data from the database based on searchKeyword will be written here
		 * Each record is required to be encapsulated as a "Customer" class object and added to the "customers" List
		 */

		List<Customer> customers = new ArrayList<Customer>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Clients C, Person P, Location L WHERE C.Id = P.SSN AND P.ZipCode = L.ZipCode");
			
			while(rs.next()) {
				Customer customer = new Customer();
				customer.setClientId(rs.getString("Id"));
				customer.setAddress(rs.getString("Address"));
				customer.setLastName(rs.getString("LastName"));
				customer.setFirstName(rs.getString("FirstName"));
				Location location = new Location();
				location.setCity(rs.getString("City"));
				location.setState(rs.getString("State"));
				location.setZipCode(rs.getInt("ZipCode"));
				customer.setLocation(location);
				customer.setEmail(rs.getString("Email"));
				customer.setTelephone(rs.getString("Telephone"));
				customer.setCreditCard(rs.getString("CreditCardNumber"));
				customer.setRating(rs.getInt("Rating"));
				customers.add(customer);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

		return customers;
		//return getDummyCustomerList();
	}


	public Customer getHighestRevenueCustomer() {
		/*
		 * This method fetches the customer who generated the highest total revenue and returns it
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */

		Customer customer = new Customer();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(user,url,pw);
			Statement st = con.createStatement();
  /*String query = "Create View COrder (StockSymbol, StockType, LastName, FirstName, Fee) AS" +
        "Select S.StockSymbol, S.Type, P.LastName, P.FirstName, T.Fee" +
        "From Trade Tr, Stock S, Transactions T, Person P" +
        "Where Tr.StockId = S.StockSymbol and T.Id = Tr.TransactionId and P.SSN = Tr.AccountID;" +
        "Create View CRevenue (LastName, FirstName, Revenue) AS" +
        "Select LastName, FirstName, SUM(Fee)" +
        "From COrder" +
        "GROUP BY LastName, FirstName;" +
        "Create View HighestRevenue (MaxRevenue) AS" +
        "Select MAX(Revenue)" +
        "From CRevenue;" +
        "Select R.LastName, R.FirstName" +
        "From CRevenue R, COrder O, HighestRevenue H" +
        "Where R.Revenue >= H.MaxRevenue;";*/

			String query = "Create View COrder (StockSymbol, StockType, LastName, FirstName, Fee, SSN) AS " +
					"Select S.StockSymbol, S.Type, P.LastName, P.FirstName, T.Fee, P.SSN " +
					"From Trade Tr, Stock S, Transactions T, Person P, Accounts A " +
					"Where Tr.StockId = S.StockSymbol and T.Id = Tr.TransactionId and A.ClientId = P.SSN and A.AccountId = Tr.AccountId";

			int rs = st.executeUpdate(query);

			String query2 = "Create View CRevenue (LastName, FirstName, Revenue, SSN) AS " +
					"Select LastName, FirstName, SUM(Fee), SSN " +
					"From COrder " +
					"GROUP BY LastName, FirstName";
			int rs2 = st.executeUpdate(query2);

			String query3 = "Create View HighestRevenue (MaxRevenue, SSN) AS " +
					"Select MAX(Revenue), SSN " +
					"From CRevenue";
			int rs3 = st.executeUpdate(query3);

			String query4 = "SELECT P.LastName, P.FirstName, R.SSN, C.Id, P.Address, L.City, L.State, P.ZipCode, P.Telephone, C.Email, C.CreditCardNumber, C.Rating " +
					"FROM CRevenue R, COrder O, HighestRevenue H, Person P, Clients C, Location L " +
					"WHERE R.Revenue >= H.MaxRevenue AND R.SSN = C.Id AND R.SSN = P.SSN AND P.ZipCode = L.ZipCode";
			ResultSet rs4 = st.executeQuery(query4);

			rs4.next();
			customer.setClientId(rs4.getString("Id"));
			customer.setAddress(rs4.getString("Address"));
			customer.setLastName(rs4.getString("LastName"));
			customer.setFirstName(rs4.getString("FirstName"));
			Location location = new Location();
			location.setCity(rs4.getString("City"));
			location.setState(rs4.getString("State"));
			location.setZipCode(rs4.getInt("ZipCode"));
			customer.setLocation(location);
			customer.setEmail(rs4.getString("Email"));
			customer.setTelephone(rs4.getString("Telephone"));
			customer.setCreditCard(rs4.getString("CreditCardNumber"));
			customer.setRating(rs4.getInt("Rating"));

			String query5 = "DROP VIEW COrder";
			int rs5 = st.executeUpdate(query5);

			String query6 = "DROP VIEW CRevenue";
			int rs6 = st.executeUpdate(query6);

			String query7 = "DROP VIEW HighestRevenue";
			int rs7 = st.executeUpdate(query7);

		}
		catch(Exception e) {
			System.out.println(e);
		}

//return getDummyCustomer();
		return customer;
	}

	public Customer getCustomer(String customerID) {

		/*
		 * This method fetches the customer details and returns it
		 * customerID, which is the Customer's ID who's details have to be fetched, is given as method parameter
		 * The students code to fetch data from the database will be written here
		 * The customer record is required to be encapsulated as a "Customer" class object
		 */

		Customer customer = new Customer();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			String query = String.format("SELECT * FROM Clients C, Person P, Location L WHERE C.Id = %d AND C.Id = P.SSN AND P.ZipCode = L.ZipCode", Integer.parseInt(customerID));
			ResultSet rs = st.executeQuery(query);

			rs.next();
			customer.setClientId(rs.getString("Id"));
			customer.setAddress(rs.getString("Address"));
			customer.setLastName(rs.getString("LastName"));
			customer.setFirstName(rs.getString("FirstName"));
			Location location = new Location();
			location.setCity(rs.getString("City"));
			location.setState(rs.getString("State"));
			location.setZipCode(rs.getInt("ZipCode"));
			customer.setLocation(location);
			customer.setEmail(rs.getString("Email"));
			customer.setTelephone(rs.getString("Telephone"));
			customer.setCreditCard(rs.getString("CreditCardNumber"));
			customer.setRating(rs.getInt("Rating"));
		}
		catch(Exception e) {
			System.out.println(e);
		}

		//return getDummyCustomer();
		return customer;
	}
	
	public String deleteCustomer(String customerID) {

		/*
		 * This method deletes a customer returns "success" string on success, else returns "failure"
		 * The students code to delete the data from the database will be written here
		 * customerID, which is the Customer's ID who's details have to be deleted, is given as method parameter
		 */


		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			String query = String.format("DELETE FROM Clients WHERE Id = '%s'", customerID);
			int rs = st.executeUpdate(query);

			String query2 = String.format("DELETE FROM Person WHERE SSN = '%s'", customerID);
			int rs2 = st.executeUpdate(query2);

			return "success";
		}
		catch(Exception e) {
			System.out.println(e);

			return "failure";
		}

		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/
	}


	public String getCustomerID(String email) {
		/*
		 * This method returns the Customer's ID based on the provided email address
		 * The students code to fetch data from the database will be written here
		 * username, which is the email address of the customer, who's ID has to be returned, is given as method parameter
		 * The Customer's ID is required to be returned as a String
		 */

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			String query = String.format("SELECT Id FROM Clients WHERE Email = '%s'", email);
			ResultSet rs = st.executeQuery(query);

			rs.next();
			return "" + rs.getInt("Id");
		}
		catch(Exception e) {
			System.out.println(e);

			return "";
		}

		//return "111-11-1111";
	}


	public String addCustomer(Customer customer) {

		/*
		 * All the values of the add customer form are encapsulated in the customer object.
		 * These can be accessed by getter methods (see Customer class in model package).
		 * e.g. firstName can be accessed by customer.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the customer details and return "success" or "failure" based on result of the database insertion.
		 */


		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();

			String locQuery = "SELECT * FROM Location";
			ResultSet rsLoc = st.executeQuery(locQuery);
			boolean exists = false;
			int zip = 0;
			while(rsLoc.next()) {
				zip = rsLoc.getInt("ZipCode");
				if(zip == customer.getLocation().getZipCode())
					exists = true;
			}

			if(!exists) {
				String insertLocQuery = String.format("INSERT INTO Location VALUES('%1$d', '%2$s', '%3$s')",
						customer.getLocation().getZipCode(), customer.getLocation().getCity(), customer.getLocation().getState());
				int rsInsertLoc = st.executeUpdate(insertLocQuery);
			}

			String query = String.format("INSERT INTO Person VALUES('%1$s', '%2$s', '%3$s', '%4$s', '%5$d', '%6$s')",
					customer.getSsn(), customer.getLastName(), customer.getFirstName(), customer.getAddress(), customer.getLocation().getZipCode(), customer.getTelephone());
			int rs = st.executeUpdate(query);

			String query2 = String.format("INSERT INTO Clients VALUES('%1$s', '%2$d', '%3$s', '%4$d')",
					customer.getEmail(), customer.getRating(), customer.getCreditCard(), Integer.parseInt(customer.getId()));
			int rs2 = st.executeUpdate(query2);

			int id = (int)(Math.random()*100000000 + 899999999);
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date today = Calendar.getInstance().getTime();
			String time = sdf.format(today);

			String query3 = "INSERT INTO Accounts VALUES(" + id + ", CONVERT(" + time + ", DATETIME), " + Integer.parseInt(customer.getId());
			int rs3 = st.executeUpdate(query3);

			return "success";
		}
		catch(Exception e) {
			System.out.println(e);

			return "failure";
		}


	}

	public String editCustomer(Customer customer) {
		/*
		 * All the values of the edit customer form are encapsulated in the customer object.
		 * These can be accessed by getter methods (see Customer class in model package).
		 * e.g. firstName can be accessed by customer.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database update and return "success" or "failure" based on result of the database update.
		 */

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();

			String query = String.format("UPDATE Location SET City = '%1$s', State = '%2$s' WHERE ZipCode = '%3$d'",
					customer.getLocation().getCity(), customer.getLocation().getState(), customer.getLocation().getZipCode());
			int rs = st.executeUpdate(query);

			String query2 = String.format("UPDATE Person SET Telephone = '%1$s', Address = '%2$s', ZipCode = '%3$d' WHERE SSN = '%4$s'",
					customer.getTelephone(), customer.getAddress(), customer.getLocation().getZipCode(), customer.getSsn());
			int rs2 = st.executeUpdate(query2);

			String query3 = String.format("UPDATE Clients SET Email = '%1$s', Rating = '%2$d', CreditCardNumber = '%3$s' WHERE Id = '%4$d'",
					customer.getEmail(), customer.getRating(), customer.getCreditCard(), Integer.parseInt(customer.getId()));
			int rs3 = st.executeUpdate(query3);

			return "success";
		}
		catch(Exception e) {
			System.out.println(e);

			return "failure";
		}
		
		/*Sample data begins*/
		//return "success";
		/*Sample data ends*/

	}

    public List<Customer> getCustomerMailingList() {

		/*
		 * This method fetches the all customer mailing details and returns it
		 * The students code to fetch data from the database will be written here
		 */

        List<Customer> customers = new ArrayList<Customer>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String query = "SELECT P.SSN, P.LastName, P.FirstName, P.Address, L.Zipcode, L.City, L.State, C.Email FROM Clients C, Location L, Person P WHERE C.Id = P.SSN AND P.ZipCode = L.ZipCode;";
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                Customer customer = new Customer();
                customer.setSsn(rs.getString("SSN"));
                customer.setLastName(rs.getString("LastName"));
                customer.setFirstName(rs.getString("FirstName"));
                customer.setAddress(rs.getString("Address"));
                Location location = new Location();
                location.setCity(rs.getString("City"));
                location.setState(rs.getString("State"));
                location.setZipCode(rs.getInt("ZipCode"));
                customer.setLocation(location);
                customer.setEmail(rs.getString("Email"));
                customers.add(customer);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        return customers;
        //return getDummyCustomerList();
    }

    public List<Customer> getAllCustomers() {
        /*
		 * This method fetches returns all customers
		 */

        return getCustomers("");

        //return getDummyCustomerList();
    }
}
