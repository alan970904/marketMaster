        package com.MarketMaster.controller.restock;

        import com.MarketMaster.DTO.restock.EmployeeDTO;
        import com.MarketMaster.DTO.restock.ProductCategoryDTO;
        import com.MarketMaster.DTO.restock.ProductNameDTO;
        import com.MarketMaster.service.restock.RestockService;
        import jakarta.servlet.http.HttpServletRequest;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;
        import java.util.*;
        import java.util.logging.Level;
        import java.util.logging.Logger;
        @RequestMapping("/restock")
        @Controller
        public class RestockController {
            private static final Logger logger = Logger.getLogger(RestockController.class.getName());

            @Autowired
            private RestockService restockService;
            public RestockController() {
                System.out.println("RestockController initialized");
            }



            @GetMapping("/RestockMain")
            public String showRestockForm() {
                System.out.println("RestockMain controller method called");
                return "restock/RestockMain";
            }


            @GetMapping("/latestId")
            @ResponseBody
            public ResponseEntity<String> getLatestRestockId() {
                try {
                    String latestId = restockService.getLatestRestockId();
                    return ResponseEntity.ok(latestId);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "获取最新 ID 时发生错误", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成进货 ID 时发生错误");
                }
            }

            @GetMapping("/employees")
            @ResponseBody
            public ResponseEntity<List<EmployeeDTO>> getEmployees() {
                try {
                    List<EmployeeDTO> employees = restockService.getEmployees();
                    return ResponseEntity.ok(employees);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "获取员工列表时发生错误", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            // 获取商品类别列表
            @GetMapping("/productCategories")
            @ResponseBody
            public ResponseEntity<List<ProductCategoryDTO>> getProductCategories() {
                try {
                    List<ProductCategoryDTO> categories = restockService.getProductCategory();
                    return ResponseEntity.ok(categories);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "获取商品类别时发生错误", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            @GetMapping("/productNames")
            @ResponseBody
            public ResponseEntity<List<ProductNameDTO>> getProductNames(@RequestParam("category") String category) {
                try {
                    List<ProductNameDTO> products = restockService.getProductNamesByCategory(category);
                    return ResponseEntity.ok(products);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "获取商品名称时发生错误", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            @PostMapping("/add")
            @ResponseBody
            public ResponseEntity<Map<String, Object>> addRestock(HttpServletRequest request) {
                Map<String, Object> response = new HashMap<>();
                try {
                    String finalRestockId = restockService.getLatestRestockId();
                    restockService.insertRestockData(finalRestockId, request);

                    response.put("status", "success");
                    response.put("message", "新进货记录已成功创建");
                    response.put("restockId", finalRestockId);
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    logger.severe("处理进货时发生错误: " + e.getMessage());
                    response.put("status", "error");
                    response.put("message", "处理进货时发生错误: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            }


        }
