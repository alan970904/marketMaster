package com.MarketMaster.controller.restock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.MarketMaster.bean.restock.RestockDetailViewBean;

import com.MarketMaster.service.restock.RestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/restock")
@Controller
public class RestockDetailsController {


    private static final Logger logger = Logger.getLogger(RestockDetailsController.class.getName());
    @Autowired
    private RestockService restockService;

    @GetMapping("/GetAllRestockDetailsDetail")
    public String getAllRestockDetailsDetailPage() {
        // 返回库存明细的JSP页面名称
        return "restock/GetAllRestockDetails";  // 对应的JSP文件路径：/WEB-INF/views/restock/GetAllRestockDetails.jsp
    }


    @GetMapping("/getAllRestockDetailsJson")
    @ResponseBody
    public List<RestockDetailViewBean> getAllRestockDetailsJson() {
        try {
            return restockService.getAllRestockDetails();
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "獲取進貨明細時發生錯誤", e);
            // 返回一個空的列表，或者根據需要返回適當的錯誤響應
            return new ArrayList<>();
        }
    }


    @PostMapping("/deleteRestock")
    @ResponseBody
    public Map<String, Object> deleteRestock(@RequestParam("restockId") String restockId) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (restockId == null || restockId.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "无效的进货ID");
                return response;
            }
            boolean deleted = restockService.deleteRestock(restockId);
            if (deleted) {
                response.put("success", true);
                response.put("message", "记录已成功删除");
            } else {
                response.put("success", false);
                response.put("message", "删除失败，可能记录不存在");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "删除进货记录时发生错误", e);
            response.put("success", false);
            response.put("message", "删除时发生错误: " + e.getMessage());
        }
        return response;
    }



}