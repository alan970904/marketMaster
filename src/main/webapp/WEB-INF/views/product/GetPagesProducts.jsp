
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品資料</title>

<!-- DataTables CSS -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- DataTables Bootstrap 5 CSS -->
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

<link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">

<style>
.inventory-low {
	color: red !important;
}

.button-disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

.shelve {
	background-color: #93FF93;
}

.remove {
	background-color: #FF5151;
}

.update {
	background-color: #BEBEBE;
}

button:hover:not(:disabled) {
	color: #1a1a1a;
	background-color: #fff;
}

button:disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

.dataTables_length select {
	padding-right: 30px !important;
	background-position: right 0.5rem center !important;
}
</style>
</head>
<body>
	<%@ include file="../body/body.jsp"%>
	<main>
		<div class="container mt-5">
			<h2 class="mb-4">商品資料</h2>


			<!--  
			<form action="${pageContext.request.contextPath}/ProductsServlet"
				class="mb-3">
				<input type="hidden" name="action" value="UpdateSaleByCheckOut">
				<input type="submit" value="更新銷售數量" class="btn btn-primary">
			</form>
-->
			<table id="productTable" class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>商品編號</th>
						<th>商品類別名稱</th>
						<th>商品名稱</th>
						<th>商品售價</th>
						<th>安全庫存量</th>
						<th>上架數量</th>
						<th>庫存數量</th>
						<th>銷售數量</th>
						<th>兌換數量</th>
						<th>銷毀數量</th>
						<th>下架數量</th>
						<th>修改</th>
						<th>上架</th>
						<th>下架</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${products}" var="product" varStatus="s">
						<tr>
							<td>${product.productId}</td>
							<td>${product.productCategory}</td>
							<td><a
								href="${pageContext.request.contextPath}/getOneProduct?productId=${product.productId}">${product.productName}</a></td>
							<td>${product.productPrice}</td>
							<td>${product.productSafeInventory}</td>
							<td>${product.numberOfShelve}</td>
							<td data-safe-inventory="${product.productSafeInventory}"
								class="inventory">${product.numberOfInventory}</td>
							<td>${product.numberOfSale}</td>
							<td>${product.numberOfExchange}</td>
							<td>${product.numberOfDestruction}</td>
							<td>${product.numberOfRemove}</td>
							<td><form method="post"
									action="${pageContext.request.contextPath}/getUpdateProduct"
									class="d-inline">
									<input type="hidden" name="action" value="GetUpdateProduct">
									<input type="hidden" name="productId"
										value=${product.productId }>
									<button type="submit" class="update btn btn-sm">修改</button>
								</form></td>
							<td><form
									action="${pageContext.request.contextPath}/getShelveProduct"
									method="post" class="d-inline">
									<input type="hidden" name="action" value="GetShelveProduct">
									<input type="hidden" name="productId"
										value="${product.productId}">
									<button type="submit" class="shelve btn btn-sm">上架</button>
								</form></td>
							<td><form
									action="${pageContext.request.contextPath}/removeProduct"
									method="post" class="d-inline">
									<input type="hidden" name="action" value="RemoveProduct">
									<input type="hidden" name="productId"
										value="${product.productId}">
									<button type="button" class="remove btn btn-sm remove-button"
										id="remove">下架</button>
								</form></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<button class="btn btn-secondary mt-3"
				onclick="window.location.href='${pageContext.request.contextPath}/product/productHomepage.jsp'">返回首頁</button>
		</div>
	</main>

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

	<script>
		$(document)
				.ready(
						function() {
							var table = $('#productTable')
									.DataTable(
											{
												"pageLength" : 10,
												"language" : {
													"url" : "//cdn.datatables.net/plug-ins/1.10.24/i18n/Chinese.json"
												},
												"drawCallback" : function(
														settings) {
													applyInventoryStyles(this
															.api());
												},
												"columnDefs" : [ {
													"targets" : [ 0, 1, 3, 4,
															5, 6, 7, 8, 9, 10,
															11, 12, 13 ],
													"searchable" : false
												} ],
												"initComplete" : function() {
													$(
															'.dataTables_filter input')
															.attr(
																	"placeholder",
																	"搜索商品名稱")
															.css(
																	{
																		'width' : '200px',
																		'display' : 'inline-block'
																	});
													$(
															'.dataTables_filter label')
															.contents().first()
															.replaceWith(
																	'搜索商品名稱：');
												}
											});

							function applyInventoryStyles(api) {
								api
										.rows()
										.every(
												function(rowIdx, tableLoop,
														rowLoop) {
													var data = this.data();
													var $row = $(this.node());
													var $inventoryCell = $row
															.find('td:eq(6)'); // 假設庫存量在第7列（索引為6）
													var inventory = parseInt(data[6]); // 使用 data array 來獲取庫存量
													var safeInventory = parseInt($inventoryCell
															.attr('data-safe-inventory'));

													if (inventory < safeInventory) {
														$inventoryCell
																.addClass('inventory-low');
													} else {
														$inventoryCell
																.removeClass('inventory-low');
													}

													var $removeButton = $row
															.find('.remove');
													var numberOfShelve = parseInt(data[5]); // 假設上架數量在第6列（索引為5）
													if (inventory === 0
															&& numberOfShelve === 0) {
														$removeButton
																.prop(
																		'disabled',
																		true)
																.addClass(
																		'button-disabled');
													} else {
														$removeButton
																.prop(
																		'disabled',
																		false)
																.removeClass(
																		'button-disabled');
													}
												});
							}

							$(document).ready(function() {
								$('.remove-button').on('click', function(e) {
									e.preventDefault(); // 防止默認的提交行為
									let result = confirm("確定要下架此商品嗎？");
									if (result) {
										// 使用 `closest` 方法找到對應的表單並提交
										$(this).closest('form').submit();
									}
								});
							});

							// 在表格重繪後應用樣式
							table.on('draw', function() {
								applyInventoryStyles(table);
							});

							// 初始應用樣式
							applyInventoryStyles(table);
						});
	</script>
	<script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>