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
<link rel="stylesheet"
href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">


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

<!-- DataTables CSS -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">
<style>
.dataTables_length{
display:none;
}
</style>
</head>
   <%@ include file="/body/body.jsp" %>
		<!-- Navbar End -->
		<main style= "width:100%; " >
		<div class="card" >

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
		$(document)
				.ready(
						function() {
							const contextPath = '${pageContext.request.contextPath}';
							console.log('Context Path:', contextPath);

							var table = $('#restockDetailsTable')
									.DataTable(
											{
												"ajax" : {
													"url" : contextPath
															+ "/RestockDetailsServlet",
													"type" : "GET",
													"data" : {
														"action" : "getAllRestockDetails"
													},
													"dataSrc" : function(json) {
														console
																.log(
																		'Received data:',
																		json);
														if (json.error) {
															console
																	.error(
																			'Error in response:',
																			json.error);
															return [];
														}
														return json;
													},
													"error" : function(xhr,
															error, thrown) {
														console
																.error(
																		'Ajax request failed:',
																		error,
																		thrown);
														console
																.error(
																		'Response Text:',
																		xhr.responseText);
														alert('無法載入數據，請查看控制台以獲取更多信息。');
													}
												},
												"columns" : [
														{
															"data" : "restockId"
														},
														{
															"data" : "employeeId"
														},
														{
															"data" : "productId"
														},
														{
															"data" : "productName"
														},
														{
															"data" : "productCategory"
														},
														{
															"data" : "numberOfRestock"
														},
														{
															"data" : "productPrice"
														},
														{
															"data" : "restockDate"
														},
														{
															"data" : "productionDate"
														},
														{
															"data" : "dueDate"
														},
														{

															"data" : null,
															"render" : function(
																	data, type,
																	row) {
																return '<button class="action-btn update-btn" data-restockid="' + row.restockId + '">更新</button> '
																		+ '<button class="action-btn delete-btn" data-restockid="' + row.restockId + '">刪除</button>';
															}
														} ],
												"order": [[0, "desc"]],
												"language" : {
													"url" : "//cdn.datatables.net/plug-ins/1.11.5/i18n/zh-HANT.json"
												},
												"initComplete" : function(
														settings, json) {
													console
															.log(
																	'DataTable initialization complete. Data:',
																	json);
												}
											});

							$('#restockDetailsTable')
									.on(
											'click',
											'button.update-btn',
											function() {
												var restockId = $(this).data(
														'restockid');
												var rowData = table.row(
														$(this).closest('tr'))
														.data();
												console.log('更新按鈕被點擊，進貨ID：',
														restockId);
												console.log('行數據：', rowData);
												// 這裡添加更新邏輯
											});

							$('#restockDetailsTable')
									.on(
											'click',
											'button.delete-btn',
											function() {
												var restockId = $(this).data(
														'restockid');
												var row = $(this).closest('tr');

												if (confirm('確定要刪除這條記錄嗎？')) {
													$
															.ajax({
																url : contextPath
																		+ '/RestockDetailsServlet',
																type : 'POST',
																data : {
																	action : 'deleteRestock',
																	restockId : restockId
																},
																success : function(
																		response) {
																	if (response.success) {
																		alert(response.message);
																		table
																				.row(
																						row)
																				.remove()
																				.draw();
																	} else {
																		alert('刪除失敗: '
																				+ response.message);
																	}
																},
																error : function(
																		xhr,
																		status,
																		error) {
																	alert('刪除請求失敗: '
																			+ error);
																}
															});
												}
											});
						});
	</script>
</body>
</html>