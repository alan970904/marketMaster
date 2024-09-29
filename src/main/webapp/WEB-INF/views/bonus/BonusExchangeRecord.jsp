<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>兌換記錄</title>
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
<link href="${pageContext.request.contextPath}/resources/CSS/style.css" rel="stylesheet">

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

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/CSS/extra.css">
<style>
    .update-btn {
        margin-left: 5px;
    }

	.dataTables_length select { padding-right: 30px !important; background-position: right 0.5rem center !important; }

</style>
</head>
<body>
<%@ include file="../body/body.jsp"%>
<main>
    <div class="container mt-4">
        <h1 class="mb-4">兌換記錄</h1>
        
        <div class="mb-3 text-end">
            <h5>目前總點數: <span class="badge bg-success">${customerTotalPoints}</span></h5>
        </div>
        
        <c:if test="${empty exchangeRecords}">
            <p class="text-muted">暫無兌換記錄。</p>
        </c:if>
        
        <c:if test="${not empty exchangeRecords}">
            <table id="exchangeTable" class="table table-striped table-bordered" style="width:100%">
                <thead>
                    <tr>
                        <th>兌換ID</th>
                        <th>會員電話</th>
                        <th>商品ID</th>
                        <th>使用積分</th>
                        <th>兌換數量</th>
                        <th>兌換日期</th>
                        <th>     </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="record" items="${exchangeRecords}">
                        <tr>
                            <td>${record.exchangeId}</td>
                            <td>${record.customerTel}</td>
                            <td>${record.productId}
							<button class="btn btn-warning btn-sm update-btn" onclick="showUpdateModal
							('${record.exchangeId}', '${record.productId}', ${record.usePoints}, ${record.numberOfExchange})">更新</button>
                            </td>
                            <td>${record.usePoints}</td>
                            <td>${record.numberOfExchange}</td>
                            <td><fmt:formatDate value="${record.exchangeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <button class="btn btn-danger btn-sm" onclick="deleteExchangeRecord('${record.exchangeId}')">刪除</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/bonusExchange/list" method="post" style="display: inline;">
            <input type="hidden" name="customerTel" value="${customerTel}">
            <button type="submit" class="btn btn-secondary mt-3">
                <i class="fas fa-arrow-left"></i> 返回商品列表
            </button>
        </form>
    </div>
    
	<!-- 更新兌換記錄的 Modal -->
<div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateModalLabel">更新兌換記錄</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="updateForm">
                    <input type="hidden" id="updateExchangeId" name="exchangeId">
                    <div class="mb-3">
                        <label for="updateProductCategory" class="form-label">商品類別</label>
                        <select class="form-control" id="updateProductCategory" required>
                            <option value="">請選擇商品類別</option>
                            <option value="肉品海鮮">肉品海鮮</option>
                            <option value="飲品">飲品</option>
                            <option value="米飯麵條">米飯麵條</option>
                            <option value="零食點心">零食點心</option>
                            <option value="蔬菜水果">蔬菜水果</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="updateProductName" class="form-label">商品名稱</label>
                        <select class="form-control" id="updateProductName" required>
                            <option value="">請選擇商品名稱</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="updateProductId" class="form-label">商品ID</label>
                        <input type="text" class="form-control" id="updateProductId" name="productId" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="updateUsePoints" class="form-label">單價 (點數)</label>
                        <input type="number" class="form-control" id="updateUsePoints" name="usePoints" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="updateNumberOfExchange" class="form-label">兌換數量</label>
                        <input type="number" class="form-control" id="updateNumberOfExchange" name="numberOfExchange" required min="1">
                    </div>
                    <div class="mb-3">
                        <label for="updateTotalPoints" class="form-label">總點數</label>
                        <input type="number" class="form-control" id="updateTotalPoints" name="totalPoints" readonly>
                    </div>
                    <button type="button" class="btn btn-primary" onclick="updateExchange()">確認更新</button>
                </form>
            </div>
        </div>
    </div>
</div>
</main>   
    
