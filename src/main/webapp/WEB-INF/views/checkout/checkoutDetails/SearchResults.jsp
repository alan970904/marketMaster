<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>產品搜索結果</title>
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
    <%@ include file="../../body/body.jsp"%>
    <main>
        <h1>產品搜索結果</h1>
        <table border="1">
            <tr>
                <th>結帳編號</th>
                <th>產品編號</th>
                <th>數量</th>
                <th>產品價格</th>
                <th>結帳總價</th>
                <th>操作</th>
            </tr>
            <c:forEach var="detail" items="${checkoutDetails}">
                <tr>
                    <td>${detail.checkoutId}</td>
                    <td>${detail.productId}</td>
                    <td>${detail.numberOfCheckout}</td>
                    <td>${detail.productPrice}</td>
                    <td>${detail.checkoutPrice}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/CheckoutDetailsServlet?action=getOne&checkoutId=${detail.checkoutId}&productId=${detail.productId}" class="action-button info">詳情</a>
                        <a href="${pageContext.request.contextPath}/CheckoutDetailsServlet?action=getUpdate&checkoutId=${detail.checkoutId}&productId=${detail.productId}" class="action-button edit">修改</a>
                        <a href="${pageContext.request.contextPath}/CheckoutDetailsServlet?action=delete&checkoutId=${detail.checkoutId}&productId=${detail.productId}" onclick="return confirm('確定要刪除嗎？')" class="action-button delete">刪除</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <button id="back" class="action-button">返回</button>
   </main>
    <script>
        document.getElementById('back').addEventListener('click', function() {
            window.history.back();
        });
    </script>
</body>
</html>
