package dao;

import model.Login;

import java.sql.*;

public class LoginDao {
	/*
	 * This class handles all the database operations related to login functionality
	 */

	private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
	private String user= "andliu";
	private String pw = "111473795";

	public Login login(String username, String password, String role) {
		/*
		 * Return a Login object with role as "manager", "customerRepresentative" or "customer" if successful login
		 * Else, return null
		 * The role depends on the type of the user, which has to be handled in the database
		 * username, which is the email address of the user, is given as method parameter
		 * password, which is the password of the user, is given as method parameter
		 * Query to verify the username and password and fetch the role of the user, must be implemented
		 */

		if(username.equals("admin@123")){
			/*Sample data begins*/
			Login login = new Login();
			login.setRole(role);
			return login;
			/*Sample data ends*/
		}

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			//String sql = String.format("SELECT Username FROM Login WHERE Passcode = '%1$s' AND Role = '%2$s'", password, role);
			String sql = String.format("SELECT Role FROM Login WHERE Username = '%1$s' AND Passcode = '%2$s'", username, password);
			//System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				String userResult = rs.getString("Role");
				if(userResult.equals(role)){
					Login login = new Login();
					login.setUsername(username);
					login.setPassword(password);
					login.setRole(role);
					return login;
				}
			}

		}
		catch(Exception e){
			System.out.println(e);
		}

		/*Sample data begins
		Login login = new Login();
		login.setRole(role);
		return login;
		Sample data ends*/

		return null;
	}
	
	public String addUser(Login login) {
		/*
		 * Query to insert a new record for user login must be implemented
		 * login, which is the "Login" Class object containing username and password for the new user, is given as method parameter
		 * The username and password from login can get accessed using getter methods in the "Login" model
		 * e.g. getUsername() method will return the username encapsulated in login object
		 * Return "success" on successful insertion of a new user
		 * Return "failure" for an unsuccessful database operation
		 */

		String username = login.getUsername();
		String password = login.getPassword();
		String role = login.getRole();

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pw);
			Statement st = con.createStatement();
			String query = String.format("INSERT INTO Login VALUE('%1$s','%2$s', '%3$s')",username,password,role);
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeQuery();
		}
		catch(Exception e){

		}

		/*Sample data begins*/
		return "success";
		/*Sample data ends*/
	}

}
