<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>修改密碼</title>
	<link rel="stylesheet"
		href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
	
	<!-- Bootstrap CSS -->
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
		rel="stylesheet">
	
	<!-- DataTables CSS -->
	<link rel="stylesheet"
		href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">
	
	<!-- Font Awesome -->
	<link
		href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
		rel="stylesheet">
	
	<!-- Google Fonts -->
	<link
		href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap"
		rel="stylesheet">
	
	<!-- Custom CSS -->
	<link href="<c:url value='/resources/CSS/style.css'/>" rel="stylesheet">
	
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	
	<!-- Bootstrap JS Bundle with Popper -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	
	<!-- DataTables JS -->
	<script
		src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
	
	<link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">
</head>
<body>
	<%@ include file="../body/body.jsp"%>
	<main>
		<h1>修改密碼</h1>
		<form
			action="${pageContext.request.contextPath}/changePassword"
			method="post" onsubmit="return validateForm()">
			<table class="employee-form">
				<tr>
					<th>新密碼:</th>
					<td><input type="password" name="newPassword" id="newPassword"
						required></td>
				</tr>
				<tr>
					<th>確認新密碼:</th>
					<td><input type="password" name="confirmPassword"
						id="confirmPassword" required></td>
				</tr>
			</table>
			<br>
			<button type="submit" class="action-button">修改密碼</button>
		</form>
		<%
		if (request.getAttribute("errorMessage") != null) {
		%>
		<p style="color: red;"><%=request.getAttribute("errorMessage")%></p>
		<%
		}
		%>
	</main>
	<script>
		function validateForm() {
			var newPassword = document.getElementById('newPassword').value;
			var confirmPassword = document.getElementById('confirmPassword').value;
			if (newPassword !== confirmPassword) {
				alert('兩次輸入的密碼不一致');
				return false;
			}
			return true;
		}
	</script>
	<script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>