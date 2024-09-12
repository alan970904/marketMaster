package com.MarketMaster.service.restock;

import java.sql.SQLException;
import java.util.List;

import com.MarketMaster.DTO.restock.EmployeeDTO;
import com.MarketMaster.DTO.restock.ProductCategoryDTO;
import com.MarketMaster.DTO.restock.ProductNameDTO;
import com.MarketMaster.dao.restock.RestockDAO;
import jakarta.servlet.http.HttpServletRequest;

import com.MarketMaster.bean.restock.RestockDetailViewBean;

@SuppressWarnings("unused")
public class RestockService {
    private RestockDAO restockDAO;
    	
    public RestockService() {
        this.restockDAO = new RestockDAO();
    }
    public List<EmployeeDTO> getEmployees() throws SQLException, ClassNotFoundException {
        return restockDAO.getEmployees();
    }

    public List<ProductNameDTO> getProductNamesByCategory(String category) throws SQLException, ClassNotFoundException {
        return restockDAO.getProductNamesByCategory(category);
    }

    
    public List<ProductCategoryDTO> getProductCategory() throws SQLException, ClassNotFoundException {
        return restockDAO.getProductCategory();
    }

    public String getLatestRestockId() throws SQLException, ClassNotFoundException {
        return restockDAO.getLatestRestockId();
    }
    
    public void insertRestockData(String restockId, HttpServletRequest request) throws SQLException, ClassNotFoundException {
        restockDAO.insertRestockData(restockId, request);
    }
    public List<RestockDetailViewBean> getAllRestockDetails() throws ClassNotFoundException, SQLException {
    	return restockDAO.getAllRestockDetails();
    }

    public boolean deleteRestock(String restockId) {
        try {
            return restockDAO.delete(restockId);

        } finally {

        }
    }
    
   
    
}