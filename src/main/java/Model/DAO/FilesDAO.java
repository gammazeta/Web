package Model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Model.BEAN.Files;

public class FilesDAO {
	
	//Hàm thêm file user đã upload vào database 
	public static String addFiles(String nameFile1, String nameFile2, String idUser) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
			String query = "INSERT INTO files (nameFile1,nameFile2,result,idUser) VALUES (?, ?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, nameFile1);
			ps.setString(2, nameFile2);
			String result = "processing";
			ps.setString(3, result);
			ps.setString(4, idUser);
			ps.executeUpdate();
			
			query = "select *from files ORDER BY id DESC LIMIT 1";
			ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("Id files insert :");
				System.out.println(rs.getString(1));
				return rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	
	//Hàm cập nhật kết quả sau khi đã so sánh file xong 
	public static void updateResultFiles(String idFiles,String result) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
			
			String query = "update files set result = ? where id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, result);
			ps.setString(2, idFiles);
			ps.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//Hàm load lên dữ liệu file đã upload và kết quả theo idUser 
	public static ArrayList<Files> getListFiles(String idUser) {
		ArrayList<Files> list = new ArrayList<Files>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");

			String query = "SELECT * FROM files where idUser = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, idUser);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Files files = new Files();
				
				files.setNameFile1(rs.getString(2));
				files.setNameFile2(rs.getString(3));
				files.setResult(rs.getString(4));
				
				list.add(files);
			} 

		} catch (Exception e) {
			System.out.println(e);
		}

		return list;
	}
}
