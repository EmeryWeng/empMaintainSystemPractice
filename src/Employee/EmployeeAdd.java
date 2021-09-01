package Employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employeeAdd")
public class EmployeeAdd extends HttpServlet {
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
	
    public EmployeeAdd() {
        super();
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException ex) {
        	throw new RuntimeException(ex);
        }
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String employeeID = request.getParameter("employeeID");
		String employeeName = request.getParameter("employeeName");
		String employeeDeployment = request.getParameter("employeeDeployment");
		String employeePosition = request.getParameter("employeePosition");
		String email = request.getParameter("email");
		String extension = request.getParameter("extension");
		String employeeCard = request.getParameter("employeeCard");
		String employeeIDCard = request.getParameter("employeeIDCard");
		String hireDate = request.getParameter("date");
		String quitDate = request.getParameter("quitDate");
		
		List<Employee> modifyList = (List<Employee>)request.getSession().getAttribute("ModifyList");
		
		Employee e = new Employee();
		
		e.setID(employeeID);			
		e.setName(employeeName);
		e.setDeployment(employeeDeployment);
		e.setPosition(employeePosition);
		e.setEmail(email);
		e.setExtension(extension);
		e.setCardNumber(employeeCard);
		e.setIdCard(employeeIDCard);
		e.setHireDate(hireDate);
		e.setQuitDate(quitDate);
		if(modifyList != null) {
			saveM(modifyList,request,response);
		}else {
			save(e,request,response);
		}
								
	}
	private void save(Employee employee,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException{
		try (Connection conn = DriverManager.getConnection(jdbcUri, username, password)) {		
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO employeedata VALUES(?,?,?,?,?,?,?,?,?,?)");//REPLACE 取代 修改發生primary key重複之問題
			ps.setString(1, employee.getID());
			ps.setString(2, employee.getName());
			ps.setString(3, employee.getDeployment());
			ps.setString(4, employee.getPosition());
			ps.setString(5, employee.getEmail());
			ps.setString(6, employee.getExtension());
			ps.setString(7, employee.getCardNumber());
			ps.setString(8, employee.getIdCard());
			ps.setString(9, employee.getHireDate());
			ps.setString(10, employee.getQuitDate());
			ps.executeUpdate();		
			ps.close();
			List<Employee> list = new ArrayList<>();
			list.add(employee);
			request.getSession().setAttribute("Add",list);
			response.sendRedirect("showModify.jsp");
			return;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void saveM(List<Employee> l,HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException {
		List<Employee> list = l;
		Employee e = list.get(0);
		String sql = "UPDATE employeedata SET employeeID ='"+e.getID()+"'"+","
				+ "employeeName='"+e.getName()+"'"+","
				+ "employeeDeployment='"+e.getDeployment()+"'"+"," 
				+ "employeePosition='"+e.getPosition()+"'"+","		
				+ "email='"+e.getEmail()+"'"+","
				+ "extension='"+e.getExtension()+"'"+","
				+ "employeeCard='"+e.getCardNumber()+"'"+","
				+ "employeeIDCard='"+e.getIdCard()+"'"+","
				+ "date='"+e.getHireDate()+"'"+","
				+ "quitDate='"+e.getQuitDate()+"'"
				+ "WHERE employeeID ='"+e.getID()+"'";
		try (Connection conn = DriverManager.getConnection(jdbcUri, username, password)){
			PreparedStatement ps = conn.prepareStatement(sql);		
			ps.executeUpdate();
			list.add(e);
			ps.close();
			response.sendRedirect("showModify.jsp");
			return;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
	}	
}
