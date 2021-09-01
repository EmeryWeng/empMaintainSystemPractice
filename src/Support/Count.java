package Support;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Count implements Serializable{
	private int total = 0;
	private int noEmail = 0;
	private int noExtension = 0;
	
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
	
	public Count() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	public int getTotal() {	//改成計算無離職日期的人數 不要計算 離職後的人
		try(Connection conn = DriverManager.getConnection(jdbcUri, username,password);
				PreparedStatement ps = conn.prepareStatement(
						"SELECT employeeID FROM employeedata WHERE quitDate = ''")){
			ResultSet result = ps.executeQuery();
			List<String> list = new ArrayList<>();
			while(result.next()) {
				list.add(result.getString(1));
			}
			for(String s : list) {
			
				this.total++;
			
		}
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getNoEmail() {
		try(Connection conn = DriverManager.getConnection(jdbcUri, username,password);
				PreparedStatement ps = conn.prepareStatement(
						"SELECT email FROM employeedata")){
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				if(result.getString(1).equals("")) {
					this.noEmail++;
				}
			}
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		return noEmail;
	}
	public void setNoEmail(int noEmail) {
		this.noEmail = noEmail;
	}
	public int getNoExtension() {
		try(Connection conn = DriverManager.getConnection(jdbcUri, username,password);
				PreparedStatement ps = conn.prepareStatement(
						"SELECT extension FROM employeedata")){
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				if(result.getString(1).equals("")) {
					this.noExtension++;
				}
			}
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		return noExtension;
	}
	public void setNoExtension(int noExtension) {
		this.noExtension = noExtension;
	}
}

