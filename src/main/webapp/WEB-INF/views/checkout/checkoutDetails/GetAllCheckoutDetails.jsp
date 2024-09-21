<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>所有結帳明細</title>
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
    <link rel="stylesheet" href="/ispan/CSS/extra.css">

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    <style>
    .dataTables_length{
        display:none;
    }
    </style>
</head>
<body>
    <%@ include file="/body/body.jsp"%>
    <main>
    <div>
        <h1>所有結帳明細</h1>
        <table border="1" id="checkoutDetailsTable" class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th>結帳編號</th>
                    <th>產品編號</th>
                    <th>數量</th>
                    <th>產品價格</th>
                    <th>結帳總價</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detail" items="${checkoutDetails}">
                    <tr>
                        <td>${detail.checkoutId}</td>
                        <td>${detail.productId}</td>
                        <td>
                            <input type="number" name="numberOfCheckout" value="${detail.numberOfCheckout}" min="1" class="form-control quantity-input" data-product-id="${detail.productId}" data-product-price="${detail.productPrice}">
                        </td>
                        <td>${detail.productPrice}</td>
                        <td>
                            <input type="text" value="${detail.numberOfCheckout * detail.productPrice}" readonly class="form-control-plaintext subtotal">
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/CheckoutDetailsServlet?action=getOne&checkoutId=${detail.checkoutId}&productId=${detail.productId}" class="btn btn-info btn-sm">詳情</a>
                            <button class="btn btn-primary btn-sm update-btn" data-checkout-id="${detail.checkoutId}" data-product-id="${detail.productId}">修改</button>
                            <button class="btn btn-danger btn-sm delete-btn" data-checkout-id="${detail.checkoutId}" data-product-id="${detail.productId}">刪除</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="mt-3">
            <button id="back" class="btn btn-secondary">返回主頁</button>
        </div>
    </div>
    </main>
    <script>
    $(document).ready(function() {
        var table = $('#checkoutDetailsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.25/i18n/Chinese-traditional.json"
            }
        });

        // 數量變更時更新小計
        $('.quantity-input').on('change', function() {
            var quantity = $(this).val();
            var price = $(this).data('product-price');
            var subtotal = quantity * price;
            $(this).closest('tr').find('.subtotal').val(subtotal.toFixed(0));
        });

        // 修改按鈕點擊事件
        $('.update-btn').on('click', function() {
            var row = $(this).closest('tr');
            var checkoutId = $(this).data('checkout-id');
            var productId = $(this).data('product-id');
            var quantity = row.find('.quantity-input').val();
            var productPrice = row.find('.quantity-input').data('product-price');
            var checkoutPrice = quantity * productPrice;

            $.ajax({
                url: '${pageContext.request.contextPath}/CheckoutDetailsServlet',
                type: 'POST',
                data: {
                    action: 'update',
                    checkoutId: checkoutId,
                    productId: productId,
                    numberOfCheckout: quantity,
                    productPrice: productPrice,
                    checkoutPrice: checkoutPrice
                },
                success: function(response) {
                    if (response.status === 'success') {
                        alert('更新成功');
                        row.find('.subtotal').val(checkoutPrice.toFixed(0));
                    } else {
                        alert('更新失敗: ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    alert('更新失敗: ' + error);
                }
            });
        });

        $('.delete-btn').on('click', function() {
            if (confirm('確定要刪除嗎？')) {
                var checkoutId = $(this).data('checkout-id');
                var productId = $(this).data('product-id');
                var row = $(this).closest('tr');
                $.ajax({
                    url: '${pageContext.request.contextPath}/CheckoutDetailsServlet',
                    type: 'POST',
                    data: {
                        action: 'delete',
                        checkoutId: checkoutId,
                        productId: productId
                    },
                    success: function(response) {
                        table.row(row).remove().draw();
                        alert('刪除成功');
                    },
                    error: function(xhr, status, error) {
                        alert('刪除失敗: ' + error);
                    }
                });
            }
        });

        $('#back').click(function() {
            window.location.href = "${pageContext.request.contextPath}/checkout/checkout/index.jsp";
        });
    });
    </script>
</body>
</html>