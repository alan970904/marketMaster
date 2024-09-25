<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>可兌換商品列表</title>
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
        .action-buttons {
            display: flex;
            align-items: center;
            justify-content: flex-end;
        }
        .action-buttons form {
            margin-left: 5px;
        }
        .action-buttons input[type="number"] {
            width: 50px;
        }
    </style>
</head>
<body>
    <%@ include file="../body/body.jsp"%>
    <main class="container mt-4">
        <h1 class="mb-4 text-center">可兌換商品列表</h1>
        <!-- 新增會員累積點數顯示 -->
        <div class="mb-3 text-end">
            <h5><span class="badge bg-success">${customerTotalPoints}</span></h5>
        </div>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">${errorMessage}</div>
        </c:if>
        
        <c:if test="${empty exchangeableProducts}">
            <p class="text-center text-muted">點數不足</p>
        </c:if>
        
        <c:if test="${not empty exchangeableProducts}">
            <table id="productTable" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>商品ID</th>
                        <th>商品名稱</th>
                        <th>兌換點數</th>
                        <th>庫存數量</th>
                        <th>兌換數量</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${exchangeableProducts}">
                        <tr>
                            <td>${product.productId}</td>
                            <td>${product.productName}</td>
                            <td>${product.productPrice}</td>
                            <td>${product.numberOfInventory}</td>
                            <td>
                            	<div class="action-buttons">
	                                <form action="${pageContext.request.contextPath}/BonusExchangeServlet" method="post">
	                                    <input type="hidden" name="action" value="executeExchange">
	                                    <input type="hidden" name="customerTel" value="${param.customerTel}">
	                                    <input type="hidden" name="productId" value="${product.productId}">
	                                    <input type="number" name="exchangeQuantity" value="1" min="1" max="${product.numberOfInventory}" required>
	                                    <button type="submit" class="btn btn-primary btn-sm">兌換</button>
	                                </form>
									
<%-- 									<form action="${pageContext.request.contextPath}/BonusExchangeServlet" method="post" class="d-inline"> --%>
<!-- 	                                    <input type="hidden" name="action" value="updateExchange"> -->
<%-- 	                                    <input type="hidden" name="customerTel" value="${param.customerTel}"> --%>
<%-- 	                                    <input type="hidden" name="productId" value="${product.productId}"> --%>
<%-- 	                                    <input type="number" name="newExchangeQuantity" value="1" min="1" max="${product.numberOfInventory}" required style="width: 60px;"> --%>
<!-- 	                                    <button type="submit" class="btn btn-warning btn-sm">更正庫存</button> -->
<!-- 	                                </form> -->
	                                
<%-- 	                                <form action="${pageContext.request.contextPath}/BonusExchangeServlet" method="post" class="d-inline"> --%>
<!-- 	                                    <input type="hidden" name="action" value="deleteExchange"> -->
<%-- 	                                    <input type="hidden" name="customerTel" value="${param.customerTel}"> --%>
<%-- 	                                    <input type="hidden" name="productId" value="${product.productId}"> --%>
<!-- 	                                    <button type="submit" class="btn btn-danger btn-sm">停止兌換</button> -->
<!-- 	                                </form> -->
                               	</div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <!-- 返回和查看兌換記錄按鈕 -->
        <div class="mt-3">
            <!-- 返回按鈕 -->
            <a href="${pageContext.request.contextPath}/bonus/BonusExchangeInput.jsp" class="btn btn-secondary">返回</a>
            
            <!-- 查看兌換記錄按鈕 -->
            <form action="${pageContext.request.contextPath}/BonusExchangeServlet" method="post" class="d-inline">
                <input type="hidden" name="action" value="showExchangeRecords">
                <input type="hidden" name="customerTel" value="${param.customerTel}">
                <button type="submit" class="btn btn-info ms-2">查看兌換記錄</button>
            </form>
        </div>
    </main>
 
    <script>
        $(document).ready(function() {
            $('#productTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.25/i18n/Chinese-traditional.json"
                }
            });
        });

        function exchangeProduct(productId) {
            // 這裡添加兌換商品的邏輯
            alert('商品兌換功能尚未實現。商品ID: ' + productId);
        }
    </script>
</body>
</html>