<script>
    $(document).ready(function() {
        $('#exchangeTable').DataTable();

        // 商品類別變更時獲取對應的商品
        $('#updateProductCategory').change(function() {
            var category = $(this).val();
            if (category) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/bonusExchange/products',
                    type: 'GET',
                    data: { category: category },
                    dataType: 'json',
                    success: function(data) {
                        var productSelect = $('#updateProductName');
                        productSelect.empty();
                        productSelect.append($('<option>').text('請選擇商品名稱').attr('value', ''));
                        $.each(data, function(i, product) {
                            productSelect.append($('<option>')
                                .text(product.productName)
                                .attr('value', product.productId)
                                .data('productId', product.productId)
                                .data('productPrice', product.productPrice));
                        });
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error('獲取商品名稱失敗:', textStatus, errorThrown);
                        alert('獲取商品名稱時出錯，請稍後再試');
                    }
                });
            } else {
                resetProductFields();
            }
        });

        // 商品名稱變更時更新商品ID和價格
        $('#updateProductName').change(function() {
            var selectedOption = $(this).find('option:selected');
            var productId = selectedOption.val();
            var productPrice = selectedOption.data('productPrice');
            $('#updateProductId').val(productId);
            $('#updateUsePoints').val(productPrice);
            calculateTotalPoints();
        });

        // 兌換數量變更時更新總點數
        $('#updateNumberOfExchange').on('input', calculateTotalPoints);
        $('#updateUsePoints').on('change', calculateTotalPoints);
    });

    function calculateTotalPoints() {
        var quantity = $('#updateNumberOfExchange').val();
        var price = $('#updateUsePoints').val();
        var totalPoints = quantity * price;
        $('#updateTotalPoints').val(totalPoints);
    }

    function resetProductFields() {
        $('#updateProductName').empty().append($('<option>').text('請選擇商品名稱').attr('value', ''));
        $('#updateProductId').val('');
        $('#updateUsePoints').val('');
        $('#updateTotalPoints').val('');
    }

    function showUpdateModal(exchangeId, productId, usePoints, numberOfExchange) {
        $('#updateExchangeId').val(exchangeId);
        
        // 獲取商品詳細信息
        $.ajax({
            url: '${pageContext.request.contextPath}/bonusExchange/getProductDetails',
            type: 'GET',
            data: { productId: productId },
            dataType: 'json',
            success: function(product) {
                $('#updateProductCategory').val(product.productCategory).trigger('change');
                
                // 使用 setTimeout 確保商品名稱下拉列表已經被填充
                setTimeout(function() {
                    $('#updateProductName').val(productId);
                    $('#updateProductId').val(productId);
                    $('#updateUsePoints').val(product.productPrice);
                    $('#updateNumberOfExchange').val(numberOfExchange);
                    calculateTotalPoints();
                }, 500);
            },
            error: function() {
                alert('獲取商品信息失敗，請稍後再試');
            }
        });
        
        var updateModal = new bootstrap.Modal(document.getElementById('updateModal'));
        updateModal.show();
    }

    function updateExchange() {
        var exchangeId = $('#updateExchangeId').val();
        var productId = $('#updateProductId').val();
        var usePoints = $('#updateTotalPoints').val(); // 使用總點數
        var numberOfExchange = $('#updateNumberOfExchange').val();
        
        $.ajax({
            url: "${pageContext.request.contextPath}/bonusExchange/updateExchangeRecord",
            type: "POST",
            data: {
                exchangeId: exchangeId,
                productId: productId,
                usePoints: usePoints,
                numberOfExchange: numberOfExchange
            },
            success: function(response) {
                if (response.status === "success") {
                    alert(response.message);
                    location.reload();
                } else {
                    alert("更新失敗: " + response.message);
                }
            },
            error: function(xhr, status, error) {
                alert("更新請求失敗，請稍後再試。");
            }
        });
    }

    function deleteExchangeRecord(exchangeId) {
        if (confirm('確定要刪除這筆兌換記錄嗎？')) {
            $.ajax({
                url: "${pageContext.request.contextPath}/bonusExchange/deleteExchangeRecords",
                type: "POST",
                data: { exchangeId: exchangeId },
                success: function(response) {
                    var result = JSON.parse(response);
                    if (result.status === "success") {
                        alert(result.message);
                        location.reload();
                    } else {
                        alert("刪除失敗: " + result.message);
                    }
                },
                error: function(xhr, status, error) {
                    alert("刪除請求失敗，請稍後再試。");
                }
            });
        }
    }
</script>
</body>
</html>
