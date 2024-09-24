<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
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
<meta charset="UTF-8">
<title>商品資料</title>
<style>
.number ,.text {
	background-color: lightgrey;
	border-radius: 5px;
	padding: 5px;
	border: 1px solid #ccc;
}

table {
	border-collapse: collapse;
	width: 80%;
	margin: 20px auto;
}

td {
	padding: 10px;
	text-align: left;
}

.submit{
	background-color: #4CAF50;
	border: none;
	border-radius: 5px;
	padding: 10px 20px;
	cursor: pointer;
}

.submit:hover {
	background-color: #45a049;
}
.div1{
    width:420px;
    text-align: center;
    margin: auto;
}
</style>
</head>
<body>
<%@ include file="/body/body.jsp" %>
<main>
<div class="div1">
	<h2>商品資料</h2>
	<form method="get" action="${pageContext.request.contextPath}/ProductsServlet">
		<table>
			<tr>
				<td>商品編號
				<td><input class="text" type="text" readonly value="${product.productId}" name="productId">
			<tr>
				<td>商品類別名稱
				<td><input class="text"  type="text" value="${product.productCategory}" name="productCategory">
			<tr>
				<td>商品名稱
				<td><input class="text"  type="text" value="${product.productName}" name="productName">
			<tr>
				<td>商品售價
				<td><input class="number"  type="number" value="${product.productPrice}" name="productPrice" step="any" min="0">
			<tr>
				<td>安全庫存量
				<td><input class="number" type="number" value="${product.productSafeInventory}" name="productSafeInventory" step="any" min="0">
				
				<input class="number" type="hidden" value="${product.numberOfShelve}" name="numberOfShelve" step="any" min="0">
				<input class="number" type="hidden" value="${product.numberOfInventory}" name="numberOfInventory" step="any" min="0">
				<input class="number" type="hidden" value="${product.numberOfSale}" name="numberOfSale" step="any" min="0">
				<input class="number" type="hidden" value="${product.numberOfExchange}" name="numberOfExchange" step="any" min="0">
				<input class="number" type="hidden" value="${product.numberOfDestruction}" name="numberOfDestruction" step="any" min="0">
				<input class="number" type="hidden" value="${product.numberOfRemove}" name="numberOfRemove" step="any" min="0">
		</table>
		<p>
			<input type="hidden" name="action" value="UpdateProduct">
			<input class="submit" type="submit" value="送出">
			
	</form>
			<p>
<input type="button" value="返回首頁" onclick="window.location.href='${pageContext.request.contextPath}/product/productHomepage.jsp'">
</div>
</main>
</body>
</html>
