package Employee;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
public class Employee implements Serializable{
	private String ID;
	private String name;
	private String deployment;
	private String position;
	private String email;
	private String extension;
	private String cardNumber;
	private String idCard;
	private String hireDate;
	private String quitDate;
	
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
	
	public Employee() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeployment() {
		return deployment;
	}
	public void setDeployment(String deployment) {
		this.deployment = deployment;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	public String getQuitDate() {
		return quitDate;
	}
	public void setQuitDate(String quitDate) {
		this.quitDate = quitDate;	
	}
	
	public List<String> getAllEmployeeID() {
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT employeeID FROM employeedata")){
			ResultSet result = ps.executeQuery();
			List<String> employeeList = new ArrayList<>();
			while(result.next()) {
				employeeList.add(result.getString(1));
			}
			return employeeList;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	public List<Employee> getHire(){
		List<Employee> list = new ArrayList<>();
		
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT * FROM employeedata WHERE quitDate=''")){
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				Employee e = new Employee();
				e.setID(result.getString(1));
				e.setName(result.getString(2));
				e.setDeployment(result.getString(3));
				e.setPosition(result.getString(4));
				e.setEmail(result.getString(5));
				e.setExtension(result.getString(6));
				e.setCardNumber(result.getString(7));
				e.setIdCard(result.getString(8));
				e.setHireDate(result.getString(9));
				e.setQuitDate(result.getString(10));
				list.add(e);
			}
			return list;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	public List<Employee> getNoEmail(){
		List<Employee> list = new ArrayList<>();
		
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT * FROM employeedata WHERE email=''")){
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				Employee e = new Employee();
				e.setID(result.getString(1));
				e.setName(result.getString(2));
				e.setDeployment(result.getString(3));
				e.setPosition(result.getString(4));
				e.setEmail(result.getString(5));
				e.setExtension(result.getString(6));
				e.setCardNumber(result.getString(7));
				e.setIdCard(result.getString(8));
				e.setHireDate(result.getString(9));
				e.setQuitDate(result.getString(10));
				list.add(e);
			}
			return list;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	public List<Employee> getNoExtension(){
		List<Employee> list = new ArrayList<>();
		
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT * FROM employeedata WHERE extension=''")){
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				Employee e = new Employee();
				e.setID(result.getString(1));
				e.setName(result.getString(2));
				e.setDeployment(result.getString(3));
				e.setPosition(result.getString(4));
				e.setEmail(result.getString(5));
				e.setExtension(result.getString(6));
				e.setCardNumber(result.getString(7));
				e.setIdCard(result.getString(8));
				e.setHireDate(result.getString(9));
				e.setQuitDate(result.getString(10));
				list.add(e);
			}
			return list;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
