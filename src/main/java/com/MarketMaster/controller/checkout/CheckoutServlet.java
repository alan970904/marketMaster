package com.MarketMaster.controller.checkout;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.service.checkout.CheckoutService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CheckoutService checkoutService;
	private static final Logger logger = Logger.getLogger(CheckoutServlet.class.getName());

	// 構造函數
	public CheckoutServlet() {
		super();
		checkoutService = new CheckoutService();
	}

	// 處理GET請求
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleRequest(request, response);
	}

	// 處理POST請求
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleRequest(request, response);
	}

	// 請求處理方法
	private void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null || action.isEmpty()) {
			// 處理空 action
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 action 參數");
			return;
		}
		try {
			switch (action) {
			case "delete":
				deleteCheckout(request, response);
				break;
			case "getAll":
				getAllCheckouts(request, response);
				break;
			case "getOne":
				getCheckout(request, response);
				break;
			case "getUpdate":
				getUpdateCheckout(request, response);
				break;
			case "insert":
				insertCheckout(request, response);
				break;
			case "update":
				updateCheckout(request, response);
				break;
			case "searchByTel":
				searchByTel(request, response);
				break;
			case "getDailySalesReport":
				getDailySalesReport(request, response);
				break;
			case "getCheckoutSummary":
				getCheckoutSummary(request, response);
				break;
			case "getNextCheckoutId":
				getNextCheckoutId(request, response);
				break;
			case "insertCheckoutWithDetails":
				insertCheckoutWithDetails(request, response);
				break;
			case "getAllEmployees":
				getAllEmployees(request, response);
				break;
			case "getProductNames":
				getProductNames(request, response);
				break;
			case "updateTotalAndBonus":
				updateTotalAndBonus(request, response);
				break;

			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的操作");
				break;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "刪除結帳記錄時發生錯誤", e);
			request.setAttribute("errorMessage", "處理請求時發生錯誤：" + e.getMessage());
			request.getRequestDispatcher("/checkout/checkout/error.jsp").forward(request, response);
		}
	}

	// 刪除結帳記錄
	private void deleteCheckout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String checkoutId = request.getParameter("checkoutId");
		try {
			checkoutService.deleteCheckoutAndDetails(checkoutId);
			response.setContentType("application/json");
			response.getWriter().write("{\"status\":\"success\",\"message\":\"結帳記錄及其相關明細已成功刪除\"}");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "刪除結帳記錄及其相關明細時發生錯誤", e);
			response.setContentType("application/json");
			response.getWriter().write("{\"status\":\"error\",\"message\":\"刪除結帳記錄及其相關明細失敗: " + e.getMessage() + "\"}");
		}
	}

	// 獲取所有結帳記錄
	private void getAllCheckouts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<CheckoutBean> checkouts = checkoutService.getAllCheckouts();
		request.setAttribute("checkouts", checkouts);
		request.getRequestDispatcher("/checkout/checkout/GetAllCheckouts.jsp").forward(request, response);
	}

	// 獲取單個結帳記錄
	private void getCheckout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String checkoutId = request.getParameter("checkoutId");
		CheckoutBean checkout = checkoutService.getCheckout(checkoutId);
		request.setAttribute("checkout", checkout);
		request.getRequestDispatcher("/checkout/checkout/GetCheckout.jsp").forward(request, response);
	}

	// 獲取要更新的結帳記錄
	private void getUpdateCheckout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String checkoutId = request.getParameter("checkoutId");
		CheckoutBean checkout = checkoutService.getCheckout(checkoutId);
		request.setAttribute("checkout", checkout);
		request.getRequestDispatcher("/checkout/checkout/GetUpdateCheckout.jsp").forward(request, response);
	}

	// 插入新的結帳記錄
	private void insertCheckout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckoutBean checkout = createCheckoutFromRequest(request);
		List<CheckoutDetailsBean> details = createCheckoutDetailsFromRequest(request);
		try {
			boolean success = checkoutService.addCheckout(checkout);
			if (success) {
				request.setAttribute("message", "結帳記錄新增成功");
			} else {
				request.setAttribute("message", "結帳記錄新增失敗");
			}
		} catch (Exception e) {
			request.setAttribute("message", "新增結帳記錄時發生錯誤：" + e.getMessage());
		}
		request.setAttribute("checkout", checkout);
		request.getRequestDispatcher("/checkout/checkout/InsertCheckout.jsp").forward(request, response);
	}

	// 新增方法來從請求中創建 CheckoutDetailsBean 列表
	private List<CheckoutDetailsBean> createCheckoutDetailsFromRequest(HttpServletRequest request) {
		List<CheckoutDetailsBean> details = new ArrayList<>();
		String[] productIds = request.getParameterValues("productId");
		String[] quantities = request.getParameterValues("quantity");
		String[] prices = request.getParameterValues("productPrice");

		if (productIds != null) {
			for (int i = 0; i < productIds.length; i++) {
				CheckoutDetailsBean detail = new CheckoutDetailsBean();
				detail.setProductId(productIds[i]);

				// 處理數量可能為空的情況
				String quantityStr = quantities[i];
				if (quantityStr != null && !quantityStr.isEmpty()) {
					detail.setNumberOfCheckout(Integer.parseInt(quantityStr));
				} else {
					detail.setNumberOfCheckout(0); // 或者設置為其他默認值
				}

				// 處理價格可能為空的情況
				String priceStr = prices[i];
				if (priceStr != null && !priceStr.isEmpty()) {
					detail.setCheckoutPrice(Integer.parseInt(priceStr));
				} else {
					detail.setCheckoutPrice(0); // 或者設置為其他默認值
				}

				details.add(detail);
			}
		}

		return details;
	}

	// 更新結帳記錄
	private void updateCheckout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			CheckoutBean checkout = createCheckoutFromRequest(request);
			boolean updateSuccess = checkoutService.updateCheckout(checkout);

			if (updateSuccess) {
				String jsonResponse = "{\"status\":\"success\",\"message\":\"更新成功\"}";
				response.getWriter().write(jsonResponse);
				logger.info("結帳記錄更新成功: " + checkout.getCheckoutId());
			} else {
				String jsonResponse = "{\"status\":\"error\",\"message\":\"更新失敗\"}";
				response.getWriter().write(jsonResponse);
				logger.warning("結帳記錄更新失敗: " + checkout.getCheckoutId());
			}
		} catch (Exception e) {
			String jsonResponse = "{\"status\":\"error\",\"message\":\"更新時發生錯誤: " + e.getMessage() + "\"}";
			response.getWriter().write(jsonResponse);
			logger.log(Level.SEVERE, "更新結帳記錄時發生錯誤", e);
		}
	}

	// 根據電話號碼搜索結帳記錄
	private void searchByTel(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String customerTel = request.getParameter("customerTel");
		List<CheckoutBean> checkouts = checkoutService.searchCheckoutsByTel(customerTel);
		request.setAttribute("checkouts", checkouts);
		request.getRequestDispatcher("/checkout/checkout/SearchResults.jsp").forward(request, response);
	}

	// 獲取每日銷售報告
	private void getDailySalesReport(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Map<String, Object>> report = checkoutService.getDailySalesReport();
		request.setAttribute("dailySalesReport", report);
		request.getRequestDispatcher("/checkout/checkout/DailySalesReport.jsp").forward(request, response);
	}

	// 獲取結帳總表
	private void getCheckoutSummary(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Map<String, Object>> summary = checkoutService.getCheckoutSummary();
		request.setAttribute("checkoutSummary", summary);
		request.getRequestDispatcher("/checkout/checkout/CheckoutSummary.jsp").forward(request, response);
	}

	// 從請求中創建CheckoutBean對象
	private CheckoutBean createCheckoutFromRequest(HttpServletRequest request) {
		CheckoutBean checkout = new CheckoutBean();
		checkout.setCheckoutId(request.getParameter("checkoutId"));
		checkout.setCustomerTel(request.getParameter("customerTel"));
		checkout.setEmployeeId(request.getParameter("employeeId"));

		String totalPriceStr = request.getParameter("checkoutTotalPrice");
		if (totalPriceStr != null && !totalPriceStr.isEmpty()) {
			checkout.setCheckoutTotalPrice(Integer.parseInt(totalPriceStr));
		} else {
			// 處理 checkoutTotalPrice 缺失或無效的情況
			checkout.setCheckoutTotalPrice(0); // 或者根據需求拋出異常
		}

		String checkoutDateStr = request.getParameter("checkoutDate");
		if (checkoutDateStr != null && !checkoutDateStr.isEmpty()) {
			checkout.setCheckoutDate(Date.valueOf(checkoutDateStr));
		}

		String bonusPointsStr = request.getParameter("bonusPoints");
		if (bonusPointsStr != null && !bonusPointsStr.isEmpty()) {
			checkout.setBonusPoints(Integer.parseInt(bonusPointsStr));
		} else {
			// 處理 bonusPoints 缺失或無效的情況
			checkout.setBonusPoints(0); // 或者根據需求拋出異常
		}

		String pointsDueDateStr = request.getParameter("pointsDueDate");
		if (pointsDueDateStr != null && !pointsDueDateStr.isEmpty()) {
			checkout.setPointsDueDate(Date.valueOf(pointsDueDateStr));
		}

		return checkout;
	}

	// 獲取下一個結帳ID
	private void getNextCheckoutId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextId = checkoutService.generateNextCheckoutId();
		response.setContentType("text/plain");
		response.getWriter().write(nextId);
	}

	// 獲取所有員工
	private void getAllEmployees(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<EmpBean> employees = checkoutService.getAllEmployees();
		request.setAttribute("employees", employees);
		request.getRequestDispatcher("/checkout/checkout/InsertCheckout.jsp").forward(request, response);
	}

	// 根據類別獲取所有產品名稱 & ID & 價錢
	private void getProductNames(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String category = request.getParameter("category");
	    try {
	        List<ProductBean> products = checkoutService.getProductNamesByCategory(category);
	        if (products.isEmpty()) {
	            logger.warning("未找到類別為 '" + category + "' 的產品");
	        }
	        sendJsonResponse(response, products);
	    } catch (Exception e) {
	        logger.log(Level.SEVERE, "獲取產品名稱時發生錯誤", e);
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        sendJsonResponse(response, Collections.singletonMap("error", "獲取產品名稱失敗: " + e.getMessage()));
	    }
	}
	private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		new Gson().toJson(data, response.getWriter());
	}

	// 插入結帳記錄和明細
	private void insertCheckoutWithDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String checkoutId = request.getParameter("checkoutId");
			String customerTel = request.getParameter("customerTel");
			String employeeId = request.getParameter("employeeId");
			BigDecimal checkoutTotalPrice = new BigDecimal(request.getParameter("totalAmount"));
			Date checkoutDate = Date.valueOf(request.getParameter("checkoutDate"));
			int bonusPoints = Integer.parseInt(request.getParameter("bonusPoints"));
			Date pointsDueDate = null;
			if (request.getParameter("pointsDueDate") != null && !request.getParameter("pointsDueDate").isEmpty()) {
				pointsDueDate = Date.valueOf(request.getParameter("pointsDueDate"));
			}

			CheckoutBean checkout = new CheckoutBean();
			checkout.setCheckoutId(checkoutId);
			checkout.setCustomerTel(customerTel);
			checkout.setEmployeeId(employeeId);
			checkout.setCheckoutTotalPrice(checkoutTotalPrice.intValue());
			checkout.setCheckoutDate(checkoutDate);
			checkout.setBonusPoints(bonusPoints);
			checkout.setPointsDueDate(pointsDueDate);

			List<CheckoutDetailsBean> details = new ArrayList<>();
			String productsJson = request.getParameter("products");
			if (productsJson != null && !productsJson.isEmpty()) {
				details = parseProductsJson(productsJson, checkoutId);
			}

			boolean success = checkoutService.insertCheckoutWithDetails(checkout, details);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			if (success) {
				response.getWriter().write("{\"status\":\"success\",\"message\":\"結帳記錄新增成功\"}");
				logger.info("結帳記錄新增成功: " + checkoutId);
			} else {
				response.getWriter().write("{\"status\":\"error\",\"message\":\"結帳記錄新增失敗\"}");
				logger.warning("結帳記錄新增失敗: " + checkoutId);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "新增結帳記錄時發生錯誤", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"status\":\"error\",\"message\":\"新增結帳記錄時發生錯誤: " + e.getMessage() + "\"}");
		}
	}

	// 解析JSON格式的商品資料
	private List<CheckoutDetailsBean> parseProductsJson(String productsJson, String checkoutId) {
		logger.info("開始解析商品 JSON: " + productsJson);
		Gson gson = new Gson();
		List<Map<String, Object>> productsList = gson.fromJson(productsJson,
				new TypeToken<List<Map<String, Object>>>() {
				}.getType());
		List<CheckoutDetailsBean> details = new ArrayList<>();
		for (Map<String, Object> product : productsList) {
			CheckoutDetailsBean detail = new CheckoutDetailsBean();
			detail.setCheckoutId(checkoutId);
			detail.setProductId((String) product.get("productId"));
			detail.setNumberOfCheckout(((Double) product.get("quantity")).intValue());
			detail.setProductPrice(((Double) product.get("price")).intValue());
			detail.setCheckoutPrice(((Double) product.get("subtotal")).intValue());
			details.add(detail);
			logger.info("創建的 CheckoutDetailsBean: " + detail);
		}
		logger.info("解析完成，創建了 " + details.size() + " 個 CheckoutDetailsBean 對象");
		return details;
	}

	private void updateTotalAndBonus(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String checkoutId = request.getParameter("checkoutId");
		BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));
		int bonusPoints = Integer.parseInt(request.getParameter("bonusPoints"));

		try {
			checkoutService.updateTotalAndBonus(checkoutId, totalAmount, bonusPoints);
			response.setContentType("application/json");
			response.getWriter().write("{\"status\":\"success\",\"message\":\"總金額和紅利點數已更新\"}");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "更新總金額和紅利點數時發生錯誤", e);
			response.setContentType("application/json");
			response.getWriter().write("{\"status\":\"error\",\"message\":\"更新失敗\"}");
		}
	}

}