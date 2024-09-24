package com.MarketMaster.controller.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.schedule.AskForLeaveBean;
import com.MarketMaster.dao.schedule.AskForLeaveService;

@Controller
@RequestMapping("/AskForLeaveCon")
public class AskForLeaveCon {
    @Autowired
    private AskForLeaveService leaveService;

    // 根據員工 ID 搜尋請假紀錄
    @GetMapping("/searchLeaveRecords")
    public String searchLeaveRecords(@RequestParam String employee_id, Model model) {
        List<AskForLeaveBean> leaveRecords = leaveService.getLeaveRecordsById(employee_id);
        model.addAttribute("leaveRecords", leaveRecords);
        return "schedule/getLeaveById";
    }

    // 根據請假 ID 搜尋請假紀錄
    @GetMapping("/searchByLeaveId")
    public String searchByLeaveId(@RequestParam String leave_id, Model model) {
        List<AskForLeaveBean> leaveRecordsById = leaveService.getLeaveRecordsByLeaveId(leave_id);
        if (leaveRecordsById != null && !leaveRecordsById.isEmpty()) {
            AskForLeaveBean leaveRecord = leaveRecordsById.get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            model.addAttribute("startDatetimeFormatted", leaveRecord.getStartDatetime().format(formatter));
            model.addAttribute("endDatetimeFormatted", leaveRecord.getEndDatetime().format(formatter));
            model.addAttribute("leaveRecords", leaveRecordsById);
            return "schedule/updateLeaveById";
        } else {
            return "error/404";
        }
    }

    // 獲取所有請假紀錄
    @GetMapping("/allLeaveRecords")
    public String allLeaveRecords(Model model) {
        List<AskForLeaveBean> allLeaveRecords = leaveService.getLeaveRecords();
        model.addAttribute("leaveRecords", allLeaveRecords);
        return "schedule/getAllLeaveRecords";
    }

    // 刪除請假紀錄
    @PostMapping("/deleteLeaveRecord")
    public String deleteLeaveRecord(@RequestParam String leave_id, @RequestParam String employee_id, Model model) {
        leaveService.deleteLeaveRecord(leave_id);
        return searchLeaveRecords(employee_id, model);
    }

    // 顯示新增請假紀錄的表單
    @PostMapping("/createForm")
    public String createForm(@RequestParam String employee_id, Model model) {
        String employeeName = leaveService.getEmployeeNameById(employee_id);
        String newLeaveId = leaveService.generateNextLeaveId();
        model.addAttribute("leave_id", newLeaveId);
        model.addAttribute("employee_id", employee_id);
        model.addAttribute("employee_name", employeeName);
        return "schedule/addLeaveRecordById";
    }

    // 新增請假紀錄
    @PostMapping("/addLeaveRecord")
    public String addLeaveRecord(@RequestParam String leave_id,
                                 @RequestParam String employee_id,
                                 @RequestParam String start_datetime,
                                 @RequestParam String end_datetime,
                                 @RequestParam String leave_category,
                                 @RequestParam String reason_of_leave,
                                 Model model) {
        LocalDateTime startDatetime = LocalDateTime.parse(start_datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDatetime = LocalDateTime.parse(end_datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        AskForLeaveBean leave = new AskForLeaveBean();
        leave.setLeaveId(leave_id);
        leave.setEmployeeId(employee_id);
        leave.setStartDatetime(startDatetime);
        leave.setEndDatetime(endDatetime);
        leave.setLeaveCategory(leave_category);
        leave.setReasonOfLeave(reason_of_leave);
        leave.setApprovedStatus("未批准");

        leaveService.addLeaveRecordById(leave);

        return searchLeaveRecords(employee_id, model);
    }

    // 更新請假紀錄
    @PostMapping("/updateLeaveRecord")
    public String updateLeaveRecord(@RequestParam String leave_id,
                                    @RequestParam String employee_id,
                                    @RequestParam String start_datetime,
                                    @RequestParam String end_datetime,
                                    @RequestParam String leave_category,
                                    @RequestParam String reason_of_leave,
                                    Model model) {
        LocalDateTime startDatetime = LocalDateTime.parse(start_datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDatetime = LocalDateTime.parse(end_datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        AskForLeaveBean leave = new AskForLeaveBean();
        leave.setLeaveId(leave_id);
        leave.setEmployeeId(employee_id);
        leave.setStartDatetime(startDatetime);
        leave.setEndDatetime(endDatetime);
        leave.setLeaveCategory(leave_category);
        leave.setReasonOfLeave(reason_of_leave);

        leaveService.updateLeaveRecord(leave);

        return "redirect:/AskForLeaveCon/searchLeaveRecords?employee_id=" + employee_id;
    }
    
    @GetMapping("/view")
    public String redirectToAskForLeavePage() {
        return "schedule/AskForLeave";
    }
}
