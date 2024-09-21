package com.MarketMaster.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration.Dynamic;

//相當於web.xml的Java程式組態
public class WebAppInitailizer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override //設定註冊相當於beans.config.xml的Java程式組態類別
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootAppConfig.class};
	}

	@Override //設定註冊相當於mvc-servlet.xml的Java程式組態類別
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {WebAppConfig.class};
	}

	@Override //設定DispatcherServlet mapping url-patterns
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }

    @Override
    protected void customizeRegistration(Dynamic registration) {
        registration.setMultipartConfig(
            new MultipartConfigElement("c:/MarketMasterImages/upload/",
                1024 * 1024 * 5,  // 最大文件大小（5MB）
                1024 * 1024 * 10, // 最大請求大小（10MB）
                1024 * 1024       // 文件大小閾值，超過後寫入磁盤（1MB）
            )
        );
    }
}