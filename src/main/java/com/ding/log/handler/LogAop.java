package com.ding.log.handler;

import com.ding.log.anno.OperLog;
import com.ding.log.model.SysLog;
import com.ding.log.service.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ding
 * @date 2020/9/8 9:46
 */


@Aspect
//引用此启动器是个web项目此自动配置才会生效
@ConditionalOnWebApplication
public class LogAop {

    private Logger log = LoggerFactory.getLogger(LogAop.class);

    private LogService logService;

    public LogAop(LogService logService) {
        this.logService = logService;
    }


    @Around("@annotation(operLog)")
    public Object executeAround(ProceedingJoinPoint joinPoint, OperLog operLog) throws Throwable {

        SysLog sysLog = new SysLog();

        Object obj = null;
        String apiContent = operLog.description();

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //接口调用开始时间
        Date date = new Date();
        long startTime = date.getTime();
        //接口调用的方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //接口调用的简单类名

        //请求的uri
        String uri = request.getRequestURI();

        sysLog.setOperationContent(apiContent);
        sysLog.setIp(getRealIP(request));
        sysLog.setOperationTime(date);
        sysLog.setControllerMethod(methodName);
        sysLog.setRequestUrl(uri);
        sysLog.setMethodType(request.getMethod());
        sysLog.setReference(request.getHeader("Referer"));
        sysLog.setUserAgent(request.getHeader("user-agent"));
        sysLog.setRequestParam(converParam(args));
        sysLog.setIsSuccess(1);

        log.info("=========================================工单系统调用日志-环绕通知开始执行......=========================================");
        try {
            log.info(apiContent);

            //接口响应的时间
            log.info("请求的URI：{}", uri);
            log.info("请求的参数：{}", args);
            log.info("请求的类：{}", methodName);

            //方法执行
            obj = joinPoint.proceed(args);


            log.info("响应数据:{}", obj);

        } catch (Throwable e) {
            //发生异常
            sysLog.setIsSuccess(0);
            sysLog.setErrorMsg(e.getMessage());

            log.error(methodName + "方法报错执行耗时环绕通知出错", e);
            throw e;
        } finally {
            try {
                long diffTime = System.currentTimeMillis() - startTime;
                sysLog.setCostTime(diffTime);
                log.info("响应时间：{}", diffTime);
                saveApiLog(sysLog);
            } catch (Exception e) {
                log.error("记录日志失败：{}", methodName);
                e.printStackTrace();
            }

            log.info("=========================================工单系统调用日志-环绕通知结束执行......=========================================");

        }

        return obj;
    }

    private void saveApiLog(SysLog sysLog) {
        this.logService.saveLog(sysLog);
    }

    private String converParam(Object[] args) {
        ObjectMapper mapper = new ObjectMapper();

        List paramList = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof HttpSession) && !(args[i] instanceof HttpServletRequest) && !(args[i] instanceof MultipartFile)) {
                paramList.add(args[i]);
            }
        }
        try {
            return mapper.writeValueAsString(paramList);
        } catch (JsonProcessingException e) {
            log.error("参数转化为json时发生异常{}", e);
            e.printStackTrace();
        }
        return null;

    }


    public String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isNotBlank(ip) && ip.split(",").length > 0) {
            String[] ips = ip.split(",");
            for (String subIp : ips) {
                if (!"unknown".equalsIgnoreCase(ip)) {
                    return subIp;
                }
            }
        }
        return ip;
    }
}