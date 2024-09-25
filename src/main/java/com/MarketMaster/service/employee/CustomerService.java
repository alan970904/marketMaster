package com.MarketMaster.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.employee.CustomerBean;
import com.MarketMaster.dao.employee.CustomerDao;
import com.MarketMaster.exception.DataAccessException;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
    private CustomerDao customerDao;

    public boolean addCustomer(CustomerBean customer) throws DataAccessException {
        return customerDao.addCustomer(customer);
    }

    public CustomerBean getCustomer(String customerTel) throws DataAccessException {
        return customerDao.getCustomer(customerTel);
    }

    public List<CustomerBean> getAllCustomers() throws DataAccessException {
        return customerDao.getAllCustomers();
    }

    public boolean updateCustomer(CustomerBean customer, String originalTel) throws DataAccessException {
        return customerDao.updateCustomer(customer, originalTel);
    }

    public boolean deleteCustomer(String customerTel) throws DataAccessException {
        return customerDao.deleteCustomer(customerTel);
    }
}