package com.spring.boot;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SpringBootBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBoardApplication.class, args);

	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
		throws Exception{
		
		SqlSessionFactoryBean sessionFactory = 
				new SqlSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource);
		
		Resource[] res = new PathMatchingResourcePatternResolver()
				.getResources("classpath:mybatis/mapper/*.xml");
		
		sessionFactory.setMapperLocations(res);
		
		return sessionFactory.getObject();
		
	}
	

}
