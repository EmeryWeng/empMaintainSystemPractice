<%@ page  contentType="text/html;charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set target="${pageContext.request}" property="characterEncoding" value="UTF-8"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ImportExcel</title>
<link rel="stylesheet" type="text/css" href="css/setting.css"/>
</head>
<body>
	<%
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment; filename=excel.xls");
	%>
	<table border="1">
		<tr>
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
		</tr>
		<c:forEach var="data" items="${excel}">
			<tr>
				<td>${data.ID}</td>
				<td>${data.name}</td>
				<td>${data.deployment}</td>
				<td>${data.position}</td>
				<td>${data.email}</td>
				<td>${data.extension}</td>
				<td>${data.cardNumber}</td>
				<td>${data.idCard}</td>
				<td>${data.hireDate}</td>
				<td>${data.quitDate}</td>
			</tr>
		</c:forEach>
		<c:remove var="excel" scope="application"/>
	</table>
</body>
</html>