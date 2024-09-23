<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<!DOCTYPE html>
<html lang="zh-TW">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>排班表</title>

<!-- DataTables CSS -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Font Awesome -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
	rel="stylesheet">

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap"
	rel="stylesheet">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/CSS/style.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/CSS/extra.css">

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


<style>
.calendar-grid {
	display: grid;
	grid-template-columns: repeat(7, 1fr);
	gap: 5px;
}

.calendar-day {
	border: 1px solid #ddd;
	padding: 5px;
	min-height: 100px;
}

.calendar-day-header {
	font-weight: bold;
	text-align: center;
}
</style>
</head>
<body>
	<jsp:include page="/body/body.jsp" />
	<div class="container mt-5">

		<div class="col text-center">
			<h1>排班表</h1>
		</div>
		<div class="row mt-3">
			<div class="col text-start">
				<a href="${pageContext.request.contextPath}/body/HomePage.jsp"
					class="btn btn-secondary"> <i class="fas fa-arrow-left"></i> 返回
				</a>
			</div>
		</div>


		<hr>

		<!-- 時間選擇區 -->
		<div class="row mt-4">
			<div class="col">
				<div class="card p-4">
					<h2 class="card-title">選擇時間區段</h2>
					<div id="timeBlocks" class="mb-3">
						<!-- 時間區段將在這裡動態添加 -->
					</div>
					<button id="addTimeBlock" class="btn btn-primary">新增時間區段</button>
					<button id="applyTimeSlots" class="btn btn-secondary">套用時間區段到所有日期</button>
				</div>
			</div>
		</div>

		<hr>

		<!-- 日曆區 -->
		<div class="row mt-4">
			<div class="col">
				<div class="card p-4">
					<div class="d-flex justify-content-between align-items-center mb-3">
						<button id="prev-month" class="btn btn-secondary">&lt;</button>
						<h2 id="current-month"></h2>
						<button id="next-month" class="btn btn-secondary">&gt;</button>
					</div>
					<div class="calendar-grid" id="calendar-grid">
						<!-- 日曆天數將在此動態插入 -->
					</div>
				</div>
			</div>
		</div>

		<!-- 排程彈窗 -->
		<div id="scheduleModal" class="modal">
			<div class="modal-content">
				<span class="close">&times;</span>
				<h2>編輯排程</h2>
				<form id="scheduleForm" action="ScheduleCon" method="post">
					<input type="hidden" name="action" value="saveSchedule"> <input
						type="hidden" id="scheduleYear" name="year"> <input
						type="hidden" id="scheduleMonth" name="month"> <input
						type="hidden" id="scheduleDay" name="day">
					<div id="scheduleFormContent">
						<!-- 動態生成的時間區段和員工選擇將在這裡顯示 -->
					</div>
					<button type="submit" class="btn btn-primary">儲存排程</button>
				</form>
			</div>
		</div>

		<script>
var currentDate = new Date();
var currentDay = null;
var schedules = {};
var allSchedules = [
    <c:forEach var="schedule" items="${scheduleRecords}" varStatus="status">
        {
            employeeId: "${schedule[0]}",
            employeeName: "${schedule[1]}",
            jobDate: new Date("${schedule[2]}"),
            startTime: "${schedule[3]}",
            endTime: "${schedule[4]}"
        }<c:if test="${!status.last}">,</c:if>
    </c:forEach>
];
var employees = [
    <c:forEach var="employee" items="${employeeNames}" varStatus="status">
        "${employee}"<c:if test="${!status.last}">,</c:if>
    </c:forEach>
];

// 生成日曆
function generateCalendar(year, month) {
    var calendarGrid = document.getElementById('calendar-grid');
    calendarGrid.innerHTML = '';
    var firstDay = new Date(year, month, 1);
    var lastDay = new Date(year, month + 1, 0);
    var daysInMonth = lastDay.getDate();

    document.getElementById('current-month').textContent = year + '年' + (month + 1) + '月';

    var weekdays = ['日', '一', '二', '三', '四', '五', '六'];
    weekdays.forEach(function(day) {
        var dayHeader = document.createElement('div');
        dayHeader.className = 'calendar-day-header';
        dayHeader.textContent = day;
        calendarGrid.appendChild(dayHeader);
    });

    var startDay = firstDay.getDay();

    for (var i = 0; i < startDay; i++) {
        var emptyDayElement = document.createElement('div');
        emptyDayElement.className = 'calendar-day';
        calendarGrid.appendChild(emptyDayElement);
    }

    for (var i = 1; i <= daysInMonth; i++) {
        var dayElement = document.createElement('div');
        dayElement.className = 'calendar-day';
        dayElement.innerHTML = '<div class="calendar-day-header">' + i + '</div>' +
            '<button class="edit-schedule btn btn-sm btn-primary" onclick="openScheduleModal(' + year + ',' + month + ',' + i + ')">編輯</button>' +
            '<div class="schedule-list" id="schedule-list-' + year + '-' + month + '-' + i + '"></div>';
        calendarGrid.appendChild(dayElement);
    }

    updateAllScheduleLists(year, month);
}

// 打開排程模態框
function openScheduleModal(year, month, day) {
    currentDay = { year: year, month: month, day: day };
    document.getElementById('scheduleModal').style.display = 'block';
    document.getElementById('scheduleYear').value = year;
    document.getElementById('scheduleMonth').value = month + 1; // 月份需要 +1，因為 JavaScript 的月份是從 0 開始的
    document.getElementById('scheduleDay').value = day;
    populateScheduleForm();
}

