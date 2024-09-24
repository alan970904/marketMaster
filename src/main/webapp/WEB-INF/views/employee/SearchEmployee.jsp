<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>員工列表</title>
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
		<h1 class="mb-4">員工列表</h1>
		<c:if test="${not empty searchName}">
			<p>搜尋 "${searchName}" 的結果：共找到 ${employeeCount} 筆記錄</p>
		</c:if>
		<table class="table table-striped table-hover">
			<tr>
				<th>員工編號</th>
				<th>姓名</th>
				<th>電話</th>
				<th>Email</th>
				<th>職位編號</th>
				<th>入職日期</th>
				<th>操作</th>
			</tr>
			<!-- 使用 JSTL 迭代顯示員工列表 -->
			<c:forEach var="emp" items="${employees}">
				<tr>
					<td>${emp.employeeId}</td>
					<td>${emp.employeeName}</td>
					<td>${emp.employeeTel}</td>
					<td>${emp.employeeEmail}</td>
					<td>${emp.positionId}</td>
					<td>${emp.hiredate}</td>
					<td><a
						href="${pageContext.request.contextPath}/employee/details?employeeId=${emp.employeeId}"
						class="action-button info">資訊</a> <a
						href="${pageContext.request.contextPath}/employee/update?employeeId=${emp.employeeId}"
						class="action-button edit">修改</a>
						<button onclick="confirmDelete('${emp.employeeId}')"
							class="action-button delete">刪除</button></td>
				</tr>
			</c:forEach>
		</table>
		<button id="back" class="btn btn-secondary mt-3">返回員工資訊</button>
	</main>
	<script>
		function confirmDelete(employeeId) {
			if (confirm("確定要刪除此員工嗎？")) {
				window.location.href = "${pageContext.request.contextPath}/employee/delete?employeeId="
						+ employeeId;
			}
		}

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