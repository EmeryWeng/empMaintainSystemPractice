package Employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/employeeModify")
public class EmployeeModify extends HttpServlet {
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
	
    public EmployeeModify() {
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
		save(e,request,response);		
		
	}
	private void save(Employee e,HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException {
		List<Employee> list = new ArrayList<>();
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
			request.getSession().setAttribute("Modify",e.getID());
			request.getSession().setAttribute("ModifyList",list);
			PrintWriter out = response.getWriter();
			out.println("<script type='text/javascript'>window.opener.location.reload();window.close();</script>");
			out.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
	}	
}
