package com.MarketMaster.controller.bonus;
import java.io.IOException;
import java.util.List;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.service.bonus.BonusExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
@WebServlet("/BonusExchangeServlet")
public class BonusExchangeServlet extends HttpServlet {
    private BonusExchangeService bonusExchangeService = new BonusExchangeService();
    private Gson gson = new Gson();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "queryExchangeableProducts":
                queryExchangeableProducts(request, response);
                break;
            case "executeBatchExchange":
                executeBatchExchange(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
    private void queryExchangeableProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerTel = request.getParameter("customerTel");
        List<ProductBean> exchangeableProducts = bonusExchangeService.getExchangeableProducts(customerTel);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(exchangeableProducts));
    }
    private void executeBatchExchange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        String customerTel = request.getParameter("customerTel");

        try {
            bonusExchangeService.executeBatchExchange(productId, customerTel);
            response.getWriter().write("Batch bonus exchange executed successfully");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error executing batch exchange: " + e.getMessage());
        }
    }
}