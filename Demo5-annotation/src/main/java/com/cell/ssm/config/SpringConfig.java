package com.cell.ssm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"com.cell.ssm"})
@PropertySource("classpath:jdbc.properties")
// 导入其他配置到 SpringConfig，组合成一个完整的应用上下文
@Import({DataSourceConfig.class, MyBatisConfig.class})
// 添加事务控制
@EnableTransactionManagement
public class SpringConfig {
}
