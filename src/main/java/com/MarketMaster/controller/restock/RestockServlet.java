package com.MarketMaster.controller.restock;

import com.MarketMaster.DTO.restock.EmployeeDTO;
import com.MarketMaster.DTO.restock.ProductCategoryDTO;
import com.MarketMaster.DTO.restock.ProductNameDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.MarketMaster.service.restock.RestockService;

@SuppressWarnings("unused")
@WebServlet("/RestockServlet")
public class RestockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RestockServlet.class.getName());
    private RestockService restockService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        restockService = new RestockService();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 action 參數");
            return;
        }
        try {
            switch (action) {
                case "getLatestId":
                    handleGetLatestId(response);
                    break;
                case "getEmployees":
                    handleGetEmployees(response);
                    break;
                case "getProductCategories":
                    handleGetProductCategories(response);
                    break;
                case "getProductNames":
                    String category = request.getParameter("category");
                    handleGetProductNames(response, category);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "未知的 action");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "處理請求時發生錯誤", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "處理請求時發生錯誤: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String finalRestockId = restockService.getLatestRestockId();
            restockService.insertRestockData(finalRestockId, request);

            String jsonResponse = "{\"status\":\"success\",\"message\":\"新進貨記錄已成功創建\",\"restockId\":\"" + finalRestockId + "\"}";
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            logger.severe("處理進貨時發生錯誤: " + e.getMessage());
            String errorResponse = "{\"status\":\"error\",\"message\":\"處理進貨時發生錯誤: " + e.getMessage() + "\"}";
            response.getWriter().write(errorResponse);
        }
    }
    private void handleGetLatestId(HttpServletResponse response) throws IOException {
        try {
            String latestId = restockService.getLatestRestockId();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(latestId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取最新 ID 時發生錯誤", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "生成進貨 ID 時發生錯誤");
        }
    }

    private void handleGetEmployees(HttpServletResponse response) throws IOException {
        try {
            List<EmployeeDTO> employees = restockService.getEmployees();
            sendJsonResponse(response, employees);
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "獲取員工列表時發生錯誤", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "獲取員工列表時發生錯誤");
        }
    }

    private void handleGetProductCategories(HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        List<ProductCategoryDTO> categories = restockService.getProductCategory();
        logger.info("Product categories retrieved: " + categories.size());
        logger.info("Categories: " + gson.toJson(categories));
        sendJsonResponse(response, categories);
    }

    private void handleGetProductNames(HttpServletResponse response, String category) throws IOException, SQLException, ClassNotFoundException {
        List<ProductNameDTO> products = restockService.getProductNamesByCategory(category);
        sendJsonResponse(response, products);
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = gson.toJson(data);
        logger.info("Sending JSON response: " + json);
        response.getWriter().write(json);
    }

    private void handleException(HttpServletResponse response, String message, Exception e) throws IOException {
        logger.log(Level.SEVERE, message, e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }

    static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate date) throws IOException {
            out.value(date.toString());
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        }
    }
}