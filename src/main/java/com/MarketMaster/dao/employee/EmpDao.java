package com.MarketMaster.dao.employee;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.util.HibernateUtil;
import com.MarketMaster.viewModel.EmployeeViewModel;

//EmpDao 類：負責處理與員工相關的數據庫操作
public class EmpDao {

	// 驗證員工登入
	public EmpBean validateEmployee(String employeeId, String password) throws DataAccessException {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Query<EmpBean> query = session.createQuery("FROM EmpBean WHERE employeeId = :id AND password = :pwd", EmpBean.class);
	        query.setParameter("id", employeeId);
	        query.setParameter("pwd", password);
	        EmpBean result = query.uniqueResult();
	        if (result != null) {
	            System.out.println("Employee found: " + result.getEmployeeId());
	        } else {
	            System.out.println("No employee found for id: " + employeeId);
	        }
	        return result;
	    } catch (Exception e) {
	        throw new DataAccessException("驗證員工失敗", e);
	    }
	}

	// 檢查是否為首次登入
	public boolean isFirstLogin(String employeeId) throws DataAccessException {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			EmpBean emp = session.get(EmpBean.class, employeeId);
			return emp != null && emp.isFirstLogin();
		} catch (Exception e) {
			throw new DataAccessException("檢查首次登入狀態失敗", e);
		}
	}

	// 更新密碼
    public boolean updatePassword(String employeeId, String newPassword) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            EmpBean emp = session.get(EmpBean.class, employeeId);
            if (emp != null) {
                emp.setPassword(newPassword);
                emp.setFirstLogin(false);
                session.update(emp);
                session.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DataAccessException("更新密碼失敗", e);
        }
    }

    public boolean addEmployee(EmpBean emp) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(emp);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new DataAccessException("添加員工失敗", e);
        }
    }

    public boolean deleteEmployee(String employeeId) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            EmpBean emp = session.get(EmpBean.class, employeeId);
            if (emp != null) {
                session.delete(emp);
                session.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DataAccessException("刪除員工失敗", e);
        }
    }

    public boolean updateEmployee(EmpBean emp) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(emp);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new DataAccessException("更新員工失敗", e);
        }
    }

    public EmpBean getEmployee(String employeeId) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(EmpBean.class, employeeId);
        } catch (Exception e) {
            throw new DataAccessException("獲取員工詳情失敗", e);
        }
    }

    public List<EmpBean> getAllEmployees(boolean showAll) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = showAll ? "FROM EmpBean" : "FROM EmpBean WHERE resigndate IS NULL";
            return session.createQuery(hql, EmpBean.class).list();
        } catch (Exception e) {
            throw new DataAccessException("獲取所有員工資訊失敗", e);
        }
    }

    public List<EmpBean> searchEmployees(String searchName, boolean showAll) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = showAll
                ? "FROM EmpBean WHERE employeeName LIKE :name"
                : "FROM EmpBean WHERE employeeName LIKE :name AND resigndate IS NULL";
            Query<EmpBean> query = session.createQuery(hql, EmpBean.class);
            query.setParameter("name", "%" + searchName + "%");
            return query.list();
        } catch (Exception e) {
            throw new DataAccessException("查詢員工失敗", e);
        }
    }

    public List<RankLevelBean> getRankList() throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RankLevelBean";
            List<RankLevelBean> rankList = session.createQuery(hql, RankLevelBean.class).list();
            
            // 計算每個職級的員工數量
            for (RankLevelBean rank : rankList) {
                String countHql = "SELECT COUNT(e) FROM EmpBean e WHERE e.positionId = :positionId";
                Long totalCount = session.createQuery(countHql, Long.class)
                    .setParameter("positionId", rank.getPositionId())
                    .uniqueResult();
                
                String activeCountHql = "SELECT COUNT(e) FROM EmpBean e WHERE e.positionId = :positionId AND e.resigndate IS NULL";
                Long activeCount = session.createQuery(activeCountHql, Long.class)
                    .setParameter("positionId", rank.getPositionId())
                    .uniqueResult();
                
                rank.setTotalEmployeeCount(totalCount.intValue());
                rank.setActiveEmployeeCount(activeCount.intValue());
            }
            
            return rankList;
        } catch (Exception e) {
            throw new DataAccessException("獲取職級列表失敗", e);
        }
    }

    public EmployeeViewModel getEmployeeViewModel(String employeeId) throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Getting employee view model for ID: " + employeeId);
            
            EmpBean emp = session.get(EmpBean.class, employeeId);
            if (emp == null) {
                System.out.println("No employee found for id: " + employeeId);
                throw new DataAccessException("找不到員工：" + employeeId);
            }
            
            EmployeeViewModel viewModel = new EmployeeViewModel(
                emp.getEmployeeId(),
                emp.getEmployeeName(),
                emp.getEmployeeTel(),
                emp.getEmployeeIdcard(),
                emp.getEmployeeEmail(),
                emp.getRankLevel().getPositionName(),
                emp.getRankLevel().getSalaryLevel(),
                emp.getHiredate(),
                emp.getResigndate(),
                emp.getPassword(),
                emp.getPositionId()
            );
            
            System.out.println("Employee view model created for: " + viewModel.getEmployeeId());
            return viewModel;
        } catch (Exception e) {
            System.out.println("Error getting employee view model: " + e.getMessage());
            e.printStackTrace();
            throw new DataAccessException("獲取員工視圖失敗", e);
        }
    }

    public String getLastEmployeeId() throws DataAccessException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT e.employeeId FROM EmpBean e ORDER BY e.employeeId DESC";
            List<String> result = session.createQuery(hql, String.class).setMaxResults(1).list();
            return result.isEmpty() ? "E001" : result.get(0);
        } catch (Exception e) {
            throw new DataAccessException("獲取最後一個員工ID失敗", e);
        }
    }
}