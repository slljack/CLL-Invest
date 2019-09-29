package dao;

import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Employee;
import model.Location;
import java.sql.*;
import java.sql.DriverManager;

public class EmployeeDao {
	/*
	 * This class handles all the database operations related to the employee table
	 */

	private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
    private String user= "andliu";
    private String pw = "111473795";

    public Employee getDummyEmployee()
    {
        Employee employee = new Employee();

        Location location = new Location();
        location.setCity("Stony Brook");
        location.setState("NY");
        location.setZipCode(11790);

		/*Sample data begins*/
        employee.setEmail("shiyong@cs.sunysb.edu");
        employee.setFirstName("Shiyong");
        employee.setLastName("Lu");
        employee.setLocation(location);
        employee.setAddress("123 Success Street");
        employee.setStartDate("2006-10-17");
        employee.setTelephone("5166328959");
        employee.setEmployeeID("631-413-5555");
        employee.setHourlyRate(100);
		/*Sample data ends*/

        return employee;
    }

    public List<Employee> getDummyEmployees()
    {
       List<Employee> employees = new ArrayList<Employee>();

        for(int i = 0; i < 10; i++)
        {
            employees.add(getDummyEmployee());
        }

        return employees;
    }

	public String addEmployee(Employee employee) {

		/*
		 * All the values of the add employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database insertion of the employee details and return "success" or "failure" based on result of the database insertion.
		 */

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();

            String query = String.format("INSERT INTO Location VALUES('%1$d', '%2$s', '%3$s')",
                    employee.getLocation().getZipCode(), employee.getLocation().getCity(), employee.getLocation().getState());
            int rs = st.executeUpdate(query);

            String query2 = String.format("INSERT INTO Person VALUES('%1$d', '%2$s', '%3$s', '%4$s', '%5$d', '%6$s')",
                    Integer.parseInt(employee.getSsn()), employee.getLastName(), employee.getFirstName(), employee.getAddress(), employee.getLocation().getZipCode(), employee.getTelephone());
            int rs2 = st.executeUpdate(query2);

            String query3 = String.format("INSERT INTO Employee VALUES('%1$s', '%2$d', '%3$d', '%4$s', '%5$d')",
                    employee.getEmail(), Integer.parseInt(employee.getEmployeeID()), Integer.parseInt(employee.getSsn()), employee.getStartDate(), (int)employee.getHourlyRate());
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

	public String editEmployee(Employee employee) {
		/*
		 * All the values of the edit employee form are encapsulated in the employee object.
		 * These can be accessed by getter methods (see Employee class in model package).
		 * e.g. firstName can be accessed by employee.getFirstName() method.
		 * The sample code returns "success" by default.
		 * You need to handle the database update and return "success" or "failure" based on result of the database update.
		 */

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();

            String query = String.format("UPDATE Location SET City = '%1$s', State = '%2$s' WHERE ZipCode = '%3$d'",
                    employee.getLocation().getCity(), employee.getLocation().getState(), employee.getLocation().getZipCode());
            int rs = st.executeUpdate(query);

            String query2 = String.format("UPDATE Person SET Telephone = '%1$s', Address = '%2$s', ZipCode = '%3$d' WHERE SSN = '%4$d'",
                    employee.getTelephone(), employee.getAddress(), employee.getLocation().getZipCode(), Integer.parseInt(employee.getSsn()));
            int rs2 = st.executeUpdate(query2);

            String query3 = String.format("UPDATE Employee SET Email = '%1$s', HourlyRate = '%2$d' WHERE ID = '%3$d'",
                    employee.getEmail(), (int)employee.getHourlyRate(), Integer.parseInt(employee.getEmployeeID()));
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

	public String deleteEmployee(String employeeID) {
		/*
		 * employeeID, which is the Employee's ID which has to be deleted, is given as method parameter
		 * The sample code returns "success" by default.
		 * You need to handle the database deletion and return "success" or "failure" based on result of the database deletion.
		 */

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String query = String.format("DELETE FROM Employee WHERE Id = %s", employeeID);
            int rs = st.executeUpdate(query);

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

	
	public List<Employee> getEmployees() {

		/*
		 * The students code to fetch data from the database will be written here
		 * Query to return details about all the employees must be implemented
		 * Each record is required to be encapsulated as a "Employee" class object and added to the "employees" List
		 */

		List<Employee> employees = new ArrayList<Employee>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Employee E, Person P, Location L WHERE E.ID = P.SSN AND P.ZipCode = L.ZipCode");

            while(rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("ID"));
                employee.setAddress(rs.getString("Address"));
                employee.setLastName(rs.getString("LastName"));
                employee.setFirstName(rs.getString("FirstName"));
                Location location = new Location();
                location.setCity(rs.getString("City"));
                location.setState(rs.getString("State"));
                location.setZipCode(rs.getInt("ZipCode"));
                employee.setLocation(location);
                employee.setEmail(rs.getString("Email"));
                employee.setHourlyRate(rs.getFloat("HourlyRate"));
                employee.setStartDate(rs.getDate("StartDate").toString());
                employee.setTelephone(rs.getString("Telephone"));
                employees.add(employee);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

		/*Location location = new Location();
		location.setCity("Stony Brook");
		location.setState("NY");
		location.setZipCode(11790);

		/*Sample data begins*/
		/*for (int i = 0; i < 10; i++) {
			Employee employee = new Employee();
			employee.setId("111-11-1111");
			employee.setEmail("shiyong@cs.sunysb.edu");
			employee.setFirstName("Shiyong");
			employee.setLastName("Lu");
			employee.setAddress("123 Success Street");
			employee.setLocation(location);
			employee.setTelephone("5166328959");
			employee.setEmployeeID("631-413-5555");
			employee.setHourlyRate(100);
			employees.add(employee);
		}
		/*Sample data ends*/
		
		return employees;
	}

	public Employee getEmployee(String employeeID) {

		/*
		 * The students code to fetch data from the database based on "employeeID" will be written here
		 * employeeID, which is the Employee's ID who's details have to be fetched, is given as method parameter
		 * The record is required to be encapsulated as a "Employee" class object
		 */

		Employee employee = new Employee();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String query = String.format("SELECT * FROM Employee E, Person P, Location L WHERE E.ID = %d AND E.Id = P.SSN AND P.ZipCode = L.ZipCode", Integer.parseInt(employeeID));
            ResultSet rs = st.executeQuery(query);

            rs.next();
            employee.setEmployeeID(rs.getString("ID"));
            employee.setAddress(rs.getString("Address"));
            employee.setLastName(rs.getString("LastName"));
            employee.setFirstName(rs.getString("FirstName"));
            Location location = new Location();
            location.setCity(rs.getString("City"));
            location.setState(rs.getString("State"));
            location.setZipCode(rs.getInt("ZipCode"));
            employee.setLocation(location);
            employee.setEmail(rs.getString("Email"));
            employee.setTelephone(rs.getString("Telephone"));
            employee.setStartDate(rs.getDate("StartDate").toString());
            employee.setHourlyRate(rs.getFloat("HourlyRate"));
        }
        catch(Exception e) {
            System.out.println(e);
        }

		//return getDummyEmployee();
        return employee;
	}
	
	public Employee getHighestRevenueEmployee() {
		
		/*
		 * The students code to fetch employee data who generated the highest revenue will be written here
		 * The record is required to be encapsulated as a "Employee" class object
		 */

        Employee employee = new Employee();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pw);
            Statement st = con.createStatement();

            String query = "Create View EmployeeName (Id, SSN, LastName, FirstName) AS " +
                    "Select E.Id, P.SSN, P.LastName, P.FirstName " +
                    "From Employee E, Person P " +
                    "Where E.SSN = P.SSN";
            int rs = st.executeUpdate(query);

            String query2 = "Create View CROrder (StockSymbol, StockType, LastName, FirstName, EmployeeId, Fee) AS " +
                    "Select S.StockSymbol, S.Type, N.LastName, N.FirstName, N.Id, T.Fee " +
                    "From Trade Tr, Stock S, Transactions T, EmployeeName N " +
                    "Where Tr.StockId = S.StockSymbol and T.Id = Tr.TransactionId and N.Id = Tr.BrokerID";
            int rs2 = st.executeUpdate(query2);

            String query3 = "Create View CRRevenue (LastName, FirstName, EmployeeId, Revenue) AS " +
                    "Select LastName, FirstName, EmployeeId, SUM(Fee) " +
                    "From CROrder " +
                    "GROUP BY LastName, FirstName";
            int rs3 = st.executeUpdate(query3);

            String query4 = "Create View HighestRevenue (MaxRevenue) AS " +
                    "Select MAX(Revenue) " +
                    "From CRRevenue";
            int rs4 = st.executeUpdate(query4);

            String query5 = "Select C.LastName, C.FirstName, C.EmployeeId, P.Address, L.City, L.State, L.ZipCode, P.Telephone, E.Email, E.StartDate, E.HourlyRate " +
                    "From CRRevenue C, HighestRevenue R, Employee E, Person P, Location L " +
                    "Where C.Revenue >= R.MaxRevenue AND C.EmployeeId = E.ID AND E.SSN = P.SSN AND P.ZipCode = L.ZipCode";
            ResultSet rs5 = st.executeQuery(query5);

            rs5.next();
            employee.setEmployeeID(rs5.getString("EmployeeId"));
            employee.setAddress(rs5.getString("Address"));
            employee.setLastName(rs5.getString("LastName"));
            employee.setFirstName(rs5.getString("FirstName"));
            Location location = new Location();
            location.setCity(rs5.getString("City"));
            location.setState(rs5.getString("State"));
            location.setZipCode(rs5.getInt("ZipCode"));
            employee.setLocation(location);
            employee.setEmail(rs5.getString("Email"));
            employee.setTelephone(rs5.getString("Telephone"));
            employee.setStartDate(rs5.getDate("StartDate").toString());
            employee.setHourlyRate(rs5.getInt("HourlyRate"));

            String query6 = "DROP VIEW EmployeeName";
            int rs6 = st.executeUpdate(query6);

            String query7 = "DROP VIEW CROrder";
            int rs7 = st.executeUpdate(query7);

            String query8 = "DROP VIEW CRRevenue";
            int rs8 = st.executeUpdate(query8);

            String query9 = "DROP VIEW HighestRevenue";
            int rs9 = st.executeUpdate(query9);

        }
        catch(Exception e) {
            System.out.println(e);
        }

//return getDummyEmployee();
        return employee;
	}

	public String getEmployeeID(String username) {
		/*
		 * The students code to fetch data from the database based on "username" will be written here
		 * username, which is the Employee's email address who's Employee ID has to be fetched, is given as method parameter
		 * The Employee ID is required to be returned as a String
		 */

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String query = String.format("SELECT ID FROM Employee WHERE Email = '%s'", username);
            ResultSet rs = st.executeQuery(query);

            rs.next();
            return "" + rs.getInt("ID");
        }
        catch(Exception e) {
            System.out.println(e);

            return "";
        }

		//return "111-11-1111";
	}

}
