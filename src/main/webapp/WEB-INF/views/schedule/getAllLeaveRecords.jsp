<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>員工請假記錄</title>
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
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script src="<c:url value='/resources/js/main.js'/>"></script>
<script>
	function filterTable() {
		var inputs, table, tr, td, i, j, txtValue, filterValues;
		table = document.getElementById("leaveTable");
		tr = table.getElementsByTagName("tr");
		inputs = table.getElementsByTagName("input");

		filterValues = [];
		for (i = 0; i < inputs.length; i++) {
			filterValues.push(inputs[i].value.toLowerCase());
		}

		for (i = 1; i < tr.length; i++) { // Skip header row
			tr[i].style.display = "none";
			td = tr[i].getElementsByTagName("td");
			var showRow = true;
			for (j = 0; j < td.length; j++) {
				if (td[j]) {
					txtValue = td[j].textContent || td[j].innerText;
					if (filterValues[j]
							&& !txtValue.toLowerCase()
									.includes(filterValues[j])) {
						showRow = false;
						break;
					}
				}
			}
			if (showRow) {
				tr[i].style.display = "";
			}
		}
	}
</script>
</head>
<body>
	<%@ include file="../body/body.jsp"%>
	<div class="container mt-5">
		<h1 class="mb-4">員工請假記錄</h1>

		<div class="row mt-3">
			<div class="col text-start">
				<a href="${pageContext.request.contextPath}/AskForLeaveCon/view.jsp"
					class="btn btn-secondary"> <i class="fas fa-arrow-left"></i> 返回
				</a>
			</div>
		</div>

		<c:if test="${not empty employeeName}">
			<h2 class="mb-4">員工請假記錄</h2>
		</c:if>

		<table id="leaveTable" class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>請假編號 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>員工編號 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>員工姓名 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>開始時間 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>結束時間 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>請假類別 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>請假原因 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
					<th>批准狀態 <input type="text" onkeyup="filterTable()"
						class="form-control form-control-sm" placeholder="篩選"></th>
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
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
	<script>
		function filterTable() {
			var inputs, table, tr, td, i, j, txtValue, filterValues;
			table = document.getElementById("leaveTable");
			tr = table.getElementsByTagName("tr");
			inputs = table.getElementsByTagName("input");

			filterValues = [];
			for (i = 0; i < inputs.length; i++) {
				filterValues.push(inputs[i].value.toLowerCase());
			}

			for (i = 1; i < tr.length; i++) { // Skip header row
				tr[i].style.display = "none";
				td = tr[i].getElementsByTagName("td");
				var showRow = true;
				for (j = 0; j < td.length; j++) {
					if (td[j]) {
						txtValue = td[j].textContent || td[j].innerText;
						if (filterValues[j]
								&& !txtValue.toLowerCase().includes(
										filterValues[j])) {
							showRow = false;
							break;
						}
					}
				}
				if (showRow) {
					tr[i].style.display = "";
				}
			}
		}
	</script>
</body>
</html>