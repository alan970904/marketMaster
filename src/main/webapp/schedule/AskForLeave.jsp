<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>請假單</title>

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
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
<link href="/ispan/CSS/style.css" rel="stylesheet">

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

<link rel="stylesheet" href="/ispan/CSS/extra.css">

</head>
<body>
	<%@ include file="/body/body.jsp"%>
	<div class="container mt-5">
		<div class="row">
			<div class="col text-center">
				<h1>請假單</h1>
			</div>
		</div>

		<div class="row mt-3">
			<div class="col text-start">
				<a href="${pageContext.request.contextPath}/body/HomePage.jsp"
					class="btn btn-secondary"> <i class="fas fa-arrow-left"></i> 返回
				</a>
			</div>
		</div>

		<div class="row mt-4">
			<div class="col text-center">
				<a
					href="${pageContext.request.contextPath}/AskForLeaveCon?action=allLeaveRecords"
					class="btn btn-primary">查看所有員工請假表</a>
			</div>
		</div>

		<hr>

		<div class="row">
			<div class="col">
				<div class="card p-4">
					<form id="searchForm"
						action="${pageContext.request.contextPath}/AskForLeaveCon"
						method="get">
						<input type="hidden" name="action" value="searchLeaveRecords">
						<div class="mb-3">
							<label for="employeeId" class="form-label">輸入員工編號查詢:</label> <input
								type="text" class="form-control" id="employeeId"
								name="employee_id" placeholder="請輸入員工編號" required>
						</div>
						<button type="submit" class="btn btn-primary">查詢</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>


