package com.ding.log.config;


import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ding
 * @date 2020/9/8 14:23
 */


@Configuration
@ConfigurationProperties(prefix = "ding.log")
public class LogDbConfig extends HikariConfig {


}
