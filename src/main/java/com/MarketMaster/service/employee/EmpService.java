package com.MarketMaster.service.employee;

import java.util.List;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.dao.employee.EmpDao;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.viewModel.EmployeeViewModel;

//EmpService 類：處理與員工相關的業務邏輯
public class EmpService {
	// 創建 EmpDao 實例以進行數據訪問操作
    private EmpDao empDao = new EmpDao();

    // 驗證員工登入
    public EmpBean login(String employeeId, String password) throws DataAccessException {
        return empDao.validateEmployee(employeeId, password);
    }
    
    // 檢查是否為首次登入
    public boolean isFirstLogin(String employeeId) throws DataAccessException {
        return empDao.isFirstLogin(employeeId);
    }

    // 更新密碼
    public boolean updatePassword(String employeeId, String newPassword) throws DataAccessException {
        return empDao.updatePassword(employeeId, newPassword);
    }
    
    public boolean addEmployee(EmpBean emp) throws DataAccessException {
        if (empDao.getEmployee(emp.getEmployeeId()) != null) {
            throw new DataAccessException("員工編號已存在");
        }
        return empDao.addEmployee(emp);
    }

    public boolean updateEmployee(EmpBean emp) throws DataAccessException {
        if (empDao.getEmployee(emp.getEmployeeId()) == null) {
            throw new DataAccessException("員工不存在");
        }
        return empDao.updateEmployee(emp);
    }

    public boolean deleteEmployee(String employeeId) throws DataAccessException {
        if (empDao.getEmployee(employeeId) == null) {
            throw new DataAccessException("員工不存在");
        }
        return empDao.deleteEmployee(employeeId);
    }

    public EmpBean getEmployee(String employeeId) throws DataAccessException {
        return empDao.getEmployee(employeeId);
    }

    public List<EmpBean> getAllEmployees(boolean showAll) throws DataAccessException {
        return empDao.getAllEmployees(showAll);
    }

    public List<EmpBean> searchEmployees(String searchName, boolean showAll) throws DataAccessException {
        return empDao.searchEmployees(searchName, showAll);
    }

    public List<RankLevelBean> getRankList() throws DataAccessException {
        return empDao.getRankList();
    }
    
    // 獲取員工視圖模型的方法
    public EmployeeViewModel getEmployeeViewModel(String employeeId) throws DataAccessException {
        return empDao.getEmployeeViewModel(employeeId);
    }
    
    // 生成新的員工ID的方法
    public String generateNewEmployeeId() throws DataAccessException {
    	// 獲取最後一個員工ID
    	String lastId = empDao.getLastEmployeeId();
    	// 解析數字部分並加1
    	int numPart = Integer.parseInt(lastId.substring(1)) + 1;
    	// 格式化新的員工ID
    	return String.format("E%03d", numPart);
    }
}