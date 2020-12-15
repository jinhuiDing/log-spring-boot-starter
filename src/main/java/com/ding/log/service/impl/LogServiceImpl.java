package com.ding.log.service.impl;


import com.ding.log.config.LogDbConfig;
import com.ding.log.model.SysLog;
import com.ding.log.service.LogService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;

import javax.sql.DataSource;

/**
 * @author ding
 * @date 2020/9/8 14:19
 */

@ConditionalOnClass(JdbcTemplate.class)
public class LogServiceImpl implements LogService {

    private Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private static final String INSERT_SQL = "INSERT INTO sys_log" +
            "(" +
            "`username`," +
            "`operation_content`," +
            "`operation_time`," +
            "`is_success`," +
            "`cost_time`," +
            "`request_url`," +
            "`controller_method`," +
            "`method_type`," +
            "`request_param`," +
            "`ip`," +
            "`reference`," +
            "`user_agent`," +
            "`error_msg` )" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
    private final JdbcTemplate jdbcTemplate;


    public LogServiceImpl(@Autowired(required = false) LogDbConfig logDbConfig, DataSource dataSource) {
        //优先使用配置的日志数据源，否则使用默认的数据源

        if (logDbConfig != null && StringUtils.isNotEmpty(logDbConfig.getJdbcUrl())) {
            log.info("检测到日志数据源: {}", logDbConfig.getJdbcUrl());
            dataSource = new HikariDataSource(logDbConfig);
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Async
    @Override
    public void saveLog(SysLog sysLog) {
        int update = this.jdbcTemplate.update(INSERT_SQL
                , sysLog.getUsername()
                , sysLog.getOperationContent()
                , sysLog.getOperationTime()
                , sysLog.getIsSuccess(),
                sysLog.getCostTime(),
                sysLog.getRequestUrl(),
                sysLog.getControllerMethod(),
                sysLog.getMethodType(),
                sysLog.getRequestParam(),
                sysLog.getIp(),
                sysLog.getReference(),
                sysLog.getUserAgent(),
                sysLog.getErrorMsg()
        );
        if (log.isDebugEnabled()) {
            log.debug("【{}】日志保存成功:{}", sysLog.getOperationContent(), update == 1);
        }
    }
}
