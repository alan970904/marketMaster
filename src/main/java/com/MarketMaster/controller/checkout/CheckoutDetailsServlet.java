package com.MarketMaster.controller.checkout;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.service.checkout.CheckoutDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CheckoutDetailsServlet")
public class CheckoutDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CheckoutDetailsService checkoutDetailsService;

    public CheckoutDetailsServlet() {
        super();
        checkoutDetailsService = new CheckoutDetailsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
            case "delete":
                deleteCheckoutDetails(request, response);
                break;
            case "getAll":
                getAllCheckoutDetails(request, response);
                break;
            case "getPart":
                getPartCheckoutDetails(request, response);
                break;
            case "getOne":
                getCheckoutDetails(request, response);
                break;
            case "getUpdate":
                getUpdateCheckoutDetails(request, response);
                break;
            case "insert":
                insertCheckoutDetails(request, response);
                break;
            case "update":
                updateCheckoutDetails(request, response);
                break;
            case "searchByProductId":
                searchByProductId(request, response);
                break;
            case "updateAfterReturn":
                updateAfterReturn(request, response);
                break;
            case "cancelReturn":
                cancelReturn(request, response);
                break;
            case "getProductReturnRates":
                getProductReturnRates(request, response);
                break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的操作");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "處理請求時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void deleteCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        try {
            checkoutDetailsService.deleteCheckoutDetails(checkoutId, productId);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\",\"message\":\"結帳明細已成功刪除\"}");
        } catch (Exception e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"刪除結帳明細失敗\"}");
        }
    }

    private void getAllCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CheckoutDetailsBean> checkoutDetails = checkoutDetailsService.getAllCheckoutDetails();
        request.setAttribute("checkoutDetails", checkoutDetails);
        request.getRequestDispatcher("/checkoutDetails/GetAllCheckoutDetails.jsp").forward(request, response);
    }

    private void getPartCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        List<CheckoutDetailsBean> checkoutDetailsList = checkoutDetailsService.getPartCheckoutDetails(checkoutId);
        request.setAttribute("checkoutDetailsList", checkoutDetailsList);
        request.getRequestDispatcher("/checkoutDetails/GetPartCheckoutDetails.jsp").forward(request, response);
    }

    private void getCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        CheckoutDetailsBean checkoutDetails = checkoutDetailsService.getCheckoutDetails(checkoutId, productId);
        request.setAttribute("checkoutDetails", checkoutDetails);
        request.getRequestDispatcher("/checkoutDetails/GetCheckoutDetails.jsp").forward(request, response);
    }

    private void getUpdateCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        CheckoutDetailsBean checkoutDetails = checkoutDetailsService.getCheckoutDetails(checkoutId, productId);
        request.setAttribute("checkoutDetails", checkoutDetails);
        request.getRequestDispatcher("/checkoutDetails/GetUpdateCheckoutDetails.jsp").forward(request, response);
    }

    private void insertCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CheckoutDetailsBean checkoutDetails = createCheckoutDetailsFromRequest(request);
        checkoutDetailsService.addCheckoutDetails(checkoutDetails);
        request.setAttribute("checkoutDetails", checkoutDetails);
        request.getRequestDispatcher("/checkoutDetails/InsertCheckoutDetails.jsp").forward(request, response);
    }

    private void updateCheckoutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        int numberOfCheckout = Integer.parseInt(request.getParameter("numberOfCheckout"));
        int productPrice = Integer.parseInt(request.getParameter("productPrice"));
        int checkoutPrice = Integer.parseInt(request.getParameter("checkoutPrice"));

        CheckoutDetailsBean checkoutDetails = new CheckoutDetailsBean();
        checkoutDetails.setCheckoutId(checkoutId);
        checkoutDetails.setProductId(productId);
        checkoutDetails.setNumberOfCheckout(numberOfCheckout);
        checkoutDetails.setProductPrice(productPrice);
        checkoutDetails.setCheckoutPrice(checkoutPrice);

        try {
            checkoutDetailsService.updateCheckoutDetails(checkoutDetails);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\",\"message\":\"更新成功\"}");
        } catch (Exception e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private void searchByProductId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        List<CheckoutDetailsBean> checkoutDetails = checkoutDetailsService.searchCheckoutDetailsByProductId(productId);
        request.setAttribute("checkoutDetails", checkoutDetails);
        request.getRequestDispatcher("/checkoutDetails/SearchResults.jsp").forward(request, response);
    }

    private void updateAfterReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        int returnQuantity = Integer.parseInt(request.getParameter("returnQuantity"));
        BigDecimal returnPrice = new BigDecimal(request.getParameter("returnPrice"));
        checkoutDetailsService.updateAfterReturn(checkoutId, productId, returnQuantity, returnPrice);
        request.setAttribute("message", "退貨後結帳明細已更新。");
        getAllCheckoutDetails(request, response);
    }

    private void cancelReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        int returnQuantity = Integer.parseInt(request.getParameter("returnQuantity"));
        BigDecimal returnPrice = new BigDecimal(request.getParameter("returnPrice"));
        checkoutDetailsService.cancelReturn(checkoutId, productId, returnQuantity, returnPrice);
        request.setAttribute("message", "退貨已取消，結帳明細已更新。");
        getAllCheckoutDetails(request, response);
    }

    private void getProductReturnRates(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, Object>> returnRates = checkoutDetailsService.getProductReturnRates();
        request.setAttribute("returnRates", returnRates);
        request.getRequestDispatcher("/checkoutDetails/ProductReturnRates.jsp").forward(request, response);
    }

    private CheckoutDetailsBean createCheckoutDetailsFromRequest(HttpServletRequest request) {
        CheckoutDetailsBean checkoutDetails = new CheckoutDetailsBean();

        String checkoutId = request.getParameter("checkoutId");
        String productId = request.getParameter("productId");
        String numberOfCheckoutStr = request.getParameter("numberOfCheckout");
        String productPriceStr = request.getParameter("productPrice");
        String checkoutPriceStr = request.getParameter("checkoutPrice");

        // 檢查必要參數是否存在
        if (checkoutId == null || productId == null || numberOfCheckoutStr == null ||
            productPriceStr == null || checkoutPriceStr == null) {
            throw new IllegalArgumentException("必要參數缺失");
        }

        checkoutDetails.setCheckoutId(checkoutId);
        checkoutDetails.setProductId(productId);

        try {
            int numberOfCheckout = Integer.parseInt(numberOfCheckoutStr);
            int productPrice = Integer.parseInt(productPriceStr);
            int checkoutPrice = Integer.parseInt(checkoutPriceStr);

            checkoutDetails.setNumberOfCheckout(numberOfCheckout);
            checkoutDetails.setProductPrice(productPrice);
            checkoutDetails.setCheckoutPrice(checkoutPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("無效的數字格式: " + e.getMessage());
        }

        return checkoutDetails;
    }



}