package com.MarketMaster.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

// HouseDao 類：用於處理 House 實體的數據訪問操作
public class HouseDao implements IHouseDao{

	// 聲明一個 private 的 Session 成員變數
	private Session session;

	// 構造函數：接受一個 Session 對象作為參數
	public HouseDao(Session session) {
		this.session = session;
	}

    /**
     * 插入新的 House 對象
     * @param bean 要插入的 House 對象
     * @return 成功插入的 House 對象，如果已存在相同 ID 的對象則返回 null
     */
	@Override
    public House insert(House bean) {
        // 如果沒有找到相同 id 的對象（即 resultBean 為 null）
        if (bean != null) {
            // 將新的 bean 對象持久化到數據庫
            session.persist(bean);
            // 返回成功插入的 bean 對象
            return bean;
        }
        // 如果找到了相同 id 的對象，則返回 null（表示插入失敗）
        return null;
    }

    /**
     * 根據 ID 選擇單個 House 對象
     * @param houseid 要查詢的 House 對象的 ID
     * @return 找到的 House 對象，如果不存在則返回 null
     */
	@Override
    public House selectedById(Integer houseid) {
        // 使用 session.get() 方法根據主鍵（houseid）獲取 House 對象
        return session.get(House.class, houseid);
    }

    /**
     * 選擇所有 House 對象
     * @return 包含所有 House 對象的 List
     */
	@Override
    public List<House> selectAll() {
        // 創建一個 HQL (Hibernate Query Language) 查詢
        // "from House" 表示選擇 House 實體的所有實例
        Query<House> query = session.createQuery("from House", House.class);

        // 執行查詢並返回結果列表
        return query.list();
    }

	/**
	 * 更新指定 ID 的 House 對象的名稱
	 *
	 * @param houseid   要更新的 House 對象的 ID
	 * @param housename 新的 House 名稱
	 * @return 更新後的 House 對象，如果找不到指定 ID 的 House 則返回 null
	 */
	@Override
	public House update(Integer houseid, String housename) {
		// 使用 session.get() 方法根據 ID 獲取 House 對象
		House resultBean = session.get(House.class, houseid);

		// 如果找到了對應的 House 對象
		if (resultBean != null) {
			// 更新 House 對象的名稱
			resultBean.setHousename(housename);
		}

		// 返回更新後的 House 對象（如果沒找到則返回 null）
		return resultBean;
	}

    /**
     * 根據 ID 刪除 House 對象
     * @param houseid 要刪除的 House 對象的 ID
     * @return 布爾值，表示刪除操作是否成功
     */
	@Override
    public boolean deleteById(Integer houseid) {
        // 使用 session.get() 方法根據 ID 獲取 House 對象
        House resultBean = session.get(House.class, houseid);

        // 如果找到了對應的 House 對象
        if (resultBean != null) {
            // 從數據庫中刪除該對象
            session.remove(resultBean);
            // 返回 true 表示刪除成功
            return true;
        }

        // 如果沒找到對應的對象，返回 false 表示刪除失敗
        return false;
    }

}