package com.cell.ssm.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class MyBatisConfig {
    // 注册 SqlSessionFactoryBean，生成 SqlSession 实例
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.cell.ssm.bean");
        return sqlSessionFactoryBean;
    }

    // 注册 MapperScannerConfigurer，扫描 Mapper 接口并自动生成代理对象注册到 Spring 容器中（不用每个 Mapper 都 @Bean）
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.cell.ssm.dao");
        return msc;
    }
}
