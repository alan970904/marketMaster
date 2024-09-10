package com.MarketMaster.util;

// 導入必要的 Hibernate 類
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// 定義 HibernateUtil 類，用於管理 Hibernate SessionFactory
public class HibernateUtil {

    // 聲明一個靜態的 final SessionFactory 實例
    // 使用 static 確保全局唯一，final 確保不可變
    private static final SessionFactory factory = createSessionFactory();

    // 私有靜態方法，用於創建 SessionFactory
    // 這個方法只在類初始化時被調用一次
    private static SessionFactory createSessionFactory() {
        // 創建 StandardServiceRegistry，它保存了 Hibernate 的服務配置
        // .configure() 方法會加載默認的 hibernate.cfg.xml 配置文件。
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        // 使用 registry 創建 MetadataSources，然後構建 Metadata，最後創建 SessionFactory
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        // 返回創建的 SessionFactory
        return sf;
    }

    // 公共靜態方法，提供對 SessionFactory 的訪問
    // 這允許應用程序的其他部分獲取 SessionFactory 實例
    public static SessionFactory getSessionFactory() {
        return factory;
    }

    // 公共靜態方法，用於關閉 SessionFactory
    // 這通常在應用程序關閉時調用，以釋放資源
    public static void closeSessionFactory() {
        // 檢查 factory 是否為 null，避免 NullPointerException
        if (factory != null) {
            // 如果 factory 不為 null，則關閉它
            factory.close();
        }
    }
}