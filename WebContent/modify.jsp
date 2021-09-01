<%@ page  contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<jsp:useBean id="count" class="Support.Count" scope="request"/>
<c:set target="${pageContext.request}" property="characterEncoding" value="UTF-8"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
                        <META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
                        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
                        <META HTTP-EQUIV="Expires" CONTENT="0">
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0"/>
<title>員工資料</title>
<script src="js/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/modifysetting.css"/>

</head>
	<script type="text/javascript">
		function modify_sure(){
			
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
			var sure = confirm("確定修改?");
			if(sure){
				var id  = form1.employeeID.value;
				document.getElementById("form1").submit();
				alert(id+"修改成功");				
			}else{
				alert("已經取消修改動作");
				return false;
			}
			
		}
		
	</script>

<body>
					
	<%
         response.setHeader("Pragma","No-cache"); 
         response.setHeader("Cache-Control","no-cache"); 
         response.setDateHeader("Expires", 0); 
         response.flushBuffer();
    %>
	<sql:setDataSource var="MySQL" 
					driver="com.mysql.cj.jdbc.Driver"
					user="root"
					password="850421"
					url="jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=UTC"					
	/>
	
	<c:if test="${not empty param.id}">
		<sql:query dataSource="${MySQL}" var="employee">		
			SELECT * FROM employeedata WHERE employeeID='${param.id}'
		</sql:query>
		<sql:query dataSource="${MySQL}" var="Name">
			SELECT employeeName FROM employeedata WHERE employeeID='${param.id}'
		</sql:query>
		<c:forEach var="name" items="${Name.rowsByIndex}">
			<c:set var="ModifyName" value="${name[0]}" scope="session"/>
		</c:forEach>
	</c:if>
	

 <div class="div1">
	 <pre> <b>員工資料修改區</b>									   								 	  	  <u>(*標記欄位為必填)</u></pre>
		<form action="employeeModify" method="post" id="form1" name="form1">
			<label for="employeeID" ><b class="red">*</b>員工代號</label>
			<c:forEach var="Modify" items="${employee.rowsByIndex}">
			<input type="text" id="employeeID" name="employeeID"  value="${Modify[0]}" readonly> 		
			<label for="employeeName"><b class="red">*</b>員工姓名</label>
			<input type="text" id="employeeName" name="employeeName"  value="${Modify[1]}">
			<br>
			<label for="employeeDeployment"><b class="red">*</b>部門代號-部門名稱</label>
			
			<select form="form1" id="employeeDeployment" class="D" name="employeeDeployment">
			<option selected>
			
			<c:choose>
				<c:when test="${Modify[2] =='行銷部門'}">
					<option selected>行銷部門
				</c:when>
				<c:otherwise>
					<option>行銷部門
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${Modify[2] =='企劃部門'}">
					<option selected>行銷部門
				</c:when>
				<c:otherwise>
					<option>企劃部門
				</c:otherwise>
			</c:choose>
			
			</select>
			
			<label for="emplyeePosition"><b class="red">*</b>職位名稱</label>
			<select form="form1" id="employeePosition" class="P" name="employeePosition">
			<option selected>
			
			<c:choose>
				<c:when test="${Modify[3] =='董事長'}">
					<option selected>董事長
				</c:when>
				<c:otherwise>
					<option>董事長
				</c:otherwise>
			</c:choose>
			
			
			<c:choose>
				<c:when test="${Modify[3] =='經理'}">
					<option selected>經理
				</c:when>
				<c:otherwise>
					<option>經理
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${Modify[3] =='組長'}">
					<option selected>組長
				</c:when>
				<c:otherwise>
					<option>組長
				</c:otherwise>
			</c:choose>
			
			</select>
			
			<br>
			<label for="email">E-mail</label>			
			<input type="text" id="email" name="email" value="${Modify[4]}">		
			<label for="extension">分機號碼</label>
			<input type="tel" id="extension" name="extension"  
 			value="${Modify[5]}">
			<br>
			<label for="employeeCard"><b class="red">*</b>卡號</label>
			<input type="text" id="employeeCard" name="employeeCard" 
			 value="${Modify[6]}">
			<label for="employeeIDCard" ><b class="red">*</b>身分證號</label>
			<input type="text" id="employeeIDCard" name="employeeIDCard"   maxlength="10"
			 value="${Modify[7]}">
			<br>
			<label for="date"><b class="red">*</b>到職日期</label>
			<input type="date" id="date" name="date"  value="${Modify[8]}">
			<label for="quitDate">離職日期</label>
			<input type="date" id="quitDate" name="quitDate"  value="${Modify[9]}">
			</c:forEach>
			
			<br>
			<br>
			<br>
			<br>
			<br>
			
			<div class="div2">
				<input type="button" class="lightblue" name="modifybutton" value="送出" onclick="modify_sure();">
				<input type="button" value="取消"  class="cancel"onclick="window.close();">
			</div>
		</form>
		</div>
</body>
</html>