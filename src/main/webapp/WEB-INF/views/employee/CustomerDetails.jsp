<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>會員詳細資訊</title>
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
        <h1 class="mb-4">會員詳細資訊</h1>
        <table class="table table-bordered">
            <tr>
                <th>手機號碼:</th>
                <td>${customer.customerTel}</td>
            </tr>
            <tr>
                <th>姓名:</th>
                <td>${customer.customerName}</td>
            </tr>
            <tr>
                <th>Email:</th>
                <td>${customer.customerEmail}</td>
            </tr>
            <tr>
                <th>註冊日期:</th>
                <td>${customer.dateOfRegistration}</td>
            </tr>
            <tr>
                <th>累積紅利點數:</th>
                <td>${customer.totalPoints}</td>
            </tr>
        </table>
        <button id="back" class="btn btn-secondary mt-3">返回會員資訊</button>
    </main>
    <script>
        document.getElementById('back').addEventListener('click', function() {
            window.location.href = "${pageContext.request.contextPath}/employee/CustomerMain.jsp";
        });
    </script>
    <script src="<c:url value='/resources/js/main.js'/>"></script>
</body>
</html>