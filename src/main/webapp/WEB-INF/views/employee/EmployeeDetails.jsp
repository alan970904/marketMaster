<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>員工詳細資訊</title>
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
	<main class="container mt-4">
		<h1 class="mb-4">員工詳細資訊</h1>
		<table class="table table-bordered">
			<tr>
				<th>員工編號:</th>
				<td>${viewEmployee.employeeId}</td>
			</tr>
			<tr>
				<th>姓名:</th>
				<td>${viewEmployee.employeeName}</td>
			</tr>
			<tr>
				<th>電話:</th>
				<td>${viewEmployee.employeeTel}</td>
			</tr>
			<tr>
				<th>身分證號碼:</th>
				<td>${viewEmployee.employeeIdcard}</td>
			</tr>
			<tr>
				<th>Email:</th>
				<td>${viewEmployee.employeeEmail}</td>
			</tr>
			<tr>
				<th>職位名稱:</th>
				<td>${viewEmployee.positionName}</td>
			</tr>
			<tr>
				<th>薪資級數:</th>
				<td>${viewEmployee.salaryLevel}</td>
			</tr>
			<tr>
				<th>入職日期:</th>
				<td>${viewEmployee.hiredate}</td>
			</tr>
			<tr>
				<th>離職日期:</th>
				<td>${viewEmployee.resigndate != null ? viewEmployee.resigndate : '尚未離職'}</td>
			</tr>
		</table>
		<button id="back" class="btn btn-secondary mt-3">返回員工資訊</button>
	</main>
	<script>
		// 返回按鈕的點擊事件處理
		document
				.getElementById('back')
				.addEventListener(
						'click',
						function() {
							window.location.href = "${pageContext.request.contextPath}/employee/empMain";
						});
	</script>
	<script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>