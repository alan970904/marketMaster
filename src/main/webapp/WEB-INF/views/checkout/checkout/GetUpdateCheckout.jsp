<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>更新結帳資訊</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/resources/CSS/style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/CSS/extra.css">

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
</head>
<body>
    <%@ include file="../../body/body.jsp"%>
    <main class="container mt-5">
        <h1 class="mb-4">更新結帳資訊</h1>
        <form id="updateForm" action="${pageContext.request.contextPath}/checkout/update" method="post" class="card">
            <div class="card-body">
                <input type="hidden" name="action" value="update">
                <div class="mb-3">
                    <label for="checkoutId" class="form-label">結帳編號:</label>
                    <input type="text" id="checkoutId" name="checkoutId" value="${checkout.checkoutId}" class="form-control" readonly>
                </div>
                <div class="mb-3 phone-group">
                    <label for="customerTel" class="form-label">顧客手機:</label>
                    <div class="phone-inputs d-flex">
                        <input type="text" id="customerTel1" name="customerTel1" class="form-control" maxlength="4">
                        <span class="mx-2">-</span>
                        <input type="text" id="customerTel2" name="customerTel2" class="form-control" maxlength="6">
                    </div>
                </div>
                <div class="mb-3">
                    <label for="employeeId" class="form-label">員工編號:</label>
                    <input type="text" id="employeeId" name="employeeId" value="${checkout.employeeId}" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="checkoutTotalPrice" class="form-label">總價:</label>
                    <input type="text" id="checkoutTotalPrice" name="checkoutTotalPrice" value="${checkout.checkoutTotalPrice}" class="form-control" readonly>
                </div>
                <div class="mb-3">
                    <label for="checkoutDate" class="form-label">結帳日期:</label>
                    <input type="date" id="checkoutDate" name="checkoutDate" value="${checkout.checkoutDate}" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="bonusPoints" class="form-label">紅利點數:</label>
                    <input type="text" id="bonusPoints" name="bonusPoints" value="${checkout.bonusPoints}" class="form-control" readonly>
                </div>
                <div class="mb-3">
                    <label for="pointsDueDate" class="form-label">點數到期日:</label>
                    <input type="date" id="pointsDueDate" name="pointsDueDate" value="${checkout.pointsDueDate}" class="form-control" readonly>
                </div>
                <button type="submit" class="btn btn-primary">更新</button>
            </div>
        </form>
        <button id="back" class="btn btn-secondary mt-3">返回結帳列表</button>
    </main>
    <script>
    $(document).ready(function() {
    	 var originalCustomerTel = "${checkout.customerTel}";
    	 var totalPrice = parseFloat($("#checkoutTotalPrice").val());

     	// 初始化時分割並填充電話號碼
        if (originalCustomerTel) {
        	$("#customerTel1").val(originalCustomerTel.substring(0, 4));
       	 	$("#customerTel2").val(originalCustomerTel.substring(4));
    	}

        function updateBonusAndDueDate() {
            var customerTel = $("#customerTel1").val() + $("#customerTel2").val();
            var checkoutDate = $("#checkoutDate").val();

            if (customerTel) {
                var bonusPoints = Math.floor(totalPrice / 100);
                $("#bonusPoints").val(bonusPoints);

                var dueDate = new Date(checkoutDate);
                dueDate.setFullYear(dueDate.getFullYear() + 1);
                $("#pointsDueDate").val(dueDate.toISOString().split('T')[0]);
            } else {
                $("#bonusPoints").val("");
                $("#pointsDueDate").val("");
            }
        }

        $("#customerTel1, #customerTel2, #checkoutDate").on('change', updateBonusAndDueDate);

        // 顧客手機號碼自動跳轉
        $("#customerTel1").on('input', function() {
        	if (this.value.length === 4) {
            	$("#customerTel2").focus();
        	}
    	});

        $("#updateForm").submit(function(e) {
            e.preventDefault();
            var formData = {
                checkoutId: $("#checkoutId").val(),
                customerTel: $("#customerTel1").val() + $("#customerTel2").val(),
                employeeId: $("#employeeId").val(),
                checkoutTotalPrice: $("#checkoutTotalPrice").val(),
                checkoutDate: $("#checkoutDate").val(),
                bonusPoints: $("#bonusPoints").val(),
                pointsDueDate: $("#pointsDueDate").val()
            };
            
            $.ajax({
                url: '${pageContext.request.contextPath}/checkout/update',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) {
                    if (response.status === 'success') {
                        alert('更新成功');
                        window.location.href = "${pageContext.request.contextPath}/checkout/list";
                    } else {
                        alert('更新失敗: ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 錯誤:", status, error);
                    alert('發生錯誤，請查看控制台以獲取詳細信息');
                }
            });
        });

        $('#back').click(function() {
        	 window.location.href = "${pageContext.request.contextPath}/checkout/list";
        });

        // 初始化時更新紅利點數和到期日
        updateBonusAndDueDate();
    });
    </script>
</body>
</html>