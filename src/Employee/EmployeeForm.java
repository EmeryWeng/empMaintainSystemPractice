package Employee;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/employeeForm")
public class EmployeeForm extends HttpServlet {
	
    public EmployeeForm() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String queryButton = request.getParameter("queryhidden");
		String addButton = request.getParameter("addhidden");
		
		if(addButton.equals("add")) {
			request.getRequestDispatcher("employeeAdd").forward(request,response);
			return;
		}
		if(queryButton.equals("query")) {
			request.getRequestDispatcher("employeeQuery").forward(request, response);
			return;
		}
	}
	
}
