<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.MarketMaster.bean.employee.EmpBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增員工</title>
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
<link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">

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

<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">
</head>
<body>
	<%@ include file="/body/body.jsp"%>
	<main class="container mt-4">
		<h1 class="mb-4">新增員工</h1>
		<h2 class="mb-3">(員工編號 ${newEmployeeId})</h2>
		<form action="${pageContext.request.contextPath}/EmpServlet"
			method="post" onsubmit="return validateForm()">
			<input type="hidden" name="action" value="add"> <input
				type="hidden" name="employeeId" value="${newEmployeeId}">
			<table class="table table-bordered">
				<tr>
					<th>姓名:</th>
					<td><input type="text" name="employeeName" id="employeeName"
						required></td>
				</tr>
				<tr>
					<th>電話:</th>
					<td><input type="text" name="employeeTel" id="employeeTel"
						maxlength="10" required></td>
				</tr>
				<tr>
					<th>身分證號碼:</th>
					<td><input type="text" name="employeeIdcard"
						id="employeeIdcard" maxlength="10" required></td>
				</tr>
				<tr>
					<th>Email:</th>
					<td><input type="email" name="employeeEmail"
						id="employeeEmail" required></td>
				</tr>
				<tr>
					<th>職位編號:</th>
					<td><input type="text" name="positionId" id="positionId"
						required></td>
				</tr>
				<tr>
					<th>入職日期:</th>
					<td><input type="date" name="hiredate" required></td>
				</tr>
			</table>
			<input type="hidden" name="password" id="password" value="0000"
				required>
			<button type="submit" class="btn btn-primary">新增員工</button>
		</form>
		<div id="errorMessages" class="text-danger mt-3"></div>
		<button id="back" class="btn btn-secondary mt-3">返回員工資訊</button>
	</main>

	<script>
		function validateForm() {
			var name = document.getElementById('employeeName').value;
			var tel = document.getElementById('employeeTel').value;
			var idcard = document.getElementById('employeeIdcard').value;
			var email = document.getElementById('employeeEmail').value;
			var password = document.getElementById('password').value;
			var positionId = document.getElementById('positionId').value;
			var errorMessages = [];

			// 驗證姓名（至少兩個字的中文）
			if (!/^[\u4e00-\u9fa5]{2,}$/.test(name)) {
				errorMessages.push('姓名格式不正確，請輸入至少兩個字的中文。');
			}

			// 驗證電話（09開頭，總共10個數字）
			if (!/^09\d{8}$/.test(tel)) {
				errorMessages.push('電話格式不正確，請輸入09開頭的10位數字。');
			}

			// 驗證身分證號碼（台灣身分證號碼格式）
			if (!/^[A-Z][12]\d{8}$/.test(idcard)) {
				errorMessages.push('身分證號碼格式不正確，請輸入正確的台灣身分證號碼。');
			}

			// 驗證Email
			if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
				errorMessages.push('Email格式不正確，請輸入有效的Email地址。');
			}

			// 驗證密碼（至少4個數字）
			if (!/^\d{4,}$/.test(password)) {
				errorMessages.push('密碼格式不正確，請輸入至少4個數字。');
			}

			// 驗證職位編號(M開頭，兩位數)
			if (!/^M\d{2}$/.test(positionId)) {
				errorMessages.push('職位編號格式不正確，請輸入M開頭加上兩位數字。');
			}

			if (errorMessages.length > 0) {
				document.getElementById('errorMessages').innerHTML = errorMessages
						.join('<br>');
				return false;
			}

			return true;
		}

		// 返回按鈕的點擊事件處理
		document
				.getElementById('back')
				.addEventListener(
						'click',
						function() {
							window.location.href = "${pageContext.request.contextPath}/employee/EmployeeMain.jsp";
						});
	</script>
</body>
</html>