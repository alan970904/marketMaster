//package com.MarketMaster.controller.bonus;
//
//import java.io.IOException;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import com.MarketMaster.bean.bonus.BonusExchangeBean;
//import com.MarketMaster.bean.product.ProductBean;
//import com.MarketMaster.service.bonus.BonusExchangeService;
//import com.MarketMaster.exception.DataAccessException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/BonusExchangeServlet")
//public class BonusExchangeServlet extends HttpServlet {
//    private static final Logger logger = Logger.getLogger(BonusExchangeServlet.class.getName());
//    private BonusExchangeService bonusExchangeService = new BonusExchangeService();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        logger.info("Received GET request");
//        request.getRequestDispatcher("/bonus/BonusExchangeInput.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        logger.info("doPost method called in BonusExchangeServlet");
//        String action = request.getParameter("action");
//        logger.info("Received POST request with action: " + action);
//        
//        // 打印所有請求參數
//        Enumeration<String> parameterNames = request.getParameterNames();
//        while (parameterNames.hasMoreElements()) {
//            String paramName = parameterNames.nextElement();
//            String paramValue = request.getParameter(paramName);
//            logger.info("Parameter: " + paramName + " = " + paramValue);
//        }
//        
//        
//        try {
//            switch (action) {
//                case "queryExchangeableProducts":
//                    queryExchangeableProducts(request, response);
//                    break;
//                case "executeExchange":
//                    executeExchange(request, response);
//                    break;
//                case "showExchangeRecords":
//                    showExchangeRecords(request, response);
//                    break;
//                case "updateExchange":
//                    logger.info("Calling updateExchange method");
//                    updateExchange(request, response);
//                    break;
//                case "deleteExchange":
//                    logger.info("Calling deleteExchange method");
//                    deleteExchange(request, response);
//                    break;
//                default:
//                    logger.warning("Invalid action: " + action);
//                    sendJsonResponse(response, "{\"status\":\"error\",\"message\":\"無效的操作\"}");
//            }
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error processing request", e);
//            sendJsonResponse(response, "{\"status\":\"error\",\"message\":\"處理請求時發生錯誤: " + e.getMessage() + "\"}");
//        }
//    }
//
//    private void queryExchangeableProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String customerTel = request.getParameter("customerTel");
//        logger.info("Querying exchangeable products for customer: " + customerTel);
//        try {
//            List<ProductBean> exchangeableProducts = bonusExchangeService.getExchangeableProducts(customerTel);
//            request.setAttribute("exchangeableProducts", exchangeableProducts);
//            request.getRequestDispatcher("/bonus/BonusExchangeList.jsp").forward(request, response);
//        } catch (DataAccessException e) {
//            logger.log(Level.SEVERE, "Error querying exchangeable products", e);
//            request.setAttribute("errorMessage", "查詢可兌換商品失敗: " + e.getMessage());
//            request.getRequestDispatcher("/bonus/BonusExchangeInput.jsp").forward(request, response);
//        }
//    }
//
//    private void executeExchange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String customerTel = request.getParameter("customerTel");
//        String productId = request.getParameter("productId");
//        int exchangeQuantity = Integer.parseInt(request.getParameter("exchangeQuantity"));
//
//        logger.info("Executing exchange for customer: " + customerTel + ", product: " + productId + ", quantity: " + exchangeQuantity);
//
//        try {
//            bonusExchangeService.executeExchange(customerTel, productId, exchangeQuantity);
//            request.setAttribute("successMessage", "兌換成功！");
//        } catch (DataAccessException e) {
//            logger.log(Level.SEVERE, "Exchange execution failed", e);
//            request.setAttribute("errorMessage", "兌換失敗：" + e.getMessage());
//        }
//        queryExchangeableProducts(request, response);
//    }
//
//    private void showExchangeRecords(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String customerTel = request.getParameter("customerTel");
//        logger.info("Showing exchange records for customer: " + customerTel);
//        try {
//            List<BonusExchangeBean> exchangeRecords = bonusExchangeService.getExchangeRecords(customerTel);
//            request.setAttribute("exchangeRecords", exchangeRecords);
//            request.getRequestDispatcher("/bonus/BonusExchangeRecord.jsp").forward(request, response);
//        } catch (DataAccessException e) {
//            logger.log(Level.SEVERE, "Error retrieving exchange records", e);
//            request.setAttribute("errorMessage", "獲取兌換記錄失敗: " + e.getMessage());
//            request.getRequestDispatcher("/bonus/BonusExchangeInput.jsp").forward(request, response);
//        }
//    }
//
//    private void updateExchange(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String exchangeId = request.getParameter("exchangeId");
//        int newPoints = Integer.parseInt(request.getParameter("newPoints"));
//
//        logger.info("Updating exchange: " + exchangeId + " with new points: " + newPoints);
//
//        try {
//            bonusExchangeService.updateExchange(exchangeId, newPoints);
//            sendJsonResponse(response, "{\"status\":\"success\",\"message\":\"兌換記錄更新成功！\"}");
//        } catch (DataAccessException e) {
//            logger.log(Level.SEVERE, "Error updating exchange", e);
//            sendJsonResponse(response, "{\"status\":\"error\",\"message\":\"更新兌換記錄失敗: " + e.getMessage() + "\"}");
//        }
//    }
//
//    private void deleteExchange(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String exchangeId = request.getParameter("exchangeId");
//
//        logger.info("Deleting exchange: " + exchangeId);
//
//        try {
//            bonusExchangeService.deleteExchange(exchangeId);
//            sendJsonResponse(response, "{\"status\":\"success\",\"message\":\"兌換記錄刪除成功！\"}");
//        } catch (DataAccessException e) {
//            logger.log(Level.SEVERE, "Error deleting exchange", e);
//            sendJsonResponse(response, "{\"status\":\"error\",\"message\":\"刪除兌換記錄失敗: " + e.getMessage() + "\"}");
//        }
//    }
//
//    private void sendJsonResponse(HttpServletResponse response, String jsonString) throws IOException {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonString);
//    }
//}