// 關閉模態框
function closeModal() {
    document.getElementById('scheduleModal').style.display = 'none';
}

// 填充排程表單
function populateScheduleForm() {
    var scheduleFormContent = document.getElementById('scheduleFormContent');
    scheduleFormContent.innerHTML = '';

    var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
    var daySchedules = schedules[dayKey] || [];

    // 確保下拉選單中包含所有員工選項
    var employeeOptions = employees.map(function(emp) {
        return '<option value="' + emp + '">' + emp + '</option>';
    }).join('');

    // 創建一個時間段表格，合併同一時間段的員工選項
    var groupedSchedules = {};

    daySchedules.forEach(function(schedule) {
        var key = schedule.startTime + '-' + schedule.endTime;
        if (!groupedSchedules[key]) {
            groupedSchedules[key] = new Set();
        }
        schedule.employees.forEach(function(emp) {
            groupedSchedules[key].add(emp);
        });
    });

    Object.keys(groupedSchedules).forEach(function(timeSlot, index) {
        var [startTime, endTime] = timeSlot.split('-');
        var timeSlotDiv = document.createElement('div');
        timeSlotDiv.innerHTML = '<input type="time" name="startTime_' + index + '" value="' + startTime + '">' +
            '<input type="time" name="endTime_' + index + '" value="' + endTime + '">' +
            '<select multiple name="employees_' + index + '[]">' +
            employeeOptions +
            '</select>' +
            '<button type="button" onclick="removeScheduleTimeSlot(' + index + ')" class="btn btn-danger btn-sm">刪除</button>';
        
        // 選擇已經排程的員工
        var select = timeSlotDiv.querySelector('select');
        groupedSchedules[timeSlot].forEach(function(emp) {
            select.querySelector('option[value="' + emp + '"]').selected = true;
        });

        scheduleFormContent.appendChild(timeSlotDiv);
    });

    var addButton = document.createElement('button');
    addButton.textContent = '新增時間區段';
    addButton.type = 'button';
    addButton.onclick = addScheduleTimeSlot;
    addButton.className = 'btn btn-primary mt-2';
    scheduleFormContent.appendChild(addButton);
}

// 移除時間區段
function removeScheduleTimeSlot(index) {
    var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
    var schedule = schedules[dayKey][index];
    
    if (confirm('確定要刪除這個時間區段嗎？')) {
        fetch('ScheduleCon?action=deleteSchedule' +
              '&employeeId=' + encodeURIComponent(schedule.employeeId) +
              '&date=' + encodeURIComponent(dayKey) +
              '&startTime=' + encodeURIComponent(schedule.startTime) +
              '&endTime=' + encodeURIComponent(schedule.endTime), {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            if (data.status === 'success') {
                schedules[dayKey].splice(index, 1);
                populateScheduleForm();
                updateScheduleList(currentDay.year, currentDay.month, currentDay.day);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('刪除排程時發生錯誤');
        });
    }
}

// 新增時間區段
function addScheduleTimeSlot() {
    var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
    if (!schedules[dayKey]) {
        schedules[dayKey] = [];
    }
    schedules[dayKey].push({ startTime: '09:00', endTime: '17:00', employees: [] });
    populateScheduleForm();
}

// 更新排程列表
function updateScheduleList(year, month, day) {
    var scheduleList = document.getElementById('schedule-list-' + year + '-' + month + '-' + day);
    if (scheduleList) {
        scheduleList.innerHTML = '';
        var dayKey = year + '-' + month + '-' + day;
        var daySchedules = schedules[dayKey] || [];

        var groupedSchedules = {};

        daySchedules.forEach(function(schedule) {
            var key = schedule.startTime + ' - ' + schedule.endTime;
            if (!groupedSchedules[key]) {
                groupedSchedules[key] = [];
            }
            groupedSchedules[key] = groupedSchedules[key].concat(schedule.employees);
        });

        Object.keys(groupedSchedules).forEach(function(timeSlot) {
            var scheduleItem = document.createElement('div');
            scheduleItem.className = 'schedule-block';
            scheduleItem.textContent = timeSlot + ': ' + groupedSchedules[timeSlot].join(', ');
            scheduleList.appendChild(scheduleItem);
        });
    }
}

// 更新所有排程列表
function updateAllScheduleLists(year, month) {
    var daysInMonth = new Date(year, month + 1, 0).getDate();
    for (var day = 1; day <= daysInMonth; day++) {
        updateScheduleList(year, month, day);
    }
}

// 新增時間區塊
function addTimeBlock() {
    var timeBlocks = document.getElementById('timeBlocks');
    var timeBlock = document.createElement('div');
    timeBlock.className = 'time-block mb-2';
    timeBlock.innerHTML = '<input type="time" class="start-time me-2">' +
        '<input type="time" class="end-time me-2">' +
        '<button onclick="removeTimeBlock(this)" class="btn btn-danger btn-sm">刪除</button>';
    timeBlocks.appendChild(timeBlock);
}

// 移除時間區塊
function removeTimeBlock(button) {
    var timeBlocks = document.getElementById('timeBlocks');
    timeBlocks.removeChild(button.parentElement);
}

// 提交排程
function submitSchedule() {
    var scheduleForm = document.getElementById('scheduleForm');
    var formData = new FormData(scheduleForm);

    fetch('ScheduleCon?action=saveSchedule', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        if (data.status === 'success') {
            closeModal();
            generateCalendar(currentDay.year, currentDay.month);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('提交排程時發生錯誤');
    });
}

document.addEventListener('DOMContentLoaded', function() {
    generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
});
</script>
</body>
</html>