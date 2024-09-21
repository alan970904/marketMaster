package com.MarketMaster.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.dao.employee.EmpDao;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.viewModel.EmployeeViewModel;

@Service
@Transactional
public class EmpService {

    @Autowired
    private EmpDao empDao;

    public EmpBean login(String employeeId, String password) throws DataAccessException {
        return empDao.validateEmployee(employeeId, password);
    }

    public boolean isFirstLogin(String employeeId) throws DataAccessException {
        return empDao.isFirstLogin(employeeId);
    }

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

    public EmployeeViewModel getEmployeeViewModel(String employeeId) throws DataAccessException {
        return empDao.getEmployeeViewModel(employeeId);
    }

    public String generateNewEmployeeId() throws DataAccessException {
        String lastId = empDao.getLastEmployeeId();
        int numPart = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("E%03d", numPart);
    }
}