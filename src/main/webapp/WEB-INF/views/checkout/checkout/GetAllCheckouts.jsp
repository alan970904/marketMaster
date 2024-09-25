<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>所有結帳記錄</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/CSS/style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/CSS/extra.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    <style>

	.dataTables_length select { padding-right: 30px !important; background-position: right 0.5rem center !important; }

</style>
</head>
<body>
    <%@ include file="../../body/body.jsp"%>
    <main>
    <div class="container mt-5">
        <h1 class="mb-4">所有結帳記錄</h1>
        <table id="checkoutsTable" class="table table-striped table-bordered">
            <thead>
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
            </thead>
            <tbody>
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
                            <a href="${pageContext.request.contextPath}/checkout/checkoutDetails/listByCheckoutId?checkoutId=${checkout.checkoutId}" class="btn btn-info btn-sm">查看明細</a>
                            <a href="${pageContext.request.contextPath}/checkout/update?checkoutId=${checkout.checkoutId}" class="btn btn-primary btn-sm">修改</a>
                            <button class="btn btn-danger btn-sm delete-btn" data-checkout-id="${checkout.checkoutId}">刪除</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/checkout/checkoutMain" class="btn btn-secondary">返回主頁</a>
            <a href="${pageContext.request.contextPath}/checkout/checkoutDetails/list" class="btn btn-info">所有結帳明細</a>
        </div>
    </div>
    </main>

    <script>
    $(document).ready(function() {
        var table = $('#checkoutsTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.25/i18n/Chinese-traditional.json"
            }
        });

        $('.delete-btn').on('click', function() {
            var checkoutId = $(this).data('checkout-id');
            var row = $(this).closest('tr');
            if (confirm('確定要刪除嗎？')) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/checkout/delete',
                    type: 'POST',
                    data: {
                        checkoutId: checkoutId
                    },
                    success: function(response) {
                        var result = JSON.parse(response);
                        if (result.status === 'success') {
                            // 显示成功提示
                            alert(result.message);
                            // 从 DataTable 中移除该行
                            table.row(row).remove().draw();
                        } else {
                            // 如果删除失败，显示错误信息
                            alert('刪除失敗: ' + result.message);
                        }
                    },
                    error: function(xhr, status, error) {
                        alert('刪除失敗: ' + error);
                    }
                });
            }
        });

        
    });
    </script>
</body>
</html>