package ExcelHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Employee.Employee;

/**
 * Servlet implementation class UploadExcel
 */
@WebServlet("/uploadExcel")
public class UploadExcel extends HttpServlet {
	private String jdbcUri ="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC";
	private String username = "root";
	private String password = "850421";
    public UploadExcel() {
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
		String savePath = this.getServletContext().getRealPath("/WEB-INF/uploadExcel");
		String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
		File tmpFile = new File(tempPath);
		if(!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*100);
			factory.setRepository(tmpFile);
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			if(!ServletFileUpload.isMultipartContent(request)) {
				return;
			}
			upload.setFileSizeMax(1024*1024);
			upload.setSizeMax(1024*1024*10);
			
			List<FileItem> fileList = upload.parseRequest(request);
			for(FileItem item : fileList) {
				if(item.isFormField()) {
					
				}else {
					String fileName = item.getFieldName();
					if(fileName == null || fileName.trim().equals("")) {
						continue;
					}
					fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
					//String fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
					//取得副檔名 以Excel為例可以判斷if(xls>HSSF xlsx>XSSF)
					InputStream in = item.getInputStream();
					String saveFileName = makeFileName(fileName);
					String realSavePath = makePath(saveFileName,savePath);
					FileOutputStream out = new FileOutputStream(realSavePath + "\\" + saveFileName);
					byte[] buffer = new byte[1024];	
					int length = 0;		
					while((length=in.read(buffer))>0) {
						out.write(buffer,0,length);
					}		
					in.close();
					out.close();
					saveDB((realSavePath + "\\" + saveFileName),request,response);
				}
			}
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private String makeFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;//UUID 生成隨機碼 為上傳檔案產生唯一檔名
	}
	private String makePath(String fileName,String savePath) {
		int hashcode = fileName.hashCode();
		int dir1 = hashcode&0xf;
		int dir2 = (hashcode&0xf0)>>4;
		String dir = savePath + "\\" + dir1 + "\\" +dir2;
		File file = new File(dir);
		if(!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}
	
	private List<Employee> readXlsx(String path)throws IOException{
		InputStream is = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		Employee employee = null;
		List<Employee> list = new ArrayList<>();
		
		for(int numSheet = 0 ; numSheet < workbook.getNumberOfSheets();numSheet++) {
			XSSFSheet sheet = workbook.getSheetAt(numSheet);
			
			if(sheet == null) {
				continue;
			}
			
			for(int numRow = 1;numRow<=sheet.getLastRowNum();numRow++) {
				XSSFRow row = sheet.getRow(numRow);
				if(row != null) {
					employee = new Employee();
					XSSFCell id = row.getCell(0,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell name = row.getCell(1,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell deployment = row.getCell(2,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell position = row.getCell(3,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell email = row.getCell(4,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell extension = row.getCell(5,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell card = row.getCell(6,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell idCard = row.getCell(7,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell hireDate = row.getCell(8,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell quitDate = row.getCell(9,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					
					if(!getValue(id).isEmpty()) {	
						employee.setID(getValue(id));		
					}
					if(!getValue(name).isEmpty()) {
						employee.setName(getValue(name));
					}				
					if(!getValue(deployment).isEmpty()) {
						employee.setDeployment(getValue(deployment));
					}
					if(!getValue(position).isEmpty()) {
						employee.setPosition(getValue(position));
					}
					if(getValue(email).isEmpty()) {
						employee.setEmail("");
					}else {
						String regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
						Pattern p = Pattern.compile(regex);
						Matcher m = p.matcher(getValue(email));
						if(m.find()) {
							employee.setEmail(getValue(email));
						}
					}
					if(getValue(extension).isEmpty()) {
						employee.setExtension("");
					}else {
						employee.setExtension(getValue(extension));
					}
					if(!getValue(card).isEmpty()) {
						employee.setCardNumber(getValue(card));
					}
					if(!getValue(idCard).isEmpty()) {
						employee.setIdCard(getValue(idCard));
					}
					if(!getValue(hireDate).isEmpty()) {
						employee.setHireDate(getValue(hireDate));
					}
					if(getValue(quitDate).isEmpty()) {
						employee.setQuitDate("");
					}else {
						employee.setQuitDate(getValue(quitDate));
					}
				}				
				list.add(employee);
			}
		}
		return list;
	}
	
	private String getValue(XSSFCell cell) {
		if(cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(Math.round(cell.getNumericCellValue()));
		}
		if(cell.getCellType() == CellType.STRING) {	//日期轉字串
			String date = cell.getStringCellValue();
			date = date.replaceAll("'","");
			return date;
		}
		else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
	private void saveDB(String path,HttpServletRequest request,HttpServletResponse response)throws IOException,SQLException{
		Employee employee = null;
		String modify = (String)request.getSession().getAttribute("Modify");
		List<Employee> list = readXlsx(path);
		List<String> errors = getError(path);
		for(int i=0;i<list.size();i++) {
			employee = list.get(i);
			if(!errors.isEmpty()) {				
				try {
					request.setAttribute("error",errors);
					request.getRequestDispatcher("uploadExcelFail.jsp").forward(request, response);
					return;
				} catch (ServletException ex) {
					throw new RuntimeException(ex);
				} 
				
			}else if(modify !="" && modify !=null){
				response.sendRedirect("showModify.jsp");
				return;
			}else if(employeeIDIsExist(employee.getID())) {
				List<String> existid = getExistID(list);		
				try {
					request.setAttribute("existID",existid);
					request.getRequestDispatcher("employee_exceladdfail.jsp").forward(request, response);
					return;
				} catch (ServletException ex) {
					throw new RuntimeException(ex);
				}				
			}else{
				Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareStatement("INSERT INTO employeedata VALUES(?,?,?,?,?,?,?,?,?,?)");
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
			}		
		}
		try {
			request.setAttribute("employee",list);
			request.setAttribute("size",list.size());
			request.getRequestDispatcher("employee_addsuccess.jsp").forward(request, response);
			return;			
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}		
	}
	private boolean employeeIDIsExist(String employeeID) {
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT employeeID FROM employeedata")){
			ResultSet result = ps.executeQuery();
			List<String> employeeList = new ArrayList<>();
			while(result.next()) {
				employeeList.add(result.getString(1));
			}
			for(String s : employeeList) {
				if(employeeID.equals(s)) {
					return true;
				}
			}
			return false;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	private List<String> getExistID(List<Employee> list){
		try(Connection conn = DriverManager.getConnection(jdbcUri, username, password);
				PreparedStatement ps = conn.prepareCall(
					"SELECT employeeID FROM employeedata")){
			ResultSet result = ps.executeQuery();
			List<String> employeeList = new ArrayList<>();
			List<String> existid = new ArrayList<>();
			while(result.next()) {
				employeeList.add(result.getString(1));
			}
			for(int i=0;i<list.size();i++) {
				Employee e = list.get(i);
				String id = e.getID();
				for(int j=0;j<employeeList.size();j++) {
					if(id.equals(employeeList.get(j))) {
						existid.add(id);
					}
				}
			}	
			return existid;
		}catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	private List<String> getError(String path)throws IOException{
		InputStream is = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		List<String> error = new ArrayList<>();
		
		for(int numSheet = 0 ; numSheet < workbook.getNumberOfSheets();numSheet++) {
			XSSFSheet sheet = workbook.getSheetAt(numSheet);
			
			if(sheet == null) {
				continue;
			}
			
			for(int numRow = 1;numRow<=sheet.getLastRowNum();numRow++) {
				XSSFRow row = sheet.getRow(numRow);
				if(row != null) {
					XSSFCell id = row.getCell(0,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell name = row.getCell(1,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell deployment = row.getCell(2,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell position = row.getCell(3,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell email = row.getCell(4,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell extension = row.getCell(5,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell card = row.getCell(6,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell idCard = row.getCell(7,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell hireDate = row.getCell(8,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					XSSFCell quitDate = row.getCell(9,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					
					String regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(getValue(email));
					
					if(getValue(id).isEmpty()) {	
						error.add("員工代號未填寫");		
					}
					if(getValue(name).isEmpty()) {
						error.add("員工姓名未填寫");
					}
					if(getValue(deployment).isEmpty()) {
						error.add("部門代號未填寫");
					}
					if(getValue(position).isEmpty()) {
						error.add("職位名稱未填寫");
					}
					if(!m.find() && !getValue(email).isEmpty()) {
						error.add("E-mail格式錯誤");
					}
					if(getValue(card).isEmpty()) {
						error.add("員工卡號未填寫");
					}
					if(getValue(idCard).isEmpty()) {
						error.add("身分證號未填寫");
					}
					if(getValue(hireDate).isEmpty()) {
						error.add("到職日期未填寫");
					}					
				}
			}
		}
		return error;
	}
}
