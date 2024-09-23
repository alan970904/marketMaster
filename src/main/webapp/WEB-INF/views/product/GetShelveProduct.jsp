<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
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
<link href="<c:url value='/resources/CSS/style.css'/>" rel="stylesheet">

<link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">

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
 
<meta charset="UTF-8">
<title>商品資料</title>
<style>
body {
	font-family: Arial, sans-serif;
	font-size: 1.2em;
}

input[type="text"], input[type="number"] {
 width: 100%;
    box-sizing: border-box; /* 確保 padding 不會影響寬度 */
	background-color: lightgrey;
	border-radius: 5px;
	border: 1px solid #ccc;
	padding: 5px;
}

table {
    border-collapse: collapse;
    margin-top: 20px;
}

tr td {
	border-bottom: 2px solid grey;
	padding: 10px;
}

input[type="submit"] {
	background-color: #4CAF50;

	border: none;
	border-radius: 5px;
	padding: 10px 20px;
	cursor: pointer;
	margin-top:10px;
}

input[type="submit"]:hover {
	background-color: #45a049;
}
</style>
</head>
<body>
<%@ include file="../body/body.jsp" %>
<main>
<div align="center">
<h2>商品資料</h2>
<table>
<tr><td>商品編號:<td><input type="text" readonly value="${product.productId}">
<tr><td>商品名稱:<td><input type="text" readonly value="${product.productName}">
<tr><td>安全庫存量:<td><input type="text" readonly value="${product.productSafeInventory}">
<tr><td>上架數量:<td><input type="text" readonly value="${product.numberOfShelve}">
<tr><td>庫存數量:<td><input type="number" readonly value="${product.numberOfInventory}" id="inventory" min="0">
</table>
<p>
<form method="post" action="${pageContext.request.contextPath}/shelveProduct">
<h2>上架物品數量更改</h2>
    <table>
	<tr><td>商品編號:<td><input type="text" readonly value="${product.productId}" name="productId">
    <tr><td>要上架數量:<td><input type="number" name="numberOfShelve" id="readyShelve" max="${product.numberOfInventory}" min="0">
    </table> 
    <input type="hidden" value="ShelveProduct" name="action">
	<input type="submit" value="送出">
</form>
<p>
<p>
<input type="button" value="返回首頁" onclick="window.location.href='${pageContext.request.contextPath}/product/productHomepage.jsp'">

</div>
</main>

</body>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
$(document).ready(function() {
    $("#readyShelve").blur(function() {
        let safeInventory = ${product.productSafeInventory};
        let readyShelve = parseInt($(this).val());
        let finalInventory = ${product.numberOfInventory} - readyShelve;

        if(finalInventory < safeInventory) {
            alert("庫存量小於安全庫存量");
        }
        if(finalInventory <= 0) {
            alert("庫存數量不足 無法再增加上架數量");
        }
    });
});
</script>
<script src="<c:url value='/resources/js/main.js'/>"></script>
</html>
