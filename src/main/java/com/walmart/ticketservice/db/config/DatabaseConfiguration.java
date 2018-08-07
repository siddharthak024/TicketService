package com.walmart.ticketservice.db.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@EnableJpaRepositories(basePackages = "com.walmart.ticketservice")
@Configuration("dbConfiguration")
public class DatabaseConfiguration {

	@Value("${db.isInMemory:true}")
	private boolean isInMemoryDb;

	@Value("${db.url}")
	private String url;

	@Value("${db.userName}")
	private String userName;

	@Value("${db.driverName}")
	private String driverName;

	@Value("${db.showSqlStatements}")
	private boolean showSqlStatements;

	@Bean
	@Primary
	public DataSource dataSource() {
		DriverManagerDataSource driver = new DriverManagerDataSource();
		driver.setDriverClassName(driverName);
		driver.setUrl(url);
		driver.setUsername(userName);
		return driver;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		Database db = Database.HSQL;
		String dialect = "org.hibernate.dialect.HSQLDialect";

		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter.setDatabase(db);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		factoryBean.setPackagesToScan("com.walmart.ticketservice.db.entity");
		factoryBean.setDataSource(dataSource);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", dialect);
		jpaProperties.put("hibernate.show_sql", showSqlStatements);
		jpaProperties.put("hibernate.format_sql", showSqlStatements);

		if (!isInMemoryDb) {
			jpaProperties.put("hibernate.hbm2ddl.auto", "none");
		} else {
			jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		}
		jpaProperties.put("hibernate.enable_lazy_load_no_trans", "true");
		jpaProperties.put("spring.jpa.properties.hibernate.cache.use_second_level_cache", "true");
		jpaProperties.put("spring.jpa.properties.hibernate.cache.use_query_cache", "true");
		jpaProperties.put("spring.jpa.properties.hibernate.cache.region.factory_class",
				"org.hibernate.cache.ehcache.EhCacheRegionFactory");
		jpaProperties.put("spring.jpa.properties.javax.persistence.sharedCache.mode", "ALL");

		factoryBean.setJpaProperties(jpaProperties);

		return factoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}
}
