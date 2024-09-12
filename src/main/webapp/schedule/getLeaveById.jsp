<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>個人請假紀錄</title>

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
				<h1>個人請假紀錄</h1>
			</div>
		</div>

		<div class="row mt-3">
			<div class="col text-start">
				<a href="${pageContext.request.contextPath}/schedule/AskForLeave.jsp"
					class="btn btn-secondary"> <i class="fas fa-arrow-left"></i> 返回
				</a>
			</div>
		</div>

		<hr>

		<c:if test="${empty leaveRecords}">
			<div class="alert alert-info">沒有找到符合條件的請假記錄。</div>
		</c:if>

		<c:if test="${not empty leaveRecords}">
		
			<c:set var="firstEmployeeId" value="${leaveRecords[0].employeeId}" />

			<form action="${pageContext.request.contextPath}/AskForLeaveCon"
				method="post" class="mb-4">
				<input type="hidden" name="action" value="createForm"> <input
					type="hidden" name="employee_id" value="${firstEmployeeId}">
				<button type="submit" class="btn btn-primary">建立請假表單</button>
			</form>

			<table id="leaveRecordsTable"
				class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>請假編號</th>
						<th>員工編號</th>
						<th>員工姓名</th>
						<th>開始時間</th>
						<th>結束時間</th>
						<th>請假類別</th>
						<th>請假原因</th>
						<th>批准狀態</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="leave" items="${leaveRecords}">
						<tr>
							<td><c:out value="${leave.leaveId}" /></td>
							<td><c:out value="${leave.employeeId}" /></td>
							<td><c:out value="${leave.empBean.employeeName}" /></td>
							<td><c:out value="${leave.startDatetime}" /></td>
							<td><c:out value="${leave.endDatetime}" /></td>
							<td><c:out value="${leave.leaveCategory}" /></td>
							<td><c:out value="${leave.reasonOfLeave}" /></td>
							<td><c:out value="${leave.approvedStatus}" /></td>
							<td><c:if test="${leave.approvedStatus != '已批准'}">
									<!-- 刪除按鈕 -->
									<form
										action="${pageContext.request.contextPath}/AskForLeaveCon"
										method="post" class="d-inline"
										onsubmit="return handleAction(event, '刪除', '${leave.approvedStatus}')">
										<input type="hidden" name="action" value="delete" /> <input
											type="hidden" name="leave_id" value="${leave.leaveId}" /> <input
											type="hidden" name="employee_id" value="${leave.employeeId}" />
										<input type="hidden" name="approved_status"
											value="${leave.approvedStatus}" />
										<button type="submit" class="btn btn-danger btn-sm">刪除</button>
									</form>

									<!-- 修改按鈕 -->
									<form
										action="${pageContext.request.contextPath}/AskForLeaveCon"
										method="post" class="d-inline"
										onsubmit="return handleAction(event, '修改', '${leave.approvedStatus}')">
										<input type="hidden" name="action" value="searchByLeaveId" />
										<input type="hidden" name="leave_id" value="${leave.leaveId}" />
										<input type="hidden" name="approved_status"
											value="${leave.approvedStatus}" />
										<button type="submit" class="btn btn-warning btn-sm">修改</button>
									</form>
								</c:if> <c:if test="${leave.approvedStatus == '已批准'}">
									<span class="text-muted">已批准不可編輯</span>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>


	</div>
</body>
</html>

