<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>更新會員資訊</title>
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
    <link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">
</head>
<body>
    <%@ include file="/body/body.jsp"%>
    <main class="container mt-4">
        <h1 class="mb-4">更新會員資訊</h1>
        <form action="${pageContext.request.contextPath}/CustomerServlet" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="originalTel" value="${customer.customerTel}">
            <table class="table table-bordered">
                <tr>
                    <th>手機號碼:</th>
                    <td><input type="text" name="customerTel" id="customerTel" value="${customer.customerTel}" required></td>
                </tr>
                <tr>
                    <th>姓名:</th>
                    <td><input type="text" name="customerName" id="customerName" value="${customer.customerName}" required></td>
                </tr>
                <tr>
                    <th>Email:</th>
                    <td><input type="email" name="customerEmail" id="customerEmail" value="${customer.customerEmail}" required></td>
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
            <button type="submit" class="btn btn-primary">更新</button>
        </form>
        <div id="errorMessages" class="text-danger mt-3"></div>
        <button id="back" class="btn btn-secondary mt-3">返回會員資訊</button>
    </main>
    <script>
        function validateForm() {
            var tel = document.getElementById('customerTel').value;
            var name = document.getElementById('customerName').value;
            var email = document.getElementById('customerEmail').value;
            var errorMessages = [];

            if (!/^09\d{8}$/.test(tel)) {
                errorMessages.push('手機號碼格式不正確，請輸入09開頭的10位數字。');
            }

            if (!/^[\u4e00-\u9fa5]{2,}$/.test(name)) {
                errorMessages.push('姓名格式不正確，請輸入至少兩個字的中文。');
            }

            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                errorMessages.push('Email格式不正確，請輸入有效的Email地址。');
            }

            if (errorMessages.length > 0) {
                document.getElementById('errorMessages').innerHTML = errorMessages.join('<br>');
                return false;
            }

            return true;
        }

        document.getElementById('back').addEventListener('click', function() {
            window.location.href = "${pageContext.request.contextPath}/employee/CustomerMain.jsp";
        });
    </script>
</body>
</html>