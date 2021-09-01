package Employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
/**
 * Servlet implementation class EmployeeQuery
 */
@WebServlet("/employeeQuery")
public class EmployeeQuery extends HttpServlet {
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
	
    public EmployeeQuery() {
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
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
		
		String modifyID = (String)request.getSession().getAttribute("Modify");
		
		Employee queryEmployee = new Employee();
		if(modifyID !="" && modifyID != null) {
			queryEmployee.setID(modifyID);
		}else {
			queryEmployee.setID(employeeID);
		}		
		queryEmployee.setName(employeeName);
		queryEmployee.setDeployment(employeeDeployment);
		queryEmployee.setPosition(employeePosition);
		queryEmployee.setEmail(email);
		queryEmployee.setExtension(extension);
		queryEmployee.setCardNumber(employeeCard);
		queryEmployee.setIdCard(employeeIDCard);
		queryEmployee.setHireDate(hireDate);
		queryEmployee.setQuitDate(quitDate);
		request.setAttribute("QueryEmployee", queryEmployee);
		
	
		String sql = "SELECT * FROM employeedata WHERE 1=1 ";
		
		if(modifyID != null && modifyID !="") {
			sql += "AND employeeID LIKE '" + modifyID +"'";
		}
		
		if(employeeID !="") {
			sql += "AND employeeID LIKE '" + employeeID +"%'";
			
		}if(employeeName !="") {
			sql += "AND employeeName LIKE '" + employeeName +"%'";
			
		}if(employeeDeployment !="") {
			sql += "AND employeeDeployment LIKE '" + employeeDeployment + "%'";
			
		}if(employeePosition !="") {
			sql += "AND employeePosition LIKE '" + employeePosition +"%'";
			
		}if(email !="") {
			sql += "AND email LIKE '" + email +"%'";
						
		}if(extension !="") {
			sql += "AND extension LIKE '" + extension +"%'";
			
		}if(employeeCard !="") {
			sql += "AND employeeCard LIKE '" + employeeCard +"%'";
			
		}if(employeeIDCard !="") {
			sql += "AND employeeIDCard LIKE '" + employeeIDCard +"%'";
					
		}if(hireDate !="") {
			sql += "AND date LIKE '" + hireDate +"%'";
				
		}if(quitDate !="") {
			sql += "AND quitDate LIKE '" + quitDate +"%'";
		}
		try (Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			List<Employee> list = new ArrayList<>();
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				Employee employee = new Employee();
				employee.setID(result.getString("employeeID"));
				employee.setName(result.getString("employeeName"));
				employee.setDeployment(result.getString("employeeDeployment"));
				employee.setPosition(result.getString("employeePosition"));
				employee.setEmail(result.getString("email"));
				employee.setExtension(result.getString("extension"));
				employee.setCardNumber(result.getString("employeeCard"));
				employee.setIdCard(result.getString("employeeIDCard"));
				employee.setHireDate(result.getString("date"));
				employee.setQuitDate(result.getString("quitDate"));
				list.add(employee);
			}
					
			request.setAttribute("Query",list);
			request.getRequestDispatcher("query.jsp").forward(request, response);
			return;		
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}	
	}
	
}
