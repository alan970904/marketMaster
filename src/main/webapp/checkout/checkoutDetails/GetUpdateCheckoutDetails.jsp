<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改結帳明細</title>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
</head>
<body>
    <%@ include file="/body/body.jsp"%>
    <main class="container mt-5">
        <h1 class="mb-4">修改結帳明細</h1>
        <form id="updateForm" action="${pageContext.request.contextPath}/CheckoutDetailsServlet" method="post" class="card">
            <div class="card-body">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="checkoutId" value="${checkoutDetails.checkoutId}">
                <input type="hidden" name="productId" value="${checkoutDetails.productId}">
                
                <div class="mb-3">
                    <label for="numberOfCheckout" class="form-label">數量:</label>
                    <input type="number" class="form-control" id="numberOfCheckout" name="numberOfCheckout" value="${checkoutDetails.numberOfCheckout}" min="1" required>
                </div>
                
                <div class="mb-3">
                    <label for="productPrice" class="form-label">產品價格:</label>
                    <input type="text" class="form-control" id="productPrice" name="productPrice" value="${checkoutDetails.productPrice}" readonly>
                </div>
                
                <div class="mb-3">
                    <label for="checkoutPrice" class="form-label">結帳總價:</label>
                    <input type="text" class="form-control" id="checkoutPrice" name="checkoutPrice" value="${checkoutDetails.checkoutPrice}" readonly>
                </div>
                
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary">提交</button>
                    <button id="back" type="button" class="btn btn-secondary">返回</button>
                </div>
            </div>
        </form>
    </main>
    <script>
    $(document).ready(function() {
        function updateCheckoutPrice() {
            var quantity = $('#numberOfCheckout').val();
            var price = parseFloat($('#productPrice').val());
            var total = quantity * price;
            $('#checkoutPrice').val(total.toFixed(2));
        }

        $('#numberOfCheckout').on('input', updateCheckoutPrice);

        $('#back').click(function() {
            window.history.back();
        });

        $('#updateForm').submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: $(this).attr('action'),
                type: 'POST',
                data: $(this).serialize(),
                success: function(response) {
                    window.location.href = "${pageContext.request.contextPath}/CheckoutServlet?action=getAll";
                },
                error: function(xhr, status, error) {
                    alert('更新失敗: ' + error);
                }
            });
        });
    });
    </script>
</body>
</html>