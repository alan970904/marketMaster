<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List, com.MarketMaster.bean.employee.EmpBean"%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>員工資訊</title>
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
<link href="/marketMaster/CSS/style.css" rel="stylesheet">

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

<link rel="stylesheet" href="/marketMaster/CSS/extra.css">
</head>
<body>
	<%@ include file="/body/body.jsp"%>
	<main class="container mt-4">
		<h1 class="mb-4">員工資訊</h1>
		<form action="${pageContext.request.contextPath}/EmpServlet"
			method="get" class="mb-3">
			<input type="hidden" name="action" value="getAll"> <input
				type="submit" value="查詢全部員工" class="btn btn-primary">
		</form>
		<form action="${pageContext.request.contextPath}/EmpServlet"
			method="get" class="mb-3">
			<input type="hidden" name="action" value="search">
			<div class="input-group">
				<input type="text" name="searchName" placeholder="輸入員工姓名"
					class="form-control">
				<button type="submit" class="btn btn-outline-secondary">查詢</button>
			</div>
		</form>
		<button id="add" class="btn btn-success">新增員工</button>
	</main>
	<script>
		// 新增員工按鈕的點擊事件處理
		document
				.getElementById('add')
				.addEventListener(
						'click',
						function() {
							window.location.href = "${pageContext.request.contextPath}/EmpServlet?action=showAddForm";
						});
	</script>
</body>
</html>