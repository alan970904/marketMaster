<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>搜尋結果</title>
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
<style>
main{
 body {
            background-color: #f8f9fa;
        }
        main {
            padding: 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }
        h1 {
            color: #343a40;
            margin-bottom: 1.5rem;
            text-align: center;
        }
        .search-info {
            background-color: #e9ecef;
            padding: 1rem;
            border-radius: 0.25rem;
            margin-bottom: 1.5rem;
        }
        .table-container {
            background-color: #ffffff;
            border-radius: 0.25rem;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            overflow-x: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 0.75rem;
            text-align: left;
            border-bottom: 1px solid #dee2e6;
        }
        th {
            background-color: #007bff;
            color: #ffffff;
        }
        tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        .action-button {
            margin: 0.25rem;
            padding: 0.375rem 0.75rem;
            font-size: 0.875rem;
            line-height: 1.5;
            border-radius: 0.2rem;
            color: #fff;
            background-color: #007bff;
            border: none;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .action-button.info { background-color: #17a2b8; }
        .action-button.edit { background-color: #ffc107; color: #212529; }
        .action-button.delete { background-color: #dc3545; }
        #back {
            margin-top: 1.5rem;
            background-color: #6c757d;
        }


}

</style>


</head>
<body>
    <%@ include file="../../body/body.jsp"%>
     <main>
        <h1>搜尋結果</h1>
        <div class="search-info">
            <p>搜尋電話號碼: <strong>${param.customerTel}</strong></p>
        </div>
        <div class="table-container">
            <table>
                <tr>
                    <th>結帳編號</th>
                    <th>客戶電話</th>
                    <th>員工編號</th>
                    <th>總價</th>
                    <th>結帳日期</th>
                    <th>紅利點數</th>
                    <th>點數到期日</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="checkout" items="${checkouts}">
                    <tr>
                        <td>${checkout.checkoutId}</td>
                        <td>${checkout.customerTel}</td>
                        <td>${checkout.employeeId}</td>
                        <td>${checkout.checkoutTotalPrice}</td>
                        <td>${checkout.checkoutDate}</td>
                        <td>${checkout.bonusPoints}</td>
                        <td>${checkout.pointsDueDate}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/CheckoutDetailsServlet?action=getPart&checkoutId=${checkout.checkoutId}" class="action-button info">查看明細</a>
                            <a href="${pageContext.request.contextPath}/CheckoutServlet?action=getUpdate&checkoutId=${checkout.checkoutId}" class="action-button edit">修改</a>
                            <button onclick="confirmDelete('${checkout.checkoutId}')" class="action-button delete">刪除</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <button id="back" class="action-button">返回主頁</button>
    </main>
    
    <script>
        function confirmDelete(checkoutId) {
            if (confirm("確定要刪除此結帳記錄嗎？")) {
                window.location.href = "${pageContext.request.contextPath}/CheckoutServlet?action=delete&checkoutId=" + checkoutId;
            }
        }
        document.getElementById('back').addEventListener('click', function() {
            window.location.href = "${pageContext.request.contextPath}/checkout/checkout/index.jsp";
        });
    </script>
</body>
</html>