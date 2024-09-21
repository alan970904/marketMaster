<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>取消退貨</title>
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
    <main>
        <h1>取消退貨</h1>
        <form action="${pageContext.request.contextPath}/CheckoutDetailsServlet" method="post">
            <input type="hidden" name="action" value="cancelReturn">
            <input type="hidden" name="checkoutId" value="${checkoutDetails.checkoutId}">
            <input type="hidden" name="productId" value="${checkoutDetails.productId}">
            <p>數量: <input type="text" name="numberOfCheckout" value="${checkoutDetails.numberOfCheckout}"></p>
            <p>產品價格: <input type="text" name="productPrice" value="${checkoutDetails.productPrice}"></p>
            <p>結帳總價: <input type="text" name="checkoutPrice" value="${checkoutDetails.checkoutPrice}"></p>
            <button type="submit" class="action-button">提交</button>
            <button id="back" class="action-button">返回</button>
        </form>
    </main>
    <script>
        document.getElementById('back').addEventListener('click', function() {
            window.history.back();
        });
    </script>
</body>
</html>
