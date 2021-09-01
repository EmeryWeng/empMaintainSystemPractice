<%@ page  contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ page import="java.util.regex.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.*" %>
<%@ page import="Employee.Employee" %>
<jsp:useBean id="count" class="Support.Count" scope="request"/>
<c:set target="${pageContext.request}" property="characterEncoding" value="UTF-8"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>員工資料</title>
<script src="js/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/setting.css"/>
</head>
	<script  type="text/javascript">
		function check_field(){
			
			if(form1.employeeID.value == ""){
				alert("未填寫員工代號");
				form1.employeeID.focus();
				return false;
			}			
			if(form1.employeeName.value == ""){
				alert("未填寫員工姓名");
				form1.employeeName.focus();
				return false;
			}
			if(form1.employeeDeployment.value == ""){
				alert("未填寫部門代號");
				form1.employeeDeployment.focus();
				return false;
			}
			if(form1.employeePosition.value == ""){
				alert("未填寫職位名稱");
				form1.employeePosition.focus();
				return false;
			}
			if(form1.email.value != ""){
				var regex = 
					/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z]+$/;
					if(!regex.test(form1.email.value)){
						alert("E-mail格式錯誤");
						form1.email.focus();
						return false;
					}
			}
			if(form1.employeeCard.value == ""){
				alert("未填寫員工卡號");
				form1.employeeCard.focus();
				return false;
			}
			if(form1.employeeIDCard.value == ""){
				alert("未填寫身分證號");
				form1.employeeIDCard.focus();
				return false;
			}
			if(form1.date.value == ""){
				alert("未填寫到職日期");
				form1.date.focus();
				return false;
			}
			<%
			Employee employee = new Employee();
			List<String> list = employee.getAllEmployeeID();		
			%>
			var id = form1.employeeID.value;
			
			var arr = new Array();
			<%
				for(int i=0;i<list.size();i++){
			%>
				arr[<%=i%>] = "<%=list.get(i)%>";
			<%
				}
			%>
			for(var i=0;i<arr.length;i++){
				if(id == arr[i]){
					alert("員工代號已重複");
					form1.employeeID.focus();
					return false;
				}
			}
			var id = form1.employeeID.value;
			alert(id+"新增成功");
			var add ="add";
			form1.addhidden.value = add;
			var formObj = document.getElementById('form1');
			formObj.submit();
		}	
	</script>	
	<script type="text/javascript">
		function query(){
			var query = "query";
			form1.queryhidden.value = query;
			var formObj = document.getElementById('form1');
			formObj.submit();
		}
	</script>	
	<script type="text/javascript">
		function modify(id){			
			var iWidth = 1650;  //視窗的寬度;
			var iHeight = 700; //視窗的高度;
			var iTop = (window.screen.availHeight-30-iHeight)/2;  //視窗的垂直位置;
			var iLeft = (window.screen.availWidth-10-iWidth)/2;   //視窗的水平位置;
			window.open('modify.jsp?id='+id,'modify','height='+iHeight+',innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',resizable=no,scrollbars=no');
		}
	</script>
	<script type="text/javascript">
		function deleteEmployee(id){			
			var sure = confirm("確定刪除?");
			var number=id;
			deleteForm.deletehidden.value = id;
			if(sure){				
				alert(number+"刪除成功");				
				var formObj = document.getElementById('deleteForm');
				formObj.submit();
			}else{
				alert("已經取消刪除動作");
				return false;
			}			
		}
	</script>
	<script type="text/javascript">
		function check_file(){
			var file = fileUpload.upfile.value;
			var type = file.substring(file.lastIndexOf('.')).toLowerCase();
			if(file ==''){
				alert("請選擇檔案");
				file.focus();
				return false;
			}
			if(type !='.xlsx'){
				alert("格式不對，只接受xlsx格式檔案");
				file.focus();
				return false;
			}
			
			var formObj = document.getElementById('fileUpload');
			formObj.submit();
		}
	</script>
<body>
	<%
		int x = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String time = LocalDateTime.now().format(formatter);
	%>
 <div class="div1">
	<pre> <b>員工資料新增 & 查詢區</b>																					         			     <u>(*標記欄位為必填)</u></pre>
		<form action="employeeForm" method="post" id="form1" name="form1">
			<label for="employeeID"><b class="red">*</b>員工代號</label>
			<input type="text" id="employeeID" name="employeeID" size="40">
				
			<label for="employeeName"><b class="red">*</b>員工姓名</label>
			<input type="text" id="employeeName" name="employeeName" size="40">
			<br>
			<label for="employeeDeployment"><b class="red">*</b>部門代號-部門名稱</label>
			<select form="form1" id="employeeDeployment" name="employeeDeployment">
			
			<option selected>
			<option>行銷部門
			<option>企劃部門
						
			</select>
			<label for="emplyeePosition"><b class="red">*</b>職位名稱</label>
			<select form="form1" id="employeePosition" name="employeePosition">
			
			<option selected>
			<option>董事長
			<option>經理
			<option>組長
			
			</select>
			<br>
			<label for="email">E-mail</label>
			<input type="text" id="email" name="email" size="40">
			
			<label for="extension">分機號碼</label>
			<input type="tel" id="extension" name="extension" size="40">
			
			<br>
			
			<label for="employeeCard"><b class="red">*</b>卡號</label>
			<input type="text" id="employeeCard" name="employeeCard" size="40">
			
			<label for="employeeIDCard"><b class="red">*</b>身分證號</label>
			<input type="text" id="employeeIDCard" name="employeeIDCard" size="40" maxlength="10">
			
			<br>
			
			<label for="date"><b class="red">*</b>到職日期</label>
			<input type="date" name="date" size="40">
			
			<label for="quitDate">離職日期</label>
			<input type="date" name="quitDate">
			<br>
			
			<div class="div2">
				<input type="hidden" name="addhidden">
				<input type="button" name="addbutton" class="lightblue" value="新增"  onclick="check_field()">	
				<input type="hidden" name="queryhidden">
				<input type="button" name="querybutton" class="pink" value="查詢" onclick="query();">
			</div>
		</form>				
		<br>
	<pre> <b>員工資料檔案上傳區</b></pre>
		<form action="uploadExcel" method="post" enctype="multipart/form-data" name="fileUpload" id="fileUpload">
			<label for="upfile" class="filelabel">員工資料上傳</label>
			<input type="file"  size="40" id="upfile" name="upfile" class="file">
			<input type="button" value="上傳檔案" class="lightblue" onclick="check_file();">			
		</form>
		<br>	
		<br>
		
		<form action="showNumber.jsp" method="post" name="showHire">
			<input type="hidden" name="hire" value="yes">
		</form>
		
		<form action="showNumber.jsp" method="post" name="showNoEmail">
			<input type="hidden" name="noEmail" value="yes">
		</form>
		
		<form action="showNumber.jsp" method="post" name="showNoExtension">
			<input type="hidden" name="noExtension" value="yes">
		</form>
			
	<pre><b>員工資料維護區</b>									在職人數:<a href="javascript:document.showHire.submit();"><b class="red">${count.total}</b></a>  無E-mail人數:<a href="javascript:document.showNoEmail.submit();"><b class="red">${count.noEmail}</b></a>  無分機人數:<a href="javascript:document.showNoExtension.submit();"><b class="red">${count.noExtension}</b></a>	</pre>

		<form action="employeeDelete" method="post" id="deleteForm" name="deleteForm">
				<input type="hidden" name="deletehidden">
		</form>
		
		<table border="1">
			<tbody>
				<tr>
					<th>流水號</th>
					<th>修改功能</th>
					<th>刪除功能</th>
					<th>員工代號</th>
					<th>員工姓名</th>
					<th>部門代號-部門名稱</th>
					<th>職位名稱</th>
					<th>E-mail</th>
					<th>分機號碼</th>
					<th>卡號</th>
					<th>身分證號</th>
					<th>到職日期</th>
					<th>離職日期</th>
					<th>新增人員</th>
					<th>新增時間</th>
					<th>修改人員</th>
					<th>修改時間</th>
				</tr>
				
			<sql:setDataSource var="MySQL" 
				driver="com.mysql.cj.jdbc.Driver"
				user="root"
				password="850421"
				url="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC"					
			/>

				<c:if test="${not empty param.hire}">
					<sql:query dataSource="${MySQL}" var="employee">		
						SELECT * FROM employeedata WHERE quitDate =""
					</sql:query>
					<%
						Employee e = new Employee();
						List<Employee> hire = e.getHire();
						request.getServletContext().setAttribute("excel",hire);
					%>
					<c:forEach var="employee" items="${employee.rowsByIndex}">
						<tr>
							<td><%=++x%></td>

							<td><input type="button" class="mdbutton" value="修改"
								onclick="modify('${employee[0]}');"></td>

							<td><input type="button" class="mdbutton" value="刪除"
									onclick="deleteEmployee('${employee[0]}');"></td>
							
							<td>${employee[0]}</td>
							<td>${employee[1]}</td>
							<td>${employee[2]}</td>
							<td>${employee[3]}</td>
							<td>${employee[4]}</td>
							<td>${employee[5]}</td>
							<td>${employee[6]}</td>
							<td>${employee[7]}</td>
							<td>${employee[8]}</td>
							<td>${employee[9]}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</c:if>

				<c:if test="${not empty param.noEmail}">
					<sql:query dataSource="${MySQL}" var="noEmail">		
						SELECT * FROM employeedata WHERE email =""
					</sql:query>
				<%
					Employee e = new Employee();
					List<Employee> noEmail = e.getNoEmail();
					request.getServletContext().setAttribute("excel",noEmail);
				%>
					<c:forEach var="noEmailEmployee" items="${noEmail.rowsByIndex}">
						<tr>
							<td><%=++x%></td>

							<td><input type="button" class="mdbutton" value="修改"
								onclick="modify('${noEmailEmployee[0]}');"></td>

							<td><input type="button" class="mdbutton" value="刪除"
									onclick="deleteEmployee('${noEmailEmployee[0]}');"></td>
							
							<td>${noEmailEmployee[0]}</td>
							<td>${noEmailEmployee[1]}</td>
							<td>${noEmailEmployee[2]}</td>
							<td>${noEmailEmployee[3]}</td>
							<td>${noEmailEmployee[4]}</td>
							<td>${noEmailEmployee[5]}</td>
							<td>${noEmailEmployee[6]}</td>
							<td>${noEmailEmployee[7]}</td>
							<td>${noEmailEmployee[8]}</td>
							<td>${noEmailEmployee[9]}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${not empty param.noExtension}">
					<sql:query dataSource="${MySQL}" var="noExtension">		
						SELECT * FROM employeedata WHERE extension =""
					</sql:query>
				<%
					Employee e = new Employee();
					List<Employee> noExtension = e.getNoExtension();
					request.getServletContext().setAttribute("excel",noExtension);
				%>
					<c:forEach var="noExtensionEmployee" items="${noExtension.rowsByIndex}">
						<tr>
							<td><%=++x%></td>

							<td><input type="button" class="mdbutton" value="修改"
								onclick="modify('${noExtensionEmployee[0]}');"></td>

							<td><input type="button" class="mdbutton" value="刪除"
									onclick="deleteEmployee('${noExtensionEmployee[0]}');"></td>
							
							<td>${noExtensionEmployee[0]}</td>
							<td>${noExtensionEmployee[1]}</td>
							<td>${noExtensionEmployee[2]}</td>
							<td>${noExtensionEmployee[3]}</td>
							<td>${noExtensionEmployee[4]}</td>
							<td>${noExtensionEmployee[5]}</td>
							<td>${noExtensionEmployee[6]}</td>
							<td>${noExtensionEmployee[7]}</td>
							<td>${noExtensionEmployee[8]}</td>
							<td>${noExtensionEmployee[9]}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</c:if>
				
				<c:if test="${not empty Modify}">
					<c:redirect url="showModify.jsp"/>
				</c:if>
			</tbody>
		</table>
		<br>
		<br>
		<br>
		<form action="ExportExcel.jsp" method="post">
			<input type="submit" class="lightblue" value="資料匯出至Excel">
		</form>
  	</div>
</body>
</html>