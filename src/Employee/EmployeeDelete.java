package Employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/employeeDelete")
public class EmployeeDelete extends HttpServlet {
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";   
    
    public EmployeeDelete() {
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
		String id = request.getParameter("deletehidden");
		String sql = "DELETE FROM employeedata WHERE employeeID=?";
		try(Connection conn = DriverManager.getConnection(jdbcUri,username,password);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.executeUpdate();			
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		List<Employee> list = getAllEmployee();
		request.getSession().setAttribute("All",list);
		response.sendRedirect("showModify.jsp");
		return;
	}
	public List<Employee> getAllEmployee(){
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT * FROM employeedata")){
			ResultSet result = ps.executeQuery();
			List<Employee> employeeList = new ArrayList<>();
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
				employeeList.add(e);
			}
			return employeeList;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
