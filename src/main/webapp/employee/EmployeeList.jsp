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
		<h1 class="mb-4">員工列表</h1>
		<c:if test="${not empty message}">
			<p style="color: lightgreen;">${message}</p>
		</c:if>
		<c:if test="${not empty searchName}">
			<p>搜尋 "${searchName}" 的結果：共找到 ${employeeCount} 筆記錄</p>
		</c:if>
		<!-- 切換顯示所有員工/在職員工的按鈕 -->
		<c:if test="${not isSearchResult}">
			<button id="toggleEmployees" class="action-button">
				${showAll ? '顯示在職員工' : '顯示所有員工(包含離職)'}</button>
		</c:if>
		<table id="employeeTable" class="table table-striped table-hover">
			<thead>
				<tr>
					<th>員工編號</th>
					<th>姓名</th>
					<th>電話</th>
					<th>Email</th>
					<th>職位編號</th>
					<th>入職日期</th>
					<th>狀態</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="emp" items="${employees}">
					<tr>
						<td>${emp.employeeId}</td>
						<td>${emp.employeeName}</td>
						<td>${emp.employeeTel}</td>
						<td>${emp.employeeEmail}</td>
						<td>${emp.positionId}</td>
						<td>${emp.hiredate}</td>
						<td>${empty emp.resigndate ? '在職' : '離職'}</td>
						<td><a
							href="${pageContext.request.contextPath}/EmpServlet?action=get&employeeId=${emp.employeeId}"
							class="action-button info">資訊</a> <c:if
								test="${empty emp.resigndate}">
								<a
									href="${pageContext.request.contextPath}/EmpServlet?action=getForUpdate&employeeId=${emp.employeeId}"
									class="action-button edit">修改</a>
								<button class="action-button delete" data-id="${emp.employeeId}">刪除</button>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<c:if test="${not isSearchResult}">
			<h2>職級列表</h2>
			<table class="table table-striped table-hover">
				<tr>
					<th>職位編號</th>
					<th>職位名稱</th>
					<th>權限</th>
					<th>人數</th>
				</tr>
				<c:forEach var="rank" items="${ranks}">
					<tr>
						<td>${rank.positionId}</td>
						<td>${rank.positionName}</td>
						<td>${rank.limitsOfAuthority}</td>
						<td>${rank.activeEmployeeCount}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		<button
			onclick="location.href='${pageContext.request.contextPath}/employee/EmployeeMain.jsp'"
			class="btn btn-secondary mt-3">返回員工資訊</button>
	</main>
	<script>
        $(document).ready(function() {
            var table = $('#employeeTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.25/i18n/Chinese-traditional.json"
                }
            });

            // 使用事件委派來處理刪除按鈕點擊
            $('#employeeTable').on('click', '.delete', function() {
                var employeeId = $(this).data('id');
                confirmDelete(employeeId);
            });

            function confirmDelete(employeeId) {
                if (confirm("確定要刪除此員工嗎？")) {
                    $.ajax({
                        url: "${pageContext.request.contextPath}/EmpServlet?action=delete&employeeId=" + employeeId,
                        type: 'GET',
                        success: function(response) {
                            // 假設刪除成功後，服務器返回更新後的員工列表數據
                            // 這裡需要根據實際情況來處理返回的數據
                            location.reload(); // 暫時使用重新加載頁面的方式
                        },
                        error: function(xhr, status, error) {
                            alert("刪除失敗：" + error);
                        }
                    });
                }
            }
        });
        
     	// 切換顯示所有員工(包含離職)/在職員工的函數
        function toggleEmployees() {
            var showAll = ${showAll} ? 'false' : 'true';
            window.location.href = '${pageContext.request.contextPath}/EmpServlet?action=getAll&showAll=' + showAll;
        }
        
        // 頁面加載完成後添加事件監聽器
        document.addEventListener('DOMContentLoaded', function() {
            var toggleButton = document.getElementById('toggleEmployees');
            if (toggleButton) {
                toggleButton.addEventListener('click', toggleEmployees);
            }
        });
    </script>
</body>
</html>