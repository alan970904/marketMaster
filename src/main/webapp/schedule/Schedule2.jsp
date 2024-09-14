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
<link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">
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
		<div class="row">
			<div class="col text-start">
				<button type="button" class="btn btn-secondary" onclick="goBack()">
					<i class="fas fa-arrow-left"></i> 返回
				</button>
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
	</div>

	<!-- 排程彈窗 -->
	<div id="scheduleModal" class="modal">
		<div class="modal-content">
			<span class="close">&times;</span>
			<h2>編輯排程</h2>
			<div id="scheduleFormContent">
				<!-- 動態生成的時間區段和員工選擇將在這裡顯示 -->
			</div>
			<button id="saveSchedule" class="btn btn-primary">儲存排程</button>
		</div>
	</div>

	<form id="fetchEmployeeNamesForm" method="get"
		action="<c:url value='/ScheduleCon'/>">
		<input type="hidden" name="action" value="viewEmployeeNames" />
	</form>

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

    function goBack() {
        window.location.href = '<c:url value="/body/HomePage.jsp"/>';
    }

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

    function openScheduleModal(year, month, day) {
        currentDay = { year: year, month: month, day: day };
        console.log("Setting currentDay:", currentDay);
        document.getElementById('scheduleModal').style.display = 'block';
        populateScheduleForm();
    }

    function closeModal() {
        document.getElementById('scheduleModal').style.display = 'none';
    }

    function populateScheduleForm() {
        var scheduleFormContent = document.getElementById('scheduleFormContent');
        scheduleFormContent.innerHTML = '';

        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        var daySchedules = schedules[dayKey] || [];

        daySchedules.forEach(function(schedule, index) {
            var timeSlotDiv = document.createElement('div');
            timeSlotDiv.innerHTML = '<input type="time" value="' + schedule.startTime + '" onchange="updateScheduleTime(' + index + ', \'startTime\', this.value)">' +
                '<input type="time" value="' + schedule.endTime + '" onchange="updateScheduleTime(' + index + ', \'endTime\', this.value)">' +
                '<select multiple class="employee-select" onchange="updateScheduleEmployees(' + index + ', this)">' +
                employees.map(function(emp) {
                    return '<option value="' + emp + '"' + (schedule.employees.includes(emp) ? ' selected' : '') + '>' + emp + '</option>';
                }).join('') +
                '</select>' +
                '<button type="button" onclick="removeScheduleTimeSlot(' + index + ')" class="btn btn-danger btn-sm">刪除</button>';
            scheduleFormContent.appendChild(timeSlotDiv);
        });

        var addButton = document.createElement('button');
        addButton.textContent = '新增時間區段';
        addButton.type = 'button';
        addButton.onclick = addScheduleTimeSlot;
        addButton.className = 'btn btn-primary mt-2';
        scheduleFormContent.appendChild(addButton);
    }

    function updateScheduleTime(index, type, value) {
        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        schedules[dayKey][index][type] = value;
    }

    function updateScheduleEmployees(index, selectElement) {
        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        schedules[dayKey][index].employees = Array.from(selectElement.selectedOptions).map(option => option.value);
    }

    function removeScheduleTimeSlot(index) {
        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        schedules[dayKey].splice(index, 1);
        populateScheduleForm();
    }

    function addScheduleTimeSlot() {
        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        if (!schedules[dayKey]) {
            schedules[dayKey] = [];
        }
        schedules[dayKey].push({ startTime: '09:00', endTime: '17:00', employees: [] });
        populateScheduleForm();
    }

    function getEmployeeIdByName(name) {
        var employee = allSchedules.find(function(schedule) {
            return schedule.employeeName === name;
        });
        return employee ? employee.employeeId : null;
    }

    function formatTime(time) {
        return time + ":00"; 
    }

    function saveSchedule() {
        var dayKey = currentDay.year + '-' + currentDay.month + '-' + currentDay.day;
        var updatedSchedules = schedules[dayKey] || [];
        
        var formData = new FormData();
        formData.append('year', currentDay.year);
        formData.append('month', currentDay.month + 1);
        formData.append('day', currentDay.day);

        console.log("Sending date:", currentDay.year, currentDay.month + 1, currentDay.day);

        var previousSchedules = allSchedules.filter(function(schedule) {
            return schedule.jobDate.getFullYear() === currentDay.year &&
                   schedule.jobDate.getMonth() === currentDay.month &&
                   schedule.jobDate.getDate() === currentDay.day;
        });

        var previousEmployees = previousSchedules.map(schedule => schedule.employeeName.trim());
        var currentEmployees = [];

        updatedSchedules.forEach(function(schedule, index) {
            formData.append('startTime_' + index, formatTime(schedule.startTime));
            formData.append('endTime_' + index, formatTime(schedule.endTime));
            schedule.employees.forEach(function(employee) {
                var employeeId = getEmployeeIdByName(employee);
                if (employeeId) {
                    formData.append('employees_' + index, employeeId);
                    currentEmployees.push(employee.trim());
                }
            });
        });

        var employeesToAdd = currentEmployees.filter(emp => !previousEmployees.includes(emp));
        var employeesToRemove = previousEmployees.filter(emp => !currentEmployees.includes(emp));

        formData.append('employeesToAdd', employeesToAdd.join(','));
        formData.append('employeesToRemove', employeesToRemove.join(','));

        console.log("發送數據:", Object.fromEntries(formData));
        
        fetch('<c:url value="/ScheduleCon?action=saveSchedule"/>', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            console.log('Response status:', response.status);
            return response.text();
        })
        .then(data => {
            console.log('Response data:', data);
            if (data.startsWith("Error")) {
                throw new Error(data);
            }
            alert('排程已成功保存');
            updateScheduleList(currentDay.year, currentDay.month, currentDay.day);
            closeModal();
        })
        .catch((error) => {
            console.error('錯誤:', error);
            alert('保存排程時發生錯誤: ' + error.message);
        });
    }


    function updateScheduleList(year, month, day) {
        var scheduleList = document.getElementById('schedule-list-' + year + '-' + month + '-' + day);
        if (scheduleList) {
            scheduleList.innerHTML = '';
            var dayKey = year + '-' + month + '-' + day;
            var daySchedules = schedules[dayKey] || [];
            daySchedules.forEach(function(schedule) {
                var scheduleItem = document.createElement('div');
                scheduleItem.className = 'schedule-block';
                scheduleItem.textContent = schedule.startTime + ' - ' + schedule.endTime + ': ' + schedule.employees.join(', ');
                scheduleList.appendChild(scheduleItem);
            });
        }
    }

    function updateAllScheduleLists(year, month) {
        var daysInMonth = new Date(year, month + 1, 0).getDate();
        for (var day = 1; day <= daysInMonth; day++) {
            updateScheduleList(year, month, day);
        }
    }

    function addTimeBlock() {
        var timeBlocks = document.getElementById('timeBlocks');
        var timeBlock = document.createElement('div');
        timeBlock.className = 'time-block mb-2';
        timeBlock.innerHTML = '<input type="time" class="start-time me-2">' +
            '<input type="time" class="end-time me-2">' +
            '<button onclick="removeTimeBlock(this)" class="btn btn-danger btn-sm">移除</button>';
        timeBlocks.appendChild(timeBlock);
    }

    function removeTimeBlock(button) {
        var timeBlocks = document.getElementById('timeBlocks');
        timeBlocks.removeChild(button.parentElement);
    }

    function applyTimeSlots() {
        var timeBlocks = document.querySelectorAll('.time-block');
        var timeSlots = Array.from(timeBlocks).map(function(block) {
            return {
                startTime: block.querySelector('.start-time').value,
                endTime: block.querySelector('.end-time').value,
                employees: []
            };
        });

        var year = currentDate.getFullYear();
        var month = currentDate.getMonth();
        var daysInMonth = new Date(year, month + 1, 0).getDate();

        for (var day = 1; day <= daysInMonth; day++) {
            var dayKey = year + '-' + month + '-' + day;
            if (!schedules[dayKey] || schedules[dayKey].length === 0) {
                schedules[dayKey] = JSON.parse(JSON.stringify(timeSlots)); // 深拷貝
            }
        }

        updateAllScheduleLists(year, month);
        alert('時間區段已套用到所有無排程的日期');
    }

    function changeMonth(offset) {
        currentDate.setMonth(currentDate.getMonth() + offset);
        generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
    }

    function initializeSchedules() {
        allSchedules.forEach(function(schedule) {
            var day = schedule.jobDate.getDate();
            var month = schedule.jobDate.getMonth();
            var year = schedule.jobDate.getFullYear();
            var dayKey = year + '-' + month + '-' + day;
            
            if (!schedules[dayKey]) {
                schedules[dayKey] = [];
            }
            schedules[dayKey].push({
                startTime: schedule.startTime,
                endTime: schedule.endTime,
                employees: [schedule.employeeName]
            });
        });
    }

    document.getElementById('prev-month').addEventListener('click', function() { changeMonth(-1); });
    document.getElementById('next-month').addEventListener('click', function() { changeMonth(1); });
    document.getElementsByClassName('close')[0].addEventListener('click', closeModal);
    document.getElementById('saveSchedule').addEventListener('click', saveSchedule);
    document.getElementById('addTimeBlock').addEventListener('click', addTimeBlock);
    document.getElementById('applyTimeSlots').addEventListener('click', applyTimeSlots);

    window.onload = function() {
        initializeSchedules();
        generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
        addTimeBlock(); 
    };
</script>
</body>
</html>