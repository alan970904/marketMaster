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
</head>
<body>
<%@ include file="/body/body.jsp"%>
<main>
    <div class="container mt-4">
        <h1 class="mb-4">兌換記錄</h1>
        
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
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="record" items="${exchangeRecords}">
                        <tr>
                            <td>${record.exchangeId}</td>
                            <td>${record.customerTel}</td>
                            <td>${record.productId}</td>
                            <td>${record.usePoints}</td>
                            <td>${record.numberOfExchange}</td>
                            <td><fmt:formatDate value="${record.exchangeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="showUpdateModal('${record.exchangeId}', ${record.usePoints})">更改</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteRecord('${record.exchangeId}')">刪除</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/bonus/BonusExchangeInput.jsp" class="btn btn-secondary mt-3"><i class="fas fa-arrow-left"></i> 返回</a>
    </div>
    
	<!-- 更改點數的 Modal -->
    <div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateModalLabel">更改兌換點數</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
	            <div class="modal-body">
	                <form id="updateForm">
	                    <input type="hidden" id="updateExchangeId" name="exchangeId">
	                    <div class="mb-3">
	                        <label for="newPoints" class="form-label">新的兌換點數</label>
	                        <input type="number" class="form-control" id="newPoints" name="newPoints" required>
	                    </div>
	                    <button type="button" class="btn btn-primary" onclick="updateExchange()">確認更改</button>
	                </form>
	            </div>
	        </div>
	    </div>
	</div>
 </main>   
    
    <script>
    $(document).ready(function() {
        $('#exchangeTable').DataTable();
    });

    function showUpdateModal(exchangeId, currentPoints) {
        $('#updateExchangeId').val(exchangeId);
        $('#newPoints').val(currentPoints);
        var updateModal = new bootstrap.Modal(document.getElementById('updateModal'));
        updateModal.show();
    }

    function updateExchange() {
        console.log("updateExchange function called");
        var exchangeId = $('#updateExchangeId').val();
        var newPoints = $('#newPoints').val();
        console.log("Sending update request for exchangeId: " + exchangeId + ", newPoints: " + newPoints);
        $.ajax({
            url: "${pageContext.request.contextPath}/BonusExchangeServlet",
            type: "POST",
            data: {
                action: "updateExchange",
                exchangeId: exchangeId,
                newPoints: newPoints
            },
            success: function(response) {
                console.log("Update response received:", response);
                if (response.status === "success") {
                    alert(response.message);
                    location.reload();
                } else {
                    alert("更新失敗: " + response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error("Update request failed:", status, error);
                console.error("Response Text:", xhr.responseText);
                alert("更新請求失敗，請稍後再試。");
            }
        });
    }

    function deleteRecord(exchangeId) {
        console.log("deleteRecord function called for exchangeId: " + exchangeId);
        if (confirm('確定要刪除這筆兌換記錄嗎？')) {
            $.ajax({
                url: "${pageContext.request.contextPath}/BonusExchangeServlet",
                type: "POST",
                data: {
                    action: "deleteExchange",
                    exchangeId: exchangeId
                },
                success: function(response) {
                    console.log("Delete response received:", response);
                    if (response.status === "success") {
                        alert(response.message);
                        location.reload();
                    } else {
                        alert("刪除失敗: " + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Delete request failed:", status, error);
                    console.error("Response Text:", xhr.responseText);
                    alert("刪除請求失敗，請稍後再試。");
                }
            });
        }
    }
</script>
</body>
</html>
