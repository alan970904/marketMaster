<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>會員列表</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="<c:url value='/resources/CSS/style.css'/>" rel="stylesheet">

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

    <link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">
</head>
<body>
    <%@ include file="../body/body.jsp"%>
    <main class="container mt-4">
        <h1 class="mb-4">會員列表</h1>
        <table id="customerTable" class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>手機號碼</th>
                    <th>姓名</th>
                    <th>Email</th>
                    <th>註冊日期</th>
                    <th>累積紅利點數</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="customer" items="${customers}">
                    <tr>
                        <td>${customer.customerTel}</td>
                        <td>${customer.customerName}</td>
                        <td>${customer.customerEmail}</td>
                        <td>${customer.dateOfRegistration}</td>
                        <td>${customer.totalPoints}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/customer/get?customerTel=${customer.customerTel}" class="btn btn-primary btn-sm">詳情</a>
                            <a href="${pageContext.request.contextPath}/customer/getForUpdate?customerTel=${customer.customerTel}" class="btn btn-warning btn-sm">修改</a>
                            <button onclick="confirmDelete('${customer.customerTel}')" class="btn btn-danger btn-sm">刪除</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <button id="back" class="btn btn-secondary mt-3">返回會員資訊</button>
    </main>
    <script>
        $(document).ready(function() {
            var table = $('#customerTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.25/i18n/Chinese-traditional.json"
                }
            });

            window.confirmDelete = function(customerTel) {
                if (confirm("確定要刪除此會員嗎？")) {
                    $.ajax({
                        url: "<c:url value='/customer/delete'/>",
                        type: "POST",
                        data: {
                            customerTel: customerTel
                        },
                        success: function(response) {
                            if(response.status === "success") {
                                // 從 DataTable 中移除該行
                                table.row($("td:contains('" + customerTel + "')").closest("tr")).remove().draw();
                                alert(response.message);
                            } else {
                                alert(response.message);
                            }
                        },
                        error: function(xhr, status, error) {
                            alert("刪除操作失敗。錯誤：" + error);
                        }
                    });
                }
            }
        });

        document.getElementById('back').addEventListener('click', function() {
            window.location.href = "${pageContext.request.contextPath}/customer/cusMain";
        });
    </script>
    <script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>