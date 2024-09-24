<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.MarketMaster.bean.schedule.AskForLeaveBean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>修改請假單</title>

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
<link href="<c:url value='/resources/CSS/extra.css'/>" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

<!-- Flatpickr JS -->
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://cdn.jsdelivr.net/npm/date-fns"></script>
<script src="<c:url value='/resources/js/main.js'/>"></script>
</head>
<body>
    <%@ include file="../body/body.jsp"%>
    <div class="container mt-5">
        <div class="row">
            <div class="col text-center">
                <h1>修改請假單</h1>
            </div>
        </div>

  

        <div class="row mt-4">
            <div class="col text-center">
                <a
                    href="${pageContext.request.contextPath}/AskForLeaveCon?action=allLeaveRecords"
                    class="btn btn-primary">查看所有員工請假表</a>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col">
                <div class="card p-4 shadow-sm">
                    <form id="leaveForm" action="${pageContext.request.contextPath}/AskForLeaveCon/updateLeaveRecord" method="post">

                        <c:choose>
                            <c:when test="${not empty leaveRecords}">
                                <div class="mb-3">
                                    <label for="leave_id" class="form-label">請假編號:</label> 
                                    <input type="text" id="leave_id" name="leave_id" value="${leaveRecords[0].leaveId}" readonly class="form-control">
                                </div>

                                <div class="mb-3">
                                    <label for="employee_id" class="form-label">員工編號:</label> 
                                    <input type="text" id="employee_id" name="employee_id" value="${leaveRecords[0].employeeId}" readonly class="form-control">
                                </div>

                                <div class="mb-3">
                                    <label for="employee_name" class="form-label">員工姓名:</label> 
                                    <input type="text" id="employee_name" name="employee_name" value="${leaveRecords[0].empBean.employeeName}" readonly class="form-control">
                                </div>

                                <div class="mb-3">
                                    <label for="start_datetime" class="form-label">開始日期時間:</label> 
                                    <input type="text" id="start_datetime" name="start_datetime" value="${leaveRecords[0].startDatetime}" required class="form-control">
                                </div>

                                <div class="mb-3">
                                    <label for="end_datetime" class="form-label">結束日期時間:</label> 
                                    <input type="text" id="end_datetime" name="end_datetime" value="${leaveRecords[0].endDatetime}" required class="form-control">
                                </div>

                                <div class="mb-3">
                                    <label for="leave_category" class="form-label">請假類別:</label> 
                                    <select id="leave_category" name="leave_category" required class="form-select">
                                        <option value="病假" ${leaveRecords[0].leaveCategory == '病假' ? 'selected' : ''}>病假</option>
                                        <option value="年假" ${leaveRecords[0].leaveCategory == '年假' ? 'selected' : ''}>年假</option>
                                        <option value="事假" ${leaveRecords[0].leaveCategory == '事假' ? 'selected' : ''}>事假</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="reason_of_leave" class="form-label">請假原因:</label>
                                    <textarea id="reason_of_leave" name="reason_of_leave" rows="4" required class="form-control">${leaveRecords[0].reasonOfLeave}</textarea>
                                </div>

                                <div class="mb-3">
                                    <label for="approved_status" class="form-label">批准狀態:</label> 
                                    <span>${leaveRecords[0].approvedStatus}</span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-warning">沒有找到請假記錄。</div>
                            </c:otherwise>
                        </c:choose>

                        <div class="d-flex justify-content-end mt-4">
                            <button type="button" id="submitButton" class="btn btn-primary me-2">提交</button>
                            <button type="button" id="cancelButton" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        var startDatetime = "${startDatetimeFormatted}";
        var endDatetime = "${endDatetimeFormatted}";

        var startDatetimePicker = flatpickr("#start_datetime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            minDate: null, // 允許選擇過去的日期
            defaultDate: startDatetime,
            time_24hr: true,
            onChange: function(selectedDates) {
                if (selectedDates.length > 0) {
                    var startDatetime = selectedDates[0];
                    endDatetimePicker.set('minDate', startDatetime);
                }
            }
        });

        var endDatetimePicker = flatpickr("#end_datetime", {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            minDate: null, // 允許選擇過去的日期
            time_24hr: true,
            defaultDate: endDatetime
        });

        function submitForm() {
            var form = document.getElementById("leaveForm");
            form.submit();
        }

        function cancelForm() {
			var employeeId = document
					.getElementById("employee_id").value;
			window.location.href = '${pageContext.request.contextPath}/AskForLeaveCon/searchLeaveRecords?employee_id='
					+ encodeURIComponent(employeeId);
		}


        document.getElementById('submitButton').addEventListener('click', submitForm);
        document.getElementById('cancelButton').addEventListener('click', cancelForm);
    });
</script>
    
</body>
</html>


