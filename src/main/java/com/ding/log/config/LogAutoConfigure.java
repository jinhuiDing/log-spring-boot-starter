package com.ding.log.config;


import com.ding.log.handler.LogAop;
import com.ding.log.service.impl.LogServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ding
 * @date 2020/9/8 14:54
 */

@ConditionalOnClass(HikariConfig.class)
@EnableConfigurationProperties(LogDbConfig.class)
@Configuration
@Import({LogServiceImpl.class, LogAop.class})
public class LogAutoConfigure {



}
