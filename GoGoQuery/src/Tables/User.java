package Tables;

import java.sql.Date;
import java.util.HashMap;

import Database.ManipulateData;

public class User extends ManipulateData{
	int userID;
	String userDOB;
	String userEmail;
	String userPassword;
	String userGender;
	String userRole;
	
	public User(int userID, String userDOB, String userEmail, String userPassword, String userGender, String userRole) {
		super("msuser");
		this.userID = userID;
		this.userDOB = userDOB;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userGender = userGender;
		this.userRole = userRole;
	}
	
	@Override
	public void sendData() {
		HashMap<String, String> res = new HashMap<>();
		res.put("UserID", Integer.toString(userID));
		res.put("UserDOB", userDOB);
		res.put("UserEmail", userEmail);
		res.put("UserPassword", userPassword);
		res.put("UserGender", userGender);
		res.put("UserRole", userRole);
		
		this.insertData(res);
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserDOB() {
		return userDOB;
	}

	public void setUserDOB(String userDOB) {
		this.userDOB = userDOB;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	
	
}
