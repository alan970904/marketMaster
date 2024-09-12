<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
input[type="text"], input[type="number"] {
 width: 100%;
    box-sizing: border-box; /* 確保 padding 不會影響寬度 */
	background-color: lightgrey;
	border-radius: 5px;
	border: 1px solid #ccc;
	padding: 5px;
}

input[type="button"] {
	background-color: #4CAF50;

	border: none;
	border-radius: 5px;
	margin-top:10px;
	padding: 10px 20px;
	cursor: pointer;
}

input[type="button"]:hover {
	background-color: #45a049;
}
tr td {
	border-bottom: 2px solid grey;
	padding: 10px;
}
</style>
</head>
<body class="body">
<%@ include file="/body/body.jsp" %>
<main>
<div align="center">
    <h2>成功新增一筆商品資料</h2>

<table>
<tr><td>商品編號:<td><input type="text" disabled value=${product.productId }>
<tr><td>商品類別名稱:<td><input type="text" disabled value=${product.productCategory }>
<tr><td>商品名稱:<td><input type="text" disabled value=${product.productName }>
<tr><td>商品售價:<td><input type="text" disabled value=${product.productPrice }>
<tr><td>安全庫存量:<td><input type="text" disabled value=${product.productSafeInventory }>
<tr><td>上架數量:<td><input type="text" disabled value=${product.numberOfShelve }>
<tr><td>庫存數量:<td><input type="text" disabled value=${product.numberOfInventory }>
<tr><td>銷售數量:<td><input type="text" disabled value=${product.numberOfSale }>
<tr><td>兌換數量:<td><input type="text" disabled value=${product.numberOfExchange }>
<tr><td>銷毀數量:<td><input type="text" disabled value=${product.numberOfDestruction }>
<tr><td>下架數量:<td><input type="text" disabled value=${product.numberOfRemove }>
</table>
<p>
<input type="button" value="返回首頁" onclick="window.location.href='${pageContext.request.contextPath}/product/productHomepage.jsp'">
    
</div>
</main>
</body>
</html>
