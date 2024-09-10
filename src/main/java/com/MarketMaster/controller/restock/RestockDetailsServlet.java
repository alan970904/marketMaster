package com.MarketMaster.controller.restock;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.*;

import com.MarketMaster.bean.restock.RestockDetailViewBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.MarketMaster.service.restock.RestockService;

@WebServlet("/RestockDetailsServlet")
public class RestockDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RestockDetailsServlet.class.getName());
    private RestockService restockService;

    @Override
    public void init() throws ServletException {
        super.init();
        restockService = new RestockService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("getAllRestockDetails".equals(action)) {
            getAllRestockDetailsJson(request, response);
        } else {
            try {
                List<RestockDetailViewBean> restockDetailsList = restockService.getAllRestockDetails();
                request.setAttribute("restockDetailsList", restockDetailsList);
                request.getRequestDispatcher("restock/GetAllRestockDetails.jsp").forward(request, response);
            } catch (ClassNotFoundException | SQLException e) {
                logger.log(Level.SEVERE, "獲取進貨明細時發生錯誤", e);
                handleError(request, response, "獲取進貨明細時發生錯誤: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("deleteRestock".equals(action)) {
            deleteRestock(request, response);
        }
    }

    private void getAllRestockDetailsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            logger.info("開始獲取進貨明細");
            List<RestockDetailViewBean> restockDetailsList = restockService.getAllRestockDetails();
            logger.info("成功獲取進貨明細，數量: " + restockDetailsList.size());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            String json = gson.toJson(restockDetailsList);

            logger.info("轉換為JSON: " + json);
            out.print(json);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取進貨明細時發生錯誤", e);
            out.print("{\"error\": \"獲取進貨明細時發生錯誤: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    private void deleteRestock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String restockId = request.getParameter("restockId");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if (restockId == null || restockId.trim().isEmpty()) {
                out.print("{\"success\": false, \"message\": \"無效的進貨ID\"}");
                return;
            }
            boolean deleted = restockService.deleteRestock(restockId);
            if (deleted) {
                out.print("{\"success\": true, \"message\": \"記錄已成功刪除\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"刪除失敗，可能記錄不存在\"}");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除進貨記錄時發生錯誤", e);
            out.print("{\"success\": false, \"message\": \"刪除時發生錯誤: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("jsp/GetAllRestockDetails.jsp").forward(request, response);
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}