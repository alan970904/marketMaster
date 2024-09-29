<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta content="width=device-width, initial-scale=1.0" name="viewport">
	<meta content="" name="keywords">
	<meta content="" name="description">

	<title>庫存查詢</title>
	<link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

	<!-- Bootstrap CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

	<!-- Font Awesome -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

	<!-- Google Fonts -->
	<link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

	<!-- Custom CSS -->
	<link href="<c:url value='/resources/CSS/style.css'/>" rel="stylesheet">
	
	<!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

	<!-- DataTables CSS -->
	<link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">
	
	<link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">

	<style>
		.dataTables_length select { padding-right: 30px !important; background-position: right 0.5rem center !important; }
	</style>
</head>

<body>
<%@ include file="../body/body.jsp"%>
<main style="width: 100%;">
	<div class="card">
		<h1>進貨明細</h1>
		<table id="restockDetailsTable" class="display">
			<thead>
			<tr>
				<th>進貨ID</th>
				<th>員工ID</th>
				<th>產品ID</th>
				<th>產品類別</th>
				<th>產品名稱</th>
				<th>進貨數量</th>
				<th>進貨價格</th>
				<th>進貨日期</th>
				<th>生產日期</th>
				<th>到期日期</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<!-- DataTable 將在這裡動態填充數據 -->
			</tbody>
		</table>
	</div>
</main>

<script>
	$(document).ready(function () {
		const contextPath = '${pageContext.request.contextPath}';
		console.log('Context Path:', contextPath);

		var table = $('#restockDetailsTable').DataTable({
			"ajax": {
				"url": contextPath + "/restock/getAllRestockDetailsJson",
				"type": "GET",
				"dataSrc": "",
				"error": function (xhr, error, thrown) {
					console.error('Error fetching data: ', error);
					console.error('Response: ', xhr.responseText);
					alert("DataTable Error: " + error + ". Please check the console for more details.");
				}
			},
			"columns": [
				{ "data": "restockId" },
				{ "data": "employeeId" },
				{ "data": "productId" },
				{ "data": "productCategory" },
				{ "data": "productName" },
				{ "data": "numberOfRestock" },
				{ "data": "productPrice" },
				{
					"data": "restockDate",
					"render": function (data) {
						return formatDateArray(data);  // 调用格式化日期函数
					}
				},
				{
					"data": "productionDate",
					"render": function (data) {
						return formatDateArray(data);
					}
				},
				{
					"data": "dueDate",
					"render": function (data) {
						return formatDateArray(data);
					}
				},
				{
					"data": null,
					"render": function (data, type, row) {
						return '<button class="action-btn update-btn" data-restockid="' + row.restockId + '">更新</button> ' +
								'<button class="action-btn delete-btn" data-restockid="' + row.restockId + '">刪除</button>';
					}
				}
			],
			"order": [[0, "desc"]],
			"language": {
				"url": "//cdn.datatables.net/plug-ins/1.11.5/i18n/zh-HANT.json"
			},
			"initComplete": function (settings, json) {
				console.log('DataTable initialization complete. Data:', json);
			}
		});

		// 日期格式化函数
		function formatDateArray(dateArray) {
			if (dateArray && Array.isArray(dateArray)) {
				return dateArray[0] + '-' + (dateArray[1] < 10 ? '0' + dateArray[1] : dateArray[1]) + '-' + (dateArray[2] < 10 ? '0' + dateArray[2] : dateArray[2]);
			}
			return '';
		}

		// 更新和刪除按鈕的事件處理器
		$('#restockDetailsTable').on('click', 'button.update-btn', function () {
			var restockId = $(this).data('restockid');
			var rowData = table.row($(this).closest('tr')).data();
			console.log('更新按鈕被點擊，進貨ID：', restockId);
			console.log('行數據：', rowData);
			// 這裡添加更新邏輯
		});

		$('#restockDetailsTable').on('click', 'button.delete-btn', function () {
			var restockId = $(this).data('restockid');
			var row = $(this).closest('tr');

			if (confirm('確定要刪除這條記錄嗎？')) {
				$.ajax({
					url: contextPath + '/restock/deleteRestock',
					type: 'POST',
					data: {
						restockId: restockId
					},
					success: function (response) {
						if (response.success) {
							alert(response.message);
							table.row(row).remove().draw();
						} else {
							alert('删除失败: ' + response.message);
						}
					},
					error: function (xhr, status, error) {
						alert('删除请求失败: ' + error);
					}
				});
			}
		});
	});
</script>

<script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>
