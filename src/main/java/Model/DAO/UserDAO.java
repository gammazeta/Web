package Model.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Model.BEAN.User;

public class UserDAO {
	
	//Hàm thêm user vào database 
	public static void addUser(String idUser, String password){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db","user", "pass");
			String query = "INSERT INTO user (idUser, password) VALUES (?, ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, idUser);
			ps.setString(2, password);
			ps.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e);
		}	
	}
	
	//Hàm lấy lên dữ liệu user để kiểm tra đăng nhập 
	public static User getUser(String idUser, String password){
		try { 
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db","user","pass");
			 
			 String query = "SELECT * FROM user where idUser = ? AND password = ?";
			 PreparedStatement ps = con.prepareStatement(query);
			 ps.setString(1,idUser);
			 ps.setString(2,password);
			 
			 ResultSet rs = ps.executeQuery();
			 
		     if(rs.next()){
		    	 User user = new User();
		    	 user.setIdUser(rs.getString(1));
		    	 user.setPassword(rs.getString(2));
		    	 return user;
		     }else{
		    	 return null;
		     }
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	//Hàm lấy lên idUser để kiểm tra đăng ký 
	public static boolean checkIdUser(String idUser){
		try { 
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db","user","pass");
			 
			 String query = "SELECT * FROM user where idUser = ? ";
			 PreparedStatement ps = con.prepareStatement(query);
			 ps.setString(1,idUser);
			 
			 ResultSet rs = ps.executeQuery();
			 
		     if(rs.next()){
		    	 return true;
		     }else{
		    	 return false;
		     }
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
}
